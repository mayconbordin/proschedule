package com.proschedule.core.persistence.view.component;

import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
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
 * Painel de Componentes - tela principal para exibição e edição de componentes
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private ComponentTableModel componentTableModel;
    private ComponentDetailTableModel componentDetailTableModel;
    private ComponentController controller;

    private ComponentFieldComboBoxModel componentFieldComboBoxModel;
    private StringOperatorComboBoxModel stringOperatorComboBoxModel;

    private MouseAdapterBalloonTip idBalloonTip;
    private MouseAdapterBalloonTip rawMaterialBalloonTip;

    /**
     * Construtor da Classe
     * 
     * @param parent O frame principal
     */
    public ComponentPanel( java.awt.Frame parent ) {
        this.parent = parent;
        startUp();
    }

    /**
     * Inicia as configurações, componentens, variáveis
     */
    public void startUp() {
        initComponents();
        
        controller = new ComponentController();

        //Carrega o table model
        loadComponentTableModel();

        loadComponentDetailTableModel();

        //Carrega a barra de busca
        loadSearchBar();
    }

    /**
     * Carrega a barra de busca de componentes
     */
    public void loadSearchBar() {
        componentFieldComboBoxModel = new ComponentFieldComboBoxModel();
        stringOperatorComboBoxModel = new StringOperatorComboBoxModel();

        jcbField.setModel(componentFieldComboBoxModel);
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
     * Carrega a tabela de componentes, listando todos os registros existentes
     */
    public void loadComponentTableModel() {
        try {
            componentTableModel = new ComponentTableModel( getController().getComponents() );
            jtComponents.setModel(componentTableModel);
        } catch (ComponentPersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }

    /**
     * Carrega a tabela de detalhes de um componente
     */
    public void loadComponentDetailTableModel() {
        if ( controller.getComponent() != null ) {
            componentDetailTableModel = new ComponentDetailTableModel( controller.getComponentDetails() );
            componentDetailTableModel.orderByOrder();
            componentDetailTableModel.loadOrder();
        } else {
            componentDetailTableModel = new ComponentDetailTableModel();
            componentDetailTableModel.orderByOrder();
        }

        jtDetail.setModel( getComponentDetailTableModel());
    }

    /**
     * Carrega a tabela de detalhes de um componente
     */
    public void clearComponentDetailTableModel() {
        componentDetailTableModel = new ComponentDetailTableModel();
        componentDetailTableModel.orderByOrder();
        
        jtDetail.setModel( getComponentDetailTableModel());
    }

    /**
     * Desabilita o formulário de edição de componentes
     */
    public void disableForm() {
        //Desabilita componentes
        jtfId.setEditable(false);
        jtaRawMaterial.setEditable(false);
        jbAddDetails.setEnabled(false);
        jbModifyDetails.setEnabled(false);
        jbRemoveDetails.setEnabled(false);
        jbUp.setEnabled(false);
        jbDown.setEnabled(false);
        jbSave.setEnabled(false);
    }

    /**
     * Habilita o formulário de edição de componentes
     */
    public void enableForm() {
        //Habilita componentes
        if ( !controller.isEdit() ) {
            jtfId.setEditable(true);
        }
        jtaRawMaterial.setEditable(true);
        jbAddDetails.setEnabled(true);
        jbModifyDetails.setEnabled(true);
        jbRemoveDetails.setEnabled(true);
        jbUp.setEnabled(true);
        jbDown.setEnabled(true);
        jbSave.setEnabled(true);
    }

    /**
     * Limpa o formulário de edição de componentes
     */
    public void clearForm() {
        //Limpa os campos e tabela
        jtfId.setText(null);
        jtaRawMaterial.setText(null);
        clearComponentDetailTableModel();
    }

    /**
     * Carrega o objeto selecionado no controlador
     *
     * @return True se um objeto foi selecionado ou false se não havia objeto selecionado
     */
    public boolean loadSelectedComponent() {
        int index = jtComponents.getSelectedRow();

        if ( index != -1 ) {
            //Seta o componente selecionado para edição
            getController().editComponent( componentTableModel.getComponent( index ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar um componente para fazer isso!", parent);
        }

        return false;
    }

    /**
     * Carrega o componente selecionado na tabela de componentes no formulário
     * de edição
     */
    public void loadSelectedComponentOnForm() {
        if ( loadSelectedComponent() ) {
            //Seta os valores do componente na tela
            jtfId.setText( getController().getComponentId() );
            jtaRawMaterial.setText( getController().getComponentRawMaterial() );

            componentDetailTableModel = new ComponentDetailTableModel( getController().getComponentDetails() );

            jtDetail.setModel( componentDetailTableModel );
        }
    }

    /**
     * Carrega o detalhe de componente selecionada na tabela para o controlador
     *
     * @return True se um registro foi selecionado ou false se nada foi selecionado
     */
    public boolean loadSelectedComponentDetail() {
        int index = jtDetail.getSelectedRow();

        if ( index != -1 ) {
            getController().editComponentDetail( componentDetailTableModel.getComponentDetail( jtDetail.getSelectedRow() ) );
            return true;
        } else {
            MessageDialog.error("É preciso selecionar uma operação de componente para fazer isso!", parent);
        }

        return false;
    }

    /**
     * Executa as funções para iniciar a criação de um novo componente
     */
    public void addComponent() {
        controller.newComponent();
        clearForm();
        enableForm();
        jtfId.requestFocus();
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfId.setBackground(Color.white);
        jtfId.removeMouseListener(idBalloonTip);
        
        jtaRawMaterial.setBackground(Color.white);
        jtaRawMaterial.removeMouseListener(rawMaterialBalloonTip);
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
        } else if ( fieldName.equals("rawMaterial") ) {
            jtaRawMaterial.setBackground(Color.yellow);

            rawMaterialBalloonTip = new MouseAdapterBalloonTip(jtaRawMaterial, message);
            jtaRawMaterial.addMouseListener(rawMaterialBalloonTip);
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

        jspComponents = new javax.swing.JScrollPane();
        jtComponents = new javax.swing.JTable();
        jbAdd = new javax.swing.JButton();
        jbModify = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jlSearchingFor = new javax.swing.JLabel();
        jcbField = new javax.swing.JComboBox();
        jcbOperator = new javax.swing.JComboBox();
        jtfValue = new javax.swing.JTextField();
        jbGoSearch = new javax.swing.JButton();
        jpDetails = new javax.swing.JPanel();
        jpRawMaterial = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaRawMaterial = new javax.swing.JTextArea();
        jpId = new javax.swing.JPanel();
        jtfId = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jlDetails = new javax.swing.JLabel();
        jspDetails = new javax.swing.JScrollPane();
        jtDetail = new javax.swing.JTable();
        jbAddDetails = new javax.swing.JButton();
        jbModifyDetails = new javax.swing.JButton();
        jbRemoveDetails = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jlComponentOperations = new javax.swing.JLabel();
        jbClearSearch = new javax.swing.JButton();

        jtComponents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtComponentsMouseClicked(evt);
            }
        });
        jtComponents.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtComponentsKeyPressed(evt);
            }
        });
        jspComponents.setViewportView(jtComponents);

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

        jpDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jpRawMaterial.setBorder(javax.swing.BorderFactory.createTitledBorder("Matéria Prima"));

        jtaRawMaterial.setColumns(20);
        jtaRawMaterial.setEditable(false);
        jtaRawMaterial.setRows(5);
        jScrollPane1.setViewportView(jtaRawMaterial);

        javax.swing.GroupLayout jpRawMaterialLayout = new javax.swing.GroupLayout(jpRawMaterial);
        jpRawMaterial.setLayout(jpRawMaterialLayout);
        jpRawMaterialLayout.setHorizontalGroup(
            jpRawMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpRawMaterialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpRawMaterialLayout.setVerticalGroup(
            jpRawMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpRawMaterialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
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

        jlDetails.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/component.png"))); // NOI18N
        jlDetails.setText("Componente:");

        jtDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ordem", "Operação", "Lead Time"
            }
        ));
        jtDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtDetailMouseClicked(evt);
            }
        });
        jspDetails.setViewportView(jtDetail);

        jbAddDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/add_16.png"))); // NOI18N
        jbAddDetails.setText("Adicionar");
        jbAddDetails.setEnabled(false);
        jbAddDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddDetailsActionPerformed(evt);
            }
        });

        jbModifyDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/edit_16.png"))); // NOI18N
        jbModifyDetails.setText("Modificar");
        jbModifyDetails.setEnabled(false);
        jbModifyDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyDetailsActionPerformed(evt);
            }
        });

        jbRemoveDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbRemoveDetails.setText("Remover");
        jbRemoveDetails.setEnabled(false);
        jbRemoveDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveDetailsActionPerformed(evt);
            }
        });

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

        jbUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/arrow_up.png"))); // NOI18N
        jbUp.setEnabled(false);
        jbUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUpActionPerformed(evt);
            }
        });

        jbDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/arrow_down.png"))); // NOI18N
        jbDown.setEnabled(false);
        jbDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDownActionPerformed(evt);
            }
        });

        jlComponentOperations.setFont(new java.awt.Font("Tahoma", 1, 12));
        jlComponentOperations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/operation.png"))); // NOI18N
        jlComponentOperations.setText("Operações do Componente:");

        javax.swing.GroupLayout jpDetailsLayout = new javax.swing.GroupLayout(jpDetails);
        jpDetails.setLayout(jpDetailsLayout);
        jpDetailsLayout.setHorizontalGroup(
            jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jpRawMaterial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlDetails))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDetailsLayout.createSequentialGroup()
                        .addComponent(jbAddDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbModifyDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRemoveDetails))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDetailsLayout.createSequentialGroup()
                        .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jspDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                            .addGroup(jpDetailsLayout.createSequentialGroup()
                                .addComponent(jlComponentOperations)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                                .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbUp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbDown, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jpDetailsLayout.setVerticalGroup(
            jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addGroup(jpDetailsLayout.createSequentialGroup()
                        .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jlDetails)
                                .addComponent(jlComponentOperations))
                            .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbCancel)
                                .addComponent(jbSave)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpDetailsLayout.createSequentialGroup()
                                .addComponent(jpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpRawMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDetailsLayout.createSequentialGroup()
                                .addComponent(jspDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jbAddDetails)
                                    .addComponent(jbModifyDetails)
                                    .addComponent(jbRemoveDetails)))
                            .addGroup(jpDetailsLayout.createSequentialGroup()
                                .addComponent(jbUp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbDown)))))
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
                    .addComponent(jspComponents, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                    .addComponent(jpDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jspComponents, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jpDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Carrega os dados do componente selecionado no formulário toda vez que um
     * componente é selecionado na tabela. Em caso de clique duplo, o componente
     * entre em modo de edição.
     * 
     * @param evt
     */
    private void jtComponentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtComponentsMouseClicked
        clearForm();
        disableForm();
        loadSelectedComponentOnForm();

        //Clique duplo
        if (evt.getClickCount() == 2) {
            enableForm();
            jtfId.requestFocus();
        }
    }//GEN-LAST:event_jtComponentsMouseClicked

    /**
     * Adição de novo componente - botão adicionar.
     * @param evt
     */
    private void jbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddActionPerformed
        addComponent();
    }//GEN-LAST:event_jbAddActionPerformed

    /**
     * Limpa o formulário de edição de componentes - botão limpar
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        clearObjectErrors();
        clearForm();
        disableForm();
        loadComponentTableModel();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Coloca em modo de edição o componente selecionado - botão modificar
     * @param evt
     */
    private void jbModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyActionPerformed
        loadSelectedComponentOnForm();
        enableForm();
        jtfId.requestFocus();
    }//GEN-LAST:event_jbModifyActionPerformed

    /**
     * Salva o componente novo ou em edição.
     *
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        try {
            clearObjectErrors();

            getController().setComponentId(jtfId.getText());
            getController().setComponentRawMaterial(jtaRawMaterial.getText());
            getController().saveComponent();

            String message = "";

            if ( getController().isEdit() ) {
                message = " modificado";
            } else {
                message = " adicionado";
            }

            //Exibe mensagem de sucesso
            MessageDialog.sucess("Componente Cod. " + getController()
                    .getComponentId() + message + " com sucesso!", parent);

            controller.clear();

            loadComponentTableModel();

            clearForm();
            disableForm();
        } catch (ComponentDetailPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (ComponentPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
            setObjectErrors(ex);
        }
    }//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Abre a tela para adicionar novo detalhe ao componente - botão adicionar
     *
     * @param evt
     */
    private void jbAddDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddDetailsActionPerformed
        controller.newComponentDetail();
        
        NewComponentDetailDialog dialog = new NewComponentDetailDialog(parent, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jbAddDetailsActionPerformed

    /**
     * Se as teclas para cima e para baixo forem pressionadas, a exibição dos
     * detalhes de componente é atualizada para o novo componente selecionado.
     * 
     * @param evt
     */
    private void jtComponentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtComponentsKeyPressed
        if ( evt.getKeyCode() == 38 || evt.getKeyCode() == 40 ) {
            loadSelectedComponentOnForm();
        }
    }//GEN-LAST:event_jtComponentsKeyPressed

    /**
     * Passa para baixo na tabela o componente selecionado.
     *
     * @param evt
     */
    private void jbDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDownActionPerformed
        int row = componentDetailTableModel.down( jtDetail.getSelectedRow() );
        jtDetail.getSelectionModel().setSelectionInterval(row, row);
    }//GEN-LAST:event_jbDownActionPerformed

    /**
     * Passa para cima na tabela o componente selecionado
     *
     * @param evt
     */
    private void jbUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUpActionPerformed
        int row = componentDetailTableModel.up( jtDetail.getSelectedRow() );
        jtDetail.getSelectionModel().setSelectionInterval(row, row);
    }//GEN-LAST:event_jbUpActionPerformed

    /**
     * Realiza a busca de acordo com os parâmetros informados e lista-os na tabela
     * de componentes.
     * 
     * @param evt
     */
    private void jbGoSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGoSearchActionPerformed
        try {
            SearchParam field = (SearchParam) jcbField.getSelectedItem();
            String value = jtfValue.getText();
            SearchParam operator = (SearchParam) jcbOperator.getSelectedItem();

            componentTableModel = new ComponentTableModel( getController().getComponents(field, value, operator) );
            jtComponents.setModel(componentTableModel);
        } catch (ComponentPersistenceException ex) {
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
        loadComponentTableModel();
        reloadSearchBar();
    }//GEN-LAST:event_jbClearSearchActionPerformed

    /**
     * Remove o componente selecionado - botão remover
     * 
     * @param evt
     */
    private void jbRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveActionPerformed
        if ( loadSelectedComponent() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover o componente Cod. "
                    + getController().getComponentId() + " do sistema?" ) ) {
                try {
                    getController().removeComponent();

                    MessageDialog.sucess("Componente removido com sucesso!", parent);

                    controller.clear();
                    loadComponentTableModel();

                    clearForm();
                    disableForm();
                } catch (ComponentPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
    }//GEN-LAST:event_jbRemoveActionPerformed

    /**
     * Modifica o detalhe de componente selecionado - botão modificar
     * 
     * @param evt
     */
    private void jbModifyDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyDetailsActionPerformed
        if ( loadSelectedComponentDetail() ) {
            NewComponentDetailDialog dialog = new NewComponentDetailDialog(parent, this);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jbModifyDetailsActionPerformed

    /**
     * Remove o detalhe de componente selecionado - botão remover
     * 
     * @param evt
     */
    private void jbRemoveDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveDetailsActionPerformed
        if ( loadSelectedComponentDetail() ) {
            if ( QuestionDialog.confirm("Deseja realmente remover a operação "
                    + controller.getComponentDetailOperationDescription() + " do componente?" ) ) {
                try {
                    controller.removeComponentDetail();
                    MessageDialog.sucess("Componente removido com sucesso!", parent);
                    loadComponentDetailTableModel();
                    componentDetailTableModel.loadOrder();
                } catch (ComponentDetailPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                }
            }
        }
    }//GEN-LAST:event_jbRemoveDetailsActionPerformed

    /**
     * Abre o diálogo de edição de detalhes quando for efetuado duplo clique sobre
     * um registro da tabela de detalhes
     * 
     * @param evt
     */
    private void jtDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtDetailMouseClicked
        if ( evt.getClickCount() == 2 ) {
            if ( loadSelectedComponentDetail() ) {
                NewComponentDetailDialog dialog = new NewComponentDetailDialog(parent, this);
                dialog.setVisible(true);
            }
        }
    }//GEN-LAST:event_jtDetailMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddDetails;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbClearSearch;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbGoSearch;
    private javax.swing.JButton jbModify;
    private javax.swing.JButton jbModifyDetails;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveDetails;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbUp;
    private javax.swing.JComboBox jcbField;
    private javax.swing.JComboBox jcbOperator;
    private javax.swing.JLabel jlComponentOperations;
    private javax.swing.JLabel jlDetails;
    private javax.swing.JLabel jlSearchingFor;
    private javax.swing.JPanel jpDetails;
    private javax.swing.JPanel jpId;
    private javax.swing.JPanel jpRawMaterial;
    private javax.swing.JScrollPane jspComponents;
    private javax.swing.JScrollPane jspDetails;
    private javax.swing.JTable jtComponents;
    private javax.swing.JTable jtDetail;
    private javax.swing.JTextArea jtaRawMaterial;
    private javax.swing.JTextField jtfId;
    private javax.swing.JTextField jtfValue;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * @return the componentDetailTableModel
     */
    public ComponentDetailTableModel getComponentDetailTableModel() {
        return componentDetailTableModel;
    }

    /**
     * @return the controller
     */
    public ComponentController getController() {
        return controller;
    }
}
