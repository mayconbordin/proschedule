package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.view.customer.CustomerSearchDialog;
import com.proschedule.core.persistence.view.set.SetSearchDialog;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.util.forms.MouseAdapterBalloonTip;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Color;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * Painel de adição/modificação de ordens de produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:52
 */
public class NewOrderPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private OrderPanel panel;
    private OrderController controller;

    private OrderDetailTableModel detailTableModel;

    private MouseAdapterBalloonTip idBalloonTip;
    private MouseAdapterBalloonTip deliveryDateBalloonTip;
    private MouseAdapterBalloonTip setIdBalloonTip;
    private MouseAdapterBalloonTip setQuantityBalloonTip;
    private MouseAdapterBalloonTip customerIdBalloonTip;

    /**
     * Construtor da Classe - com vinculação ao painel de listagem de ordens
     *
     * @param parent O frame principal
     * @param panel O painel de ordens de produção
     */
    public NewOrderPanel(java.awt.Frame parent, OrderPanel panel) {
        this.parent = parent;
        this.panel = panel;
        this.controller = panel.getController();
        startUp();
    }

    /**
     * Construtor da Classe - independente do painel de listagem de ordens
     *
     * @param parent O frame principal
     */
    public NewOrderPanel(java.awt.Frame parent) {
        this.parent = parent;
        this.panel = null;
        this.controller = new OrderController();
        controller.newOrder();
        startUp();
    }

    /**
     * Construtor da Classe - sem vinculação ao painel de listagem de ordens, mas
     * para edição de ordens de produção
     *
     * @param parent O frame principal
     * @param order A ordem de produção a ser editada
     */
    public NewOrderPanel(java.awt.Frame parent, Order order) {
        this.parent = parent;
        this.panel = null;
        this.controller = new OrderController();
        this.controller.editOrder(order);
        startUp();
    }

    /**
     * Inicializa as configurações
     */
    public void startUp() {
        initComponents();
        loadDetailTableModel();

        getJlCustomerName().setText(null);
        
        if ( getController().isEdit() ) {
            modelToView();
            jtfId.setEditable(false);
        } else {
            jtfId.requestFocus();
        }
    }

    /**
     * Passa os dados do model para os componentes visuais
     */
    public void modelToView() {
        jtfId.setText( getController().getOrderId() );
        jdcDeliveryDate.setDate( getController().getOrderDeliveryDate() );
        getJtfSetId().setText( getController().getOrderSetId() );
        jtfSetQuantity.setText( String.valueOf( getController().getOrderSetQuantity() ) );
        getJtfCustomerId().setText( getController().getOrderCustomerId() );
        getJlCustomerName().setText( getController().getOrderCustomerName() );
    }

    /**
     * Carrega a tabela de detalhes do conjunto
     */
    public void loadDetailTableModel() {
        if ( getController().getOrder() != null ) {
            detailTableModel = new OrderDetailTableModel( getController().getOrderDetails() );
        } else {
            detailTableModel = new OrderDetailTableModel();
        }

        jtDetail.setModel( detailTableModel );
    }

    /**
     * Carrega o detalhe de conjunto selecionada na tabela para o controlador
     *
     * @return True se um registro foi selecionado ou false se nada foi selecionado
     */
    public boolean loadSelectedOrderDetail() {
        int index = jtDetail.getSelectedRow();

        if ( index != -1 ) {
            getController().editOrderDetail( detailTableModel.getOrderDetail( jtDetail.getSelectedRow() ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar um componente para fazer isso!", parent);
        }

        return false;
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfId.setBackground(Color.white);
        jtfId.removeMouseListener(idBalloonTip);

        jdcDeliveryDate.setBackground(Color.white);
        jdcDeliveryDate.removeMouseListener(deliveryDateBalloonTip);
        
        jtfSetId.setBackground(Color.white);
        jtfSetId.removeMouseListener(setIdBalloonTip);

        jtfSetQuantity.setBackground(Color.white);
        jtfSetQuantity.removeMouseListener(setQuantityBalloonTip);

        jtfCustomerId.setBackground(Color.white);
        jtfCustomerId.removeMouseListener(customerIdBalloonTip);
    }

    /**
     * Exibe no formulário os erros de validação colocando os componentes com
     * dados inválidos em amarelo e exibindo um balão com o erro.
     *
     * @param ve A exceção que contém os erros de validação
     */
    public void setObjectErrors(ValidatorException ve) {
        if ( ve.getConstraintViolations() != null ) {
            Set<ConstraintViolation> constraints = (Set<ConstraintViolation>) ve.getConstraintViolations();

            for ( ConstraintViolation cv : constraints ) {
                String fieldName = cv.getPropertyPath().toString();

                setObjectError(fieldName, cv.getMessage());
            }
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
        } else if ( fieldName.equals("deliveryDate") ) {
            jdcDeliveryDate.setBackground(Color.yellow);

            deliveryDateBalloonTip = new MouseAdapterBalloonTip(jdcDeliveryDate, message);
            jdcDeliveryDate.addMouseListener(deliveryDateBalloonTip);
        } else if ( fieldName.equals("set") ) {
            jtfSetId.setBackground(Color.yellow);

            setIdBalloonTip = new MouseAdapterBalloonTip(jtfSetId, message);
            jtfSetId.addMouseListener(setIdBalloonTip);
        } else if ( fieldName.equals("setQuantity") ) {
            jtfSetQuantity.setBackground(Color.yellow);

            setQuantityBalloonTip = new MouseAdapterBalloonTip(jtfSetQuantity, message);
            jtfSetQuantity.addMouseListener(setQuantityBalloonTip);
        } else if ( fieldName.equals("customer") ) {
            jtfCustomerId.setBackground(Color.yellow);

            customerIdBalloonTip = new MouseAdapterBalloonTip(jtfCustomerId, message);
            jtfCustomerId.addMouseListener(customerIdBalloonTip);
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

        jpId = new javax.swing.JPanel();
        jtfId = new javax.swing.JTextField();
        jdcDeliveryDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpComponents = new javax.swing.JPanel();
        jlComponents = new javax.swing.JLabel();
        jspDetail = new javax.swing.JScrollPane();
        jtDetail = new javax.swing.JTable();
        jbModify = new javax.swing.JButton();
        jpCustomer = new javax.swing.JPanel();
        jtfCustomerId = new javax.swing.JTextField();
        jbSearchCustomer = new javax.swing.JButton();
        jlCustomerId = new javax.swing.JLabel();
        jlCustomerName = new javax.swing.JLabel();
        jlCustomerNameLabel = new javax.swing.JLabel();
        jbLoadCustomerName = new javax.swing.JButton();
        jpSet = new javax.swing.JPanel();
        jtfSetId = new javax.swing.JTextField();
        jbSearchSet = new javax.swing.JButton();
        jlSet = new javax.swing.JLabel();
        jtfSetQuantity = new javax.swing.JTextField();
        jlSetQuantity = new javax.swing.JLabel();
        jbLoadComponents = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        jpId.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordem de Produção"));

        jdcDeliveryDate.setDateFormatString("dd/MM/yyyy");

        jLabel1.setText("Código:");

        jLabel2.setText("Data de Entrega:");

        javax.swing.GroupLayout jpIdLayout = new javax.swing.GroupLayout(jpId);
        jpId.setLayout(jpIdLayout);
        jpIdLayout.setHorizontalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpIdLayout.createSequentialGroup()
                .addGroup(jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpIdLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18))
                    .addGroup(jpIdLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtfId)
                    .addComponent(jdcDeliveryDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpIdLayout.setVerticalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIdLayout.createSequentialGroup()
                .addGroup(jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jdcDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jlComponents.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlComponents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/component.png"))); // NOI18N
        jlComponents.setText("Quantidades dos Componentes do Conjunto:");

        jtDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtDetailMouseClicked(evt);
            }
        });
        jspDetail.setViewportView(jtDetail);

        jbModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbModify.setText("Modificar Quantidade");
        jbModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpComponentsLayout = new javax.swing.GroupLayout(jpComponents);
        jpComponents.setLayout(jpComponentsLayout);
        jpComponentsLayout.setHorizontalGroup(
            jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpComponentsLayout.createSequentialGroup()
                .addComponent(jlComponents)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 509, Short.MAX_VALUE)
                .addComponent(jbModify))
            .addComponent(jspDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
        );
        jpComponentsLayout.setVerticalGroup(
            jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpComponentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpComponentsLayout.createSequentialGroup()
                        .addComponent(jlComponents)
                        .addGap(18, 18, 18))
                    .addComponent(jbModify))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jbSearchCustomer.setText("...");
        jbSearchCustomer.setToolTipText("Buscar Cliente");
        jbSearchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSearchCustomerActionPerformed(evt);
            }
        });

        jlCustomerId.setText("Código:");

        jlCustomerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCustomerName.setText("jLabel1");
        jlCustomerName.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlCustomerNameLabel.setText("Nome:");

        jbLoadCustomerName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/load_14.png"))); // NOI18N
        jbLoadCustomerName.setToolTipText("Carregar Nome do Cliente");
        jbLoadCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoadCustomerNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCustomerLayout = new javax.swing.GroupLayout(jpCustomer);
        jpCustomer.setLayout(jpCustomerLayout);
        jpCustomerLayout.setHorizontalGroup(
            jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCustomerId)
                    .addComponent(jlCustomerNameLabel))
                .addGap(18, 18, 18)
                .addGroup(jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlCustomerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jtfCustomerId, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbLoadCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(jbSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        jpCustomerLayout.setVerticalGroup(
            jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCustomerLayout.createSequentialGroup()
                .addGroup(jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbLoadCustomerName, 0, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                        .addComponent(jlCustomerNameLabel)))
                .addGap(18, 18, 18)
                .addGroup(jpCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlCustomerId))
                .addGap(15, 15, 15))
        );

        jpSet.setBorder(javax.swing.BorderFactory.createTitledBorder("Conjunto"));

        jbSearchSet.setText("...");
        jbSearchSet.setToolTipText("Buscar Conjunto");
        jbSearchSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSearchSetActionPerformed(evt);
            }
        });

        jlSet.setText("Código:");

        jlSetQuantity.setText("Quantidade:");

        jbLoadComponents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/load_components.png"))); // NOI18N
        jbLoadComponents.setToolTipText("Carregar Componentes do Conjunto");
        jbLoadComponents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoadComponentsActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jpSetLayout = new javax.swing.GroupLayout(jpSet);
        jpSet.setLayout(jpSetLayout);
        jpSetLayout.setHorizontalGroup(
            jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpSetLayout.createSequentialGroup()
                        .addComponent(jlSet)
                        .addGap(33, 33, 33)
                        .addComponent(jtfSetId, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSearchSet, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpSetLayout.createSequentialGroup()
                        .addComponent(jlSetQuantity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfSetQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jbLoadComponents, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpSetLayout.setVerticalGroup(
            jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSetLayout.createSequentialGroup()
                .addGroup(jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jbLoadComponents, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpSetLayout.createSequentialGroup()
                        .addGroup(jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpSetLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jlSet))
                            .addComponent(jbSearchSet, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfSetId, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jpSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtfSetQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlSetQuantity))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções"));

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/save_16.png"))); // NOI18N
        jbSave.setText("Salvar");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbCancel)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpComponents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jpComponents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cancela a operação - botão cancelar
     * 
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        getController().clear();
        tab.remove();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Carrega os componentes de um conjunto no table model - botão carregar componentes
     * 
     * @param evt
     */
    private void jbLoadComponentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoadComponentsActionPerformed
        try {
            clearObjectErrors();
            
            String setId = getJtfSetId().getText();
            Double quantity = null;

            boolean validSetId = false;
            boolean validQuantity = false;

            try {
                quantity = Double.parseDouble( jtfSetQuantity.getText() );
                getController().setOrderSetQuantity(quantity);
                validQuantity = true;
            } catch (NumberFormatException ex) {
                setObjectError("setQuantity", "A quantidade do conjunto deve ser um número!");
            }

            if ( !setId.isEmpty() || !setId.equals("") ) {
                getController().setOrderSet(setId, false);
                loadDetailTableModel();
                validSetId = true;
            } else {
                setObjectError("set", "O código do conjunto não pode ser vazio!");
            }

            if ( !validSetId || !validQuantity ) {
                throw new ValidatorException();
            }
        } catch (SetPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (OrderDetailPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
        }
    }//GEN-LAST:event_jbLoadComponentsActionPerformed

    /**
     * Abre diálogo para busca de conjuntos - botão buscar
     * 
     * @param evt
     */
    private void jbSearchSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSearchSetActionPerformed
        SetSearchDialog dialog = new SetSearchDialog(parent, new OrderSetSearchAdapter(this));
        dialog.setVisible(true);
    }//GEN-LAST:event_jbSearchSetActionPerformed

    /**
     * Passa os dados da view para a model e salva através do controller estes dados - botão salvar
     * 
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        try {
            clearObjectErrors();

            Date date = null;
            Double setQuantity = null;

            boolean validDate = false;
            boolean validSetQuantity = false;

            date = jdcDeliveryDate.getDate();

            if ( date == null ) {
                MessageDialog.warning("A data de entrega é inválida!", parent);
            } else {
                validDate = true;
            }

            try {
                setQuantity = Double.parseDouble( jtfSetQuantity.getText() );
                validSetQuantity = true;
            } catch (NumberFormatException ex) {
                setObjectError("setQuantity", "A quantidade do conjunto deve ser numérica!");
            }

            if ( validDate && validSetQuantity ) {
                try {
                    getController().setOrderCustomer(getJtfCustomerId().getText());
                    getController().setOrderDeliveryDate(date);
                    getController().setOrderId(jtfId.getText());
                    getController().setOrderSet(jtfSetId.getText(), true);
                    getController().setOrderSetQuantity(setQuantity);
                    getController().saveOrder();

                    String message = "";

                    if ( getController().isEdit() ) {
                        message = " modificado";
                    } else {
                        message = " adicionado";
                    }

                    //Exibe mensagem de sucesso
                    MessageDialog.sucess("Ordem Cod. " + getController().getOrderId()
                            + message + " com sucesso!", parent);

                    getController().clear();

                    if ( panel != null ) {
                        panel.loadOrderTableModel();
                    }

                    tab.remove();

                } catch (OrderPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                } catch (SetPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                } catch (OrderDetailPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                } catch (CustomerPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            } else {
                throw new ValidatorException();
            }
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
            setObjectErrors(ex);
        }

    }//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Abre diálogo para modificação da quantidade de determinado componente - botão modificar quantidades
     * 
     * @param evt
     */
    private void jbModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyActionPerformed
        if ( loadSelectedOrderDetail() ) {
            OrderDetailDialog dialog = new OrderDetailDialog(parent, this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jbModifyActionPerformed

    /**
     * Abre diálogo para modificação da quantidade de determinado componente
     * através de clique duplo.
     * 
     * @param evt
     */
    private void jtDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtDetailMouseClicked
        if ( evt.getClickCount() == 2 ) {
            if ( loadSelectedOrderDetail() ) {
                OrderDetailDialog dialog = new OrderDetailDialog(parent, this);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jtDetailMouseClicked

    /**
     * Abre diálogo para busca de clientes - botão buscar
     * 
     * @param evt
     */
    private void jbSearchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSearchCustomerActionPerformed
        CustomerSearchDialog dialog = new CustomerSearchDialog(parent, new OrderCustomerSearchAdapter(this));
        dialog.setVisible(true);
    }//GEN-LAST:event_jbSearchCustomerActionPerformed

    /**
     * Carrega para um label o nome do cliente selecionado - botão carregar
     * 
     * @param evt
     */
    private void jbLoadCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoadCustomerNameActionPerformed
        try {
            controller.setOrderCustomer(jtfCustomerId.getText());
            jlCustomerName.setText( controller.getOrderCustomerName() );
        } catch (CustomerPersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }//GEN-LAST:event_jbLoadCustomerNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbLoadComponents;
    private javax.swing.JButton jbLoadCustomerName;
    private javax.swing.JButton jbModify;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbSearchCustomer;
    private javax.swing.JButton jbSearchSet;
    private com.toedter.calendar.JDateChooser jdcDeliveryDate;
    private javax.swing.JLabel jlComponents;
    private javax.swing.JLabel jlCustomerId;
    private javax.swing.JLabel jlCustomerName;
    private javax.swing.JLabel jlCustomerNameLabel;
    private javax.swing.JLabel jlSet;
    private javax.swing.JLabel jlSetQuantity;
    private javax.swing.JPanel jpComponents;
    private javax.swing.JPanel jpCustomer;
    private javax.swing.JPanel jpId;
    private javax.swing.JPanel jpSet;
    private javax.swing.JScrollPane jspDetail;
    private javax.swing.JTable jtDetail;
    private javax.swing.JTextField jtfCustomerId;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfSetId;
    private javax.swing.JTextField jtfSetQuantity;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * @return the jtfSetId
     */
    public javax.swing.JTextField getJtfSetId() {
        return jtfSetId;
    }

    /**
     * @return the controller
     */
    public OrderController getController() {
        return controller;
    }

    /**
     * @return the jtfCustomerId
     */
    public javax.swing.JTextField getJtfCustomerId() {
        return jtfCustomerId;
    }

    /**
     * @return the jlCustomerName
     */
    public javax.swing.JLabel getJlCustomerName() {
        return jlCustomerName;
    }

}
