package com.proschedule.core.calendar.messages;

import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Calendário - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CalendarMessages {
    /**
     * @return the addError
     */
    public String getAddError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.addError");
    }

    /**
     * @return the modifyError
     */
    public String getModifyError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.modifyError");
    }

    /**
     * @return the removeError
     */
    public String getRemoveError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.removeError");
    }

    /**
     * @return the listError
     */
    public String getListError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.listError");
    }

    /**
     * @return the searchError
     */
    public String getSearchError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.searchError");
    }

    /**
     * @return the alreadyExist
     */
    public String getAlreadyExist() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.alreadyExist");
    }

    /**
     * @return the notFoundError
     */
    public String getNotFoundError() {
        return ProSchedule.getProperty("system.messages.calendar.calendar.notFoundError");
    }

}
