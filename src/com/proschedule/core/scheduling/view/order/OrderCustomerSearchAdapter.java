package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.persistence.model.Customer;
import com.proschedule.util.search.ISearchDialog;

/**
 * Adaptador para busca de cliente da ordem de produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderCustomerSearchAdapter implements ISearchDialog {
    private NewOrderPanel panel;

    /**
     * Construtor da Classe
     *
     * @param panel O painel de nova ordem de produção
     */
    public OrderCustomerSearchAdapter(NewOrderPanel panel) {
        this.panel = panel;
    }

    public void selectItem(Object obj) {
        Customer c = (Customer) obj;

        panel.getJtfCustomerId().setText( c.getId() );
        panel.getJlCustomerName().setText( c.getName() );
    }

}
