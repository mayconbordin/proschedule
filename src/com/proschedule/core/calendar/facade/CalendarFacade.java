package com.proschedule.core.calendar.facade;

import com.proschedule.core.calendar.dao.CalendarDAO;
import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.calendar.messages.CalendarMessages;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.joda.time.DateTime;

/**
 * Interface de comunicação com o  módulo Calendário
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CalendarFacade {
    private CalendarDAO dao;
    private OperationSchedulingFacade operationSchedulingFacade;
    private Validator validator;
    private CalendarMessages msg;

    /**
     * Construtor da Classe
     */
    public CalendarFacade(){
        dao = new CalendarDAO();
        operationSchedulingFacade = new OperationSchedulingFacade();
        validator = HibernateValidatorUtil.getValidator();
        msg = new CalendarMessages();
    }

    /**
     * Valida um objeto calendário.
     *
     * @param calendar O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Calendar calendar) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<Calendar>> constraintViolations
                = validator.validate(calendar);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um calendário.
     * 
     * @param calendar O calendário a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws CalendarPersistenceException
     * @throws ValidatorException
     * @throws OperationSchedulingPersistenceException
     */
    public boolean add(Calendar calendar) throws CalendarPersistenceException,
            ValidatorException, OperationSchedulingPersistenceException {
        try {
            validate(calendar);

            //Verifica se já não existe o registro
            if ( alreadyExist( calendar ) ) {
                throw new CalendarPersistenceException( msg.getAlreadyExist() );
            }

            //Adiciona o calendário
            boolean result = dao.add(calendar);

            //Cria o sequenciamento do ano
            operationSchedulingFacade.createYearOperationsScheduling( calendar );

            return result;
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        } catch (ValidatorException ex) {
            throw ex;
        } catch (CalendarPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um calendário.
     *
     * @param calendar O calendário a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws CalendarPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(Calendar calendar) throws CalendarPersistenceException, ValidatorException {
        try {
            validate(calendar);
            return dao.modify(calendar);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (CalendarPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um calendário.
     * 
     * @param calendar Calendário a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws CalendarPersistenceException
     */
    public boolean remove(Calendar calendar) throws CalendarPersistenceException {
        try {
            return dao.remove(calendar);
        } catch (CalendarPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os calendários.
     *
     * @return Lista dos calendários.
     * @throws CalendarPersistenceException
     */
    public List<Calendar> list() throws CalendarPersistenceException {
        try {
            return dao.list();
        } catch (CalendarPersistenceException ex) {
            throw ex;
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
            return dao.list(field, order);
        } catch (CalendarPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista os calendários que estiverem de acordo com os parâmetros informados.
     * 
     * @param field O campo a ser buscado
     * @param value O valor a ser buscado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de calendários encontrados
     * @throws CalendarPersistenceException
     */
    public List<Calendar> list( String field , Integer value , String operator ) throws CalendarPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (CalendarPersistenceException ex) {
            throw ex;
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
            return dao.listDays(startDate, endDate);
        } catch (DayPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um calendário no banco de dados.
     * 
     * @param calendar Calendário com os dados da chave primária
     * @return True se o calendário já existe ou false se não existe
     * @throws CalendarPersistenceException
     */
    public boolean alreadyExist( Calendar calendar ) throws CalendarPersistenceException {
        try {
            return dao.alreadyExist(calendar);
        } catch (CalendarPersistenceException ex) {
            throw ex;
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
        try {
            return dao.getCalendar(year);
        } catch (CalendarPersistenceException ex) {
            throw ex;
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
        try {
            return dao.getDay(date);
        } catch (DayPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se existe um calendário para o ano corrente.
     *
     * @return True se o calendário do ano corrente existe ou false se não existe
     * @throws CalendarPersistenceException
     */
    public boolean checkCurrentYearCalendar() throws CalendarPersistenceException {
        //Pega a data atual
        DateTime dt = new DateTime();

        //Pega o ano da data atual
        int year = dt.getYear();

        //Cria um calendário para fazer a busca
        Calendar c = new Calendar();
        c.setYear(year);

        //Verifica se o calendário já existe
        if ( alreadyExist(c) ) {
            return true;
        } else {
            return false;
        }
    }
}