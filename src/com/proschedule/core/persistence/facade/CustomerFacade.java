package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.dao.CustomerDAO;
import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.messages.CustomerMessages;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Interface de comunicação com o  módulo de Customere.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerFacade {
    private CustomerDAO dao;
    private Validator validator;
    private CustomerMessages msg;

    /**
     * Construtor da Classe
     */
    public CustomerFacade(){
        dao = new CustomerDAO();
        validator = HibernateValidatorUtil.getValidator();
        msg = new CustomerMessages();
    }

    /**
     * Valida um objeto cliente.
     *
     * @param customer O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Customer customer) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<Customer>> constraintViolations
                = validator.validate(customer);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um cliente.
     *
     * @param customer O cliente a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws CustomerPersistenceException
     * @throws ValidatorException
     */
    public boolean add(Customer customer)
            throws CustomerPersistenceException, ValidatorException {
        try {
            validate(customer);

            //Verifica se já não existe o registro
            if ( alreadyExist( customer ) ) {
                throw new CustomerPersistenceException( msg.getAlreadyExist() );
            }

            return dao.add(customer);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um cliente.
     *
     * @param customer O cliente a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws CustomerPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(Customer customer)
            throws CustomerPersistenceException, ValidatorException {
        try {
            validate(customer);
            return dao.modify(customer);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um cliente.
     *
     * @param customer cliente a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws CustomerPersistenceException
     */
    public boolean remove(Customer customer)
            throws CustomerPersistenceException {
        try {
            return dao.remove(customer);
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os clientes.
     *
     * @return Lista dos clientes.
     * @throws CustomerPersistenceException
     */
    public List<Customer> list() throws CustomerPersistenceException {
        try {
            return dao.list();
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista com todos clientes em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de clientes ordenada
     * @throws CustomerPersistenceException
     */
    public List<Customer> list( String field , String order )
            throws CustomerPersistenceException {
        try {
            return dao.list(field, order);
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista os clientes que estiverem de acordo com os parâmetros informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de clientes ou null se um operador inválido for informado
     * @throws CustomerPersistenceException
     */
    public List<Customer> list( String field , String value , String operator )
            throws CustomerPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um cliente no banco de dados.
     *
     * @param customer cliente com os dados da chave primária
     * @return True se o cliente já existe ou false se não existe
     * @throws CustomerPersistenceException
     */
    public boolean alreadyExist( Customer customer ) throws CustomerPersistenceException {
        try {
            return dao.alreadyExist(customer);
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um cliente com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return O cliente encontrada ou null se nada for encontrado
     * @throws CustomerPersistenceException
     */
    public Customer getCustomer( String id ) throws CustomerPersistenceException {
        try {
            return dao.getCustomer(id);
        } catch (CustomerPersistenceException ex) {
            throw ex;
        }
    }
}