package com.proschedule.core.persistence.view.operation;

import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.util.forms.MouseAdapterBalloonTip;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Color;
import java.util.Set;
import javax.validation.ConstraintViolation;
import net.java.balloontip.BalloonTip;

/**
 * Tela para adição/modificação de operações
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class NewOperationDialog extends javax.swing.JDialog {
    private OperationPresenter presenter;
    private java.awt.Frame parent;
    private OperationPanel panel;
    private OperationTypeComboBoxModel operationTypeComboBoxModel;

    private MouseAdapterBalloonTip descriptionBalloonTip;
    private MouseAdapterBalloonTip leadTimeValueBalloonTip;
    private MouseAdapterBalloonTip typeBalloonTip;

    /**
     * Construtor da Classe - Dependente da aba principal
     *
     * @param parent Frame principal
     * @param panel Painel de operações
     */
    public NewOperationDialog(java.awt.Frame parent, OperationPanel panel) {
        super(parent, true);
        this.parent = parent;
        this.presenter = panel.getPresenter();
        this.panel = panel;
        startUp();
    }

    /**
     * Inicializa as configurações, componentes
     */
    public void startUp() {
        initComponents();

        //Centraliza o diálogo
        setLocationRelativeTo(null);

        if ( presenter.isEdit() ) {
            modelToView();
            setTitle("Modificar Operação");
        } else {
            loadOperationTypeComboBox(null);
            setTitle("Adicionar Operação");
        }
    }

    /**
     * Construtor da Classe - Independente de abas
     *
     * @param parent Frame principal
     */
    public NewOperationDialog(java.awt.Frame parent) {
        super(parent, true);
        this.parent = parent;
        this.presenter = new OperationPresenter();
        this.panel = null;
        initComponents();

        //Centraliza o diálogo
        setLocationRelativeTo(null);

        loadOperationTypeComboBox(null);
        setTitle("Adicionar Operação");
    }

    /**
     * Pega a operação em edição do presenter e passa seus valores para os
     * componentes visuais, para edição
     */
    public void modelToView() {
        jtfDescription.setText( presenter.getOperation().getDescription() );
        jtfLeadTimeValue.setText( String.valueOf( presenter.getOperation().getLeadTime().getValue() ) );
        jcbLeadTimeType.setSelectedItem( presenter.getOperation().getLeadTime().getType() );
        loadOperationTypeComboBox( presenter.getOperation().getType() );
    }

    /**
     * Carrega o combobox com a lista de tipos de operações
     * 
     * @param type O objeto que será selecionado na lista, se não houver use null
     */
    public void loadOperationTypeComboBox( OperationType type ) {
        try {
            operationTypeComboBoxModel = new OperationTypeComboBoxModel(presenter.listTypes());
            
            if ( type != null ) {
                operationTypeComboBoxModel.setSelectedItem( type );
            }

            jcbType.setModel(operationTypeComboBoxModel);
        } catch (OperationTypePersistenceException ex) {
            MessageDialog.error(ex, parent);
        }
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfDescription.setBackground(Color.white);
        jtfDescription.removeMouseListener(descriptionBalloonTip);

        jtfLeadTimeValue.setBackground(Color.white);
        jtfLeadTimeValue.removeMouseListener(leadTimeValueBalloonTip);

        jcbType.setBackground(Color.white);
        jcbType.removeMouseListener(typeBalloonTip);
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
        if ( fieldName.equals("description") ) {
            jtfDescription.setBackground(Color.yellow);

            descriptionBalloonTip = new MouseAdapterBalloonTip(jtfDescription, message);
            jtfDescription.addMouseListener(descriptionBalloonTip);
        } else if ( fieldName.equals("leadTimeValue") ) {
            jtfLeadTimeValue.setBackground(Color.yellow);

            leadTimeValueBalloonTip = new MouseAdapterBalloonTip(jtfLeadTimeValue, message);
            jtfLeadTimeValue.addMouseListener(leadTimeValueBalloonTip);
        } else if ( fieldName.equals("type") ) {
            jcbType.setBackground(Color.yellow);

            typeBalloonTip = new MouseAdapterBalloonTip(jcbType, message);
            jcbType.addMouseListener(typeBalloonTip);
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

        jpDescription = new javax.swing.JPanel();
        jtfDescription = new javax.swing.JTextField();
        jpLeadTime = new javax.swing.JPanel();
        jtfLeadTimeValue = new javax.swing.JTextField();
        jcbLeadTimeType = new javax.swing.JComboBox();
        jpType = new javax.swing.JPanel();
        jcbType = new javax.swing.JComboBox();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpDescription.setBorder(javax.swing.BorderFactory.createTitledBorder("Descrição"));

        javax.swing.GroupLayout jpDescriptionLayout = new javax.swing.GroupLayout(jpDescription);
        jpDescription.setLayout(jpDescriptionLayout);
        jpDescriptionLayout.setHorizontalGroup(
            jpDescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescriptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpDescriptionLayout.setVerticalGroup(
            jpDescriptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescriptionLayout.createSequentialGroup()
                .addComponent(jtfDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpLeadTime.setBorder(javax.swing.BorderFactory.createTitledBorder("Lead Time Padrão"));

        jcbLeadTimeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "horas" }));

        javax.swing.GroupLayout jpLeadTimeLayout = new javax.swing.GroupLayout(jpLeadTime);
        jpLeadTime.setLayout(jpLeadTimeLayout);
        jpLeadTimeLayout.setHorizontalGroup(
            jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpLeadTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfLeadTimeValue, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbLeadTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpLeadTimeLayout.setVerticalGroup(
            jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLeadTimeLayout.createSequentialGroup()
                .addGroup(jpLeadTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfLeadTimeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbLeadTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpType.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Operação"));

        javax.swing.GroupLayout jpTypeLayout = new javax.swing.GroupLayout(jpType);
        jpType.setLayout(jpTypeLayout);
        jpTypeLayout.setHorizontalGroup(
            jpTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbType, 0, 209, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpTypeLayout.setVerticalGroup(
            jpTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpTypeLayout.createSequentialGroup()
                .addComponent(jcbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpLeadTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpLeadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Fecha o diálogo - botão cancelar
     * 
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Salva a nova/modificada operação no banco de dados através do presenter
     * 
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        try {
            clearObjectErrors();

            String description = "";
            Double leadTimeValue = 0.0;
            String leadTimeType = "";
            OperationType type = null;

            boolean validLeadTimeValue = false;

            description = jtfDescription.getText();
            leadTimeType = (String) jcbLeadTimeType.getSelectedItem();
            type = (OperationType) jcbType.getSelectedItem();

            try {
                leadTimeValue = Double.parseDouble( jtfLeadTimeValue.getText() );
                validLeadTimeValue = true;
            } catch (NumberFormatException ex) {
                setObjectError("leadTimeValue", "O valor do lead time deve ser numérico.");
            }

            if ( validLeadTimeValue ) {
                try {
                    presenter.save(description, leadTimeValue, leadTimeType, type);

                    setVisible(false);

                    if ( presenter.isEdit() ) {
                        //Exibe mensagem de sucesso
                        MessageDialog.sucess("Operação " + presenter.getOperation()
                                .getDescription() + " modificada com sucesso!", parent);
                    } else {
                        //Exibe mensagem de sucesso
                        MessageDialog.sucess("Operação" + presenter.getOperation()
                                .getDescription() + " adicionada com sucesso!", parent);
                    }

                    if ( panel != null ) {
                        panel.reloadOperationTableModel();
                    }

                    dispose();
                } catch (OperationPersistenceException ex) {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSave;
    private javax.swing.JComboBox jcbLeadTimeType;
    private javax.swing.JComboBox jcbType;
    private javax.swing.JPanel jpDescription;
    private javax.swing.JPanel jpLeadTime;
    private javax.swing.JPanel jpType;
    private javax.swing.JTextField jtfDescription;
    private javax.swing.JTextField jtfLeadTimeValue;
    // End of variables declaration//GEN-END:variables

}
