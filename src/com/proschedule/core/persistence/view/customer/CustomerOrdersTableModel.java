package com.proschedule.core.persistence.view.customer;

import com.proschedule.core.scheduling.model.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Detalhamento dos Componentes.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerOrdersTableModel extends AbstractTableModel {

    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_QUANTITY = 1;
    private final int COL_SET_ID = 2;
    private final int COL_DELIVERY_DATE = 3;


    /**
     * Lista dos objetos que serão exibidos
     */
    private List orders;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public CustomerOrdersTableModel() {
        orders = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public CustomerOrdersTableModel(List list) {
        this();
        orders.addAll(list);
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     * 
     * @param list Conjunto de objetos a serem exibidos
     */
    public CustomerOrdersTableModel(Set list) {
        this();
        orders = new ArrayList(list);
    }

    public int getRowCount() {
        return orders.size();
    }

    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_QUANTITY) {
            return "Quantidade";
        } else if ( column == COL_SET_ID ) {
            return "Cod. Conjunto";
        } else if ( column == COL_DELIVERY_DATE ) {
            return "Data de Entrega";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return String.class;
        } else if (columnIndex == COL_QUANTITY) {
            return Double.class;
        } else if (columnIndex == COL_SET_ID) {
            return String.class;
        } else if (columnIndex == COL_DELIVERY_DATE) {
            return Date.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Order p = (Order) orders.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return p.getId();
        } else if (columnIndex == COL_QUANTITY) {
            return p.getSetQuantity();
        } else if (columnIndex == COL_SET_ID) {
            return p.getSet().getId();
        } else if (columnIndex == COL_DELIVERY_DATE) {
            return p.getDeliveryDate();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        //avisa que os dados mudaram
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Adiciona um objeto a lista
     * 
     * @param c Objeto a ser adicionado a lista
     */
    public void add(Order c) {
        getOrders().add(c);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     * 
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        getOrders().remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     * 
     * @param c O objeto a ser removido
     */
    public void remove(Order c) {
        getOrders().remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "ordem"
     */
    public void orderById() {
        Collections.sort(orders, new Comparator() {

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
     * @return Objeto na posição informada ou null se nada
     * for encontrado.
     */
    public Order getOrder(int pos) {
        if (pos < 0 || pos >= getOrders().size()) {
            return null;
        }

        return (Order) getOrders().get(pos);
    }

    /**
     * @return the orders
     */
    public List getOrders() {
        return orders;
    }
}
