package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.model.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Conjuntos.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_COM_LEAD_TIME = 1;

    /**
     * lista dos conjuntos que serão exibidos
     */
    private List sets;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public SetTableModel() {
        sets = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public SetTableModel(List list) {
        this();
        sets.addAll(list);
    }

    public int getRowCount() {
        return sets.size();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_COM_LEAD_TIME) {
            return "Lead Time p/ Componentes";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return String.class;
        } else if (columnIndex == COL_COM_LEAD_TIME) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Set p = (Set) sets.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return p.getId();
        } else if (columnIndex == COL_COM_LEAD_TIME) {
            return p.getComponentsLeadTime().getValue() + " " + p.getComponentsLeadTime().getType();
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
    public void add(Set c) {
        sets.add(c);

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
     * @param c O objeto a ser removido
     */
    public void remove(Set c) {
        sets.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "id"
     */
    public void orderById() {
        Collections.sort(sets, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Set)o1).getId().compareTo(((Set)o2).getId());
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
    public Set getSet(int pos) {
        if (pos < 0 || pos >= sets.size()) {
            return null;
        }

        return (Set) sets.get(pos);
    }
}
