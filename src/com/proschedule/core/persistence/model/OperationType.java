package com.proschedule.core.persistence.model;

/**
 * Descreve os tipos de operação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class OperationType {
    /**
     * Constante do tipo de descrição para componentes
     */
    public static final String COMPONENT_TYPE_DESCRIPTION = "Componente";
    
    /**
     * Constante do tipo de descrição para conjuntos
     */
    public static final String SET_TYPE_DESCRIPTION = "Conjunto";

    /**
     * Código do tipo de operação
     */
    private Integer id;

    /**
     * Descrição do tipo de operação
     */
    private String description;

    /**
     * Construtor da Classe
     */
    public OperationType(){

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

    @Override
    public String toString() {
        return description;
    }
}