package com.proschedule.core.persistence.model;

import com.proschedule.core.persistence.model.keys.SetDetailKey;

/**
 * Detalhamento de um Conjunto - informa uma operação
 * pela qual o conjunto passa, o tempo que leva para fazer ela
 * e a ordem em que acontece.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:53
 */
public class SetDetail implements java.io.Serializable {
    /**
     * Chave Primária Composta de Operation e Set
     */
    private SetDetailKey primaryKey;

    private Double leadTimeValue;

    private String leadTimeType;

    /**
     * Lead time para a operação
     */
    private LeadTime leadTime;

    /**
     * Ordem em que a operação é realizada
     */
    private Integer order;

    /**
     * Construtor da Classe
     */
    public SetDetail() {

    }

    /**
     * @return the primaryKey
     */
    public SetDetailKey getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(SetDetailKey primaryKey) {
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