package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.messages.CustomerMessages;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.util.dao.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para Customer.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerDAO extends AbstractDAO {

    private CustomerMessages msg = new CustomerMessages();

    /**
     * Adicionar um novo cliente
     *
     * @param customer O cliente que será adicionado
     * @return True se a operação for bem sucedida
     * @throws CustomerPersistenceException
     */
    public boolean add(Customer customer) throws CustomerPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(customer);
            t.commit();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um cliente existente
     *
     * @param customer O cliente que será modificado
     * @return True se a operação for bem sucedida
     * @throws CustomerPersistenceException
     */
    public boolean modify(Customer customer) throws CustomerPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(customer);
            t.commit();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um cliente existente
     *
     * @param Customer O cliente que será removido
     * @return True se a operação for bem sucedida
     * @throws CustomerPersistenceException
     */
    public boolean remove(Customer Customer) throws CustomerPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(Customer);
            t.commit();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os clientes
     *
     * @return Lista de clientes
     * @throws CustomerPersistenceException
     */
    public List<Customer> list() throws CustomerPersistenceException {
        try {
            Criteria select = session.createCriteria( Customer.class );
            return select.list();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getListError());
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
    public List<Customer> list( String field , String order ) throws CustomerPersistenceException {
        try {
            Criteria select = session.createCriteria( Customer.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos clientes dentro dos parâmetros
     * informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de clientes ou null se um operador inválido for informado
     * @throws CustomerPersistenceException
     */
    public List<Customer> list( String field , String value , String operator ) throws CustomerPersistenceException {
        try {
            Criteria select = session.createCriteria( Customer.class );

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq(field, value) );
            } else if ( operator.equals("<>") ) {
                select.add( Restrictions.ne(field, value) );
            } else if ( operator.equals("like") ) {
                select.add( Restrictions.like(field, value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se um cliente já existe com base na chave primária.
     *
     * @param customer O cliente com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro cliente ou false se nada for econtrado.
     * @throws CustomerPersistenceException
     */
    public boolean alreadyExist( Customer customer ) throws CustomerPersistenceException {
        try {
            Criteria select = session.createCriteria( Customer.class )
                    .add( Restrictions.eq("id", customer.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new CustomerPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CustomerPersistenceException(e, msg.getListError());
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
            Customer o = (Customer) session.get(Customer.class, id);

            if ( o == null ) {
                throw new CustomerPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }
}