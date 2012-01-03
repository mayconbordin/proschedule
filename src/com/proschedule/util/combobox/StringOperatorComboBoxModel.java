package com.proschedule.util.combobox;

import com.proschedule.util.search.SearchParam;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.List;

/**
 * Modelo de combobox para variáveis string
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class StringOperatorComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private List<SearchParam> values = new ArrayList();
    SearchParam selection = null;
 
    /**
     * Construtor da Classe
     */
    public StringOperatorComboBoxModel() {
        SearchParam sp1 = new SearchParam();
        sp1.setDescription("igual");
        sp1.setName("=");

        SearchParam sp2 = new SearchParam();
        sp2.setDescription("diferente");
        sp2.setName("<>");

        SearchParam sp3 = new SearchParam();
        sp3.setDescription("parecido");
        sp3.setName("like");

        values.add(sp1);
        values.add(sp2);
        values.add(sp3);
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