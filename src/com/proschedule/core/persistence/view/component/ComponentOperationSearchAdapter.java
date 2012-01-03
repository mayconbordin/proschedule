package com.proschedule.core.persistence.view.component;

import com.proschedule.core.persistence.model.Operation;
import com.proschedule.util.search.ISearchDialog;

/**
 * Adaptador para busca de operações
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class ComponentOperationSearchAdapter implements ISearchDialog {
    private NewComponentDetailDialog dialog;

    /**
     * Construtor da Classe
     * @param dialog
     */
    public ComponentOperationSearchAdapter(NewComponentDetailDialog dialog) {
        this.dialog = dialog;
    }

    public void selectItem(Object obj) {
        Operation o = (Operation) obj;

        dialog.getJtfOperation().setText( String.valueOf( o.getId() ) );
        dialog.getJtfLeadTimeValue().setText( String.valueOf(o.getLeadTime().getValue() ) );
        dialog.getJcbLeadTimeType().setSelectedItem( o.getLeadTime().getType() );
    }

}
