package com.proschedule.core.persistence.messages;

import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Detalhe de Conjunto - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetDetailMessages {
    /**
     * @return the addError
     */
    public String getAddError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.addError");
    }

    /**
     * @return the modifyError
     */
    public String getModifyError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.modifyError");
    }

    /**
     * @return the removeError
     */
    public String getRemoveError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.removeError");
    }

    /**
     * @return the listError
     */
    public String getListError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.listError");
    }

    /**
     * @return the searchError
     */
    public String getSearchError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.searchError");
    }

    /**
     * @return the alreadyExist
     */
    public String getAlreadyExist() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.alreadyExist");
    }

    /**
     * @return the notFoundError
     */
    public String getNotFoundError() {
        return ProSchedule.getProperty("system.messages.persistence.setDetail.notFoundError");
    }

}
