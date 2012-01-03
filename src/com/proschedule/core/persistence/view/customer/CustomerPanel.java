package com.proschedule.core.persistence.view.customer;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.scheduling.view.order.NewOrderPanel;
import com.proschedule.core.scheduling.view.order.OrderController;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import com.proschedule.util.combobox.StringOperatorComboBoxModel;
import com.proschedule.util.forms.MouseAdapterBalloonTip;
import com.proschedule.util.messages.QuestionDialog;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Color;
import java.util.Set;
import javax.validation.ConstraintViolation;
import net.java.balloontip.BalloonTip;

/**
 * Painel de Clientes - tela principal para exibição e edição de clientes
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class CustomerPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private javax.swing.JTabbedPane tabPane;
    private AbstractTab tabNewOrder;

    private CustomerTableModel customerTableModel;
    private CustomerOrdersTableModel customerOrdersTableModel;
    private CustomerPresenter presenter;

    private boolean edit = false;

    private OrderController orderController;

    private CustomerFieldComboBoxModel customerFieldComboBoxModel;
    private StringOperatorComboBoxModel stringOperatorComboBoxModel;

    private MouseAdapterBalloonTip idBalloonTip;
    private MouseAdapterBalloonTip nameBalloonTip;

    /**
     * Construtor da Classe
     * 
     * @param parent O frame principal
     * @param tabPane O painel de abas
     */
    public CustomerPanel( java.awt.Frame parent, javax.swing.JTabbedPane tabPane ) {
        this.parent = parent;
        this.tabPane = tabPane;
        startUp();
    }

    /**
     * Inicializa as configurações e componentes
     */
    public void startUp() {
        initComponents();

        presenter = new CustomerPresenter();
        orderController = new OrderController();

        //Carrega o table model
        loadCustomerTableModel();

        //Carrega a barra de busca
        loadSearchBar();
    }

    /**
     * Carrega a barra de busca de clientes
     */
    public void loadSearchBar() {
        customerFieldComboBoxModel = new CustomerFieldComboBoxModel();
        stringOperatorComboBoxModel = new StringOperatorComboBoxModel();

        jcbField.setModel(customerFieldComboBoxModel);
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
     * Carrega a tabela de clientes, listando todos os registros existentes
     */
    public void loadCustomerTableModel() {
        try {
            presenter.loadCustomers();
            customerTableModel = new CustomerTableModel( presenter.getCustomers() );
            jtCustomers.setModel(customerTableModel);
            
            customerOrdersTableModel = new CustomerOrdersTableModel();
        } catch (CustomerPersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }

    /**
     * Recarrega a tabela de clientes. É usado quando modificações são efetuadas
     * em algum registro.
     */
    public void reloadCustomerTableModel() {
        try {
            presenter.loadCustomers();
            customerTableModel = new CustomerTableModel( presenter.getCustomers() );
            jtCustomers.setModel(customerTableModel);
        } catch (CustomerPersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }

    /**
     * Desabilita o formulário de edição de clientes
     */
    public void disableForm() {
        //Desabilita clientes
        jtfId.setEditable(false);
        jtfName.setEditable(false);
        jbSave.setEnabled(false);
    }

    /**
     * Habilita o formulário de edição de clientes
     */
    public void enableForm() {
        //Habilita clientes
        if ( !edit ) {
            jtfId.setEditable(true);
        }
        jtfName.setEditable(true);
        jbSave.setEnabled(true);
    }

    /**
     * Limpa o formulário de edição de clientes
     */
    public void clearForm() {
        //Limpa os campos e tabela
        jtfId.setText(null);
        jtfName.setText(null);
        jtOrders.setModel( new CustomerOrdersTableModel() );
 
        //Limpa dados no presenter
        presenter.setCustomer(null);
    }

    /**
     * Carrega no presenter o registro selecionado na tabela de clientes
     *
     * @return True se houver um registro selecionado ou falso se nada foi selecionado
     */
    public boolean loadSelectedRow() {
        int index = jtCustomers.getSelectedRow();

        if ( index != -1 ) {
            //Seta o cliente selecionado para edição
            presenter.setCustomer( customerTableModel.getCustomer( index ) );

            return true;
        } else {
            MessageDialog.error("É preciso selecionar um cliente para fazer isso!", parent);
            return false;
        }
    }

    /**
     * Carrega o cliente selecionado na tabela de clientes no formulário
     * de edição
     */
    public void loadSelectedOnForm() {
        if ( loadSelectedRow() ) {
            try {
                //Seta os valores do cliente na tela
                jtfId.setText(presenter.getCustomer().getId());
                jtfName.setText(presenter.getCustomer().getName());
                customerOrdersTableModel = new CustomerOrdersTableModel(
                            orderController.getOrdersPerCustomer(presenter.getCustomer())
                        );
                jtOrders.setModel(customerOrdersTableModel);
            } catch (OrderPersistenceException ex) {
                MessageDialog.error(ex, parent);
            }
        }
    }

    /**
     * Função de adição de novo cliente - Limpa o formulário, habilita o formulário
     * e seta o foco no primeiro campo do formulário.
     */
    public void addCustomer() {
        edit = false;
        clearForm();
        enableForm();
        jtfId.requestFocus();
    }

    /**
     * Quando uma ordem da tabela de ordens for pressionada, a aba das ordens é
     * aberta com a ordem selecionada carregada.
     */
    public void openOrderTab() {
        int index = jtOrders.getSelectedRow();

        if ( index != -1 ) {
            if ( tabNewOrder == null || !tabNewOrder.isActivated() ) {
                NewOrderPanel form = new NewOrderPanel(parent, customerOrdersTableModel.getOrder( index ));
                tabNewOrder = new AbstractTab(tabPane , "Ordem de Produção: " + customerOrdersTableModel.getOrder( index ).getId(), form);
                form.setTab( tabNewOrder );
            } else {
                tabNewOrder.setAsSelected();
            }
        } else {
            MessageDialog.error("É preciso selecionar um conjunto para fazer isso!", parent);
        }
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfId.setBackground(Color.white);
        jtfId.removeMouseListener(idBalloonTip);

        jtfName.setBackground(Color.white);
        jtfName.removeMouseListener(nameBalloonTip);
    }

    /**
     * Exibe no formulário os erros de validação colocando os componentes com
     * dados inválidos em amarelo e exibindo um balão com o erro.
     *
     * @param ve A exceção que contém os erros de validação
     */
    public void setObjectErrors(ValidatorException ve) {
        Set<ConstraintViolation> constraints = (Set<ConstraintViolation>) ve.getConstraintViolations();

        for ( ConstraintViolation cv : constraints ) {
            String fieldName = cv.getPropertyPath().toString();

            setObjectError(fieldName, cv.getMessage());
        }
    }

    /**
     * Exibe no formulário o erro de validação no componente indicado.
     *
     * @param fieldName O nome do campo que receberá o erro
     * @param message A mensagem a ser exibida no balão
     */
    public void setObjectError(String fieldName, String message) {
        if ( fieldName.equals("id") ) {
            jtfId.setBackground(Color.yellow);

            idBalloonTip = new MouseAdapterBalloonTip(jtfId, message);
            jtfId.addMouseListener(idBalloonTip);
        } else if ( fieldName.equals("name") ) {
            jtfName.setBackground(Color.yellow);

            nameBalloonTip = new MouseAdapterBalloonTip(jtfName, message);
            jtfName.addMouseListener(nameBalloonTip);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jspCustomers = new javax.swing.JScrollPane();
        jtCustomers = new javax.swing.JTable();
        jbAdd = new javax.swing.JButton();
        jbModify = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jlSearchingFor = new javax.swing.JLabel();
        jcbField = new javax.swing.JComboBox();
        jcbOperator = new javax.swing.JComboBox();
        jtfValue = new javax.swing.JTextField();
        jbGoSearch = new javax.swing.JButton();
        jpOrders = new javax.swing.JPanel();
        jpName = new javax.swing.JPanel();
        jtfName = new javax.swing.JTextField();
        jpId = new javax.swing.JPanel();
        jtfId = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jlCustomer = new javax.swing.JLabel();
        jspOrders = new javax.swing.JScrollPane();
        jtOrders = new javax.swing.JTable();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jlCustomerOrders = new javax.swing.JLabel();
        jbOrderModify = new javax.swing.JButton();
        jbClearSearch = new javax.swing.JButton();

        jtCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtCustomersMouseClicked(evt);
            }
        });
        jtCustomers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtCustomersKeyPressed(evt);
            }
        });
        jspCustomers.setViewportView(jtCustomers);

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

        jpOrders.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jpName.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome"));

        jtfName.setEditable(false);

        javax.swing.GroupLayout jpNameLayout = new javax.swing.GroupLayout(jpName);
        jpName.setLayout(jpNameLayout);
        jpNameLayout.setHorizontalGroup(
            jpNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpNameLayout.setVerticalGroup(
            jpNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpId.setBorder(javax.swing.BorderFactory.createTitledBorder("Código"));

        jtfId.setEditable(false);

        javax.swing.GroupLayout jpIdLayout = new javax.swing.GroupLayout(jpId);
        jpId.setLayout(jpIdLayout);
        jpIdLayout.setHorizontalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIdLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfId, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpIdLayout.setVerticalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpIdLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jlCustomer.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/customer.png"))); // NOI18N
        jlCustomer.setText("Cliente:");

        jtOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Quantidade", "Cod. Conjunto", "Data de Entrega"
            }
        ));
        jtOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtOrdersMouseClicked(evt);
            }
        });
        jspOrders.setViewportView(jtOrders);

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/save_16.png"))); // NOI18N
        jbSave.setText("Salvar");
        jbSave.setEnabled(false);
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbCancel.setText("Cancelar");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jlCustomerOrders.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlCustomerOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/order.png"))); // NOI18N
        jlCustomerOrders.setText("Ordens de Produção do Cliente:");

        jbOrderModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbOrderModify.setText("Modificar");
        jbOrderModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOrderModifyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpOrdersLayout = new javax.swing.GroupLayout(jpOrders);
        jpOrders.setLayout(jpOrdersLayout);
        jpOrdersLayout.setHorizontalGroup(
            jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOrdersLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpOrdersLayout.createSequentialGroup()
                        .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jpName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlCustomer, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(20, 20, 20))
                    .addGroup(jpOrdersLayout.createSequentialGroup()
                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpOrdersLayout.createSequentialGroup()
                        .addComponent(jlCustomerOrders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                        .addComponent(jbOrderModify))
                    .addComponent(jspOrders, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpOrdersLayout.setVerticalGroup(
            jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addGroup(jpOrdersLayout.createSequentialGroup()
                        .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jlCustomer)
                                .addComponent(jlCustomerOrders))
                            .addComponent(jbOrderModify))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpOrdersLayout.createSequentialGroup()
                                .addComponent(jpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addGroup(jpOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jbCancel)
                                    .addComponent(jbSave)))
                            .addComponent(jspOrders, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))))
                .addContainerGap())
        );

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE)
                    .addComponent(jpOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbModify)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                .addComponent(jspCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jpOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Carrega os dados do cliente selecionado no formulário toda vez que um
     * cliente é selecionado na tabela. Em caso de clique duplo, o cliente
     * entre em modo de edição.
     * 
     * @param evt
     */
    private void jtCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtCustomersMouseClicked
        loadSelectedOnForm();
        disableForm();

        //Clique duplo
        if (evt.getClickCount() == 2) {
            edit = true;
            enableForm();
            jtfId.requestFocus();
        }
    }//GEN-LAST:event_jtCustomersMouseClicked

    /**
     * Adição de novo cliente - botão adicionar.
     * @param evt
     */
    private void jbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddActionPerformed
        addCustomer();
    }//GEN-LAST:event_jbAddActionPerformed

    /**
     * Limpa o formulário de edição de clientes - botão limpar
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        edit = false;
        clearObjectErrors();
        clearForm();
        disableForm();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Coloca em modo de edição o cliente selecionado - botão modificar
     * @param evt
     */
    private void jbModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyActionPerformed
        edit = true;
        loadSelectedOnForm();
        enableForm();
        jtfId.requestFocus();
    }//GEN-LAST:event_jbModifyActionPerformed

    /**
     * Salva o cliente novo ou em edição.
     *
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        clearObjectErrors();

        if ( edit ) { //Modo de edição
            try {
                presenter.getCustomer().setId(jtfId.getText());
                presenter.getCustomer().setName(jtfName.getText());
                presenter.save();

                //Exibe mensagem de sucesso
                MessageDialog.sucess("O cliente " + presenter.getCustomer()
                        .getName() + " foi modificado com sucesso!", parent);

                reloadCustomerTableModel();

                clearForm();
                disableForm();
            } catch (CustomerPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (ValidatorException ex) {
                MessageDialog.warning(ex, parent);
                setObjectErrors(ex);
            }
        } else { //Novo cliente
            try {
                presenter.newCustomer(jtfId.getText(), jtfName.getText());

                //Exibe mensagem de sucesso
                MessageDialog.sucess("O cliente " + getPresenter().getCustomer()
                        .getName() + " foi adicionado com sucesso!", parent);

                reloadCustomerTableModel();

                clearForm();
                disableForm();
            } catch (CustomerPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (ValidatorException ex) {
                MessageDialog.warning(ex, parent);
                setObjectErrors(ex);
            }
        }
    }//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Se as teclas para cima e para baixo forem pressionadas, a exibição dos
     * detalhes de cliente é atualizada para o novo cliente selecionado.
     * 
     * @param evt
     */
    private void jtCustomersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtCustomersKeyPressed
        if ( evt.getKeyCode() == 38 || evt.getKeyCode() == 40 ) {
            loadSelectedOnForm();
        }
    }//GEN-LAST:event_jtCustomersKeyPressed

    /**
     * Realiza a busca de acordo com os parâmetros informados e lista-os na tabela
     * de clientes.
     * 
     * @param evt
     */
    private void jbGoSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGoSearchActionPerformed
        try {
            SearchParam field = (SearchParam) jcbField.getSelectedItem();
            String value = jtfValue.getText();
            SearchParam operator = (SearchParam) jcbOperator.getSelectedItem();

            presenter.loadCustomers(field, value, operator);
            customerTableModel = new CustomerTableModel( presenter.getCustomers() );
            jtCustomers.setModel(customerTableModel);
        } catch (CustomerPersistenceException ex) {
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
        reloadCustomerTableModel();
        reloadSearchBar();
    }//GEN-LAST:event_jbClearSearchActionPerformed

    /**
     * Remove o cliente selecionado - botão remover
     * 
     * @param evt
     */
    private void jbRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveActionPerformed
        if ( loadSelectedRow() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover o cliente Cod. "
                    + presenter.getCustomer().getId() + " do sistema?" ) ) {
                try {
                    presenter.remove();

                    MessageDialog.sucess("Cliente removido com sucesso!", parent);
                    
                    reloadCustomerTableModel();

                    clearForm();
                    disableForm();
                } catch (CustomerPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
    }//GEN-LAST:event_jbRemoveActionPerformed

    private void jtOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtOrdersMouseClicked
        if ( evt.getClickCount() == 2 ) {
            openOrderTab();
        }
    }//GEN-LAST:event_jtOrdersMouseClicked

    private void jbOrderModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOrderModifyActionPerformed
        openOrderTab();
    }//GEN-LAST:event_jbOrderModifyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbClearSearch;
    private javax.swing.JButton jbGoSearch;
    private javax.swing.JButton jbModify;
    private javax.swing.JButton jbOrderModify;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbSave;
    private javax.swing.JComboBox jcbField;
    private javax.swing.JComboBox jcbOperator;
    private javax.swing.JLabel jlCustomer;
    private javax.swing.JLabel jlCustomerOrders;
    private javax.swing.JLabel jlSearchingFor;
    private javax.swing.JPanel jpId;
    private javax.swing.JPanel jpName;
    private javax.swing.JPanel jpOrders;
    private javax.swing.JScrollPane jspCustomers;
    private javax.swing.JScrollPane jspOrders;
    private javax.swing.JTable jtCustomers;
    private javax.swing.JTable jtOrders;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfValue;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * @return the customerDetailTableModel
     */
    public CustomerOrdersTableModel getCustomerDetailTableModel() {
        return customerOrdersTableModel;
    }

    /**
     * @return the presenter
     */
    public CustomerPresenter getPresenter() {
        return presenter;
    }

}
