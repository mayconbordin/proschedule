package com.proschedule.core.calendar.view;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import com.proschedule.validator.util.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * Painel principal do Calendário.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class CalendarPanel extends javax.swing.JPanel {
    private CalendarPresenter presenter = new CalendarPresenter();
    private List<MonthPanel> months = new ArrayList();
    private AbstractTab tab;
    private java.awt.Frame parent;
    private boolean workingDaysMarked = false;

    /**
     * Construtor da Classe - Inicia os componentes e
     * carrega o calendario mais novo.
     * 
     * @param parent O frame principal.
     */
    public CalendarPanel( java.awt.Frame parent ) {
        this.parent = parent;
        initComponents();

        loadLastCalendar();
    }

    /**
     * Carrega o calendário mais novo no banco de dados
     */
    public void loadLastCalendar() {
        try {
            //Carrega os calendários
            getPresenter().loadCalendars();

            //Cria o modelo de combo box
            createJcbYearModel( getPresenter().getLastCalendar() );

            //Carrega as abas dos meses
            loadMonthTabs();
        } catch (CalendarPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (IndexOutOfBoundsException ex) {
            MessageDialog.info(ex.getMessage(), parent);
        }
    }

    /**
     * Depois de iniciado o painel, esta função deve ser usada para recarregar
     * um calendário.
     * 
     * @param calendar O calendário que será carregado no painel
     */
    public void loadCalendar( Calendar calendar ) {
        try {
            getPresenter().setCalendar( calendar );
            reloadMonths();

            //Carrega todos os calendários
            getPresenter().loadCalendars();

            //Cria o modelo de combo box
        createJcbYearModel( calendar );
        } catch (CalendarPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } 
    }

    /**
     * Cria o modelo para o combo box de seleção do ano do calendário.
     *
     * @param obj O calendário que será setado como selecionado
     */
    public void createJcbYearModel(Calendar obj) {
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(
                new Vector(getPresenter().getCalendars())
        );

        if ( obj != null ) {
            comboBoxModel.setSelectedItem(obj);
        }

        jcbYear.setModel(comboBoxModel);
    }

    /**
     * Carrega as abas dos meses e salva elas em uma lista.
     */
    public void loadMonthTabs() {
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(1) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(2) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(3) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(4) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(5) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(6) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(7) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(8) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(9) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(10) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(11) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(12) ) );

        jtpMonths.add("Jan", months.get(0));
        jtpMonths.add("Fev", months.get(1));
        jtpMonths.add("Mar", months.get(2));
        jtpMonths.add("Abr", months.get(3));
        jtpMonths.add("Mai", months.get(4));
        jtpMonths.add("Jun", months.get(5));
        jtpMonths.add("Jul", months.get(6));
        jtpMonths.add("Ago", months.get(7));
        jtpMonths.add("Set", months.get(8));
        jtpMonths.add("Out", months.get(9));
        jtpMonths.add("Nov", months.get(10));
        jtpMonths.add("Dez", months.get(11));
    }

    /**
     * Recarrega as abas dos meses, limpa a lista e as salva nela.
     */
    public void reloadMonths() {
        months = new ArrayList();
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(1) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(2) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(3) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(4) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(5) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(6) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(7) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(8) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(9) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(10) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(11) ) );
        months.add( new MonthPanel( getPresenter().getCalendar().getDaysPerMonth(12) ) );

        jtpMonths.removeAll();

        jtpMonths.add("Jan", months.get(0));
        jtpMonths.add("Fev", months.get(1));
        jtpMonths.add("Mar", months.get(2));
        jtpMonths.add("Abr", months.get(3));
        jtpMonths.add("Mai", months.get(4));
        jtpMonths.add("Jun", months.get(5));
        jtpMonths.add("Jul", months.get(6));
        jtpMonths.add("Ago", months.get(7));
        jtpMonths.add("Set", months.get(8));
        jtpMonths.add("Out", months.get(9));
        jtpMonths.add("Nov", months.get(10));
        jtpMonths.add("Dez", months.get(11));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpYear = new javax.swing.JPanel();
        jcbYear = new javax.swing.JComboBox();
        jpWorkingHours = new javax.swing.JPanel();
        jtfWorkingHours = new javax.swing.JTextField();
        jbSetWorkingHours = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbWorkingDays = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jbAdd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jtpMonths = new javax.swing.JTabbedPane();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jpYear.setBorder(javax.swing.BorderFactory.createTitledBorder("Ano"));

        jcbYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbYearItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jpYearLayout = new javax.swing.GroupLayout(jpYear);
        jpYear.setLayout(jpYearLayout);
        jpYearLayout.setHorizontalGroup(
            jpYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpYearLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbYear, 0, 159, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpYearLayout.setVerticalGroup(
            jpYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpYearLayout.createSequentialGroup()
                .addComponent(jcbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpWorkingHours.setBorder(javax.swing.BorderFactory.createTitledBorder("Horas Trabalho/Dia"));

        jbSetWorkingHours.setText("Aplicar a Todos");
        jbSetWorkingHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetWorkingHoursActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpWorkingHoursLayout = new javax.swing.GroupLayout(jpWorkingHours);
        jpWorkingHours.setLayout(jpWorkingHoursLayout);
        jpWorkingHoursLayout.setHorizontalGroup(
            jpWorkingHoursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpWorkingHoursLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfWorkingHours, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSetWorkingHours, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpWorkingHoursLayout.setVerticalGroup(
            jpWorkingHoursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpWorkingHoursLayout.createSequentialGroup()
                .addGroup(jpWorkingHoursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfWorkingHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSetWorkingHours))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Dias Trabalhados"));

        jbWorkingDays.setText("Marcar Todos");
        jbWorkingDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbWorkingDaysActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbWorkingDays, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jbWorkingDays)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Opções"));

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/save_16.png"))); // NOI18N
        jbSave.setText("Salvar");
        jbSave.setToolTipText("Salvar Calendário Atual");
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/close_16.png"))); // NOI18N
        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("Cancelar Operação Atual");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jbAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/add_16.png"))); // NOI18N
        jbAdd.setText("Adicionar");
        jbAdd.setToolTipText("Adicionar Novo Calendário");
        jbAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jbAdd)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jbCancel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbSave, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jpWorkingHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jpYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpWorkingHours, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jtpMonths, javax.swing.GroupLayout.DEFAULT_SIZE, 1213, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jtpMonths, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Marca todos os dias do calendário selecionado como dias
     * que serão trabalhados ou desmarca todos os dias.
     * 
     * @param evt
     */
    private void jbWorkingDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbWorkingDaysActionPerformed
        if ( !workingDaysMarked ) {
            //Seta  o título
            jbWorkingDays.setText("Desmarcar Todos");
            
            for ( MonthPanel panel : months ) {
                panel.markAllChecks();
            }

            workingDaysMarked = true;

            //Faz as alterações no model
            getPresenter().markAllWorkingDays();
        } else {
            //Seta  o título
            jbWorkingDays.setText("Marcar Todos");

            for ( MonthPanel panel : months ) {
                panel.unmarkAllChecks();
            }

            workingDaysMarked = false;

            //Faz as alterações no model
            getPresenter().unmarkAllWorkingDays();
        }
    }//GEN-LAST:event_jbWorkingDaysActionPerformed

    /**
     * Seta para todos os dias o número de horas de trabalho por dia através do
     * text field.
     * 
     * @param evt
     */
    private void jbSetWorkingHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetWorkingHoursActionPerformed
        try {
            Double workingHours = Double.parseDouble(jtfWorkingHours.getText());
            getPresenter().validateWorkingHours(workingHours);
            
            for (MonthPanel panel : months) {
                panel.applyToAllFields(jtfWorkingHours.getText());
            }
            //Aplica o valor ao model
            getPresenter().applyWorkingHoursToAll(workingHours);
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
        } catch (NumberFormatException ex) {
            MessageDialog.warning("As horas trabalhadas deve ser um número.", ex.getMessage(), parent);
        }
    }//GEN-LAST:event_jbSetWorkingHoursActionPerformed

    /**
     * Salva o calendário em edição.
     * 
     * @param evt
     */
    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        try {
            //Recupera dados dos meses
            for (MonthPanel panel : months) {
                panel.viewToModel();
            }

            //Salva no banco de dados
            getPresenter().save();

            //Mensagem de sucesso
            MessageDialog.sucess("Calendário de " + getPresenter().getCalendar()
                    .getYear() + " alterado com sucesso!", parent);
        } catch (CalendarPersistenceException ex) {
            MessageDialog.error(ex, parent);
        } catch (ValidatorException ex) {
            MessageDialog.warning(ex, parent);
        }
    }//GEN-LAST:event_jbSaveActionPerformed

    /**
     * Fecha o painel de calendários.
     * 
     * @param evt
     */
    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        tab.remove();
    }//GEN-LAST:event_jbCancelActionPerformed

    /**
     * Abre diálogo para adição de novo calendário.
     * 
     * @param evt
     */
    private void jbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddActionPerformed
        NewCalendarDialog newCalendar = new NewCalendarDialog(parent, this);
        newCalendar.setVisible(true);
    }//GEN-LAST:event_jbAddActionPerformed

    /**
     * Recarrega o calendário quando for modificado no combo box.
     * 
     * @param evt
     */
    private void jcbYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbYearItemStateChanged
        loadCalendar( (Calendar) jcbYear.getModel().getSelectedItem() );
    }//GEN-LAST:event_jcbYearItemStateChanged

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbSetWorkingHours;
    private javax.swing.JButton jbWorkingDays;
    private javax.swing.JComboBox jcbYear;
    private javax.swing.JPanel jpWorkingHours;
    private javax.swing.JPanel jpYear;
    private javax.swing.JTextField jtfWorkingHours;
    private javax.swing.JTabbedPane jtpMonths;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the presenter
     */
    public CalendarPresenter getPresenter() {
        return presenter;
    }

}
