package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.model.Component;
import com.proschedule.util.search.ISearchDialog;

/**
 * Adaptador para busca de componentes do conjunto
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetComponentSearchAdapter implements ISearchDialog {
    private NewSetComponentDialog dialog;

    /**
     * Construtor da Classe
     *
     * @param dialog Diálogo que receberá o código do componente
     */
    public SetComponentSearchAdapter(NewSetComponentDialog dialog) {
        this.dialog = dialog;
    }

    public void selectItem(Object obj) {
        Component o = (Component) obj;

        dialog.getJtfComponent().setText( o.getId() );
    }

}
