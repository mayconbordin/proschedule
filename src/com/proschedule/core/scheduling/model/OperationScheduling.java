package com.proschedule.core.scheduling.model;

import com.proschedule.core.scheduling.model.keys.OperationSchedulingKey;
import java.util.HashSet;
import java.util.Set;

/**
 * Sequenciamento por Operação por Dia - detalha os componentes
 * que devem começar a ser produzidos no dia em determinada operação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class OperationScheduling implements java.io.Serializable {
    /**
     * Chave Primária Composta por Day e Operation
     */
    private OperationSchedulingKey primaryKey;

    /**
     * Detalhamentos do sequenciamento por operação para componentes
     */
    private Set<OperationSchedulingComponentDetail> componentDetails = new HashSet<OperationSchedulingComponentDetail>(0);

    /**
     * Detalhamentos do sequenciamento por operação para conjuntos
     */
    private Set<OperationSchedulingSetDetail> setDetails = new HashSet<OperationSchedulingSetDetail>(0);

    /**
     * Construtor da Classe
     */
    public OperationScheduling(){
    
    }

    /**
     * @return the primaryKey
     */
    public OperationSchedulingKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(OperationSchedulingKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the details
     */
    public Set<OperationSchedulingComponentDetail> getComponentDetails() {
        return componentDetails;
    }

    /**
     * @param details the details to set
     */
    public void setComponentDetails(Set<OperationSchedulingComponentDetail> details) {
        this.componentDetails = details;
    }

    /**
     * @return the setDetails
     */
    public Set<OperationSchedulingSetDetail> getSetDetails() {
        return setDetails;
    }

    /**
     * @param setDetails the setDetails to set
     */
    public void setSetDetails(Set<OperationSchedulingSetDetail> setDetails) {
        this.setDetails = setDetails;
    }

}