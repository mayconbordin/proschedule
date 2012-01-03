package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.messages.OperationMessages;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.util.dao.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para operação.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationDAO extends AbstractDAO {

    private OperationMessages msg = new OperationMessages();

    /**
     * Adicionar um novo operação
     *
     * @param operation O operação que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OperationPersistenceException
     */
    public boolean add(Operation operation) throws OperationPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(operation);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um operação existente
     *
     * @param operation O operação que será modificado
     * @return True se a operação for bem sucedida
     * @throws OperationPersistenceException
     */
    public boolean modify(Operation operation) throws OperationPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(operation);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um operação existente
     *
     * @param Operation O operação que será removido
     * @return True se a operação for bem sucedida
     * @throws OperationPersistenceException
     */
    public boolean remove(Operation Operation) throws OperationPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(Operation);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os operaçãos
     *
     * @return Lista de operaçãos
     * @throws OperationPersistenceException
     */
    public List<Operation> list() throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class );
            return select.list();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista com todos operaçãos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de operaçãos ordenada
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , String order ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos operaçãos dentro dos parâmetros informados.
     * É usada para campos do tipo string.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de operaçãos ou null se um operador inválido for informado
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , String value , String operator ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class );

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
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
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
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , Integer value , String operator ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class );

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
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de operação.
     * 
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationPersistenceException
     */
    public List<Operation> list( OperationType value , String operator ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class )
                                     .createAlias("type", "t");

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq("t.description", value.getDescription()) );
            } else if ( operator.equals("<>") ) {
                select.add( Restrictions.ne("t.description", value.getDescription()) );
            } else if ( operator.equals("like") ) {
                select.add( Restrictions.like("t.description", value.getDescription()) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se um operação já existe com base na chave primária.
     *
     * @param operation O operação com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro operação ou false se nada for
     * econtrado.
     * @throws OperationPersistenceException
     */
    public boolean alreadyExist( Operation operation ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class )
                    .add( Restrictions.eq("id", operation.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se existe outra operação com a mesma descrição.
     *
     * @param description A descrição a ser procurada
     * @return True se houver outro operação ou false se nada for econtrado.
     * @throws OperationPersistenceException
     */
    public boolean searchForDescription( String description ) throws OperationPersistenceException {
        try {
            Criteria select = session.createCriteria( Operation.class )
                    .add( Restrictions.eq("description", description) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Recupera uma operação com o código informado.
     * 
     * @param id O código a ser buscado no banco de dados
     * @return A operação encontrada ou null se nada for encontrado
     * @throws OperationPersistenceException
     */
    public Operation getOperation( Integer id ) throws OperationPersistenceException {
            Operation o = (Operation) session.get(Operation.class, id);

            if ( o == null ) {
                throw new OperationPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }
}