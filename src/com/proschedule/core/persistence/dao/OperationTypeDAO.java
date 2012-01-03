package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.core.persistence.messages.OperationTypeMessages;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.util.dao.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para tipo de operação.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationTypeDAO extends AbstractDAO {

    private OperationTypeMessages msg = new OperationTypeMessages();

    /**
     * Adicionar um novo tipo de operação
     *
     * @param operationType O tipo de operação que será adicionado
     * @return True se a tipo de operação for bem sucedida
     * @throws OperationTypePersistenceException
     */
    public boolean add(OperationType operationType) throws OperationTypePersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(operationType);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um tipo de operação existente
     *
     * @param operationType O tipo de operação que será modificado
     * @return True se a tipo de operação for bem sucedida
     * @throws OperationTypePersistenceException
     */
    public boolean modify(OperationType operationType) throws OperationTypePersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(operationType);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um tipo de operação existente
     *
     * @param OperationType O tipo de operação que será removido
     * @return True se a tipo de operação for bem sucedida
     * @throws OperationTypePersistenceException
     */
    public boolean remove(OperationType OperationType) throws OperationTypePersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(OperationType);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os tipo de operaçãos
     *
     * @return Lista de tipo de operaçãos
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list() throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class );
            return select.list();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista com todos tipo de operaçãos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de tipo de operaçãos ordenada
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , String order ) throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos tipo de operaçãos dentro dos parâmetros informados.
     * É usada para campos do tipo string.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de tipo de operaçãos ou null se um operador inválido for informado
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , String value , String operator ) throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class );

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq(field, value) );
            } else if ( operator.equals("") ) {
                select.add( Restrictions.ne(field, value) );
            } else if ( operator.equals("like") ) {
                select.add( Restrictions.like(field, value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informados.
     * É usada para campos do tipo inteiro.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , Integer value , String operator ) throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class );

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq(field, value) );
            } else if ( operator.equals("<>") ) {
                select.add( Restrictions.ne(field, value) );
            } else if ( operator.equals(">") ) {
                select.add( Restrictions.gt(field, value) );
            } else if ( operator.equals("<") ) {
                select.add( Restrictions.lt(field, value) );
            } else if ( operator.equals(">=") ) {
                select.add( Restrictions.ge(field, value) );
            } else if ( operator.equals("<=") ) {
                select.add( Restrictions.le(field, value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se um tipo de operação já existe com base na chave primária.
     *
     * @param operationType O tipo de operação com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro tipo de operação ou false se nada for
     * econtrado.
     * @throws OperationTypePersistenceException
     */
    public boolean alreadyExist( OperationType operationType ) throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class )
                    .add( Restrictions.eq("id", operationType.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se existe outra tipo de operação com a mesma descrição.
     *
     * @param description A descrição a ser procurada
     * @return True se houver outro tipo de operação ou false se nada for econtrado.
     * @throws OperationTypePersistenceException
     */
    public boolean searchForDescription( String description ) throws OperationTypePersistenceException {
        try {
            Criteria select = session.createCriteria( OperationType.class )
                    .add( Restrictions.eq("description", description) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationTypePersistenceException(e, msg.getListError());
        }
    }
}