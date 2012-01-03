package com.proschedule.core.scheduling.view.order;

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
public class OrderFieldComboBoxModel extends AbstractListModel implements ComboBoxModel {
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
    public OrderFieldComboBoxModel() {
        SearchParam so1 = new SearchParam();
        so1.setDescription("Código");
        so1.setName("id");

        SearchParam so2 = new SearchParam();
        so2.setDescription("Quantidade");
        so2.setName("setQuantity");

        SearchParam so3 = new SearchParam();
        so3.setDescription("Data de Entrega");
        so3.setName("deliveryDate");

        SearchParam so4 = new SearchParam();
        so4.setDescription("Conjunto");
        so4.setName("set");

        SearchParam so5 = new SearchParam();
        so5.setDescription("Cliente");
        so5.setName("customer");

        values.add(so1);
        values.add(so2);
        values.add(so3);
        values.add(so4);
        values.add(so5);
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