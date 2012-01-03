package com.proschedule.core.persistence.view.operation;

import com.proschedule.core.persistence.model.Operation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Operações.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_DESCRIPTION = 1;
    private final int COL_LEADTIME = 2;
    private final int COL_TYPE = 3;

    /**
     * lista dos clientes que serão exibidos
     */
    private List operations;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public OperationTableModel() {
        operations = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public OperationTableModel(List list) {
        this();
        operations.addAll(list);
    }

    public int getRowCount() {
        return operations.size();
    }

    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_DESCRIPTION) {
            return "Descrição";
        } else if (column == COL_LEADTIME) {
            return "Lead Time";
        } else if (column == COL_TYPE) {
            return "Tipo";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return Integer.class;
        } else if (columnIndex == COL_DESCRIPTION) {
            return String.class;
        } else if (columnIndex == COL_LEADTIME) {
            return String.class;
        } else if (columnIndex == COL_TYPE) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Operation p = (Operation) operations.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return p.getId();
        } else if (columnIndex == COL_DESCRIPTION) {
            return p.getDescription();
        } else if (columnIndex == COL_LEADTIME) {
            return p.getLeadTime().getValue() + " " + p.getLeadTime().getType();
        } else if (columnIndex == COL_TYPE) {
            return p.getType().getDescription();
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
    public void add(Operation c) {
        operations.add(c);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     *
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        operations.remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     *
     * @param c O objeto a ser removido
     */
    public void remove(Operation c) {
        operations.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "id"
     */
    public void orderById() {
        Collections.sort(operations, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Operation)o1).getId().compareTo(((Operation)o2).getId());
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
    public Operation getOperation(int pos) {
        if (pos < 0 || pos >= operations.size()) {
            return null;
        }

        return (Operation) operations.get(pos);
    }
}
