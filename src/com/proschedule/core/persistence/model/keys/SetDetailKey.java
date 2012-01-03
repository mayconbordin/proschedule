package com.proschedule.core.persistence.model.keys;

import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta para SetDetail
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 * @see com.proschedule.core.persistence.model.SetDetail
 */
public class SetDetailKey implements Serializable, PrimaryKey {
    private Set set;
    private Operation operation;

    /**
     * Construtor da Classe
     *
     * @param set Conjunto da chave
     * @param operation Operação da chave
     */
    public SetDetailKey(Set set, Operation operation) {
        this.setSet(set);
        this.setOperation(operation);
    }

    /**
     * Construtor da Classe
     */
    public SetDetailKey() {

    }

    /**
     * @return the set
     */
    public Set getSet() {
        return set;
    }

    /**
     * @param set the set to set
     */
    public void setSet(Set set) {
        this.set = set;
    }

    /**
     * @return the component
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Recupera a chave.
     * @return ComponentDetailKey
     */
    public Object getKey() {
        return this;
    }

    /**
     * Seta a chave
     * @param obj
     */
    public void setKey(Object obj) {
        if (obj instanceof SetDetailKey) {
            SetDetailKey pk = (SetDetailKey)obj;

            this.setSet(pk.getSet());
            this.setOperation(pk.getOperation());
        }
    }
}
