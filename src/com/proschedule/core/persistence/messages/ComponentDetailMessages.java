package com.proschedule.core.persistence.messages;

import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Componente - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentDetailMessages {
    /**
     * @return the addError
     */
    public String getAddError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.addError");
    }

    /**
     * @return the modifyError
     */
    public String getModifyError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.modifyError");
    }

    /**
     * @return the removeError
     */
    public String getRemoveError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.removeError");
    }

    /**
     * @return the listError
     */
    public String getListError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.listError");
    }

    /**
     * @return the searchError
     */
    public String getSearchError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.searchError");
    }

    /**
     * @return the alreadyExist
     */
    public String getAlreadyExist() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.alreadyExist");
    }

    /**
     * @return the notFoundError
     */
    public String getNotFoundError() {
        return ProSchedule.getProperty("system.messages.persistence.componentDetail.notFoundError");
    }
    
}