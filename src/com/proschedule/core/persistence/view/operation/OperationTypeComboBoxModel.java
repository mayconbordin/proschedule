package com.proschedule.core.persistence.view.operation;

import com.proschedule.core.persistence.model.OperationType;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.List;

/**
 * Modelo de Combo Box para listagem dos tipos de Operações.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationTypeComboBoxModel extends AbstractListModel implements ComboBoxModel {
    /**
     * Valores a serem exibidos
     */
    private List<OperationType> values = new ArrayList();

    /**
     * Valor selecionado
     */
    OperationType selection = null;

    /**
     * Construtor da Classe
     * @param list
     */
    public OperationTypeComboBoxModel( List<OperationType> list ) {
        values = list;
    }

    public Object getElementAt(int index) {
        return values.get(index);
    }

    public int getSize() {
        return values.size();
    }

    public void setSelectedItem(Object anItem) {
        selection = (OperationType) anItem;
    }

    public Object getSelectedItem() {
        return selection;
    }
}