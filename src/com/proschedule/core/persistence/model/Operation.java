package com.proschedule.core.persistence.model;

/**
 * Componentes e Conjuntos passam por operações durante
 * o processo de fabricação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class Operation {
    /**
     * Código da operação
     */
    private Integer id;

    /**
     * Descrição da operação
     */
    private String description;

    private Double leadTimeValue;

    private String leadTimeType;
    
    /**
     * Lead time para a operação
     */
    private LeadTime leadTime;

    /**
     * O tipo de operação
     */
    private OperationType type;

    /**
     * Construtor da Classe
     */
    public Operation(){

    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the type
     */
    public OperationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(OperationType type) {
        this.type = type;
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