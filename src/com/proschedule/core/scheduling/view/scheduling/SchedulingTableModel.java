package com.proschedule.core.scheduling.view.scheduling;

import com.proschedule.util.table.AttributiveCellTableModel;

/**
 * Modelo de tabela do sequenciamento da produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SchedulingTableModel extends AttributiveCellTableModel {

    /**
     * Construtor da Classe
     */
    public SchedulingTableModel() {

    }

    /**
     * Construtor da Classe
     * @param rowSize Número de linhas
     * @param colSize Número de colunas
     */
    public SchedulingTableModel(int rowSize, int colSize) {
        super(rowSize,colSize);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
