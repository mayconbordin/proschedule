package com.proschedule.core.persistence.model;

/**
 * Cliente que faz os pedidos que se transformam
 * em ordens de produção.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:51
 */
public class Customer {
    /**
     * Código do cliente
     */
    private String id;

    /**
     * Nome do cliente
     */
    private String name;

    /**
     * Construtor da Classe
     */
    public Customer(){

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}