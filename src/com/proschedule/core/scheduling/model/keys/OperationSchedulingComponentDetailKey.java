/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.scheduling.model.keys;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta de Operation, Day, Component e Order
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 * @see com.proschedule.core.scheduling.model.OperationSchedulingDetail
 */
public class OperationSchedulingComponentDetailKey implements Serializable, PrimaryKey {
    /**
     * Operação do Sequenciamento
     */
    private Operation operation;

    /**
     * Dia do sequenciamento
     */
    private Day day;

    /**
     * Componente a ser sequenciado
     */
    private Component component;

    /**
     * Ordem de produção a qual pertence o componente
     */
    private Order order;

    /**
     * Construtor da Classe
     *
     * @param operation A operação da chave
     * @param day O dia da chave
     * @param component O componente da chave
     * @param order A ordem de produção da chave
     */
    public OperationSchedulingComponentDetailKey(Operation operation, Day day, Component component, Order order) {
        this.setOperation(operation);
        this.setDay(day);
        this.setComponent(component);
        this.setOrder(order);
    }

    /**
     * Construtor da Classe
     */
    public OperationSchedulingComponentDetailKey() {

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
     * @return the day
     */
    public Day getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(Day day) {
        this.day = day;
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
     * Recupera a chave.
     * @return OperationSchedulingKey
     */
    public Object getKey() {
        return this;
    }

    public void setKey(Object obj) {
        if (obj instanceof OperationSchedulingComponentDetailKey) {
            OperationSchedulingComponentDetailKey pk = (OperationSchedulingComponentDetailKey)obj;

            this.setOperation(pk.getOperation());
            this.setDay(pk.getDay());
            this.setComponent(pk.getComponent());
            this.setOrder(pk.getOrder());
        }
    }
}
