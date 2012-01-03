package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.facade.CustomerFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.scheduling.facade.OrderFacade;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.keys.OrderDetailKey;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views da Ordem de Produção.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class OrderController {
    private OrderFacade facade;
    private SetFacade setFacade;
    private CustomerFacade customerFacade;
    private ComponentFacade componentFacade;

    private Order order;
    private OrderDetail orderDetail;
    
    private java.util.Set<OrderDetail> details;

    private boolean edit = false;
    private boolean detailEdit = false;

    /**
     * Construtor da Classe
     */
    public OrderController() {
        facade = new OrderFacade();
        setFacade = new SetFacade();
        customerFacade = new CustomerFacade();
        componentFacade = new ComponentFacade();
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Ordem de Produção
    //--------------------------------------------------------------------------
    /**
     * Cria um novo ordem de produção para receber os dados na hora de salvar.
     */
    public void newOrder() {
        edit = false;
        order = new Order();
        details = new HashSet();
    }

    /**
     * Coloca um ordem de produção em edição para depois ser salvo com as devidas
     * alterações.
     * 
     * @param c O ordem de produção que será editada
     */
    public void editOrder( Order c ) {
        edit = true;
        order = c;
        details = new HashSet();
    }

    /**
     * Remove o ordem de produção
     * 
     * @throws OrderPersistenceException
     */
    public void removeOrder() throws OrderPersistenceException {
        facade.remove(getOrder());
    }

    /**
     * Salva a ordem de produção nova ou em edição
     * 
     * @throws OrderPersistenceException
     * @throws ValidatorException
     * @throws OrderDetailPersistenceException
     */
    public void saveOrder() throws OrderPersistenceException,
            ValidatorException, OrderDetailPersistenceException {
        if ( isEdit() ) {
            facade.modify(getOrder());

            if ( !isDetailEdit() ) {
                //Adiciona os detalhes
                for ( OrderDetail cd : details ) {
                    order.getDetails().add(cd);
                }
            }
        } else {
            //Limpa a lista de detalhes antes de salvar
            //Depois elas serão novamente adicionada ao ordem de produção
            order.setDetails(new HashSet());

            //Salva o ordem de produção
            facade.add(getOrder());

            //Salva os detalhes e os adiciona ao ordem de produção novamente
            for ( OrderDetail cd : details ) {
                cd.getPrimaryKey().setOrder(order);
                facade.addDetail(cd);
                order.getDetails().add(cd);
            }
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Ordem de Produção
    //--------------------------------------------------------------------------
    /**
     * @return Lista de detalhes do ordem de produção
     */
    public List<OrderDetail> getOrderDetails() {
        return new ArrayList( getOrder().getDetails() );
    }

    /**
     * @return Código da ordem de produção
     */
    public String getOrderId() {
        return getOrder().getId();
    }

    /**
     * @return Quantidade para o conjunto
     */
    public double getOrderSetQuantity() {
        return getOrder().getSetQuantity();
    }

    /**
     * @return Data de entrega
     */
    public Date getOrderDeliveryDate() {
        return getOrder().getDeliveryDate();
    }

    /**
     * @return Código do conjunto
     */
    public String getOrderSetId() {
        return getOrder().getSet().getId();
    }

    /**
     * @return Código do clientes
     */
    public String getOrderCustomerId() {
        return getOrder().getCustomer().getId();
    }

    /**
     * @return Nome do cliente
     */
    public String getOrderCustomerName() {
        return getOrder().getCustomer().getName();
    }

    /**
     * Seta o código da ordem de produção
     *
     * @param id Código da ordem de produção
     */
    public void setOrderId( String id ) {
        getOrder().setId(id);
    }

    /**
     * Seta a quantidade do conjunto
     * 
     * @param quantity Quantidade do conjunto
     */
    public void setOrderSetQuantity( double quantity ) {
        getOrder().setSetQuantity(quantity);
    }

    /**
     * Seta a data de entrega da ordem
     * 
     * @param deliveryDate Data de entrega
     */
    public void setOrderDeliveryDate( Date deliveryDate ) {
        getOrder().setDeliveryDate(deliveryDate);
    }

    /**
     * Seta o conjunto da ordem de produção e cria a lista com os detalhes da ordem
     * de produção, que são os componentes que formam o conjunto
     * 
     * @param id Código da ordem de produção
     * @param isSave 
     * @throws SetPersistenceException
     * @throws OrderDetailPersistenceException
     * @throws ValidatorException
     */
    public void setOrderSet( String id, boolean isSave ) throws SetPersistenceException,
            OrderDetailPersistenceException, ValidatorException {
        //Só faz as modificações se for um conjunto diferente
        if ( !isSave || (isSave && getOrder().getDetails().size() <= 0) ) {
            //Recupera o conjunto
            Set set = setFacade.getSet(id);

            //Seta o conjunto na ordem de produção
            getOrder().setSet(set);

            //Remove os detalhes antigos
            removeAllOrderDetails();

            //Cria os detalhes da ordem e salva usando os métodos desta classe
            for ( SetComponent c : set.getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(order);

                OrderDetail od = new OrderDetail();
                od.setPrimaryKey(key);
                od.setComponentQuantity( order.getSetQuantity() * c.getComponentQuantity() );

                newOrderDetail(od);
                saveOrderDetail();
            }
        }
    }

    /**
     * Seta o cliente da ordem de produção
     * 
     * @param id Código do cliente
     * @throws CustomerPersistenceException
     */
    public void setOrderCustomer( String id ) throws CustomerPersistenceException {
        Customer customer = customerFacade.getCustomer(id);
        getOrder().setCustomer(customer);
    }

    /**
     * Remove os detalhes da ordem de produção
     * @throws OrderDetailPersistenceException
     */
    public void removeAllOrderDetails() throws OrderDetailPersistenceException {
        Collection<OrderDetail> remove = new LinkedList<OrderDetail>(getOrder().getDetails());

        for(OrderDetail element : remove) {
            editOrderDetail(element);
            removeOrderDetail();
        }
    }
    
    //--------------------------------------------------------------------------
    //Métodos de ação de Detalhe de Ordem de Produção
    //--------------------------------------------------------------------------
    /**
     * Cria um novo detalhe de ordem de produção
     * @param od O novo detalhe da ordemd de produção
     */
    public void newOrderDetail( OrderDetail od) {
        detailEdit = false;
        orderDetail = od;
    }

    /**
     * Edita um detalhe de ordem de produção
     *
     * @param cd O detalhe de ordem de produção a ser editado
     */
    public void editOrderDetail( OrderDetail cd ) {
        detailEdit = true;
        orderDetail = cd;
    }

    /**
     * Salva um detalhe de ordem de produção
     * 
     * @throws OrderDetailPersistenceException
     * @throws ValidatorException
     */
    public void saveOrderDetail() throws OrderDetailPersistenceException, ValidatorException {
        if ( !isDetailEdit() ) { //Novo detalhe
            if ( !isEdit() ) { //Novo Ordem de Produção
                details.add(orderDetail);
                order.getDetails().add(orderDetail);
            } else { //Edição de Ordem de Produção
                facade.addDetail(orderDetail);
                order.getDetails().add(orderDetail);
            }
        } else { //Edição de detalhe
            if ( isEdit() ) { //Edição de Ordem de Produção
                facade.modifyDetail(orderDetail);
            }
        }
    }

    /**
     * Remove um detalhe de ordem de produção
     * 
     * @throws OrderDetailPersistenceException
     */
    public void removeOrderDetail() throws OrderDetailPersistenceException {
        if ( !isEdit() ) {
            order.getDetails().remove(orderDetail);
            details.remove(orderDetail);
        } else {
            order.getDetails().remove(orderDetail);
            facade.removeDetail(orderDetail);
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Detalhe de Ordem de Produção
    //--------------------------------------------------------------------------
    /**
     * Seta o componente do detalhe de ordem de produção novo ou em edição
     * 
     * @param id O código do componente
     * @return True se o detalhe não existir no ordem de produção ou false se já existir
     * @throws ComponentPersistenceException
     */
    public boolean setOrderDetailComponent( String id ) throws ComponentPersistenceException {
        Component c = componentFacade.getComponent(id);

        if ( isEdit() ) {
            if ( getOrder().getOrderDetail(c) != null &&
                    !orderDetail.getPrimaryKey().getComponent().getId().equals( id ) ) {
                return false;
            }
        } else {
            for ( OrderDetail od : details ) {
                if ( od.getPrimaryKey().getComponent().getId().equals( id ) ) {
                    return false;
                }
            }
        }

        if ( !isDetailEdit() ) {
            OrderDetailKey pk = new OrderDetailKey();
            pk.setOrder(order);

            orderDetail.setPrimaryKey(pk);
        }

        orderDetail.getPrimaryKey().setComponent(c);

        return true;
    }

    /**
     * Seta a quantidade do componente do detalhe de ordem de produção
     *
     * @param componentQuantity
     */
    public void setOrderDetailComponentQuantity( double componentQuantity ) {
        orderDetail.setComponentQuantity(componentQuantity);
    }

    /**
     * @return Código do componente do detalhe de ordem de produção
     */
    public String getOrderDetailComponentId() {
        return orderDetail.getPrimaryKey().getComponent().getId();
    }

    /**
     * @return A ordem do detalhe de ordem de produção
     */
    public double getOrderDetailComponentQuantity() {
        return orderDetail.getComponentQuantity();
    }

    //--------------------------------------------------------------------------
    //Métodos de listagem de Ordem de Produção
    //--------------------------------------------------------------------------
    /**
     * Lista todos os ordem de produçãos
     *
     * @return Lista de todas as ordens de produção
     * @throws OrderPersistenceException
     */
    public List<Order> getOrders() throws OrderPersistenceException {
        return facade.list("id", "asc");
    }

    /**
     * Lista as ordens de produção que batem com os parâmetros informados.
     * 
     * @param field O nome do campo a ser buscado
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de ordens de produção encontradas
     * @throws OrderPersistenceException
     */
    public List<Order> getOrders( SearchParam field , String value , SearchParam operator ) throws OrderPersistenceException {
        return facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Lista as ordens de produção que batem com os parâmetros informados.
     *
     * @param field O nome do campo a ser buscado
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de ordens de produção encontradas
     * @throws OrderPersistenceException
     */
    public List<Order> getOrders( SearchParam field , Double value , SearchParam operator ) throws OrderPersistenceException {
        return facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Lista as ordens de produção que batem com os parâmetros informados.
     *
     * @param field O nome do campo a ser buscado
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de ordens de produção encontradas
     * @throws OrderPersistenceException
     */
    public List<Order> getOrders( SearchParam field , Date value , SearchParam operator ) throws OrderPersistenceException {
        return facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Lista as ordens de produção que batem com os parâmetros informados.
     *
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de ordens de produção encontradas
     * @throws OrderPersistenceException
     * @throws SetPersistenceException
     */
    public List<Order> getOrdersPerSet(String value , SearchParam operator )
            throws OrderPersistenceException, SetPersistenceException {
        Set set = setFacade.getSet(value);
        return facade.list(set, operator.getName());
    }

    /**
     * Lista as ordens de produção que batem com os parâmetros informados.
     *
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de ordens de produção encontradas
     * @throws CustomerPersistenceException
     * @throws OrderPersistenceException
     */
    public List<Order> getOrdersPerCustomer(String value , SearchParam operator ) throws CustomerPersistenceException, OrderPersistenceException {
        Customer customer = customerFacade.getCustomer(value);
        return facade.list(customer, operator.getName());
    }

    /**
     * Lista as ordens de produção de determinado cliente.
     *
     * @param customer O cliente a ser buscado
     * @return Lista de ordens de produção encontradas
     * @throws OrderPersistenceException
     */
    public List<Order> getOrdersPerCustomer(Customer customer) throws OrderPersistenceException {
        return facade.list(customer, "=");
    }

    /**
     * Retorna uma ordem de produção com a chave primária informada
     * 
     * @param id Código da ordem de produção
     * @return Ordem de Produção com a chave informada ou null se não encontrar nada
     * @throws OrderPersistenceException
     */
    public Order getOrder( String id ) throws OrderPersistenceException {
        return facade.getOrder(id);
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso
    //--------------------------------------------------------------------------
    /**
     * Limpa os dados do controlador
     */
    public void clear() {
        edit = false;
        detailEdit = false;
        order = null;
        orderDetail = null;
    }

    /**
     * @return the edit
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * @return the detailEdit
     */
    public boolean isDetailEdit() {
        return detailEdit;
    }

    /**
     * @return the set
     */
    public Order getOrder() {
        return order;
    }
}
