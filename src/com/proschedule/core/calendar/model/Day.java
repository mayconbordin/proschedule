package com.proschedule.core.calendar.model;

import java.util.Date;

/**
 * O Dia indica se neste haverá trabalho, a qual semana pertence
 * e a que calendário pertence. Muitos dias formam um calendário.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:51
 */
public class Day implements java.io.Serializable {

    /**
     * Data do dia
     */
    private Date date;

    /**
     * Semana do ano ao qual o dia pertence
     */
    private Integer week;

    /**
     * Indica se será trabalhado neste dia
     */
    private boolean workingDay;

    /**
     * Horas trabalhadas no dia
     */
    private Double workingHours;

    /**
     * Calendário ao qual pertence
     */
    private Calendar calendar;

    /**
     * Construtor da Classe
     */
    public Day(){

    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the week
     */
    public Integer getWeek() {
        return week;
    }

    /**
     * @param week the week to set
     */
    public void setWeek(Integer week) {
        this.week = week;
    }

    /**
     * @return the workingDay
     */
    public boolean isWorkingDay() {
        return workingDay;
    }

    /**
     * @param workingDay the workingDay to set
     */
    public void setWorkingDay(boolean workingDay) {
        this.workingDay = workingDay;
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
     * @return the workingHours
     */
    public Double getWorkingHours() {
        return workingHours;
    }

    /**
     * @param workingHours the workingHours to set
     */
    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    /**
     * Calcula a semana em que está o dia, baseado na data informada.
     */
    public void calcWeek() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(getDate());
        setWeek((Integer) c.get(java.util.Calendar.WEEK_OF_YEAR));
    }
}