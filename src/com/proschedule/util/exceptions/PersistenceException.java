package com.proschedule.util.exceptions;

/**
 * Exception para persistência - inclui erros para o Hibernate
 * e Hibernate Validator.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class PersistenceException extends Exception {
    private Object exception;
    private String message;
    private String detailMessage;
    
    /**
     * Construtor da Classe
     * @param ex
     * @param message
     */
    public PersistenceException( Object ex , String message ) {
        this.exception = ex;
        this.message = message;
        makeMessage();
    }

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
