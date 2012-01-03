package com.proschedule.core.persistence.view.set;

import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.util.forms.MouseAdapterBalloonTip;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.messages.QuestionDialog;
import com.proschedule.util.tabs.AbstractTab;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Color;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * Painel para adição/modificação de Conjuntos
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class NewSetPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private SetPanel panel;
    private SetController controller;
    private SetComponentTableModel componentTableModel;
    private SetDetailTableModel detailTableModel;

    private MouseAdapterBalloonTip idBalloonTip;
    private MouseAdapterBalloonTip leadTimeValueBalloonTip;

    /**
     * Construtor da Classe - com vinculação ao painel de listagem de conjuntos
     *
     * @param parent O frame principal
     * @param panel O painel de conjuntos
     */
    public NewSetPanel(java.awt.Frame parent, SetPanel panel) {
        this.parent = parent;
        this.panel = panel;
        this.controller = panel.getController();
        startUp();
    }

    /**
     * Construtor da Classe - independente do painel de listagem de conjuntos
     *
     * @param parent O frame principal
     */
    public NewSetPanel(java.awt.Frame parent) {
        this.parent = parent;
        this.panel = null;
        this.controller = new SetController();
        controller.newSet();
        startUp();
    }

    /**
     * Inicializa as configurações
     */
    public void startUp() {
        initComponents();
        loadComponentsTableModel();
        loadDetailTableModel();

        if ( controller.isEdit() ) {
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
        jtfId.setText( controller.getSetId() );
        jtfLeadTimeValue.setText( String.valueOf ( controller.getComponentsLeadTimeValue() ) );
        jcbLeadTimeType.setSelectedItem( controller.getComponentsLeadTimeType() );

        loadTotalLeadTime();
    }

    /**
     * Carrega a tabela de componentes do conjunto
     */
    public void loadComponentsTableModel() {
        if ( getController().getSet() != null ) {
            componentTableModel = new SetComponentTableModel( getController().getSetComponents() );
        } else {
            componentTableModel = new SetComponentTableModel();
        }

        jtComponents.setModel(componentTableModel);
    }

    /**
     * Carrega a tabela de detalhes do conjunto
     */
    public void loadDetailTableModel() {
        if ( getController().getSet() != null ) {
            detailTableModel = new SetDetailTableModel( getController().getSetDetails() );
        } else {
            detailTableModel = new SetDetailTableModel();
        }

        jtDetail.setModel(getDetailTableModel());
    }

    /**
     * Carrega o detalhe de conjunto selecionada na tabela para o controlador
     *
     * @return True se um registro foi selecionado ou false se nada foi selecionado
     */
    public boolean loadSelectedSetDetail() {
        int index = jtDetail.getSelectedRow();

        if ( index != -1 ) {
            getController().editSetDetail( detailTableModel.getSetDetail( jtDetail.getSelectedRow() ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar uma operação de conjunto para fazer isso!", parent);
        }

        return false;
    }

    /**
     * Carrega o detalhe de conjunto selecionada na tabela para o controlador
     *
     * @return True se um registro foi selecionado ou false se nada foi selecionado
     */
    public boolean loadSelectedSetComponent() {
        int index = jtComponents.getSelectedRow();

        if ( index != -1 ) {
            getController().editSetComponent( componentTableModel.getSetComponent( jtComponents.getSelectedRow() ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar um componente do conjunto para fazer isso!", parent);
        }

        return false;
    }

    /**
     * Carrega o lead time total do conjunto
     */
    public void loadTotalLeadTime() {
        jlTotalLeadTime.setText( controller.getTotalLeadTime() );
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfId.setBackground(Color.white);
        jtfId.removeMouseListener(idBalloonTip);

        jtfLeadTimeValue.setBackground(Color.white);
        jtfLeadTimeValue.removeMouseListener(leadTimeValueBalloonTip);
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
        } else if ( fieldName.equals("leadTimeValue") ) {
            jtfLeadTimeValue.setBackground(Color.yellow);

            leadTimeValueBalloonTip = new MouseAdapterBalloonTip(jtfLeadTimeValue, message);
            jtfLeadTimeValue.addMouseListener(leadTimeValueBalloonTip);
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
        jpLeadTime = new javax.swing.JPanel();
        jcbLeadTimeType = new javax.swing.JComboBox();
        jtfLeadTimeValue = new javax.swing.JTextField();
        jbCalcComponentLeadTime = new javax.swing.JButton();
        jpComponents = new javax.swing.JPanel();
        jlComponents = new javax.swing.JLabel();
        jspComponents = new javax.swing.JScrollPane();
        jtComponents = new javax.swing.JTable();
        jbRemoveComponent = new javax.swing.JButton();
        jbAddComponent = new javax.swing.JButton();
        jbModifyComponent = new javax.swing.JButton();
        jpOperations = new javax.swing.JPanel();
        jlOperations = new javax.swing.JLabel();
        jspDetail = new javax.swing.JScrollPane();
        jtDetail = new javax.swing.JTable();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jbRemoveDetail = new javax.swing.JButton();
        jbModifyDetail = new javax.swing.JButton();
        jbAddDetail = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlTotalLeadTime = new javax.swing.JLabel();
        jbCalcTotalLeadTime = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        jpId.setBorder(javax.swing.BorderFactory.createTitledBorder("Código"));

        javax.swing.GroupLayout jpIdLayout = new javax.swing.GroupLayout(jpId);
        jpId.setLayout(jpIdLayout);
        jpIdLayout.setHorizontalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIdLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfId, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpIdLayout.setVerticalGroup(
            jpIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIdLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jpLeadTime.setBorder(javax.swing.BorderFactory.createTitledBorder("Lead Time p/ Componentes"));

        jcbLeadTimeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "horas" }));

        jbCalcComponentLeadTime.setText("Calcular Lead Time");
        jbCalcComponentLeadTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCalcComponentLeadTimeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpLeadTimeLayout = new javax.swing.GroupLayout(jpLeadTime);
        jpLeadTime.setLayout(jpLeadTimeLayout);
        jpLeadTimeLayout.setHorizontalGroup(
            jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLeadTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jbCalcComponentLeadTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpLeadTimeLayout.createSequentialGroup()
                        .addComponent(jtfLeadTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbLeadTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpLeadTimeLayout.setVerticalGroup(
            jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLeadTimeLayout.createSequentialGroup()
                .addGroup(jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfLeadTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbLeadTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jbCalcComponentLeadTime))
        );

        jlComponents.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlComponents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/component.png"))); // NOI18N
        jlComponents.setText("Componentes:");

        jtComponents.setModel(new javax.swing.table.DefaultTableModel(
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
        jtComponents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtComponentsMouseClicked(evt);
            }
        });
        jspComponents.setViewportView(jtComponents);

        jbRemoveComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbRemoveComponent.setText("Remover");
        jbRemoveComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveComponentActionPerformed(evt);
            }
        });

        jbAddComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/add_16.png"))); // NOI18N
        jbAddComponent.setText("Adicionar");
        jbAddComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddComponentActionPerformed(evt);
            }
        });

        jbModifyComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbModifyComponent.setText("Modificar");
        jbModifyComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyComponentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpComponentsLayout = new javax.swing.GroupLayout(jpComponents);
        jpComponents.setLayout(jpComponentsLayout);
        jpComponentsLayout.setHorizontalGroup(
            jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpComponentsLayout.createSequentialGroup()
                .addGroup(jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jspComponents, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1115, Short.MAX_VALUE)
                    .addGroup(jpComponentsLayout.createSequentialGroup()
                        .addComponent(jlComponents)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 686, Short.MAX_VALUE)
                        .addComponent(jbAddComponent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbModifyComponent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemoveComponent)))
                .addContainerGap())
        );
        jpComponentsLayout.setVerticalGroup(
            jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpComponentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpComponentsLayout.createSequentialGroup()
                        .addComponent(jlComponents)
                        .addGap(11, 11, 11))
                    .addGroup(jpComponentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbRemoveComponent)
                        .addComponent(jbModifyComponent)
                        .addComponent(jbAddComponent)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspComponents, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jlOperations.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlOperations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/operation.png"))); // NOI18N
        jlOperations.setText("Operações:");

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

        jbUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/arrow_up.png"))); // NOI18N
        jbUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUpActionPerformed(evt);
            }
        });

        jbDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/arrow_down.png"))); // NOI18N
        jbDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDownActionPerformed(evt);
            }
        });

        jbRemoveDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbRemoveDetail.setText("Remover");
        jbRemoveDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveDetailActionPerformed(evt);
            }
        });

        jbModifyDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbModifyDetail.setText("Modificar");
        jbModifyDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyDetailActionPerformed(evt);
            }
        });

        jbAddDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/add_16.png"))); // NOI18N
        jbAddDetail.setText("Adicionar");
        jbAddDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddDetailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpOperationsLayout = new javax.swing.GroupLayout(jpOperations);
        jpOperations.setLayout(jpOperationsLayout);
        jpOperationsLayout.setHorizontalGroup(
            jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOperationsLayout.createSequentialGroup()
                .addGroup(jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpOperationsLayout.createSequentialGroup()
                        .addComponent(jlOperations)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 674, Short.MAX_VALUE)
                        .addComponent(jbAddDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbModifyDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemoveDetail))
                    .addComponent(jspDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1076, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jbDown, 0, 0, Short.MAX_VALUE)
                    .addComponent(jbUp, javax.swing.GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpOperationsLayout.setVerticalGroup(
            jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOperationsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpOperationsLayout.createSequentialGroup()
                        .addComponent(jlOperations)
                        .addGap(11, 11, 11))
                    .addGroup(jpOperationsLayout.createSequentialGroup()
                        .addGroup(jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbRemoveDetail)
                            .addComponent(jbModifyDetail)
                            .addComponent(jbAddDetail))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jpOperationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpOperationsLayout.createSequentialGroup()
                        .addComponent(jbUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDown))
                    .addComponent(jspDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Lead Time Total"));

        jlTotalLeadTime.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlTotalLeadTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTotalLeadTime.setText("0.0 horas");

        jbCalcTotalLeadTime.setText("Calcular Total");
        jbCalcTotalLeadTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCalcTotalLeadTimeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlTotalLeadTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(jbCalcTotalLeadTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jlTotalLeadTime, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCalcTotalLeadTime))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções"));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbCancel))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpComponents, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpOperations, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpLeadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpLeadTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jpComponents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpOperations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cancela a operação e fecha a aba - botão cancelar
     * 
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        controller.clear();
        tab.remove();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Abre o diálogo para adicionar novo componente ao conjunto - botão adicionar
     * 
     * @param evt
     */
    private void jbAddComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddComponentActionPerformed
        controller.newSetComponent();

        NewSetComponentDialog dialog = new NewSetComponentDialog(parent, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jbAddComponentActionPerformed

    /**
     * Salva os dados do conjunto - botão salvar
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        try {
            clearObjectErrors();
            
            Double ltValue = null;
            boolean isLtValid = false;

            try {
                ltValue = Double.parseDouble( jtfLeadTimeValue.getText() );
                isLtValid = true;
            } catch(NumberFormatException ex ) {
                setObjectError("leadTimeValue", "O valor do lead time precisa ser numérico.");
            }

            if ( isLtValid ) {
                getController().setSetId( jtfId.getText() );
                getController().setComponentsLeadTime(ltValue, (String) jcbLeadTimeType.getSelectedItem());
                getController().saveSet();

                String message = "";

                if ( getController().isEdit() ) {
                    message = " modificado";
                } else {
                    message = " adicionado";
                }

                //Exibe mensagem de sucesso
                MessageDialog.sucess("Conjunto Cod. " +
                        getController().getSetId() + message + " com sucesso!", parent);

                controller.clear();

                if ( panel != null ) {
                    panel.loadSetTableModel();
                }

                tab.remove();
            } else {
                throw new ValidatorException();
            }
        } catch (SetComponentPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (SetDetailPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (SetPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
            setObjectErrors(ex);
        }
    }//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Abre diálogo para confirmação de remoção de componente do conjunto - botão remover
     * @param evt
     */
    private void jbRemoveComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveComponentActionPerformed
        if ( loadSelectedSetComponent() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover o componente "
                    + controller.getSetComponentComponentId() + " do conjunto?" ) ) {
                try {
                    controller.removeSetComponent();

                    MessageDialog.sucess("Componente removido com sucesso!", parent);
                    loadComponentsTableModel();
                } catch (SetComponentPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
}//GEN-LAST:event_jbRemoveComponentActionPerformed

    /**
     * Abre diálogo para adicionar novo detalhe ao conjunto - botão adicionar
     * @param evt
     */
    private void jbAddDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddDetailActionPerformed
        controller.newSetDetail();

        NewSetDetailDialog dialog = new NewSetDetailDialog(parent, this);
        dialog.setVisible(true);
}//GEN-LAST:event_jbAddDetailActionPerformed

    /**
     * Abre o diálogo para modificação de detalhe do conjunto - botão modificar
     * 
     * @param evt
     */
    private void jbModifyDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyDetailActionPerformed
        if ( loadSelectedSetDetail() ) {
            NewSetDetailDialog dialog = new NewSetDetailDialog(parent, this);
            dialog.setVisible(true);
        }
}//GEN-LAST:event_jbModifyDetailActionPerformed

    /**
     * Abre o diálogo para confirmação de remoção de detalhe do conjunto - botão remover
     * @param evt
     */
    private void jbRemoveDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveDetailActionPerformed
        if ( loadSelectedSetDetail() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover a operação "
                    + controller.getSetDetailOperationDescription() + " do conjunto?" ) ) {
                try {
                    controller.removeSetDetail();

                    MessageDialog.sucess("Operação removida com sucesso!", parent);

                    loadDetailTableModel();
                    detailTableModel.loadOrder();
                } catch (SetDetailPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
}//GEN-LAST:event_jbRemoveDetailActionPerformed

    /**
     * Passa o detalhe selecionado para cima
     *
     * @param evt
     */
    private void jbUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUpActionPerformed
        int row = detailTableModel.up( jtDetail.getSelectedRow() );
        jtDetail.getSelectionModel().setSelectionInterval(row, row);
}//GEN-LAST:event_jbUpActionPerformed

    /**
     * Passa o detalhe selecionado para baixo
     * 
     * @param evt
     */
    private void jbDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDownActionPerformed
        int row = detailTableModel.down( jtDetail.getSelectedRow() );
        jtDetail.getSelectionModel().setSelectionInterval(row, row);
}//GEN-LAST:event_jbDownActionPerformed

    /**
     * Exibe o lead time proposto para os componentes do conjunto - botão calcular lead time
     * @param evt
     */
    private void jbCalcComponentLeadTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCalcComponentLeadTimeActionPerformed
        jtfLeadTimeValue.setText( String.valueOf( controller.getCalculatedComponentsLeadTimeValue() ) );
    }//GEN-LAST:event_jbCalcComponentLeadTimeActionPerformed

    /**
     * Exibe o lead time total do conjunto - botão calcular total
     * @param evt
     */
    private void jbCalcTotalLeadTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCalcTotalLeadTimeActionPerformed
        loadTotalLeadTime();
    }//GEN-LAST:event_jbCalcTotalLeadTimeActionPerformed

    private void jbModifyComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyComponentActionPerformed
        if ( loadSelectedSetComponent() ) {
            NewSetComponentDialog dialog = new NewSetComponentDialog(parent, this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jbModifyComponentActionPerformed

    private void jtComponentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtComponentsMouseClicked
        if ( evt.getClickCount() == 2 ) {
            if ( loadSelectedSetComponent() ) {
                NewSetComponentDialog dialog = new NewSetComponentDialog(parent, this);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jtComponentsMouseClicked

    private void jtDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtDetailMouseClicked
        if ( evt.getClickCount() == 2 ) {
            if ( loadSelectedSetDetail() ) {
                NewSetDetailDialog dialog = new NewSetDetailDialog(parent, this);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jtDetailMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbAddComponent;
    private javax.swing.JButton jbAddDetail;
    private javax.swing.JButton jbCalcComponentLeadTime;
    private javax.swing.JButton jbCalcTotalLeadTime;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbModifyComponent;
    private javax.swing.JButton jbModifyDetail;
    private javax.swing.JButton jbRemoveComponent;
    private javax.swing.JButton jbRemoveDetail;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbUp;
    private javax.swing.JComboBox jcbLeadTimeType;
    private javax.swing.JLabel jlComponents;
    private javax.swing.JLabel jlOperations;
    private javax.swing.JLabel jlTotalLeadTime;
    private javax.swing.JPanel jpComponents;
    private javax.swing.JPanel jpId;
    private javax.swing.JPanel jpLeadTime;
    private javax.swing.JPanel jpOperations;
    private javax.swing.JScrollPane jspComponents;
    private javax.swing.JScrollPane jspDetail;
    private javax.swing.JTable jtComponents;
    private javax.swing.JTable jtDetail;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfLeadTimeValue;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the tab
     */
    public AbstractTab getTab() {
        return tab;
    }

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

    /**
     * @return the detailTableModel
     */
    public SetDetailTableModel getDetailTableModel() {
        return detailTableModel;
    }

}
