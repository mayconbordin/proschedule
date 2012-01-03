package com.proschedule.core.persistence.model;

import com.proschedule.core.persistence.model.keys.ComponentDetailKey;

/**
 * Detalhamento dos componentes - informa uma operação pela
 * qual o componente passa, o lead time nessa operação e a
 * ordem de acontecimento dessa operação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentDetail implements java.io.Serializable {
    /**
     * Chave primária composta (Operation e Component)
     */
    private ComponentDetailKey primaryKey;

    /**
     * Valor do lead time
     */
    private Double leadTimeValue;

    /**
     * Tipo de lead time
     */
    private String leadTimeType;

    /**
     * Lead time do componente na operação - é um objeto que armazena
     * o leadTimeValue e o leadTimeType.
     */
    private LeadTime leadTime;

    /**
     * A ordem em que a operação ocorre
     */
    private Integer order;

    /**
     * Construtor da Classe
     */
    public ComponentDetail(){

    }

    /**
     * @return the primaryKey
     */
    public ComponentDetailKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(ComponentDetailKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the leadTime
     */
    public LeadTime getLeadTime() {
        return new LeadTime(leadTimeType,leadTimeValue);
    }

    /**
     * @param leadTime the leadTime to set
     */
    public void setLeadTime(LeadTime leadTime) {
        this.leadTime = leadTime;
        this.leadTimeType = leadTime.getType();
        this.leadTimeValue = leadTime.getValue();
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return the leadTimeValue
     */
    public Double getLeadTimeValue() {
        return leadTimeValue;
    }

    /**
     * @param leadTimeValue the leadTimeValue to set
     */
    public void setLeadTimeValue(Double leadTimeValue) {
        this.leadTimeValue = leadTimeValue;
    }

    /**
     * @return the leadTimeType
     */
    public String getLeadTimeType() {
        return leadTimeType;
    }

    /**
     * @param leadTimeType the leadTimeType to set
     */
    public void setLeadTimeType(String leadTimeType) {
        this.leadTimeType = leadTimeType;
    }
}