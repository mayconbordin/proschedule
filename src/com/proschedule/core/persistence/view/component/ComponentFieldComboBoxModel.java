package com.proschedule.core.persistence.view.component;

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
public class ComponentFieldComboBoxModel extends AbstractListModel implements ComboBoxModel {
    /**
     * Valores a serem exibidos
     */
    private List<SearchParam> values = new ArrayList();

    /**
     * Valor selecionado
     */
    SearchParam selection = null;

    /**
     * Construtor da Classe
     */
    public ComponentFieldComboBoxModel() {
        SearchParam sp1 = new SearchParam();
        sp1.setDescription("Código");
        sp1.setName("id");

        SearchParam sp2 = new SearchParam();
        sp2.setDescription("Matéria Prima");
        sp2.setName("rawMaterial");

        values.add(sp1);
        values.add(sp2);
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