package com.proschedule.core.persistence.view.component;

import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views do Componente.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class ComponentController {
    private ComponentFacade facade;
    private OperationFacade operationFacade;

    private Component component;
    private ComponentDetail componentDetail;
    
    private Set<ComponentDetail> details;

    private boolean edit = false;
    private boolean detailEdit = false;

    /**
     * Construtor da Classe
     */
    public ComponentController() {
        facade = new ComponentFacade();
        operationFacade = new OperationFacade();
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Componente
    //--------------------------------------------------------------------------
    /**
     * Cria um novo componente para receber os dados na hora de salvar.
     */
    public void newComponent() {
        edit = false;
        component = new Component();
        details = new HashSet();
    }

    /**
     * Coloca um componente em edição para depois ser salvo com as devidas
     * alterações.
     * 
     * @param c O componente que será editado
     */
    public void editComponent( Component c ) {
        edit = true;
        component = c;
        details = new HashSet();
    }

    /**
     * Remove o componente
     * 
     * @throws ComponentPersistenceException
     */
    public void removeComponent() throws ComponentPersistenceException {
        facade.remove(getComponent());
    }

    /**
     * Salva o componente novo ou em edição
     * 
     * @throws ComponentPersistenceException
     * @throws ValidatorException
     * @throws ComponentDetailPersistenceException
     */
    public void saveComponent() throws ComponentPersistenceException, 
            ValidatorException, ComponentDetailPersistenceException {
        if ( isEdit() ) {
            facade.modify(getComponent());

            if ( !isDetailEdit() ) {
                for ( ComponentDetail cd : details ) {
                    component.getDetails().add(cd);
                }
            }
        } else {
            //Limpa a lista de detalhes antes de salvar
            //Depois ela será novamente adicionada ao componente
            component.setDetails(new HashSet());
            facade.add(getComponent());

            //Salva os detalhes e os adiciona ao componente novamente
            for ( ComponentDetail cd : details ) {
                cd.getPrimaryKey().setComponent(component);
                facade.addDetail(cd);
                component.getDetails().add(cd);
            }
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Componente
    //--------------------------------------------------------------------------
    /**
     * @return Lista de detalhes do componente
     */
    public List<ComponentDetail> getComponentDetails() {
        return getComponent().getOrderedDetails();
    }

    /**
     * @return Código do componente
     */
    public String getComponentId() {
        return getComponent().getId();
    }

    /**
     * @return Matéria prima do componente
     */
    public String getComponentRawMaterial() {
        return getComponent().getRawMaterial();
    }

    /**
     * Seta o código do componente
     *
     * @param id Código do componente
     */
    public void setComponentId( String id ) {
        getComponent().setId(id);
    }

    /**
     * Seta a matéria prima do componente
     *
     * @param rawMaterial Matéria prima do componente
     */
    public void setComponentRawMaterial( String rawMaterial ) {
        getComponent().setRawMaterial(rawMaterial);
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Detalhe de Componente
    //--------------------------------------------------------------------------
    /**
     * Cria um novo detalhe de componente
     */
    public void newComponentDetail() {
        detailEdit = false;
        componentDetail = new ComponentDetail();
    }

    /**
     * Edita um detalhe de componente
     *
     * @param cd O detalhe de componente a ser editado
     */
    public void editComponentDetail( ComponentDetail cd ) {
        detailEdit = true;
        componentDetail = cd;
    }

    /**
     * Salva um detalhe de componente
     * 
     * @throws ComponentDetailPersistenceException
     * @throws ValidatorException
     */
    public void saveComponentDetail() throws ComponentDetailPersistenceException, ValidatorException {
        if ( !isDetailEdit() ) { //Novo detalhe
            if ( !isEdit() ) { //Novo Componente
                details.add(componentDetail);
                component.getDetails().add(componentDetail);
            } else { //Edição de Componente
                facade.addDetail(componentDetail);
                component.getDetails().add(componentDetail);
            }
        } else { //Edição de detalhe
            if ( isEdit() ) { //Edição de Componente
                facade.modifyDetail(componentDetail);
            }
        }
    }

    /**
     * Remove um detalhe de componente
     * 
     * @throws ComponentDetailPersistenceException
     */
    public void removeComponentDetail() throws ComponentDetailPersistenceException {
        if ( !isEdit() ) {
            component.getDetails().remove(componentDetail);
            details.remove(componentDetail);
        } else {
            component.getDetails().remove(componentDetail);
            facade.removeDetail(componentDetail);
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Detalhe de Componente
    //--------------------------------------------------------------------------
    /**
     * Seta a operação do detalhe de componente novo ou em edição
     * 
     * @param id O código da operação
     * @return True se o detalhe não existir no componente ou false se já existir
     * @throws OperationPersistenceException
     */
    public boolean setComponentDetailOperation( Integer id ) throws OperationPersistenceException {
        Operation o = operationFacade.getOperation(id);

        if ( isEdit() ) {
            if ( getComponent().getComponentDetail(o) != null &&
                    componentDetail.getPrimaryKey().getOperation().getId().intValue() != id ) {
                return false;
            }
        } else {
            for ( ComponentDetail cd : details ) {
                if ( cd.getPrimaryKey().getOperation().getId().intValue() == id.intValue() ) {
                    if ( isDetailEdit() ) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        if ( !isDetailEdit() ) {
            ComponentDetailKey pk = new ComponentDetailKey();
            pk.setComponent(component);

            componentDetail.setPrimaryKey(pk);
        }

        componentDetail.getPrimaryKey().setOperation(o);

        return true;
    }

    /**
     * Seta a ordem do detalhe de componente
     *
     * @param order A ordem do componente
     */
    public void setComponentDetailOrder( Integer order ) {
        componentDetail.setOrder(order);
    }

    /**
     * Seta o lead time do detalhe de componente
     * 
     * @param value O valor do lead time
     * @param type O tipo de lead time
     */
    public void setComponentDetailLeadTime( Double value, String type ) {
        LeadTime lt = new LeadTime();
        lt.setValue(value);
        lt.setType(type);

        componentDetail.setLeadTime(lt);
    }

    /**
     * @return Código da operação do detalhe de componente
     */
    public int getComponentDetailOperationId() {
        return componentDetail.getPrimaryKey().getOperation().getId();
    }

    /**
     * @return Descrição da operação do detalhe de componente
     */
    public String getComponentDetailOperationDescription() {
        return componentDetail.getPrimaryKey().getOperation().getDescription();
    }

    /**
     * @return A ordem do detalhe de componente
     */
    public int getComponentDetailOrder() {
        return componentDetail.getOrder();
    }

    /**
     * @return Valor do lead time do detalhe de componente
     */
    public double getComponentDetailLeadTimeValue() {
        return componentDetail.getLeadTime().getValue();
    }

    /**
     * @return Tipo do lead time do detalhe de componente
     */
    public String getComponentDetailLeadTimeType() {
        return componentDetail.getLeadTime().getType();
    }

    //--------------------------------------------------------------------------
    //Métodos de listagem de Componente
    //--------------------------------------------------------------------------
    /**
     * Lista todos os componentes
     *
     * @return Lista de todos os componentes
     * @throws ComponentPersistenceException
     */
    public List<Component> getComponents() throws ComponentPersistenceException {
        return facade.list("id", "asc");
    }

    /**
     * Lista os componentes que batem com os parâmetros informados.
     * 
     * @param field O nome do campo a ser buscado
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de componentes encontrados
     * @throws ComponentPersistenceException
     */
    public List<Component> getComponents( SearchParam field , String value , SearchParam operator ) throws ComponentPersistenceException {
        return facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Retorna um componente com a chave primária informada
     * 
     * @param id Código do componente
     * @return Componente com a chave informada ou null se não encontrar nada
     * @throws ComponentPersistenceException
     */
    public Component getComponent( String id ) throws ComponentPersistenceException {
        return facade.getComponent(id);
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
        component = null;
        componentDetail = null;
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
     * @return the component
     */
    public Component getComponent() {
        return component;
    }
}
