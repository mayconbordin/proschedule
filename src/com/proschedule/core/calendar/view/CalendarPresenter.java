package com.proschedule.core.calendar.view;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;

/**
 * Faz as alterações no model de acordo com as requisições
 * feitas pelas views do Calendar.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class CalendarPresenter {
    private CalendarFacade facade;
    private Calendar calendar;
    private List<Calendar> calendars;

    /**
     * Construtor da Classe - Carrega o facade
     */
    public CalendarPresenter() {
        facade = new CalendarFacade();
    }

    /**
     * Carrega a lista de calendários.
     * 
     * @throws CalendarPersistenceException
     */
    public void loadCalendars() throws CalendarPersistenceException {
        calendars = facade.list("year", "desc");
    }

    /**
     * Pega o calendário mais novo da lista de calendários e seta ele
     * como o atual calendário para edição.
     * @return O calendário mais novo
     * @throws IndexOutOfBoundsException 
     */
    public Calendar getLastCalendar() throws IndexOutOfBoundsException {
        try {
            calendar = calendars.get(0);
            return calendar;
        } catch(IndexOutOfBoundsException ex) {
            throw new IndexOutOfBoundsException("Não existem calendários no sistema.");
        }
    }

    /**
     * Marca todos os dias do ano do calendário em edição
     * para dias em que haverá trabalho.
     */
    public void markAllWorkingDays() {
        for ( Day day : calendar.getDays() ) {
            day.setWorkingDay(true);
        }
    }

    /**
     * Desmarca todos os dias do ano do calendário em edição
     * para dias em que não haverá trabalho.
     */
    public void unmarkAllWorkingDays() {
        for ( Day day : calendar.getDays() ) {
            day.setWorkingDay(false);
        }
    }

    /**
     * Aplica as horas de trabalho/dia para todos os dias
     * do calendário em edição.
     *
     * @param value As horas trabalhadas/dia
     */
    public void applyWorkingHoursToAll( Double value ) {
        for ( Day day : calendar.getDays() ) {
            day.setWorkingHours(value);
        }
    }

    /**
     * Faz a validação do valor de horas trabalhadas/dia. Verifica se o valor
     * não ultrapassa as 24h e se não é um valor negativo.
     *
     * @param workingHours O valor das horas trabalhadas/dia
     * @throws ValidatorException
     */
    public void validateWorkingHours( Double workingHours ) throws ValidatorException {
        if ( workingHours > 24 ) {
            throw new ValidatorException( "As horas trabalhadas não podem ultrapassar 24." );
        } else if ( workingHours < 0 ) {
            throw new ValidatorException( "As horas trabalhadas não podem ter valor negativo." );
        }
    }

    /**
     * Salva o calendário que está em edição.
     * 
     * @throws CalendarPersistenceException
     * @throws ValidatorException
     */
    public void save() throws CalendarPersistenceException, ValidatorException {
        facade.modify(calendar);
    }

    /**
     * Cria um novo calendário e salva ele no banco de dados.
     * 
     * @param year Ano do calendário
     * @param workingHours Horas trabalhadas/dia (Valor aplicado a todos
     * os dias)
     * @param workAtWeekend Se true, os finais de semana serão
     * marcados como dia de trabalho, se false eles não serão marcados, apenas
     * os outros dias da semana.
     * @throws CalendarPersistenceException
     * @throws ValidatorException
     * @throws OperationSchedulingPersistenceException
     */
    public void newCalendar( int year, Double workingHours, boolean workAtWeekend )
            throws CalendarPersistenceException, ValidatorException, OperationSchedulingPersistenceException {
        calendar = new Calendar();
        calendar.setYear(year);
        calendar.generateDaysOfYear(workingHours, workAtWeekend);
        facade.add(calendar);
    }

    /**
     * @return the calendar
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * @param calendar the calendar to set
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * @return the calendars
     */
    public List<Calendar> getCalendars() {
        return calendars;
    }
}
