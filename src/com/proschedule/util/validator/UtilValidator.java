package com.proschedule.util.validator;

/**
 * Utilidades de validação de variáveis
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class UtilValidator {

    /**
     * Verifica se valor é inteiro
     * @param input
     * @return
     */
    public static boolean isInteger( String input ) {
        if (input.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
           return true;
        } else {
            return false;
        }
    }
}

