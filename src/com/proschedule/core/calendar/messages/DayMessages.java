package com.proschedule.core.calendar.messages;

import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Dias - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class DayMessages {
    private String addError = "Ocorreu um erro ao adicionar um novo dia.";
    private String modifyError = "Ocorreu um erro ao modificar o dia.";
    private String removeError = "Ocorreu um erro ao remover o dia.";
    private String listError = "Ocorreu um erro ao listar os dias.";
    private String searchError = "Ocorreu um erro ao buscar o dia.";
    private String alreadyExist = "Já existe um dia com a data.";
    private String notFoundError = "O dia com a data informada não foi encontrado.";

    /**
     * @return the addError
     */
    public String getAddError() {
        return ProSchedule.getProperty("system.messages.calendar.day.addError");
    }

    /**
     * @return the modifyError
     */
    public String getModifyError() {
        return ProSchedule.getProperty("system.messages.calendar.day.modifyError");
    }

    /**
     * @return the removeError
     */
    public String getRemoveError() {
        return ProSchedule.getProperty("system.messages.calendar.day.removeError");
    }

    /**
     * @return the listError
     */
    public String getListError() {
        return ProSchedule.getProperty("system.messages.calendar.day.listError");
    }

    /**
     * @return the searchError
     */
    public String getSearchError() {
        return ProSchedule.getProperty("system.messages.calendar.day.searchError");
    }

    /**
     * @return the alreadyExist
     */
    public String getAlreadyExist() {
        return ProSchedule.getProperty("system.messages.calendar.day.alreadyExist");
    }

    /**
     * @return the notFoundError
     */
    public String getNotFoundError() {
        return ProSchedule.getProperty("system.messages.calendar.day.notFoundError");
    }

}
