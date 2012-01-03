package com.proschedule.util.date;

import com.proschedule.core.scheduling.exportation.excel.*;

/**
 * Exception para a classe de sequenciamento da produção para Excel
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class DateUtilException extends Exception {
    private Object exception;
    private String message;
    private String detailMessage;

    /**
     * Construtor da Classe
     * @param ex
     * @param message
     */
    public DateUtilException( Object ex , String message ) {
        this.exception = ex;
        this.message = message;
        makeMessage();
    }

    /**
     * Construtor da Classe
     * @param message
     */
    public DateUtilException( String message ) {
        this.message = message;
    }

    /**
     * Cria mensagem de detalhes com base na exceção recebida
     */
    private void makeMessage() {
        Exception e = (Exception) getException();
        setDetailMessage( String.valueOf(e) );
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return the detailMessage
     */
    public String getDetailMessage() {
        return detailMessage;
    }

    /**
     * @return the exception
     */
    public Object getException() {
        return exception;
    }

    /**
     * @param detailMessage the detailMessage to set
     */
    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}