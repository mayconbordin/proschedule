package com.proschedule.core.calendar.model;

import com.proschedule.util.date.DateUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * O Calendário é o responsável por armazenar todos
 * os dias do ano, indicando quais deles serão trabalhados.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class Calendar implements java.io.Serializable {
    /**
     * Ano do calendário
     */
    private Integer year;

    /**
     * Lista dos dias do calendário
     */
    private Set<Day> days = new HashSet<Day>(0);

    /**
     * Construtor da Classe
     */
    public Calendar() {

    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the days
     */
    public Set<Day> getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(Set<Day> days) {
        this.days = days;
    }

    /**
     * Gera a lista de todos os dias do ano
     *
     * @param workingHours Quantidade padrão de horas trabalhadas por dia
     * @param workAtWeekend Se haverá trabalho ou não nos fins de semana
     */
    public void generateDaysOfYear( Double workingHours , boolean workAtWeekend ) {
        Integer month, day, daysCount;

        java.util.Calendar c = java.util.Calendar.getInstance();

        //Percorre todos os meses
        for ( month = 0; month < 12; month++ ) {
            //Seta a data, sempre dia 1 - isso é indiferente
            c.set(year, month, 1);

            //Pega a quantidade de dias total
            daysCount = c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

            //Percorre todos os dias do mês
            for ( day = 1; day <= daysCount; day++ ) {
                try {
                    //Seta a data
                    c.set(year, month, day);
                    
                    //Cria um novo dia
                    Day d = new Day();

                    //Seta a data no dia
                    d.setDate(DateUtil.formatDate(year, month + 1, day));

                    //Seta o calendário
                    d.setCalendar(this);

                    //Calcula a semana do ano
                    d.calcWeek();

                    //Seta as horas de trabalho/dia padrão
                    d.setWorkingHours(workingHours);

                    //Verificador de fim de semana
                    if ( workAtWeekend ) {
                        d.setWorkingDay(true);
                    } else {
                        //Valores 1 e 7 correspondem a sábado e domingo
                        if ( c.get(java.util.Calendar.DAY_OF_WEEK) == 1 ||
                               c.get(java.util.Calendar.DAY_OF_WEEK) == 7 ) {
                            d.setWorkingDay(false);
                        } else {
                            d.setWorkingDay(true);
                        }
                    }
                    
                    //Adiciona ao calendário
                    days.add(d);
                } catch (Exception ex) {
                    Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Recupera um dia da lsita de dias de acordo com a data especificada.
     *
     * @param date A data especificada para o dia
     * @return O dia procurado ou null se nada for encontrado.
     */
    public Day getDay( Date date ) {
        //Percorre todos os dias
        for ( Day day : days ) {
            if ( DateUtil.compareDates(day.getDate(), date) ) {
                return day;
            }
        }
        return null;
    }

    /**
     * Obtém uma lista de todos os dias de um mês.
     *
     * @param month O mês a ser recuperado. Valores: 1 a 12
     * @return Lista dos dias do mês.
     */
    public List<Day> getDaysPerMonth( int month ) {
        int daysCount, day;
        List<Day> daysPerMonth = new ArrayList();
        
        java.util.Calendar c = java.util.Calendar.getInstance();

        c.set(year, month - 1, 1);

        //Pega a quantidade de dias total
        daysCount = c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        //Percorre todos os dias do mês
        for ( day = 1; day <= daysCount; day++ ) {
            Date date = DateUtil.formatDate(year, month, day);

            Day dayObj = getDay( date );
            daysPerMonth.add( dayObj );
        }

        return daysPerMonth;
    }

    /**
     * Lista todos os mêses de um ano ordenadamente, e também ordenados
     * os dias de cada mês.
     *
     * @return Lista dos meses do ano.
     */
    public List<List<Day>> getMonthsPerYear() {
        List<List<Day>> monthsPerYear = new ArrayList();

        for ( int i = 0; i <= 12; i++ ) {
            monthsPerYear.add( getDaysPerMonth(i) );
        }

        return monthsPerYear;
    }

    @Override
    public String toString() {
        return String.valueOf( year );
    }

}