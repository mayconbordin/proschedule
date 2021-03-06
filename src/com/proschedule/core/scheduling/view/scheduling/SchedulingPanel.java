package com.proschedule.core.scheduling.view.scheduling;

import com.proschedule.core.scheduling.exportation.excel.ExcelScheduling;
import com.proschedule.util.date.DateUtil;
import com.proschedule.util.filechooser.XLSFilter;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import java.io.File;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;

/**
 * Painel do sequenciamento da produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SchedulingPanel extends javax.swing.JPanel {
    private java.awt.Frame parent;
    private AbstractTab tab;
    private javax.swing.JTabbedPane tabPane;
    private MessageDialog msg;

    private TableScheduling task;

    /** 
     * Construtor da Classe
     *
     * @param parent O painel principal
     * @param tabPane Painel de abas
     */
    public SchedulingPanel ( java.awt.Frame parent, javax.swing.JTabbedPane tabPane ) {
        this.parent = parent;
        this.tabPane = tabPane;
        startUp();
    }

    /**
     * Inicializa componentes e variáveis
     */
    public void startUp() {
        initComponents();
        loadDefaultPeriod();
        loadSchedulingTableModel();
    }

    /**
     * Carrega o sequenciamento com o período padrão
     */
    public void loadDefaultPeriod() {
        DateTime from = new DateTime();
        DateTime to = from.plusWeeks(2);

        jdcFrom.setDate( from.toDate() );
        jdcTo.setDate( to.toDate() );
    }

    /**
     * Carrega a tabela de seqeunciamento
     */
    public void loadSchedulingTableModel() {
        Date startDate = jdcFrom.getDate();
        Date endDate = jdcTo.getDate();

        if ( startDate == null || endDate == null ) {
            MessageDialog.warning("As datas não podem ser vazias.", parent);
        } else if ( endDate.compareTo(startDate) < 0 ) {
            MessageDialog.warning("A data final deve ser maior que a data inicial.", parent);
        } else {
            task = new TableScheduling(this);
            task.setParent(parent);
            task.setPeriod( new DateTime(startDate) , new DateTime(endDate));
            task.create();
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

        jpPeriod = new javax.swing.JPanel();
        jlFrom = new javax.swing.JLabel();
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jlTo = new javax.swing.JLabel();
        jdcTo = new com.toedter.calendar.JDateChooser();
        jbLoad = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jbExportExcel = new javax.swing.JButton();
        jspScheduling = new javax.swing.JScrollPane();
        jtpScheduling = new javax.swing.JTabbedPane();

        jpPeriod.setBorder(javax.swing.BorderFactory.createTitledBorder("Período"));

        jlFrom.setText("De");

        jdcFrom.setDateFormatString("dd/MM/yyyy");

        jlTo.setText("até");

        jdcTo.setDateFormatString("dd/MM/yyyy");

        jbLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/load_16.png"))); // NOI18N
        jbLoad.setText("Carregar");
        jbLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpPeriodLayout = new javax.swing.GroupLayout(jpPeriod);
        jpPeriod.setLayout(jpPeriodLayout);
        jpPeriodLayout.setHorizontalGroup(
            jpPeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPeriodLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbLoad)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpPeriodLayout.setVerticalGroup(
            jpPeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPeriodLayout.createSequentialGroup()
                .addGroup(jpPeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jdcFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlFrom)
                    .addComponent(jlTo)
                    .addComponent(jdcTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbLoad))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Legenda"));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        jLabel1.setText("Dia sem trabalho");

        jLabel2.setText("Dia com trabalho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ferramentas"));

        jbExportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/forms/excel_16.png"))); // NOI18N
        jbExportExcel.setText("Exportar para Excel");
        jbExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExportExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbExportExcel)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jbExportExcel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jspScheduling.setBorder(null);

        jtpScheduling.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jspScheduling.setViewportView(jtpScheduling);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspScheduling, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addComponent(jpPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jspScheduling, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExportExcelActionPerformed
        Date startDate = jdcFrom.getDate();
        Date endDate = jdcTo.getDate();

        if ( startDate == null || endDate == null ) {
            MessageDialog.warning("As datas não podem ser vazias.", parent);
        } else if ( endDate.compareTo(startDate) < 0 ) {
            MessageDialog.warning("A data final deve ser maior que a data inicial.", parent);
        } else {
            JFileChooser jfc = new JFileChooser() {
                @Override
                public void approveSelection() {
                    File f = getSelectedFile();
                    if(f.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, 
                                "O arquivo '" + f.getName() + "' já existe. " +
                                "Deseja substituir o arquivo existente?",
                                "ProSchedule 1.0",JOptionPane.YES_NO_OPTION);
                        switch(result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };

            jfc.addChoosableFileFilter( new XLSFilter() );
            jfc.setAcceptAllFileFilterUsed(false);

            File file = new File( jfc.getCurrentDirectory() + "/seq_" + DateUtil.toString(startDate)
                    + "_" + DateUtil.toString(endDate) + ".xlsx" );
            jfc.setSelectedFile(file);
            int returnVal = jfc.showSaveDialog(parent);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                ExcelScheduling ee = new ExcelScheduling();
                ee.setParent(parent);
                ee.setFilePath(jfc.getSelectedFile().getAbsolutePath());

                DateTime sd = DateUtil.toDateTime(startDate);
                DateTime ed = DateUtil.toDateTime(endDate);
                ee.setPeriod(sd, ed);
                
                ee.create();
            }
        }
    }//GEN-LAST:event_jbExportExcelActionPerformed

    private void jbLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoadActionPerformed
        loadSchedulingTableModel();
    }//GEN-LAST:event_jbLoadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbExportExcel;
    private javax.swing.JButton jbLoad;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JLabel jlFrom;
    private javax.swing.JLabel jlTo;
    private javax.swing.JPanel jpPeriod;
    private javax.swing.JScrollPane jspScheduling;
    private javax.swing.JTabbedPane jtpScheduling;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tab the tab to set
     */
    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }

    /**
     * @return the jtpScheduling
     */
    public javax.swing.JTabbedPane getJtpScheduling() {
        return jtpScheduling;
    }

}
