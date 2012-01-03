package com.proschedule.core.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Conjunto, que é formado por operações e passa
 * por operações durante o processo de fabricação.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:53
 */
public class Set implements java.io.Serializable {
    /**
     * Código do conjunto
     */
    private String id;

    private Double leadTimeValue;

    private String leadTimeType;

    /**
     * Lead time para produção de todos os componentes
     */
    private LeadTime componentsLeadTime;

    /**
     * Lista dos componentes que formam o conjunto
     */
    private java.util.Set<SetComponent> components = new HashSet<SetComponent>(0);

    /**
     * Detalhamento do conjunto (operações e lead time)
     */
    private java.util.Set<SetDetail> details = new HashSet<SetDetail>(0);

    /**
     * Construtor da Classe
     */
    public Set() {

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

    /**
     * @return the componentsLeadTime
     */
    public LeadTime getComponentsLeadTime() {
        return new LeadTime(leadTimeType,leadTimeValue);
    }

    /**
     * @param componentsLeadTime the componentsLeadTime to set
     */
    public void setComponentsLeadTime(LeadTime componentsLeadTime) {
        this.componentsLeadTime = componentsLeadTime;
        this.leadTimeType = componentsLeadTime.getType();
        this.leadTimeValue = componentsLeadTime.getValue();
    }
    
    /**
     * @return the components
     */
    public java.util.Set<SetComponent> getComponents() {
        return components;
    }

    /**
     * @param components the components to set
     */
    public void setComponents(java.util.Set<SetComponent> components) {
        this.components = components;
    }

    /**
     * @return the details
     */
    public java.util.Set<SetDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(java.util.Set<SetDetail> details) {
        this.details = details;
    }

    /**
     * Retorna um detalhe de conjunto com a operação indicada.
     *
     * @param operation A operação pela qual será feita a busca
     * @return O detalhe de conjunto ou null se nada for encontrado
     */
    public SetDetail getSetDetail( Operation operation ) {
        for ( SetDetail cd : details ) {
            if ( cd.getPrimaryKey().getOperation().getId().intValue() == operation.getId().intValue() ) {
                return cd;
            }
        }

        return null;
    }

    /**
     * Retorna um componente de conjunto com a operação indicada.
     *
     * @param component A operação pela qual será feita a busca
     * @return O componente do conjunto ou null se nada for encontrado
     */
    public SetComponent getSetComponent( Component component ) {
        for ( SetComponent cd : components ) {
            if ( cd.getPrimaryKey().getComponent().getId().equals( component.getId() ) ) {
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
    public List<SetDetail> getOrderedDetails() {
        List<SetDetail> list = new ArrayList();

        SetDetail min = null;
        int flag = 0;

        //Percorre todos os detalhes ao quadrado
        for ( int i = 0; i < details.size(); i++ ) {
            for ( SetDetail cd : details ) {
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

    /**
     * Calcula o lead time para a produção de todos os componentes do conjunto
     * em horas.
     */
    public void calculateComponentsLeadTime() {
        //É a soma do lead time das operações de um componente
        //O maior deles é o lead time padrão
        Double max = 0.0;

        //Percorre cada um dos componentes
        for ( SetComponent sc : components ) {
            Double sum = 0.0;
            Component c = sc.getPrimaryKey().getComponent();

            for ( ComponentDetail cd : c.getDetails() ) {
                sum += cd.getLeadTimeValue();
            }

            if ( sum > max ) {
                max = sum;
            }
        }

        LeadTime lt = new LeadTime();
        lt.setValue(max);
        lt.setType("horas");

        setComponentsLeadTime(lt);
    }

    /**
     * Calcula o lead time necessário para produzir o conjunto. Somando o lead time
     * para componentes com o lead time para as operações de conjunto.
     * 
     * @return Valor do lead time do conjunto
     */
    public double getTotalLeadTime() {
        try {
            double leadTime = leadTimeValue;

            for ( SetDetail sd : details ) {
                leadTime += sd.getLeadTime().getValue();
            }

            return leadTime;
        } catch (NullPointerException ex) {
            return 0.0;
        }
    }

}