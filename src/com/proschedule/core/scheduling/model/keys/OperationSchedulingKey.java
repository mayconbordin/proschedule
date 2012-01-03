/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.scheduling.model.keys;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.util.pk.PrimaryKey;
import java.io.Serializable;

/**
 * Chave Primária Composta por Day e Operation
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 * @see com.proschedule.core.scheduling.model.OperationScheduling
 */
public class OperationSchedulingKey implements Serializable, PrimaryKey {
    /**
     * Operação do sequenciamento
     */
    private Operation operation;

    /**
     * Dia do sequenciamento
     */
    private Day day;

    /**
     * Construtor da Classe
     *
     * @param operation A operação da chave
     * @param day O dia da chave
     */
    public OperationSchedulingKey(Operation operation, Day day) {
        this.setOperation(operation);
        this.setDay(day);
    }

    /**
     * Construtor da Classe
     */
    public OperationSchedulingKey() {

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
     * Recupera a chave.
     * @return OperationSchedulingKey
     */
    public Object getKey() {
        return this;
    }

    public void setKey(Object obj) {
        if (obj instanceof OperationSchedulingKey) {
            OperationSchedulingKey pk = (OperationSchedulingKey)obj;

            this.setOperation(pk.getOperation());
            this.setDay(pk.getDay());
        }
    }
}
