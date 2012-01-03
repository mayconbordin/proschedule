package com.proschedule.core.persistence.model.keys;

import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta para ComponentDetail.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 * @see com.proschedule.core.persistence.model.ComponentDetail
 */
public class ComponentDetailKey implements Serializable, PrimaryKey {
    /**
     * A operação que compõe a chave
     */
    private Operation operation;

    /**
     * O componente que compõe a chave
     */
    private Component component;

    /**
     * Construtor da Classe
     */
    public ComponentDetailKey() {
        this.setOperation(operation);
        this.setComponent(component);
    }

    /**
     * Construtor da Classe
     *
     * @param operation A operação que irá compor a chave
     * @param component O componente que irá compor a chave
     */
    public ComponentDetailKey(Operation operation, Component component) {
        this.setOperation(operation);
        this.setComponent(component);
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
     * @return the component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(Component component) {
        this.component = component;
    }

    /**
     * Recupera a chave.
     * @return ComponentDetailKey
     */
    public Object getKey() {
        return this;
    }

    /**
     * Seta a chave.
     * 
     * @param obj A instância da chave primária
     */
    public void setKey(Object obj) {
        if (obj instanceof ComponentDetailKey) {
            ComponentDetailKey pk = (ComponentDetailKey)obj;

            this.setOperation(pk.getOperation());
            this.setComponent(pk.getComponent());
        }
    }
}
