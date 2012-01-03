package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.scheduling.model.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Ordens de produção.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_SET_QUANTITY = 1;
    private final int COL_DELIVERY_DATE = 2;
    private final int COL_SET = 3;
    private final int COL_CUSTOMER = 4;

    /**
     * lista dos conjuntos que serão exibidos
     */
    private List sets;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public OrderTableModel() {
        sets = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public OrderTableModel(List list) {
        this();
        sets.addAll(list);
    }

    public int getRowCount() {
        return sets.size();
    }

    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_SET_QUANTITY) {
            return "Quantidade";
        } else if (column == COL_DELIVERY_DATE) {
            return "Data de Entrega";
        } else if (column == COL_SET) {
            return "Conjunto";
        } else if (column == COL_CUSTOMER) {
            return "Cliente";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return String.class;
        } else if (columnIndex == COL_SET_QUANTITY) {
            return Double.class;
        } else if (columnIndex == COL_DELIVERY_DATE) {
            return Date.class;
        } else if (columnIndex == COL_SET) {
            return String.class;
        } else if (columnIndex == COL_CUSTOMER) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Order o = (Order) sets.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return o.getId();
        } else if (columnIndex == COL_SET_QUANTITY) {
            return o.getSetQuantity();
        } else if (columnIndex == COL_DELIVERY_DATE) {
            return o.getDeliveryDate();
        } else if (columnIndex == COL_SET) {
            return o.getSet().getId();
        } else if (columnIndex == COL_CUSTOMER) {
            return o.getCustomer().getName();
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Adiciona um objeto a lista
     *
     * @param o Objeto a ser adicionado a lista
     */
    public void add(Order o) {
        sets.add(o);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     *
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        sets.remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     *
     * @param o O objeto a ser removido
     */
    public void remove(Order o) {
        sets.remove(o);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "id"
     */
    public void orderById() {
        Collections.sort(sets, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Order)o1).getId().compareTo(((Order)o2).getId());
            }
        });

        //avisa que a tabela foi alterada
        fireTableDataChanged();
    }

     /**
     * Retorna o objeto da lista na psição informada.
     *
     * @param pos A posição do objeto a ser recuperado
     * @return Objeto na posição informada ou null se nada for encontrado.
     */
    public Order getOrder(int pos) {
        if (pos < 0 || pos >= sets.size()) {
            return null;
        }

        return (Order) sets.get(pos);
    }
}
