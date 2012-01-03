package com.proschedule.util.search;

/**
 * Interface para criação de adaptador para recebimento de objeto selecionado em
 * diálogo de busca.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public interface ISearchDialog {
    /**
     * Recebe o objeto selecionado e repassa os valores de interesse para algum componente
     * ou classe.
     * 
     * @param obj O objeto selecionado
     */
    public void selectItem( Object obj );
}
