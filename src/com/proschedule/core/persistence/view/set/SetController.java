package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.HashSet;
import java.util.List;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views do Conjunto.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class SetController {
    private SetFacade facade;
    private OperationFacade operationFacade;
    private ComponentFacade componentFacade;

    private Set set;
    private SetDetail setDetail;
    private SetComponent setComponent;
    
    private java.util.Set<SetDetail> details;
    private java.util.Set<SetComponent> components;

    private boolean edit = false;
    private boolean detailEdit = false;
    private boolean componentEdit = false;

    /**
     * Construtor da Classe
     */
    public SetController() {
        facade = new SetFacade();
        operationFacade = new OperationFacade();
        componentFacade = new ComponentFacade();
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Conjunto
    //--------------------------------------------------------------------------
    /**
     * Cria um novo conjunto para receber os dados na hora de salvar.
     */
    public void newSet() {
        edit = false;
        set = new Set();
        details = new HashSet();
        components = new HashSet();
    }

    /**
     * Coloca um conjunto em edição para depois ser salvo com as devidas
     * alterações.
     * 
     * @param c O conjunto que será editado
     */
    public void editSet( Set c ) {
        edit = true;
        set = c;
        details = new HashSet();
        components = new HashSet();
    }

    /**
     * Remove o conjunto
     * 
     * @throws SetPersistenceException
     */
    public void removeSet() throws SetPersistenceException {
        facade.remove(getSet());
    }

    /**
     * Salva o conjunto novo ou em edição
     * 
     * @throws SetPersistenceException
     * @throws ValidatorException
     * @throws SetDetailPersistenceException 
     * @throws SetComponentPersistenceException
     */
    public void saveSet() throws SetPersistenceException,
            ValidatorException, SetDetailPersistenceException, SetComponentPersistenceException {
        if ( isEdit() ) {
            facade.modify(getSet());

            if ( !isDetailEdit() ) {
                //Adiciona os detalhes
                for ( SetDetail cd : details ) {
                    set.getDetails().add(cd);
                }

                //Adiciona os componentes
                for ( SetComponent cd : components ) {
                    set.getComponents().add(cd);
                }
            }
        } else {
            //Limpa a lista de detalhes e componentes antes de salvar
            //Depois elas serão novamente adicionada ao conjunto
            set.setDetails(new HashSet());
            set.setComponents(new HashSet());

            //Salva o conjunto
            facade.add(getSet());

            //Salva os detalhes e os adiciona ao conjunto novamente
            for ( SetDetail cd : details ) {
                cd.getPrimaryKey().setSet(set);
                facade.addDetail(cd);
                set.getDetails().add(cd);
            }

            //Salva os componentes e os adiciona ao conjunto novamente
            for ( SetComponent cd : components ) {
                cd.getPrimaryKey().setSet(set);
                facade.addComponent(cd);
                set.getComponents().add(cd);
            }
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Conjunto
    //--------------------------------------------------------------------------
    /**
     * @return Lista de detalhes do conjunto
     */
    public List<SetDetail> getSetDetails() {
        return getSet().getOrderedDetails();
    }

    /**
     * @return Código do conjunto
     */
    public String getSetId() {
        return getSet().getId();
    }

    /**
     * @return Lista de componentes que formam o conjunto
     */
    public java.util.Set<SetComponent> getSetComponents() {
        return getSet().getComponents();
    }

    /**
     * @return Valor do lead time para componentes
     */
    public double getComponentsLeadTimeValue() {
        return getSet().getComponentsLeadTime().getValue();
    }

    /**
     * @return O tipo de lead time para componentes
     */
    public String getComponentsLeadTimeType() {
        return getSet().getComponentsLeadTime().getType();
    }

    /**
     * Calcula o lead time esperado para todos componentes e retorna o valor
     * calculado.
     * 
     * @return Valor do lead time para componentes
     */
    public double getCalculatedComponentsLeadTimeValue() {
        getSet().calculateComponentsLeadTime();
        return getSet().getComponentsLeadTime().getValue();
    }

    /**
     * @return Valor do lead time para o conjunto
     */
    public String getTotalLeadTime() {
        return getSet().getTotalLeadTime() + " horas";
    }

    /**
     * Seta o código do conjunto
     *
     * @param id Código do conjunto
     */
    public void setSetId( String id ) {
        getSet().setId(id);
    }

    /**
     * Seta o lead time para componentes do conjunto
     * 
     * @param value Valor do lead time
     * @param type Tipo do lead time
     */
    public void setComponentsLeadTime( Double value, String type ) {
        LeadTime lt = new LeadTime();
        lt.setValue(value);
        lt.setType(type);

        getSet().setComponentsLeadTime(lt);
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Componentes do Conjunto
    //--------------------------------------------------------------------------
    /**
     * Cria um novo componente de conjunto
     */
    public void newSetComponent() {
        componentEdit = false;
        setComponent = new SetComponent();
    }

    /**
     * Edita um componente de conjunto
     *
     * @param sc O componente de conjunto a ser editado
     */
    public void editSetComponent( SetComponent sc ) {
        componentEdit = true;
        setComponent = sc;
    }

    /**
     * Salva um componente de conjunto
     * 
     * @throws SetComponentPersistenceException
     * @throws ValidatorException
     */
    public void saveSetComponent() throws ValidatorException, SetComponentPersistenceException {
        if ( !isComponentEdit() ) { //Novo componente
            if ( !isEdit() ) { //Novo Conjunto
                components.add(setComponent);
                set.getComponents().add(setComponent);
            } else { //Edição de Conjunto
                facade.addComponent(setComponent);
                set.getComponents().add(setComponent);
            }
        } else { //Edição de componente
            if ( isEdit() ) { //Edição de Conjunto
                facade.modifyComponent(setComponent);
            }
        }
    }

    /**
     * Remove um componente de conjunto
     * 
     * @throws SetComponentPersistenceException
     */
    public void removeSetComponent() throws SetComponentPersistenceException {
        if ( !isEdit() ) {
            set.getComponents().remove(setComponent);
            components.remove(setComponent);
        } else {
            set.getComponents().remove(setComponent);
            facade.removeComponent(setComponent);
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Componente de Conjunto
    //--------------------------------------------------------------------------
    /**
     * Seta o componente do componente de conjunto novo ou em edição
     *
     * @param id O código do componente
     * @return True se o componente não existir no conjunto ou false se já existir
     * @throws ComponentPersistenceException
     */
    public boolean setSetComponentComponent( String id ) throws ComponentPersistenceException {
        Component c = componentFacade.getComponent(id);

        if ( isEdit() ) {
            if ( getSet().getSetComponent(c) != null &&
                    !setComponent.getPrimaryKey().getComponent().getId().equals( id ) ) {
                return false;
            }
        } else {
            for ( SetComponent cd : components ) {
                if ( cd.getPrimaryKey().getComponent().getId().equals( id ) ) {
                    if ( isComponentEdit() ) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        if ( !isComponentEdit() ) {
            SetComponentKey pk = new SetComponentKey();
            pk.setSet(set);

            setComponent.setPrimaryKey(pk);
        }

        setComponent.getPrimaryKey().setComponent(c);

        return true;
    }

    /**
     * Seta a quantidade do componente de conjunto
     *
     * @param quantity A quantidade do componente
     */
    public void setSetComponentComponentQuantity( double quantity ) {
        setComponent.setComponentQuantity(quantity);
    }

    /**
     * @return Código do componente de conjunto
     */
    public String getSetComponentComponentId() {
        return setComponent.getPrimaryKey().getComponent().getId();
    }

    /**
     * @return Quantidade do componente
     */
    public double getSetComponentComponentQuantity() {
        return setComponent.getComponentQuantity();
    }

    //--------------------------------------------------------------------------
    //Métodos de ação de Detalhe de Conjunto
    //--------------------------------------------------------------------------
    /**
     * Cria um novo detalhe de conjunto
     */
    public void newSetDetail() {
        detailEdit = false;
        setDetail = new SetDetail();
    }

    /**
     * Edita um detalhe de conjunto
     *
     * @param cd O detalhe de conjunto a ser editado
     */
    public void editSetDetail( SetDetail cd ) {
        detailEdit = true;
        setDetail = cd;
    }

    /**
     * Salva um detalhe de conjunto
     * 
     * @throws SetDetailPersistenceException
     * @throws ValidatorException
     */
    public void saveSetDetail() throws SetDetailPersistenceException, ValidatorException {
        if ( !isDetailEdit() ) { //Novo detalhe
            if ( !isEdit() ) { //Novo Conjunto
                details.add(setDetail);
                set.getDetails().add(setDetail);
            } else { //Edição de Conjunto
                facade.addDetail(setDetail);
                set.getDetails().add(setDetail);
            }
        } else { //Edição de detalhe
            if ( isEdit() ) { //Edição de Conjunto
                facade.modifyDetail(setDetail);
            }
        }
    }

    /**
     * Remove um detalhe de conjunto
     * 
     * @throws SetDetailPersistenceException
     */
    public void removeSetDetail() throws SetDetailPersistenceException {
        if ( !isEdit() ) {
            set.getDetails().remove(setDetail);
            details.remove(setDetail);
        } else {
            set.getDetails().remove(setDetail);
            facade.removeDetail(setDetail);
        }
    }

    //--------------------------------------------------------------------------
    //Métodos de acesso a dados de Detalhe de Conjunto
    //--------------------------------------------------------------------------
    /**
     * Seta a operação do detalhe de conjunto novo ou em edição
     * 
     * @param id O código da operação
     * @return True se o detalhe não existir no conjunto ou false se já existir
     * @throws OperationPersistenceException
     */
    public boolean setSetDetailOperation( Integer id ) throws OperationPersistenceException {
        Operation o = operationFacade.getOperation(id);

        if ( isEdit() ) {
            if ( getSet().getSetDetail(o) != null &&
                    setDetail.getPrimaryKey().getOperation().getId().intValue() != id ) {
                return false;
            }
        } else {
            for ( SetDetail cd : details ) {
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
            SetDetailKey pk = new SetDetailKey();
            pk.setSet(set);

            setDetail.setPrimaryKey(pk);
        }

        setDetail.getPrimaryKey().setOperation(o);

        return true;
    }

    /**
     * Seta a ordem do detalhe de conjunto
     *
     * @param order A ordem do conjunto
     */
    public void setSetDetailOrder( Integer order ) {
        setDetail.setOrder(order);
    }

    /**
     * Seta o lead time do detalhe de conjunto
     * 
     * @param value O valor do lead time
     * @param type O tipo de lead time
     */
    public void setSetDetailLeadTime( Double value, String type ) {
        LeadTime lt = new LeadTime();
        lt.setValue(value);
        lt.setType(type);

        setDetail.setLeadTime(lt);
    }

    /**
     * @return Código da operação do detalhe de conjunto
     */
    public int getSetDetailOperationId() {
        return setDetail.getPrimaryKey().getOperation().getId();
    }

    /**
     * @return Descrição da operação do detalhe de conjunto
     */
    public String getSetDetailOperationDescription() {
        return setDetail.getPrimaryKey().getOperation().getDescription();
    }

    /**
     * @return A ordem do detalhe de conjunto
     */
    public int getSetDetailOrder() {
        return setDetail.getOrder();
    }

    /**
     * @return Valor do lead time do detalhe de conjunto
     */
    public double getSetDetailLeadTimeValue() {
        return setDetail.getLeadTime().getValue();
    }

    /**
     * @return Tipo do lead time do detalhe de conjunto
     */
    public String getSetDetailLeadTimeType() {
        return setDetail.getLeadTime().getType();
    }

    //--------------------------------------------------------------------------
    //Métodos de listagem de Conjunto
    //--------------------------------------------------------------------------
    /**
     * Lista todos os conjuntos
     *
     * @return Lista de todos os conjuntos
     * @throws SetPersistenceException
     */
    public List<Set> getSets() throws SetPersistenceException {
        return facade.list("id", "asc");
    }

    /**
     * Lista os conjuntos que batem com os parâmetros informados.
     * 
     * @param field O nome do campo a ser buscado
     * @param value O valor a ser procurado no campo
     * @param operator O operador lógico da busca
     * @return Lista de conjuntos encontrados
     * @throws SetPersistenceException
     */
    public List<Set> getSets( SearchParam field , String value , SearchParam operator ) throws SetPersistenceException {
        return facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Retorna um conjunto com a chave primária informada
     * 
     * @param id Código do conjunto
     * @return Conjunto com a chave informada ou null se não encontrar nada
     * @throws SetPersistenceException
     */
    public Set getSet( String id ) throws SetPersistenceException {
        return facade.getSet(id);
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
        set = null;
        setDetail = null;
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
    public Set getSet() {
        return set;
    }

    /**
     * @return the componentEdit
     */
    public boolean isComponentEdit() {
        return componentEdit;
    }
}
