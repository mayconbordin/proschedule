package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import com.proschedule.util.combobox.StringOperatorComboBoxModel;
import com.proschedule.util.messages.QuestionDialog;
import com.proschedule.util.search.SearchParam;

/**
 * Painel de Conjuntos - tela principal para exibição e edição de conjuntos
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private SetTableModel setTableModel;
    private SetController controller;
    private javax.swing.JTabbedPane tabPane;
    private AbstractTab tabNewSet;

    private SetFieldComboBoxModel setFieldComboBoxModel;
    private StringOperatorComboBoxModel stringOperatorComboBoxModel;

    /**
     * Construtor da Classe
     * 
     * @param parent O frame principal
     * @param tabPane Painel de abas
     */
    public SetPanel( java.awt.Frame parent, javax.swing.JTabbedPane tabPane ) {
        this.parent = parent;
        this.tabPane = tabPane;
        startUp();
    }

    /**
     * Inicia as configurações, componentens, variáveis
     */
    public void startUp() {
        controller = new SetController();

        initComponents();
        //Carrega o table model
        loadSetTableModel();

        //Carrega a barra de busca
        loadSearchBar();
    }

    /**
     * Carrega a barra de busca de conjuntos
     */
    public void loadSearchBar() {
        setFieldComboBoxModel = new SetFieldComboBoxModel();
        stringOperatorComboBoxModel = new StringOperatorComboBoxModel();

        jcbField.setModel(setFieldComboBoxModel);
        jcbOperator.setModel(stringOperatorComboBoxModel);

        jcbField.setSelectedIndex(0);
        jcbOperator.setSelectedIndex(0);
    }

    /**
     * Recarrega a posição padrão da barra de busca
     */
    public void reloadSearchBar() {
        jcbField.setSelectedIndex(0);
        jcbOperator.setSelectedIndex(0);
        jtfValue.setText(null);
    }

    /**
     * Carrega a tabela de conjuntos, listando todos os registros existentes
     */
    public void loadSetTableModel() {
        try {
            setTableModel = new SetTableModel( getController().getSets() );
            jtSets.setModel(setTableModel);
        } catch (SetPersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }

    /**
     * Carrega o objeto selecionado no controlador
     *
     * @return True se um objeto foi selecionado ou false se não havia objeto selecionado
     */
    public boolean loadSelectedSet() {
        int index = jtSets.getSelectedRow();

        if ( index != -1 ) {
            //Seta o conjunto selecionado para edição
            getController().editSet( setTableModel.getSet( index ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar um conjunto para fazer isso!", parent);
        }

        return false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jspSets = new javax.swing.JScrollPane();
        jtSets = new javax.swing.JTable();
        jbAdd = new javax.swing.JButton();
        jbModify = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jlSearchingFor = new javax.swing.JLabel();
        jcbField = new javax.swing.JComboBox();
        jcbOperator = new javax.swing.JComboBox();
        jtfValue = new javax.swing.JTextField();
        jbGoSearch = new javax.swing.JButton();
        jbClearSearch = new javax.swing.JButton();

        jtSets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtSets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtSetsMouseClicked(evt);
            }
        });
        jspSets.setViewportView(jtSets);

        jbAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/add_16.png"))); // NOI18N
        jbAdd.setText("Adicionar");
        jbAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddActionPerformed(evt);
            }
        });

        jbModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbModify.setText("Modificar");
        jbModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyActionPerformed(evt);
            }
        });

        jbRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbRemove.setText("Remover");
        jbRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveActionPerformed(evt);
            }
        });

        jlSearchingFor.setText("Buscar por");

        jbGoSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/search_16.png"))); // NOI18N
        jbGoSearch.setToolTipText("Buscar");
        jbGoSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGoSearchActionPerformed(evt);
            }
        });

        jbClearSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/clear_16.png"))); // NOI18N
        jbClearSearch.setToolTipText("Limpar Busca");
        jbClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jspSets, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbModify)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                        .addComponent(jlSearchingFor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbGoSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClearSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbModify)
                        .addComponent(jbRemove)
                        .addComponent(jbAdd))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jbClearSearch)
                        .addComponent(jbGoSearch)
                        .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlSearchingFor))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspSets, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Carrega os dados do conjunto selecionado no formulário toda vez que um
     * conjunto é selecionado na tabela. Em caso de clique duplo, o conjunto
     * entre em modo de edição.
     * 
     * @param evt
     */
    private void jtSetsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtSetsMouseClicked
        //Clique duplo
        if (evt.getClickCount() == 2) {
            if ( loadSelectedSet() ) {
                if ( tabNewSet == null || !tabNewSet.isActivated() ) {
                    NewSetPanel form = new NewSetPanel(parent, this);
                    tabNewSet = new AbstractTab(tabPane , "Conjunto: " + controller.getSetId(), form);
                    form.setTab( tabNewSet );
                } else {
                    tabNewSet.setAsSelected();
                }
            }
        }
    }//GEN-LAST:event_jtSetsMouseClicked

    /**
     * Adição de nova conjunto - botão adicionar.
     * @param evt
     */
    private void jbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddActionPerformed
        controller.newSet();
        if ( tabNewSet == null || !tabNewSet.isActivated() ) {
            NewSetPanel form = new NewSetPanel(parent, this);
            tabNewSet = new AbstractTab(tabPane , "Novo Conjunto", form);
            form.setTab( tabNewSet );
        } else {
            tabNewSet.setAsSelected();
        }
    }//GEN-LAST:event_jbAddActionPerformed

    /**
     * Coloca em modo de edição o conjunto selecionado - botão modificar
     * @param evt
     */
    private void jbModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyActionPerformed
        if ( loadSelectedSet() ) {
            if ( tabNewSet == null || !tabNewSet.isActivated() ) {
                NewSetPanel form = new NewSetPanel(parent, this);
                tabNewSet = new AbstractTab(tabPane , "Conjunto: " + controller.getSetId(), form);
                form.setTab( tabNewSet );
            } else {
                tabNewSet.setAsSelected();
            }
        }
    }//GEN-LAST:event_jbModifyActionPerformed

    /**
     * Realiza a busca de acordo com os parâmetros informados e lista-os na tabela
     * de conjuntos.
     * 
     * @param evt
     */
    private void jbGoSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGoSearchActionPerformed
        try {
            SearchParam field = (SearchParam) jcbField.getSelectedItem();
            SearchParam operator = (SearchParam) jcbOperator.getSelectedItem();
            
            if ( field.getName().equals("id") ) {
                String value = jtfValue.getText();
                setTableModel = new SetTableModel( controller.getSets(field, value, operator) );
            }

            jtSets.setModel(setTableModel);
        } catch (SetPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (NullPointerException ex) {
            MessageDialog.error("É preciso preencher todos os valores para a busca.", ex.toString(), parent);
        }
    }//GEN-LAST:event_jbGoSearchActionPerformed

    /**
     * Limpa da tabela a busca realizada.
     * 
     * @param evt
     */
    private void jbClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearSearchActionPerformed
        loadSetTableModel();
        reloadSearchBar();
    }//GEN-LAST:event_jbClearSearchActionPerformed

    /**
     * Remove o conjunto selecionado - botão remover
     * 
     * @param evt
     */
    private void jbRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveActionPerformed
        if ( loadSelectedSet() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover o conjunto Cod. "
                    + controller.getSet().getId() + " do sistema?" ) ) {
                try {
                    controller.removeSet();

                    MessageDialog.sucess("Conjunto removido com sucesso!", parent);
                    
                    loadSetTableModel();
                } catch (SetPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
    }//GEN-LAST:event_jbRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbClearSearch;
    private javax.swing.JButton jbGoSearch;
    private javax.swing.JButton jbModify;
    private javax.swing.JButton jbRemove;
    private javax.swing.JComboBox jcbField;
    private javax.swing.JComboBox jcbOperator;
    private javax.swing.JLabel jlSearchingFor;
    private javax.swing.JScrollPane jspSets;
    private javax.swing.JTable jtSets;
    private javax.swing.JTextField jtfValue;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * @return the presenter
     */
    public SetController getController() {
        return controller;
    }

}
