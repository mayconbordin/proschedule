package com.proschedule.core.persistence.model;

/**
 * O LeadTime armazena o valor e o tipo de lead time.
 * É o tempo necessário para finalizar uma tarefa.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:51
 */
public class LeadTime {
    /**
     * Tipo de lead time
     */
    private String type;

    /**
     * Valor do lead time
     */
    private Double value;

    /**
     * Construtor da Classe
     */
    public LeadTime() {

    }

    /**
     * Construtor da Classe - inicializando valores
     * @param type tipo de valor de lead time
     * @param value o valor do lead time
     */
    public LeadTime(String type, Double value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }
}