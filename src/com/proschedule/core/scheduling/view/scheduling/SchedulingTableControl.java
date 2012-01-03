package com.proschedule.core.scheduling.view.scheduling;

import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.util.table.AttributiveCellRenderer;
import com.proschedule.util.table.MultiSpanCellTable;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.joda.time.DateTime;

/**
 * Classe que controla a tabela de sequenciamento da produção, estas são adicionadas
 * ao painel de abas para formar o sequenciamento total da produção.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SchedulingTableControl {
    private String tableName;
    private String type;
    
    private int week = -1;
    private int startWeek;

    private Set currentSet;
    private int startSet;
    
    private Operation operation;

    private MultiSpanCellTable dataTable;
    private MultiSpanCellTable sidebarTable;

    private SchedulingTableModel dataTableModel;
    private SchedulingTableModel sidebarTableModel;

    /**
     * Construtor da Classe
     */
    public SchedulingTableControl() {

    }

    /**
     * Construtor da Classe
     *
     * @param table O nome da tabela
     * @param type O tipo de tabela
     * @param week A semana em que inicia
     * @param startWeek A semana inicial
     */
    public SchedulingTableControl(String table, String type, int week, int startWeek) {
        this.tableName = table;
        this.type = type;
        this.week = week;
        this.startWeek = startWeek;
    }

    /**
     * Construtor da Classe
     *
     * @param table O nome da tabela
     * @param type O tipo de tabela
     * @param week A semana atual
     * @param startWeek A semana em que inicia
     * @param rowSize O número de linhas
     * @param colSize O número de colunas
     */
    public SchedulingTableControl(String table, String type, int week, int startWeek, int rowSize, int colSize) {
        this.tableName = table;
        this.type = type;
        this.week = week;
        this.startWeek = startWeek;
        createTables(rowSize, colSize);
    }

    /**
     * Construtor da Classe
     * 
     * @param table O nome da tabela
     * @param type O tipo de tabela
     * @param week A semana atual
     * @param startWeek A semana em que inicia
     * @param operation A operação que representa a tabela
     * @param rowSize O número de linhas
     * @param colSize O número de colunas
     */
    public SchedulingTableControl(String table, String type, int week, int startWeek, Operation operation, int rowSize, int colSize) {
        this.tableName = table;
        this.type = type;
        this.week = week;
        this.startWeek = startWeek;
        this.operation = operation;
        createTables(rowSize, colSize);
    }

    /**
     * Cria as tabelas que compõe a tabela principal
     * 
     * @param rowSize Número de linhas
     * @param colSize Número de colunas
     */
    private void createTables(int rowSize, int colSize) {
        //Tabela de dados
        setDataTableModel(new SchedulingTableModel(rowSize, colSize - 4));
        setDataTable(new MultiSpanCellTable(getDataTableModel()));

        //Tabela da sidebar
        setSidebarTableModel(new SchedulingTableModel(rowSize, 4));
        setSidebarTable(new MultiSpanCellTable(getSidebarTableModel()));

        //Configura tamanho das colunas da sidebar
        this.getSidebarColumn(3).setPreferredWidth(120);
    }

    /**
     * Cria o painel de rolagem
     * @return
     */
    public JScrollPane createScrollPane() {
        //Adiciona a tabela ao scroll pane
	JScrollPane pane = new JScrollPane(getDataTable());

	// Build a second table with one column from the table1
	// and remove that column from table1.
	getSidebarTable().setRowSelectionAllowed(false); //looks better
	getSidebarTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getSidebarTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSidebarTable().setDefaultRenderer(Object.class , new AttributiveCellRenderer());
        
        getDataTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getDataTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getDataTable().setAutoCreateColumnsFromModel(false);
        getDataTable().setDefaultRenderer(Object.class , new AttributiveCellRenderer());
        
	getSidebarTable().setPreferredScrollableViewportSize(getSidebarTable().getPreferredSize());

	// Put the second table in the row header
	pane.setRowHeaderView(getSidebarTable());

	// Put the header of the second table in the top left corner
	JTableHeader th = getSidebarTable().getTableHeader();
	//nail it down
	th.setReorderingAllowed(false);
	th.setResizingAllowed(false);
	pane.setCorner(JScrollPane.UPPER_LEFT_CORNER, th);

	return pane;
    }

    /**
     * Devolve a coluna de dados com o índice informado
     * 
     * @param columnIndex
     * @return
     */
    public TableColumn getDataColumn( int columnIndex ) {
        return getDataTable().getColumnModel().getColumn(columnIndex);
    }

    /**
     * Devolve a coluna lateral com o índice informado
     * 
     * @param columnIndex
     * @return
     */
    public TableColumn getSidebarColumn( int columnIndex ) {
        return getSidebarTable().getColumnModel().getColumn(columnIndex);
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the week
     */
    public int getWeek() {
        return week;
    }

    /**
     * @param week the week to set
     */
    public void setWeek(int week) {
        this.week = week;
    }

    /**
     * @return the startWeek
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * @param startWeek the startWeek to set
     */
    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    /**
     * @return the currentSet
     */
    public Set getCurrentSet() {
        return currentSet;
    }

    /**
     * @param currentSet the currentSet to set
     */
    public void setCurrentSet(Set currentSet) {
        this.currentSet = currentSet;
    }

    /**
     * @return the startSet
     */
    public int getStartSet() {
        return startSet;
    }

    /**
     * @param startSet the startSet to set
     */
    public void setStartSet(int startSet) {
        this.startSet = startSet;
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * @return the dataTable
     */
    public MultiSpanCellTable getDataTable() {
        return dataTable;
    }

    /**
     * @param dataTable the dataTable to set
     */
    public void setDataTable(MultiSpanCellTable dataTable) {
        this.dataTable = dataTable;
    }

    /**
     * @return the sidebarTable
     */
    public MultiSpanCellTable getSidebarTable() {
        return sidebarTable;
    }

    /**
     * @param sidebarTable the sidebarTable to set
     */
    public void setSidebarTable(MultiSpanCellTable sidebarTable) {
        this.sidebarTable = sidebarTable;
    }

    /**
     * @return the dataTableModel
     */
    public SchedulingTableModel getDataTableModel() {
        return dataTableModel;
    }

    /**
     * @param dataTableModel the dataTableModel to set
     */
    public void setDataTableModel(SchedulingTableModel dataTableModel) {
        this.dataTableModel = dataTableModel;
    }

    /**
     * @return the sidebarTableModel
     */
    public SchedulingTableModel getSidebarTableModel() {
        return sidebarTableModel;
    }

    /**
     * @param sidebarTableModel the sidebarTableModel to set
     */
    public void setSidebarTableModel(SchedulingTableModel sidebarTableModel) {
        this.sidebarTableModel = sidebarTableModel;
    }
}
