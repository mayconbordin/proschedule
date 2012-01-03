package com.proschedule.core.persistence.view.component;

import com.proschedule.core.persistence.model.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de Componentes.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_RAW_MATERIAL = 1;

    /**
     * lista dos produtos que serão exibidos
     */
    private List components;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public ComponentTableModel() {
        components = new ArrayList();
    }

    /**
     * Construtor da Classe - Cria a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public ComponentTableModel(List list) {
        this();
        components.addAll(list);
    }

    /**
     * Construtor da Classe - Cria a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public ComponentTableModel(Set list) {
        this();
        components = new ArrayList(list);
    }

    public int getRowCount() {
        //cada produto na lista será uma linha
        return components.size();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_RAW_MATERIAL) {
            return "Matéria Prima";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return String.class;
        } else if (columnIndex == COL_RAW_MATERIAL) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Component p = (Component) components.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return p.getId();
        } else if (columnIndex == COL_RAW_MATERIAL) {
            return p.getRawMaterial();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //pega o produto da linha
        Component p = (Component) components.get(rowIndex);

        //verifica qual valor vai ser alterado
        if (columnIndex == COL_ID) {
            p.setId(aValue.toString());
        } else if (columnIndex == COL_RAW_MATERIAL) {
            p.setRawMaterial(aValue.toString());
        }

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
    public void add(Component c) {
        components.add(c);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     *
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        components.remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     *
     * @param c O objeto a ser removido
     */
    public void remove(Component c) {
        components.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "id"
     */
    public void orderById() {
        Collections.sort(components, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Component)o1).getId().compareTo(((Component)o2).getId());
            }
        });

        //avisa que a tabela foi alterada
        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "rawMaterial"
     */
    public void orderByRawMaterial() {
        Collections.sort(components, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Component)o1).getRawMaterial().compareTo(((Component)o2).getRawMaterial());
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
    public Component getComponent(int pos) {
        if (pos < 0 || pos >= components.size()) {
            return null;
        }

        return (Component) components.get(pos);
    }
}
