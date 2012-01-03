package com.proschedule.core.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Componentes compõe um conjunto, e passam por
 * operações durante o processo de fabricação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class Component implements java.io.Serializable {
    /**
     * Código do componente
     */
    private String id;

    /**
     * Descrição da matéria prima utilizada
     */
    private String rawMaterial;

    /**
     * Detalhamentos do componente (operações e lead time)
     */
    private java.util.Set<ComponentDetail> details = new HashSet<ComponentDetail>(0);

    /**
     * Construtor da Classe
     */
    public Component(){

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
     * @return the rawMaterial
     */
    public String getRawMaterial() {
        return rawMaterial;
    }

    /**
     * @param rawMaterial the rawMaterial to set
     */
    public void setRawMaterial(String rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    /**
     * @return the details
     */
    public java.util.Set<ComponentDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(java.util.Set<ComponentDetail> details) {
        this.details = details;
    }


    /**
     * Retorna um detalhe de componente com a operação indicada.
     * 
     * @param operation A operação pela qual será feita a busca
     * @return O detalhe de componente ou null se nada for encontrado
     */
    public ComponentDetail getComponentDetail( Operation operation ) {
        for ( ComponentDetail cd : details ) {
            if ( cd.getPrimaryKey().getOperation().getId().intValue() == operation.getId().intValue() ) {
                return cd;
            }
        }

        return null;
    }

    /**
     * Retorna a lista de detalhes ordenada pelo atributo order em forma crescente
     * 
     * @return Lista de detalhes
     */
    public List<ComponentDetail> getOrderedDetails() {
        List<ComponentDetail> list = new ArrayList();

        ComponentDetail min = null;
        int flag = 0;

        //Percorre todos os detalhes ao quadrado
        for ( int i = 0; i < details.size(); i++ ) {
            for ( ComponentDetail cd : details ) {
                //Verifica se já está na lista
                if ( !list.contains(cd) ) {
                    //O primeiro é a base de comparação
                    if ( flag == 0 ) {
                        min = cd;
                    }

                    //Se achar alguém menor, ele é o escolhido
                    if ( cd.getOrder() < min.getOrder() ) {
                        min = cd;
                    }

                    //Adiciona +1
                    flag++;
                }
            }

            //Adiciona o menor na lista
            list.add(min);
            //Zera o indicador
            flag = 0;
        }

        return list;
    }
}