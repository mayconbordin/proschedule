package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.scheduling.model.OrderDetail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Operações.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderDetailTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_COMPONENT_ID = 0;
    private final int COL_QUANTITY = 1;
    private final int COL_RAW_MATERIAL = 2;

    /**
     * Lista dos objetos que serão exibidos
     */
    private List details;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public OrderDetailTableModel() {
        details = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public OrderDetailTableModel(List list) {
        this();
        details.addAll(list);
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Conjunto de objetos a serem exibidos
     */
    public OrderDetailTableModel(Set list) {
        this();
        details = new ArrayList(list);
    }

    public int getRowCount() {
        return getDetails().size();
    }

    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_COMPONENT_ID) {
            return "Código";
        } else if (column == COL_QUANTITY) {
            return "Quantidade";
        } else if ( column == COL_RAW_MATERIAL ) {
            return "Matéria Prima";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_COMPONENT_ID) {
            return String.class;
        } else if (columnIndex == COL_QUANTITY) {
            return Double.class;
        } else if (columnIndex == COL_RAW_MATERIAL) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        OrderDetail p = (OrderDetail) getDetails().get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_COMPONENT_ID) {
            return p.getPrimaryKey().getComponent().getId();
        } else if (columnIndex == COL_QUANTITY) {
            return p.getComponentQuantity();
        } else if (columnIndex == COL_RAW_MATERIAL) {
            return p.getPrimaryKey().getComponent().getRawMaterial();
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
     * @param c Objeto a ser adicionado a lista
     */
    public void add(OrderDetail c) {
        details.add(c);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     *
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        details.remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     *
     * @param c O objeto a ser removido
     */
    public void remove(OrderDetail c) {
        details.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "ordem"
     */
    public void orderById() {
        Collections.sort(getDetails(), new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((OrderDetail)o1).getPrimaryKey().getComponent().getId()
                        .compareTo(((OrderDetail)o2).getPrimaryKey().getComponent().getId());
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
    public OrderDetail getOrderDetail(int pos) {
        if (pos < 0 || pos >= getDetails().size()) {
            return null;
        }

        return (OrderDetail) details.get(pos);
    }

    /**
     * @return the details
     */
    public List getDetails() {
        return details;
    }
}
