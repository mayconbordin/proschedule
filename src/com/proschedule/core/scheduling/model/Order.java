package com.proschedule.core.scheduling.model;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Set;
import java.util.Date;
import java.util.HashSet;


/**
 * Ordem de Produção - informa a quantidade a ser produzida de
 * determinado conjunto em uma data de entrega especifica.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class Order implements java.io.Serializable  {
    /**
     * Código da ordem de produção
     */
    private String id;

    /**
     * Quantidade a ser produzida do conjunto
     */
    private double setQuantity;

    /**
     * Data de entrega da ordem de produção
     */
    private Date deliveryDate;

    /**
     * Conjunto a ser produzido
     */
    private Set set;

    /**
     * Cliente que solicitou a produção do conjunto
     */
    private Customer customer;

    /**
     * A qual sequenciamento mestre pertence a ordem de produção
     */
    private Day masterScheduling;

    /**
     * Detalhamento da ordem de produção
     */
    private java.util.Set<OrderDetail> details = new HashSet<OrderDetail>(0);

    /**
     * Construtor da Classe
     */
    public Order(){

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
     * @return the setQuantity
     */
    public double getSetQuantity() {
        return setQuantity;
    }

    /**
     * @param setQuantity the setQuantity to set
     */
    public void setSetQuantity(double setQuantity) {
        this.setQuantity = setQuantity;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the set
     */
    public Set getSet() {
        return set;
    }

    /**
     * @param set the set to set
     */
    public void setSet(Set set) {
        this.set = set;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the masterScheduling
     */
    public Day getMasterScheduling() {
        return masterScheduling;
    }

    /**
     * @param masterScheduling the masterScheduling to set
     */
    public void setMasterScheduling(Day masterScheduling) {
        this.masterScheduling = masterScheduling;
    }

    /**
     * @return the details
     */
    public java.util.Set<OrderDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(java.util.Set<OrderDetail> details) {
        this.details = details;
    }

    /**
     * Retorna um detalhe de ordem de produção com o componente indicado.
     *
     * @param component O componente pela qual será feita a busca
     * @return O detalhe da ordem de produção ou null se nada for encontrado
     */
    public OrderDetail getOrderDetail( Component component ) {
        for ( OrderDetail od : details ) {
            if ( od.getPrimaryKey().getComponent().getId().equals( component.getId() ) ) {
                return od;
            }
        }

        return null;
    }


}