package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.model.SetDetail;
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
public class SetDetailTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ORDER = 0;
    private final int COL_OPERATION = 1;
    private final int COL_LEAD_TIME = 2;

    /**
     * Lista dos objetos que serão exibidos
     */
    private List details;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public SetDetailTableModel() {
        details = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public SetDetailTableModel(List list) {
        this();
        details.addAll(list);
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Conjunto de objetos a serem exibidos
     */
    public SetDetailTableModel(Set list) {
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
        if (column == COL_ORDER) {
            return "Ordem";
        } else if (column == COL_OPERATION) {
            return "Operação";
        } else if ( column == COL_LEAD_TIME ) {
            return "Lead Time";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ORDER) {
            return Integer.class;
        } else if (columnIndex == COL_OPERATION) {
            return String.class;
        } else if (columnIndex == COL_LEAD_TIME) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        SetDetail p = (SetDetail) getDetails().get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ORDER) {
            return p.getOrder();
        } else if (columnIndex == COL_OPERATION) {
            return p.getPrimaryKey().getOperation().getDescription();
        } else if (columnIndex == COL_LEAD_TIME) {
            return String.valueOf( p.getLeadTimeValue() + " " + p.getLeadTimeType() );
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //pega o produto da linha
        SetDetail p = (SetDetail) getDetails().get(rowIndex);

        //verifica qual valor vai ser alterado
        if (columnIndex == COL_ORDER) {
            p.setOrder(Integer.parseInt(aValue.toString()));
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
    public void add(SetDetail c) {
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
    public void remove(SetDetail c) {
        details.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "ordem"
     */
    public void orderByOrder() {
        Collections.sort(getDetails(), new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((SetDetail)o1).getOrder().compareTo(((SetDetail)o2).getOrder());
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
    public SetDetail getSetDetail(int pos) {
        if (pos < 0 || pos >= getDetails().size()) {
            return null;
        }

        return (SetDetail) details.get(pos);
    }

    /**
     * Quando a ordem dos objetos na lista for modificado, este método deve ser
     * chamado para renumerar a ordem dos objetos armazenada em cada um deles.
     */
    public void loadOrder() {
        int size = details.size();

        for ( int i = 0; i < size; i++ ) {
            SetDetail cd = (SetDetail) details.get(i);
            cd.setOrder(i+1);
        }

        //avisa que os dados mudaram
        fireTableDataChanged();
    }

    /**
     * Move um registro para baixo na lista
     *
     * @param row A posição do objeto a ser movido para baixo na lista
     * @return A nova posição do objeto na lista
     */
    public int down(int row) {
        int row1 = row;
        int row2 = row + 1;

        if ( row2 < details.size() ) {
            Object moveDown = details.get(row1);
            Object moveUp = details.get(row2);

            details.remove(row1);
            details.add(row1, moveUp);

            details.remove(row2);
            details.add(row2, moveDown);
        }

        //avisa que os dados mudaram
        loadOrder();

        return row2;
    }

    /**
     * Move um objeto para cima na lista
     *
     * @param row A posição do objeto a ser movido para cima na lista
     * @return A nova posição do objeto na lista
     */
    public int up(int row) {
        int row1 = row;
        int row2 = row - 1;

        if ( row2 >= 0 ) {
            Object moveDown = details.get(row2);
            Object moveUp = details.get(row1);

            details.remove(row1);
            details.add(row1, moveDown);

            details.remove(row2);
            details.add(row2, moveUp);
        }

        //avisa que os dados mudaram
        loadOrder();

        return row2;
    }

    /**
     * @return the details
     */
    public List getDetails() {
        return details;
    }

     /**
     * Pega o próximo valor da ordem da lista
     * @return
     */
    public int getNextOrder() {
        return details.size() + 1;
    }
}
