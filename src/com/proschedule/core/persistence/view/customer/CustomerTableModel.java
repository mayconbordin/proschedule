package com.proschedule.core.persistence.view.customer;

import com.proschedule.core.persistence.model.Customer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo para a tabela de clientes.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerTableModel extends AbstractTableModel {
    /**
     * constantes que vão representar as colunas
     */
    private final int COL_ID = 0;
    private final int COL_NAME = 1;

    /**
     * lista dos clientes que serão exibidos
     */
    private List customers;

    /**
     * Construtor da Classe - Cria uma nova lista de objetos
     */
    public CustomerTableModel() {
        customers = new ArrayList();
    }

    /**
     * Construtor da Classe - Criau a lista de objetos a partir de outra existente
     *
     * @param list Lista de objetos a serem exibidos
     */
    public CustomerTableModel(List list) {
        this();
        customers.addAll(list);
    }

    public int getRowCount() {
        return customers.size();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        //qual o nome da coluna
        if (column == COL_ID) {
            return "Código";
        } else if (column == COL_NAME) {
            return "Nome";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        //retorna a classe que representa a coluna
        if (columnIndex == COL_ID) {
            return String.class;
        } else if (columnIndex == COL_NAME) {
            return String.class;
        }
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //pega o registro da linha
        Customer p = (Customer) customers.get(rowIndex);

        //verifica qual valor deve ser retornado
        if (columnIndex == COL_ID) {
            return p.getId();
        } else if (columnIndex == COL_NAME) {
            return p.getName();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //pega o produto da linha
        Customer p = (Customer) customers.get(rowIndex);

        //verifica qual valor vai ser alterado
        if (columnIndex == COL_ID) {
            p.setId(aValue.toString());
        } else if (columnIndex == COL_NAME) {
            p.setName(aValue.toString());
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
    public void add(Customer c) {
        customers.add(c);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista a partir de sua posição na lista
     *
     * @param pos Posição do objeto na lista
     */
    public void remove(int pos) {
        customers.remove(pos);

        fireTableDataChanged();
    }

    /**
     * Remove um objeto da lista
     *
     * @param c O objeto a ser removido
     */
    public void remove(Customer c) {
        customers.remove(c);

        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "id"
     */
    public void orderById() {
        Collections.sort(customers, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Customer)o1).getId().compareTo(((Customer)o2).getId());
            }
        });

        //avisa que a tabela foi alterada
        fireTableDataChanged();
    }

    /**
     * Ordena os registros da tabela pela coluna "rawMaterial"
     */
    public void orderByName() {
        Collections.sort(customers, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Customer)o1).getName().compareTo(((Customer)o2).getName());
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
    public Customer getCustomer(int pos) {
        if (pos < 0 || pos >= customers.size()) {
            return null;
        }

        return (Customer) customers.get(pos);
    }
}
