package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.persistence.model.Set;
import com.proschedule.util.search.ISearchDialog;

/**
 * Adaptador para busca de componentes do conjunto
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderSetSearchAdapter implements ISearchDialog {
    private NewOrderPanel panel;

    /**
     * Construtor da Classe
     *
     * @param panel O painel de novas ordens de produção
     */
    public OrderSetSearchAdapter(NewOrderPanel panel) {
        this.panel = panel;
    }

    public void selectItem(Object obj) {
        Set o = (Set) obj;

        panel.getJtfSetId().setText( o.getId() );
    }

}
