package com.proschedule.core.calendar.view;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.util.forms.MouseAdapterBalloonTip;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.validator.util.ValidatorException;
import java.awt.Color;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * Diálogo para criação de novo calendário.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class NewCalendarDialog extends javax.swing.JDialog {

    private javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
            getClass().getResource("/com/proschedule/resources/images/brand/icon.png")
    );
    private CalendarPresenter presenter;
    private CalendarPanel panel;
    private java.awt.Frame parent;

    private MouseAdapterBalloonTip yearBalloonTip;
    private MouseAdapterBalloonTip workingHoursBalloonTip;

    /**
     * Construtor da Classe - Cria um novo diálogo, este construtor é utilizado
     * quando se está dentro do painel de calendários, assim o diálogo adiciona
     * o novo calendário e recarrega o painel.
     * 
     * @param parent Frame principal
     * @param panel O painel de calendários
     */
    public NewCalendarDialog(java.awt.Frame parent, CalendarPanel panel) {
        this.parent = parent;
        this.panel = panel;
        this.presenter = panel.getPresenter();
        startUp();
    }

    /**
     * Construtor da Classe - Cria um novo diálogo sem a dependência para com
     * o painel de calendários.
     * 
     * @param parent Frame principal
     */
    public NewCalendarDialog(java.awt.Frame parent) {
        this.parent = parent;
        this.panel = null;
        this.presenter = new CalendarPresenter();
        startUp();
    }

    /**
     * Construtor da Classe - Cria um novo diálogo sem a dependência para com
     * o painel de calendários.
     *
     * @param parent Frame principal
     * @param year O ano a ser adicionado, evita o usuário de ter de digitá-lo
     */
    public NewCalendarDialog(java.awt.Frame parent, int year) {
        this.parent = parent;
        this.panel = null;
        this.presenter = new CalendarPresenter();
        startUp();

        //Seta o ano que precisa ser adicionado
        jtfYear.setText( String.valueOf( year ) );
    }

    /**
     * Inicializa as configurações, componentes
     */
    public void startUp() {
        initComponents();

        //Centraliza o diálogo
        setLocationRelativeTo(null);
    }

    /**
     * Fecha o diálogo
     */
    public void close() {
        dispose();
    }

    /**
     * Desabilita os campos
     */
    public void disableAll() {
        jtfYear.setEnabled(false);
        jtfWorkingHours.setEnabled(false);
        jcbWorkingDay.setEnabled(false);
        jbSave.setEnabled(false);
        jbCancel.setEnabled(false);
    }

    /**
     * Habilita os campos
     */
    public void enableAll() {
        jtfYear.setEnabled(true);
        jtfWorkingHours.setEnabled(true);
        jcbWorkingDay.setEnabled(true);
        jbSave.setEnabled(true);
        jbCancel.setEnabled(true);
    }

    /**
     * Limpa os erros de validação do formulário
     */
    public void clearObjectErrors() {
        jtfYear.setBackground(Color.white);
        jtfYear.removeMouseListener(yearBalloonTip);

        jtfWorkingHours.setBackground(Color.white);
        jtfWorkingHours.removeMouseListener(workingHoursBalloonTip);
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
        if ( fieldName.equals("year") ) {
            jtfYear.setBackground(Color.yellow);

            yearBalloonTip = new MouseAdapterBalloonTip(jtfYear, message);
            jtfYear.addMouseListener(yearBalloonTip);
        } else if ( fieldName.equals("workingHours") ) {
            jtfWorkingHours.setBackground(Color.yellow);

            workingHoursBalloonTip = new MouseAdapterBalloonTip(jtfWorkingHours, message);
            jtfWorkingHours.addMouseListener(workingHoursBalloonTip);
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

        jtfYear = new javax.swing.JTextField();
        jlYear = new javax.swing.JLabel();
        jlWorkingHours = new javax.swing.JLabel();
        jtfWorkingHours = new javax.swing.JTextField();
        jcbWorkingDay = new javax.swing.JCheckBox();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Novo Calendário");
        setIconImage(icon.getImage());
        setModal(true);

        jlYear.setText("Ano do Calendário:");

        jlWorkingHours.setText("Horas Trabalhadas/Dia:");

        jcbWorkingDay.setText("Haverá trabalho nos fins de semana");
        jcbWorkingDay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

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
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlYear)
                            .addComponent(jlWorkingHours))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfWorkingHours, 0, 0, Short.MAX_VALUE)
                            .addComponent(jtfYear, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                        .addGap(135, 135, 135))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcbWorkingDay)
                        .addGap(135, 135, 135))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlYear)
                    .addComponent(jtfYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfWorkingHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlWorkingHours))
                .addGap(18, 18, 18)
                .addComponent(jcbWorkingDay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Salva o novo calendário
     * 
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        SaveCalendar scThread = new SaveCalendar();
        scThread.start();
}//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Cancela o que estava sendo feito no diálogo.
     * 
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        close();
}//GEN-LAST:event_jbCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSave;
    private javax.swing.JCheckBox jcbWorkingDay;
    private javax.swing.JLabel jlWorkingHours;
    private javax.swing.JLabel jlYear;
    private javax.swing.JTextField jtfWorkingHours;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the parent
     */
    public java.awt.Frame getParentFrame() {
        return parent;
    }

    class SaveCalendar extends Thread {
        @Override
        public void run() {
            try {
                clearObjectErrors();

                int year = 0;
                double workingHours = 0.0;

                boolean yearValid = false;
                boolean workingHoursValid = false;

                try {
                    try {
                        year = Integer.parseInt(jtfYear.getText());
                        yearValid = true;
                    } catch (NumberFormatException ex) {
                        setObjectError("year", "O ano deve ser um valor inteiro.");
                    }

                    try {
                        workingHours = Double.parseDouble(jtfWorkingHours.getText());
                        presenter.validateWorkingHours(workingHours);
                        workingHoursValid = true;
                    } catch (NumberFormatException ex) {
                        setObjectError("workingHours", "As horas trabalhadas deve ser um valor numérico.");
                    }

                    if ( yearValid && workingHoursValid ) {
                        setVisible(false);
 
                        ShowProgressBar pbThread = new ShowProgressBar();
                        pbThread.start();

                        //Salva o calendário
                        presenter.newCalendar(year, workingHours, jcbWorkingDay.isSelected());

                        //Exibe mensagem de sucesso
                        MessageDialog.sucess("Calendário de " + presenter.getCalendar()
                                .getYear() + " adicionado com sucesso!", parent);

                        if ( panel != null ) {
                            panel.loadCalendar(presenter.getCalendar());
                        }

                        pbThread.close();
                        close();
                    } else {
                        throw new ValidatorException();
                    }
                } catch (CalendarPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                    enableAll();
                } catch (OperationSchedulingPersistenceException ex) {
                    MessageDialog.error(ex, parent);
                    enableAll();
                }
            } catch (ValidatorException ex) {
                MessageDialog.warning(ex, parent);
                enableAll();
                setObjectErrors(ex);
            }
        }
    }

    class ShowProgressBar extends Thread {
        private NewCalendarProgressBar dialog;
        
        @Override
        public void run() {
            dialog = new NewCalendarProgressBar(parent, true);
            dialog.setVisible(true);
        }

        public void close() {
            dialog.dispose();
        }
    }
}
