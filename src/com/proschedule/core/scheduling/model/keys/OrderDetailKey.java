/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.scheduling.model.keys;

import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta por Order e Component
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:53
 * @see com.proschedule.core.scheduling.model.OrderDetail
 */
public class OrderDetailKey implements Serializable, PrimaryKey {
    private Order order;
    private Component component;

    /**
     * Construtor da Classe
     * @param order Ordem de produção da chave
     * @param component Componente da chave
     */
    public OrderDetailKey(Order order, Component component) {
        this.setOrder(order);
        this.setComponent(component);
    }

    /**
     * Construtor da Classe
     */
    public OrderDetailKey() {

    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.order = order;
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
     * @return OperationSchedulingKey
     */
    public Object getKey() {
        return this;
    }

    public void setKey(Object obj) {
        if (obj instanceof OrderDetailKey) {
            OrderDetailKey pk = (OrderDetailKey)obj;

            this.setOrder(pk.getOrder());
            this.setComponent(pk.getComponent());
        }
    }
}
