package com.proschedule.core.scheduling.exportation.excel;

import com.proschedule.core.persistence.messages.*;
import com.proschedule.main.ProSchedule;

/**
 * Mensagens de erro de Componente - Armazena as mensagens básicas de erro.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ExcelSchedulingMessages {
    private String ioError = "Ocorreu um erro ao salvar o arquivo.";

    /**
     * @return the ioError
     */
    public String getIoError() {
        return ProSchedule.getProperty("system.messages.scheduling.exportation.excel.ioError");
    }
}
