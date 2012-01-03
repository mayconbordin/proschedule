package com.proschedule.util.messages;

import com.proschedule.main.ProSchedule;
import com.proschedule.util.exceptions.PersistenceException;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Toolkit;


/**
 *  Diálogo de mensagem
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class MessageDialog extends javax.swing.JDialog {

    /**
     * Mensagem de erro
     */
    public static final int ERROR_MESSAGE = 0;
    /**
     * Mensagem de aviso
     */
    public static final int WARNING_MESSAGE = 1;
    /**
     * Mensagem de informação
     */
    public static final int INFO_MESSAGE = 2;
    /**
     * Mensagem de sucesso
     */
    public static final int SUCESS_MESSAGE = 3;

    /**
     * Diálogo de erro
     * @param message
     * @param details
     * @param parent
     */
    public static void error(String message, String details, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, details,
                ProSchedule.getSystemTitle(),
                MessageDialog.ERROR_MESSAGE);
    }

    /**
     * Diálogo de erro
     * @param message
     * @param parent
     */
    public static void error(String message, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, "",
                ProSchedule.getSystemTitle(),
                MessageDialog.ERROR_MESSAGE);
    }

    /**
     * Diálogo de erro
     * @param ex
     * @param parent
     */
    public static void error(PersistenceException ex, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, ex.getMessage(),
                ex.getDetailMessage(), ProSchedule.getSystemTitle(),
                MessageDialog.ERROR_MESSAGE);
    }

    /**
     * Diálogo de aviso
     * @param message
     * @param details
     * @param parent
     */
    public static void warning(String message, String details, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, details,
                ProSchedule.getSystemTitle(),
                MessageDialog.WARNING_MESSAGE);
    }

    /**
     * Diálogo de aviso
     * @param message
     * @param parent
     */
    public static void warning(String message, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, "",
                ProSchedule.getSystemTitle(),
                MessageDialog.WARNING_MESSAGE);
    }

    /**
     * Diálogo de aviso
     * @param ex
     * @param parent
     */
    public static void warning(PersistenceException ex, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, ex.getMessage(),
                ex.getDetailMessage(), ProSchedule.getSystemTitle(),
                MessageDialog.WARNING_MESSAGE);
    }

    /**
     * Diálogo de aviso
     * @param ex
     * @param parent
     */
    public static void warning(ValidatorException ex, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, ex.getMessage(),
                ex.getDetailedMessage(), ProSchedule.getSystemTitle(),
                MessageDialog.WARNING_MESSAGE);
    }

    /**
     * Diálogo de informação
     * @param message
     * @param details
     * @param parent
     */
    public static void info(String message, String details, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, details,
                ProSchedule.getSystemTitle(),
                MessageDialog.INFO_MESSAGE);
    }

    /**
     * Diálogo de informação
     * @param message
     * @param parent
     */
    public static void info(String message, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, "",
                ProSchedule.getSystemTitle(),
                MessageDialog.INFO_MESSAGE);
    }

    /**
     * Diálogo de sucesso
     * @param message
     * @param details
     * @param parent
     */
    public static void sucess(String message, String details, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, details,
                ProSchedule.getSystemTitle(),
                MessageDialog.SUCESS_MESSAGE);
    }

    /**
     * Diálogo de sucesso
     * @param message
     * @param parent
     */
    public static void sucess(String message, java.awt.Frame parent) {
        MessageDialog dialog = new MessageDialog(parent, message, "",
                ProSchedule.getSystemTitle(),
                MessageDialog.SUCESS_MESSAGE);
    }

    /**
     * Icone da mensagem
     */
    private String messageIcon;

    /**
     * Exibe diálogo de erros ao usuário, permitindo a inserção de detalhes
     * sobre o erro.
     * 
     * @param parent <java.awt.Frame> O frame pai em relação ao diálogo
     * @param message <String> A mensagem a ser exibida ao usuário
     * @param details <String> Os detalhes da mensagem
     * @param title <String> O título do diálogo
     * @param msgType <Integer> O tipo de mensagem
     */
    public MessageDialog(java.awt.Frame parent, String message, String details, String title, int msgType) {
        super(parent, true);

        Toolkit.getDefaultToolkit().beep();

        //Carega o ícone
        loadIcon(msgType);

        //Inicia os componentes
        initComponents();

        //Carrega as configurações do diálogo
        loadConfig(message, details, title);
    }

    /**
     * Carega o ícone de acordo com o tipo de mensagem
     * @param msgType <Integer> O tipo de mensagem
     */
    private void loadIcon(int msgType) {
        if (msgType == ERROR_MESSAGE) {
            messageIcon = "/com/proschedule/resources/images/messages/cancel.png";
        } else if (msgType == WARNING_MESSAGE) {
            messageIcon = "/com/proschedule/resources/images/messages/warning.png";
        } else if (msgType == INFO_MESSAGE) {
            messageIcon = "/com/proschedule/resources/images/messages/info.png";
        } else if (msgType == SUCESS_MESSAGE) {
            messageIcon = "/com/proschedule/resources/images/messages/sucess.png";
        }
    }

    /**
     * Carrega as configurações do diálogo
     * 
     * @param message <String> A mensagem a ser exibida ao usuário
     * @param details <String> Os detalhes da mensagem
     * @param title <String> O título do diálogo
     */
    private void loadConfig(String message, String details, String title) {
        //Força o diálogo a contrair tamanho
        setSize(428,130);

        //Centraliza o diálogo
        setLocationRelativeTo(null);

        //Seta a mensagem
        jlMessage.setText(message);

        //Seta o título do diálogo
        this.setTitle(title);

        //Seta os detalhes da mensagem
        jtaDetails.setText(details);

        //Torna visível o diálogo
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlImage = new javax.swing.JLabel();
        jbExpand = new javax.swing.JButton();
        jlMessage = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaDetails = new javax.swing.JTextArea();
        jbOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jlImage.setIcon(new javax.swing.ImageIcon(getClass().getResource( messageIcon )));

        jbExpand.setText("▼");
        jbExpand.setToolTipText("Exibir detalhes do erro");
        jbExpand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExpandActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalhes"));

        jtaDetails.setColumns(20);
        jtaDetails.setRows(5);
        jScrollPane1.setViewportView(jtaDetails);

        jbOK.setText("OK");
        jbOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addComponent(jlImage, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbOK, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107)
                        .addComponent(jbExpand)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlImage)
                    .addComponent(jlMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbOK)
                    .addComponent(jbExpand, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbExpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExpandActionPerformed
        if ( !isExpanded ) {
            //Aumenta tamanho do diálogo
            setSize(428,300);

            //Muda a seta para contração
            jbExpand.setText("▲");

            //Muda o tooltip
            jbExpand.setToolTipText("Esconder detalhes");

            //Altera variável de indicação
            isExpanded = true;
        } else {
            //Diminui tamanho do diálogo
            setSize(428,130);

            //Muda a seta para expansão
            jbExpand.setText("▼");

            //Muda o tooltip
            jbExpand.setToolTipText("Exibir detalhes");

            //Altera variável de indicação
            isExpanded = false;
        }


    }//GEN-LAST:event_jbExpandActionPerformed

    private void jbOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOKActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbExpand;
    private javax.swing.JButton jbOK;
    private javax.swing.JLabel jlImage;
    private javax.swing.JLabel jlMessage;
    private javax.swing.JTextArea jtaDetails;
    // End of variables declaration//GEN-END:variables

    private Boolean isExpanded = false;
}
