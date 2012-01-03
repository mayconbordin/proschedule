package com.proschedule.core.persistence.view.customer;

import com.proschedule.util.search.SearchParam;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.List;

/**
 * Modelo de Combo Box para listagem dos campos do Componente.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerFieldComboBoxModel extends AbstractListModel implements ComboBoxModel {
    /**
     * Valores a serem exibidos
     */
    private List<SearchParam> values = new ArrayList();

    /**
     * Valor selecionado
     */
    SearchParam selection = null;
    
    /**
     * Monta a lista de operadores
     */
    public CustomerFieldComboBoxModel() {
        SearchParam so1 = new SearchParam();
        so1.setDescription("Código");
        so1.setName("id");

        SearchParam so2 = new SearchParam();
        so2.setDescription("Nome");
        so2.setName("name");

        values.add(so1);
        values.add(so2);
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