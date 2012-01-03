package com.proschedule.main;

/**
 * Exception para a classe de sistema
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ProScheduleException extends Exception {
    private Object exception;
    private String message;
    private String detailMessage;

    /**
     * Construtor da Classe
     * 
     * @param ex Objeto de exceção original
     * @param message Mensagem personalizada
     */
    public ProScheduleException( Object ex , String message ) {
        this.exception = ex;
        this.message = message;
        makeMessage();
    }

    /**
     * Construtor da Classe
     * @param message Mensagem de erro
     */
    public ProScheduleException( String message ) {
        this.message = message;
    }

    /**
     * Cria uma mensagem detalhada com base na exceção recebida
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