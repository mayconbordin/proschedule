package com.proschedule.core.scheduling.messages;

import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Detalhes do Sequenciamento de Operações - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationSchedulingSetDetailMessages {
    /**
     * @return the addError
     */
    public String getAddError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.addError");
    }

    /**
     * @return the modifyError
     */
    public String getModifyError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.modifyError");
    }

    /**
     * @return the removeError
     */
    public String getRemoveError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.removeError");
    }

    /**
     * @return the listError
     */
    public String getListError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.listError");
    }

    /**
     * @return the searchError
     */
    public String getSearchError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.searchError");
    }

    /**
     * @return the alreadyExist
     */
    public String getAlreadyExist() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.alreadyExist");
    }

    /**
     * @return the notFoundError
     */
    public String getNotFoundError() {
        return ProSchedule.getProperty("system.messages.scheduling.ossd.notFoundError");
    }

}
