package com.proschedule.core.persistence.view.customer;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.facade.CustomerFacade;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views do Customer.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class CustomerPresenter {
    private CustomerFacade facade;
    private Customer customer;
    private List<Customer> customers;

    /**
     * Construtor da Classe - Carrega o facade
     */
    public CustomerPresenter() {
        facade = new CustomerFacade();
    }

    /**
     * Carrega a lista de clientes.
     *
     * @throws CustomerPersistenceException
     */
    public void loadCustomers() throws CustomerPersistenceException {
        customers = facade.list("id", "asc");
    }

    /**
     * Carrega a lista de clientes.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @throws CustomerPersistenceException
     */
    public void loadCustomers( SearchParam field , String value , SearchParam operator ) throws CustomerPersistenceException {
         customers = facade.list(field.getName(), value, operator.getName());
    }

    /**
     * Salva o cliente que está em edição.
     *
     * @throws CustomerPersistenceException
     * @throws ValidatorException
     */
    public void save() throws CustomerPersistenceException, ValidatorException {
        facade.modify(customer);
    }

    /**
     * Remove o cliente do banco de dados
     * @throws CustomerPersistenceException
     */
    public void remove() throws CustomerPersistenceException {
        facade.remove(customer);
    }

    /**
     * Cria um novo cliente e salva ele no banco de dados.
     *
     * @param id O código do cliente
     * @param name O nome do cliente
     * @throws CustomerPersistenceException
     * @throws ValidatorException
     */
    public void newCustomer( String id, String name )
            throws CustomerPersistenceException, ValidatorException {
        customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        facade.add(customer);
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
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }
}
