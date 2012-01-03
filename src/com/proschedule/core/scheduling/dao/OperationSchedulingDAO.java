package com.proschedule.core.scheduling.dao;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingComponentDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.scheduling.messages.OperationSchedulingComponentDetailMessages;
import com.proschedule.core.scheduling.messages.OperationSchedulingMessages;
import com.proschedule.core.scheduling.messages.OperationSchedulingSetDetailMessages;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingComponentDetailKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingSetDetailKey;
import com.proschedule.util.dao.AbstractDAO;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

/**
 * Operações de banco de dados para Sequenciamento de Operação.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationSchedulingDAO extends AbstractDAO {

    private OperationSchedulingMessages msg = new OperationSchedulingMessages();
    private OperationSchedulingComponentDetailMessages msgComDetail = new OperationSchedulingComponentDetailMessages();
    private OperationSchedulingSetDetailMessages msgSetDetail = new OperationSchedulingSetDetailMessages();

    /**
     * Adicionar uma novo sequenciamento de operação
     *
     * @param operationScheduling O sequenciamento de operação que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingPersistenceException
     */
    public boolean add(OperationScheduling operationScheduling) throws OperationSchedulingPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(operationScheduling);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar uma sequenciamento de operação existente
     *
     * @param operationScheduling O sequenciamento de operação que será modificado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingPersistenceException
     */
    public boolean modify(OperationScheduling operationScheduling) throws OperationSchedulingPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(operationScheduling);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover uma sequenciamento de operação existente
     *
     * @param OperationScheduling O sequenciamento de operação que será removido
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingPersistenceException
     */
    public boolean remove(OperationScheduling OperationScheduling) throws OperationSchedulingPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(OperationScheduling);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Remove todo o sequenciamento de uma operação.
     * 
     * @param operation A operação cujo sequenciamento será removido
     * @return True se tudo ocorrer bem
     * @throws OperationSchedulingPersistenceException
     */
    public boolean removeAll( Operation operation ) throws OperationSchedulingPersistenceException {
        //Lista o sequenciamento
        List<OperationScheduling> list = list(operation, "=");

        //Remove tudo
        for ( OperationScheduling os : list ) {
            remove(os);
        }

        return true;
    }

    /**
     * Devolve uma lista com todas os ordens de produção
     *
     * @return Lista de sequenciamento de operaçãos
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list() throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class );
            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista com todas ordens de produção em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de sequenciamento de operaçãos ordenada
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list( String field , String order ) throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class );

            if ( order.equals("asc") ) {
                select.addOrder( org.hibernate.criterion.Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( org.hibernate.criterion.Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos de operações de acordo com a operação.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Operation value , String operator ) throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class );

            if ( operator.equals("=") ) {
                select.add( Expression.eq("primaryKey.operation", value) );
            } else if ( operator.equals("<>") ) {
                select.add( Expression.ne("primaryKey.operation", value) );
            } else if ( operator.equals(">") ) {
                select.add( Expression.gt("primaryKey.operation", value) );
            } else if ( operator.equals("<") ) {
                select.add( Expression.lt("primaryKey.operation", value) );
            } else if ( operator.equals(">=") ) {
                select.add( Expression.ge("primaryKey.operation", value) );
            } else if ( operator.equals("<=") ) {
                select.add( Expression.le("primaryKey.operation", value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos de operações dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Date startDate , Date endDate ) throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class );
            select.add( Expression.between("primaryKey.day.date", startDate, endDate) );
            
            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos de operações de acordo com o dia informado.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Day value , String operator ) throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class );

            if ( operator.equals("=") ) {
                select.add( Expression.eq("primaryKey.day", value) );
            } else if ( operator.equals("<>") ) {
                select.add( Expression.ne("primaryKey.day", value) );
            } else if ( operator.equals(">") ) {
                select.add( Expression.gt("primaryKey.day", value) );
            } else if ( operator.equals("<") ) {
                select.add( Expression.lt("primaryKey.day", value) );
            } else if ( operator.equals(">=") ) {
                select.add( Expression.ge("primaryKey.day", value) );
            } else if ( operator.equals("<=") ) {
                select.add( Expression.le("primaryKey.day", value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos componentes de determinada
     * ordem de produção.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails(Order value , String operator ) 
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingComponentDetail.class );

            if ( operator.equals("=") ) {
                select.add( Expression.eq("primaryKey.order", value) );
            } else if ( operator.equals("<>") ) {
                select.add( Expression.ne("primaryKey.order", value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos conjuntos de determinada
     * ordem de produção.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails(Order value , String operator ) 
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingSetDetail.class );

            if ( operator.equals("=") ) {
                select.add( Expression.eq("primaryKey.order", value) );
            } else if ( operator.equals("<>") ) {
                select.add( Expression.ne("primaryKey.order", value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos componentes de
     * determinado componente em determinada operação em determinado dia.
     *
     * @param component O componente a ser buscado no sequenciamento
     * @param operation A operação a ser buscada no sequenciamento
     * @param day O dia a ser buscado no sequenciamento
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails
            ( Component component, Operation operation, Day day )
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingComponentDetail.class );
            select.add( Expression.eq("primaryKey.component", component) );
            select.add( Expression.eq("primaryKey.operation", operation) );
            select.add( Expression.eq("primaryKey.day", day) );

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAddError());
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos conjuntos de
     * determinado componente em determinada operação em determinado dia.
     *
     * @param set
     * @param operation A operação a ser buscada no sequenciamento
     * @param day O dia a ser buscado no sequenciamento
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails
            ( Set set, Operation operation, Day day )
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingSetDetail.class );
            select.add( Expression.eq("primaryKey.set", set) );
            select.add( Expression.eq("primaryKey.operation", operation) );
            select.add( Expression.eq("primaryKey.day", day) );

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        }
    }

    /**
     * Devolve uma lista dos detalhes de componente dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de detalhes de componente
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails(Date startDate , Date endDate ) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingComponentDetail.class );
            select.add( Expression.between("primaryKey.day.date", startDate, endDate) );
            select.addOrder( org.hibernate.criterion.Order.asc( "primaryKey.day.date" ) );

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getListError());
        }
    }

    /**
     * Devolve uma lista dos detalhes de conjunto dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de detalhes de conjunto
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails(Date startDate , Date endDate ) throws OperationSchedulingSetDetailPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationSchedulingSetDetail.class );
            select.add( Expression.between("primaryKey.day.date", startDate, endDate) );
            select.addOrder( org.hibernate.criterion.Order.asc( "primaryKey.day.date" ) );

            return select.list();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getListError());
        }
    }

    /**
     * Verifica se um sequenciamento de operação já existe com base na chave primária.
     *
     * @param operationScheduling O sequenciamento de operação com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro sequenciamento de operação ou false se nada for
     * econtrado.
     * @throws OperationSchedulingPersistenceException
     */
    public boolean alreadyExist( OperationScheduling operationScheduling ) throws OperationSchedulingPersistenceException {
        try {
            Criteria select = session.createCriteria( OperationScheduling.class )
                 .add( Expression.eq("primaryKey.day", operationScheduling.getPrimaryKey().getDay()) )
                 .add( Expression.eq("primaryKey.operation", operationScheduling.getPrimaryKey().getOperation()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationSchedulingPersistenceException(e, msg.getAlreadyExist());
        } catch (Exception e) {
            throw new OperationSchedulingPersistenceException(e, msg.getAlreadyExist());
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param key A chave primária a ser buscada no banco de dados
     * @return O sequenciamento de operação encontrada ou null se nada for encontrado
     * @throws OperationSchedulingPersistenceException
     */
    public OperationScheduling getOperationScheduling( OperationSchedulingKey key ) throws OperationSchedulingPersistenceException {
            OperationScheduling o = (OperationScheduling) session.get(OperationScheduling.class, key);

            if ( o == null ) {
                throw new OperationSchedulingPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }

    //--------------------------------------------------------------------------
    //Métodos do ComponentDetail
    //--------------------------------------------------------------------------
    /**
     * Adicionar um novo detalhe de sequenciamento de operação
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean addComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(operationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAddError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAddError());
        }
        return true;
    }

    /**
     * Modificar um detalhe de sequenciamento de operação existente
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação que será modificado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean modifyComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(operationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getModifyError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getModifyError());
        }
        return true;
    }

    /**
     * Remover um detalhe de sequenciamento de operação existente
     *
     * @param OperationSchedulingDetail
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean removeComponentDetail(OperationSchedulingComponentDetail OperationSchedulingDetail) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(OperationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getRemoveError());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um detalhe de sequenciamento de operação já existe com base na chave primária.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro detalhe de sequenciamento de operação ou false se nada for
     * econtrado.
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean alreadyExistComponentDetail( OperationSchedulingComponentDetail operationSchedulingDetail ) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            OperationSchedulingComponentDetail o = (OperationSchedulingComponentDetail) session.get(OperationSchedulingComponentDetail.class, operationSchedulingDetail.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAlreadyExist());
        }
    }

    /**
     * Recupera um detalhe do sequenciamento de operação com o código informado.
     *
     * @param key A chave primária a ser buscada no banco de dados
     * @return O detalhe do sequenciamento de operação encontrado ou null se nada for encontrado
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public OperationSchedulingComponentDetail getOperationSchedulingComponentDetail
            ( OperationSchedulingComponentDetailKey key )
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            OperationSchedulingComponentDetail o = (OperationSchedulingComponentDetail) session.get(OperationSchedulingComponentDetail.class, key);

            if ( o == null ) {
                throw new OperationSchedulingComponentDetailPersistenceException(msgComDetail.getNotFoundError());
            } else {
                return o;
            }
        } catch (HibernateException e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new OperationSchedulingComponentDetailPersistenceException(e, msgComDetail.getAlreadyExist());
        }
    }

    //--------------------------------------------------------------------------
    //Métodos do SetDetail
    //--------------------------------------------------------------------------
    /**
     * Adicionar um novo detalhe de sequenciamento de operação
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean addSetDetail(OperationSchedulingSetDetail operationSchedulingDetail) throws OperationSchedulingSetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(operationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAddError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAddError());
        }
        return true;
    }

    /**
     * Modificar um detalhe de sequenciamento de operação existente
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação que será modificado
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean modifySetDetail(OperationSchedulingSetDetail operationSchedulingDetail) throws OperationSchedulingSetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(operationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getModifyError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getModifyError());
        }
        return true;
    }

    /**
     * Remover um detalhe de sequenciamento de operação existente
     *
     * @param OperationSchedulingDetail
     * @return True se a operação for bem sucedida
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean removeSetDetail(OperationSchedulingSetDetail OperationSchedulingDetail) throws OperationSchedulingSetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(OperationSchedulingDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getRemoveError());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um detalhe de sequenciamento de operação já existe com base na chave primária.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento de operação com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro detalhe de sequenciamento de operação ou false se nada for
     * econtrado.
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean alreadyExistSetDetail( OperationSchedulingSetDetail operationSchedulingDetail ) throws OperationSchedulingSetDetailPersistenceException {
        try {
            OperationSchedulingSetDetail o = (OperationSchedulingSetDetail) session.get(OperationSchedulingSetDetail.class, operationSchedulingDetail.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAlreadyExist());
        }
    }

    /**
     * Recupera um detalhe do sequenciamento de operação com o código informado.
     *
     * @param key A chave primária a ser buscada no banco de dados
     * @return O detalhe do sequenciamento de operação encontrado ou null se nada for encontrado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public OperationSchedulingSetDetail getOperationSchedulingSetDetail
            ( OperationSchedulingSetDetailKey key )
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            OperationSchedulingSetDetail o = (OperationSchedulingSetDetail) session.get(OperationSchedulingSetDetail.class, key);

            if ( o == null ) {
                throw new OperationSchedulingSetDetailPersistenceException(msgSetDetail.getNotFoundError());
            } else {
                return o;
            }
        } catch (HibernateException e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new OperationSchedulingSetDetailPersistenceException(e, msgSetDetail.getAlreadyExist());
        }
    }
}