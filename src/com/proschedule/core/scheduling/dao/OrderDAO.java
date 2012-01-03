package com.proschedule.core.scheduling.dao;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.scheduling.messages.OrderDetailMessages;
import com.proschedule.core.scheduling.messages.OrderMessages;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.keys.OrderDetailKey;
import com.proschedule.util.dao.AbstractDAO;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para Ordem de Produção.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderDAO extends AbstractDAO {

    private OrderMessages msg = new OrderMessages();
    private OrderDetailMessages msgDetail = new OrderDetailMessages();

    /**
     * Adicionar uma novo ordem de produção
     *
     * @param order O ordem de produção que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OrderPersistenceException
     */
    public boolean add(Order order) throws OrderPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(order);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar uma ordem de produção existente
     *
     * @param order O ordem de produção que será modificado
     * @return True se a operação for bem sucedida
     * @throws OrderPersistenceException
     */
    public boolean modify(Order order) throws OrderPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(order);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover uma ordem de produção existente
     *
     * @param Order O ordem de produção que será removido
     * @return True se a operação for bem sucedida
     * @throws OrderPersistenceException
     */
    public boolean remove(Order Order) throws OrderPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(Order);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todas os ordens de produção
     *
     * @return Lista de ordem de produçãos
     * @throws OrderPersistenceException
     */
    public List<Order> list() throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class );
            return select.list();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista com todas ordens de produção em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de ordem de produçãos ordenada
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , String order ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class );

            if ( order.equals("asc") ) {
                select.addOrder( org.hibernate.criterion.Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( org.hibernate.criterion.Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das ordens de produção dentro dos parâmetros
     * informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de ordem de produçãos ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , String value , String operator ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class );

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
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das ordens de produção dentro dos parâmetros informados.
     * É usada para campos do tipo double.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , Double value , String operator ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class );

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
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das ordens de produção dentro dos parâmetros informados.
     * É usada para campos do tipo data.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , Date value , String operator ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class );

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
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das ordens de produção com o ocnjunto e data informadas.
     *
     * @param set O conjunto da ordem de produção
     * @param deliveryDate A data de entrega da ordem de produção
     * @return Lista de operações
     * @throws OrderPersistenceException
     */
    public List<Order> list( Set set, Date deliveryDate ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class )
                             .createAlias("set", "s")
                             .add( Restrictions.eq("s.id", set.getId()) )
                             .add( Restrictions.eq("deliveryDate", deliveryDate ) );

            return select.list();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de conjunto.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( Set value , String operator ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class )
                                     .createAlias("set", "s");

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq("s.id", value.getId()) );
            } else if ( operator.equals("<>") ) {
                select.add( Restrictions.ne("s.id", value.getId()) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de conjunto.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( Customer value , String operator ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class )
                                     .createAlias("customer", "c");

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq("c.id", value.getId()) );
            } else if ( operator.equals("<>") ) {
                select.add( Restrictions.ne("c.id", value.getId()) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos detalhes da ordem de produção de um determinado componente
     * em uma data especifica.
     *
     * @param component O componente do detalhe da ordem de produção
     * @param set O conjunto ao qual o componente pertence
     * @param day A data do detalhe da ordem de produção
     * @return Lista de detalhes de ordem de produção ou null se um operador inválido for informado
     * @throws OrderDetailPersistenceException
     */
    public List<OrderDetail> listDetails( Component component, Set set, Day day ) throws OrderDetailPersistenceException {
        try {
            Query query = session.createQuery("select od from OrderDetail as od" +
                                              " inner join od.primaryKey.order as order" +
                                              " where od.primaryKey.component = :comp" +
                                              " and order.masterScheduling = :day" +
                                              " and order.set = :set");

            query.setParameter("comp", component);
            query.setParameter("day", day);
            query.setParameter("set", set);

            return query.list();
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getListError());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getListError());
        }
    }

    /**
     * Devolve uma lista dos detalhes da ordem de produção de um determinado
     * período de início de sequenciamento.
     *
     * @param startDate O limite inferior da data de início de sequenciamento das ordens
     * @param endDate O limite superior da data de início de sequenciamento das ordens
     * @return Lista de detalhes de ordem de produção ou null se um operador inválido for informado
     * @throws OrderDetailPersistenceException
     */
    public List<OrderDetail> listDetails( Date startDate, Date endDate ) throws OrderDetailPersistenceException {
        try {
            Query query = session.createQuery(
                    "select od from OrderDetail as od" +
                    " inner join od.primaryKey.order as order" +
                    " where order.masterScheduling.date between :startDate and :endDate");

            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            return query.list();
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getListError());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getListError());
        }
    }

    /**
     * Verifica se um ordem de produção já existe com base na chave primária.
     *
     * @param order O ordem de produção com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro ordem de produção ou false se nada for
     * econtrado.
     * @throws OrderPersistenceException
     */
    public boolean alreadyExist( Order order ) throws OrderPersistenceException {
        try {
            Criteria select = session.createCriteria( Order.class )
                    .add( Restrictions.eq("id", order.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OrderPersistenceException(e, msg.getAlreadyExist());
        } catch (Exception e) {
            throw new OrderPersistenceException(e, msg.getAlreadyExist());
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return O ordem de produção encontrada ou null se nada for encontrado
     * @throws OrderPersistenceException
     */
    public Order getOrder( String id ) throws OrderPersistenceException {
            Order o = (Order) session.get(Order.class, id);

            if ( o == null ) {
                throw new OrderPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Adicionar um novo detalhe de ordem de produção
     *
     * @param orderDetail O detalhe de ordem de produção que será adicionado
     * @return True se a operação for bem sucedida
     * @throws OrderDetailPersistenceException
     */
    public boolean addDetail(OrderDetail orderDetail) throws OrderDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(orderDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getAddError());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getAddError());
        }
        return true;
    }

    /**
     * Modificar um detalhe de ordem de produção existente
     *
     * @param orderDetail O detalhe de ordem de produção que será modificado
     * @return True se a operação for bem sucedida
     * @throws OrderDetailPersistenceException
     */
    public boolean modifyDetail(OrderDetail orderDetail) throws OrderDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(orderDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getModifyError());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getModifyError());
        }
        return true;
    }

    /**
     * Remover um detalhe de ordem de produção existente
     *
     * @param OrderDetail O detalhe de ordem de produção que será removido
     * @return True se a operação for bem sucedida
     * @throws OrderDetailPersistenceException
     */
    public boolean removeDetail(OrderDetail OrderDetail) throws OrderDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(OrderDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getRemoveError());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um detalhe de ordem de produção já existe com base na chave primária.
     *
     * @param orderDetail O detalhe de ordem de produção com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro detalhe de ordem de produção ou false se nada for
     * econtrado.
     * @throws OrderDetailPersistenceException
     */
    public boolean alreadyExistDetail( OrderDetail orderDetail ) throws OrderDetailPersistenceException {
        try {
            OrderDetail o = (OrderDetail) session.get(OrderDetail.class, orderDetail.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new OrderDetailPersistenceException(e, msgDetail.getAlreadyExist());
        }
    }

    /**
     * Recupera um detalhe do ordem de produção com o código informado.
     *
     * @param key A chave primária a ser procurada
     * @return O detalhe do ordem de produção encontrado ou null se nada for encontrado
     * @throws OrderDetailPersistenceException
     */
    public OrderDetail getOrderDetail( OrderDetailKey key ) throws OrderDetailPersistenceException {
            OrderDetail o = (OrderDetail) session.get(OrderDetail.class, key);

            if ( o == null ) {
                throw new OrderDetailPersistenceException(msgDetail.getNotFoundError());
            } else {
                return o;
            }
    }
}