package com.proschedule.util.search;

/**
 * Armazena os dados do operador lógico de busca
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class SearchParam {
    /**
     * Descrição do campo. Nome com formatação.
     */
    private String description;

    /**
     * Nome do campo, mesmo usado pela classe.
     */
    private String name;

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

    public String toString() {
        return description;
    }
}
