package com.proschedule.core.scheduling.exceptions;

import com.proschedule.util.exceptions.PersistenceException;

/**
 * Exception padrão para as classes relacionadas com Ordens de Produção
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderPersistenceException extends PersistenceException {

    /**
     * Construtor da Classe - Cria a exception com base em apenas uma mensagem
     * de erro.
     *
     * @param message Mensagem de erro
     */
    public OrderPersistenceException( String message ) {
        super(new Exception( message ), message);
    }

    /**
     * Construtor da Classe - Cria a exception com base em outra exception e em
     * uma mensagem personalizada para o erro.
     *
     * @param ex A exceção disparada
     * @param message A mensagem personalizada
     */
    public OrderPersistenceException( Object ex , String message ) {
        super(ex , message);
    }
}