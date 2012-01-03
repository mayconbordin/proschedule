package com.proschedule.core.scheduling.model;

import com.proschedule.core.scheduling.model.keys.OrderDetailKey;

/**
 * Detalhamento da Ordem de Produção - Indica a quantidade
 * que deve ser produzida de cada componente que compõe o
 * conjunto.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:53
 */
public class OrderDetail implements java.io.Serializable {
    /**
     * Chave primária composta por Order e Component
     */
    private OrderDetailKey primaryKey;

    /**
     * Quantidade de componente a ser produzida
     */
    private double componentQuantity;

    /**
     * Construtor da Classe
     */
    public OrderDetail(){

    }

    /**
     * @return the primaryKey
     */
    public OrderDetailKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(OrderDetailKey primaryKey) {
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