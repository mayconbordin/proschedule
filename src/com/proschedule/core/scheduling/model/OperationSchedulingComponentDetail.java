package com.proschedule.core.scheduling.model;

import com.proschedule.core.scheduling.model.keys.OperationSchedulingComponentDetailKey;


/**
 * Detalhamento do Sequenciamento por Operação por Dia - indica
 * as quantidades necessarias a serem produzidas de um componente.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class OperationSchedulingComponentDetail implements java.io.Serializable {
    /**
     * Chave Primária Composta por Operation, Day, Component e Order
     */
    private OperationSchedulingComponentDetailKey primaryKey;
    
    /**
     * Construtor da Classe
     */
    public OperationSchedulingComponentDetail(){

    }

    /**
     * @return the primaryKey
     */
    public OperationSchedulingComponentDetailKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(OperationSchedulingComponentDetailKey primaryKey) {
        this.primaryKey = primaryKey;
    }
}