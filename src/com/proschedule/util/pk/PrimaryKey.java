/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.pk;

/**
 * Define o comportamento de uma chave primária.
 * @author [url="mailto:daniel_tritone@terra.com.br"]daniel_tritone@terra.com.br[/url]
 */
public interface PrimaryKey {
    /**
     * Recupera a chave.
     * @return Objeto contendo a chave.
     */
    public Object getKey();

    /**
     * Altera o valor da chave.
     * @param obj Objeto contendo a chave.
     */
    public void setKey(Object obj);
}