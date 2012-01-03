package com.proschedule.core.persistence.model;

import com.proschedule.core.persistence.model.keys.SetComponentKey;

/**
 * Componentes de um Conjunto - informa os componentes que formam um conjunto.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:53
 */
public class SetComponent implements java.io.Serializable {
    /**
     * Chave Primária Composta de Operation e Set
     */
    private SetComponentKey primaryKey;

    private double componentQuantity;

    /**
     * Construtor da Classe
     */
    public SetComponent() {

    }

    /**
     * @return the primaryKey
     */
    public SetComponentKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(SetComponentKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the componentQuantity
     */
    public double getComponentQuantity() {
        return componentQuantity;
    }

    /**
     * @param componentQuantity the componentQuantity to set
     */
    public void setComponentQuantity(double componentQuantity) {
        this.componentQuantity = componentQuantity;
    }
}
