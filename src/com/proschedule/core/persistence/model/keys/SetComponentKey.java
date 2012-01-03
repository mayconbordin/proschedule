package com.proschedule.core.persistence.model.keys;

import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta para SetComponent
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 * @see com.proschedule.core.persistence.model.SetComponent
 */
public class SetComponentKey implements Serializable, PrimaryKey {
    private Set set;
    private Component component;

    /**
     * Construtor da Classe
     *
     * @param set Conjunto da chave
     * @param component Componente da chave
     */
    public SetComponentKey(Set set, Component component) {
        this.setSet(set);
        this.setComponent(component);
    }

    /**
     * Construtor da Classe
     */
    public SetComponentKey() {

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
     * Seta a chave
     * @param obj
     */
    public void setKey(Object obj) {
        if (obj instanceof SetComponentKey) {
            SetComponentKey pk = (SetComponentKey)obj;

            this.setSet(pk.getSet());
            this.setComponent(pk.getComponent());
        }
    }
}
