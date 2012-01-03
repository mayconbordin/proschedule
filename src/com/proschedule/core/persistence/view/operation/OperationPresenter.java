package com.proschedule.core.persistence.view.operation;

import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.facade.OperationTypeFacade;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.ArrayList;
import java.util.List;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views do Operation.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class OperationPresenter {
    private OperationFacade facade;
    private Operation operation;
    private List<Operation> operations;
    private boolean edit = false;
    private OperationTypeFacade typeFacade;
    
    private String typeFilter;

    /**
     * Construtor da Classe - Carrega o facade
     */
    public OperationPresenter() {
        facade = new OperationFacade();
        typeFacade = new OperationTypeFacade();
    }

    /**
     * Carrega a lista de operações.
     *
     * @throws OperationPersistenceException
     */
    public void loadOperations() throws OperationPersistenceException {
        operations = facade.list("id", "desc");
    }

    /**
     * Carrega a lista de operações para campo string.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @throws OperationPersistenceException
     */
    public void loadOperations( SearchParam field , String value , SearchParam operator ) throws OperationPersistenceException {
        operations = facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Carrega a lista de operações para campo inteiro.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @throws OperationPersistenceException
     */
    public void loadOperations( SearchParam field , Integer value , SearchParam operator ) throws OperationPersistenceException {
        operations = facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Carrega a lista de operações para campo OperationType.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @throws OperationPersistenceException
     */
    public void loadOperations(String value , SearchParam operator ) throws OperationPersistenceException {
        OperationType type = new OperationType();
        type.setDescription(value);
        
        operations = facade.list(type, operator.getName());
    }

    /**
     * Salva o operação que está em edição ou a nova operação.
     *
     * @param description Descrição da operação
     * @param leadTimeValue Valor do lead time da operação
     * @param leadTimeType Tipo do lead time da operação
     * @param type O tipo de operação
     * @throws OperationPersistenceException
     * @throws ValidatorException
     */
    public void save( String description, Double leadTimeValue, String leadTimeType, OperationType type ) throws OperationPersistenceException, ValidatorException {
        if ( !isEdit() ) {
            operation = new Operation();
        }

        LeadTime lt = new LeadTime();
        lt.setValue(leadTimeValue);
        lt.setType(leadTimeType);

        operation.setDescription(description);
        operation.setLeadTime(lt);
        operation.setType(type);

        if ( isEdit() ) {
            facade.modify(operation);
        } else {
            facade.add(operation);
        }
    }

    /**
     * Remove a operação do banco de dados.
     * @throws OperationPersistenceException
     */
    public void remove() throws OperationPersistenceException {
        facade.remove(operation);
    }

    /**
     * Lista os tipos de operação
     * 
     * @return Lista dos tipos de operação
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> listTypes() throws OperationTypePersistenceException {
        return typeFacade.list();
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Obtém a lista de operações
     *
     * @param useFilter Usar ou não filtro de tipo para operações
     * @return the operations
     */
    public List<Operation> getOperations( boolean useFilter ) {
        if ( useFilter ) {
            List<Operation> list = new ArrayList();

            for ( Operation o : operations ) {
                if ( o.getType().getDescription().equals(typeFilter) ) {
                    list.add(o);
                }
            }

            return list;
        } else {
            return operations;
        }
    }

    /**
     * @return the edit
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * @param edit the edit to set
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    /**
     * @param typeFilter the typeFilter to set
     */
    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }
}
