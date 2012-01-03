package com.proschedule.core.persistence.view.set;

import com.proschedule.util.search.SearchParam;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.List;

/**
 * Modelo de Combo Box para listagem dos campos de Conjunto.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetFieldComboBoxModel extends AbstractListModel implements ComboBoxModel {
    /**
     * Valores a serem exibidos
     */
    private List<SearchParam> values = new ArrayList();

    /**
     * Valor selecionado
     */
    SearchParam selection = null;

    /**
     * Cria os valores de campos para Operação
     */
    public SetFieldComboBoxModel() {
        SearchParam so1 = new SearchParam();
        so1.setDescription("Código");
        so1.setName("id");

        values.add(so1);
    }

    public Object getElementAt(int index) {
        return values.get(index);
    }

    public int getSize() {
        return values.size();
    }

    public void setSelectedItem(Object anItem) {
        selection = (SearchParam) anItem;
    }

    public Object getSelectedItem() {
        return selection;
    }
}