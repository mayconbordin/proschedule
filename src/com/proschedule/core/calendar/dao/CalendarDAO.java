package com.proschedule.core.calendar.dao;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.calendar.messages.CalendarMessages;
import com.proschedule.core.calendar.messages.DayMessages;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.util.dao.AbstractDAO;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para Calendar.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CalendarDAO extends AbstractDAO {

    private CalendarMessages msg = new CalendarMessages();
    private DayMessages msgDay = new DayMessages();

    /**
     * Adicionar um novo calendário
     * 
     * @param calendar O calendário que será adicionado
     * @return True se a operação for bem sucedida
     * @throws CalendarPersistenceException
     */
    public boolean add(Calendar calendar) throws CalendarPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(calendar);
            t.commit();
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um calendário existente
     * 
     * @param calendar O calendário que será modificado
     * @return True se a operação for bem sucedida
     * @throws CalendarPersistenceException
     */
    public boolean modify(Calendar calendar) throws CalendarPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(calendar);
            t.commit();
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um calendário existente
     * 
     * @param calendar O calendário que será removido
     * @return True se a operação for bem sucedida
     * @throws CalendarPersistenceException
     */
    public boolean remove(Calendar calendar) throws CalendarPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(calendar);
            t.commit();
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os calendários
     * 
     * @return Lista de calendários
     * @throws CalendarPersistenceException
     */
    public List<Calendar> list() throws CalendarPersistenceException {
        try {
            Criteria select = session.createCriteria( Calendar.class );
            return select.list();
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista com todos calendários em ordem crescente ou decrescente
     * em relação a um campo informado.
     * 
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de calendários ordenada
     * @throws CalendarPersistenceException
     */
    public List<Calendar> list( String field , String order ) throws CalendarPersistenceException {
        try {
            Criteria select = session.createCriteria( Calendar.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos calendários dentro dos parâmetros
     * informados.
     * 
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de calendários ou null se um operador inválido for informado
     * @throws CalendarPersistenceException
     */
    public List<Calendar> list( String field , Integer value , String operator ) throws CalendarPersistenceException {
        try {
            Criteria select = session.createCriteria( Calendar.class );

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
            throw new CalendarPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos dias dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de dias
     * @throws DayPersistenceException
     */
    public List<Day> listDays(Date startDate , Date endDate ) throws DayPersistenceException {
        try {
            Criteria select = session.createCriteria( Day.class );
            select.add( Expression.between("date", startDate, endDate) );
            select.addOrder( Order.asc( "date" ) );

            return select.list();
        } catch (HibernateException e) {
            throw new DayPersistenceException(e, msgDay.getListError());
        } catch (Exception e) {
            throw new DayPersistenceException(e, msgDay.getListError());
        }
    }

    /**
     * Verifica se um calendário já existe com base na chave primária.
     * 
     * @param calendar O calendário com os dados da chave primária a ser procurada.
     * @return True se houver outro calendário ou false se nada for econtrado.
     * @throws CalendarPersistenceException
     */
    public boolean alreadyExist( Calendar calendar ) throws CalendarPersistenceException {
        try {
            Criteria select = session.createCriteria( Calendar.class )
                    .add( Restrictions.eq("year", calendar.getYear()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new CalendarPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Recupera um calendário através do ano informado.
     *
     * @param year O ano a ser procurado no banco de dados
     * @return O calendário encontrado
     * @throws CalendarPersistenceException
     */
    public Calendar getCalendar( int year ) throws CalendarPersistenceException {
            Calendar c = (Calendar) session.get(Calendar.class, year);

            if ( c == null ) {
                throw new CalendarPersistenceException(msg.getNotFoundError());
            } else {
                return c;
            }
    }

    /**
     * Recupera um dia através da data informada.
     *
     * @param date A data a ser procurada no banco de dados
     * @return O dia encontrado ou null se nada for encontrado
     * @throws DayPersistenceException
     */
    public Day getDay( Date date ) throws DayPersistenceException {
            Day d = (Day) session.get(Day.class, date);

            if ( d == null ) {
                throw new DayPersistenceException(msgDay.getNotFoundError());
            } else {
                return d;
            }
    }
}