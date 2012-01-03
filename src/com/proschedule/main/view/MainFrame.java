package com.proschedule.main.view;

import com.proschedule.core.calendar.view.CalendarPanel;
import com.proschedule.core.calendar.view.NewCalendarDialog;
import com.proschedule.core.persistence.view.component.ComponentPanel;
import com.proschedule.core.persistence.view.customer.CustomerPanel;
import com.proschedule.core.persistence.view.operation.NewOperationDialog;
import com.proschedule.core.persistence.view.operation.OperationPanel;
import com.proschedule.core.persistence.view.set.NewSetPanel;
import com.proschedule.core.persistence.view.set.SetPanel;
import com.proschedule.core.scheduling.view.order.NewOrderPanel;
import com.proschedule.core.scheduling.view.order.OrderPanel;
import com.proschedule.core.scheduling.view.scheduling.SchedulingPanel;
import com.proschedule.main.ProSchedule;
import com.proschedule.main.ProScheduleException;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.tabs.AbstractTab;
import java.awt.Frame;

/**
 * Painel Principal do Sistema
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class MainFrame extends javax.swing.JFrame {

    private javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
            getClass().getResource("/com/proschedule/resources/images/brand/icon_32.png")
    );

    /**
     * Construtor da Classe
     */
    public MainFrame() {
        startUp();
    }

    /**
     * Inicializa os componentes e variáveis
     */
    public void startUp() {
        initComponents();

        //Maximiza a tela
        setExtendedState(Frame.MAXIMIZED_BOTH);

        //Carrega as abas padrão
        loadTabs();
        
        setTitle( ProSchedule.getSystemTitle() );
    }

    /**
     * Carrega as abas do sistema
     */
    public void loadTabs() {
        loadStartPageTab();
    }

    /**
     * Carrega a aba inicial do sistema
     */
    public void loadStartPageTab() {
        StartPagePanel form = new StartPagePanel();
        tabStartPage = new AbstractTab(jtpMainTabPanel , "Página Inicial", form);
    }

    /**
     * Sai do sistema
     */
    public void exit() {
        if ( ProSchedule.getConfig("system.close_without_ask").equals("false") ) {
            CloseAppDialog dialog = new CloseAppDialog(this);
            dialog.setVisible(true);
        } else {
            try {
                ProSchedule.exit(0);
            } catch (ProScheduleException ex) {
                MessageDialog.error(ex.getMessage(), ex.getDetailMessage(), this);
            }
        }
    }

    /**
     * Abre aba de calendário
     */
    public void actionCalendar() {
        if ( tabCalendar == null || !tabCalendar.isActivated() ) {
            CalendarPanel form = new CalendarPanel( this );
            tabCalendar = new AbstractTab(jtpMainTabPanel , "Calendários", form);
            form.setTab( tabCalendar );
        } else {
            tabCalendar.setAsSelected();
        }
    }

    /**
     * Abre novo calendário
     */
    public void actionAddCalendar() {
        NewCalendarDialog newCalendar = new NewCalendarDialog(this);
        newCalendar.setVisible(true);
    }

    /**
     * Abre aba de componentes
     */
    public void actionComponents() {
        if ( tabComponent == null || !tabComponent.isActivated() ) {
            ComponentPanel form = new ComponentPanel( this );
            tabComponent = new AbstractTab(jtpMainTabPanel , "Componentes", form);
            form.setTab( tabComponent );
        } else {
            tabComponent.setAsSelected();
        }
    }

    /**
     * Abre aba de novo componente
     */
    public void actionAddComponent() {
        if ( tabComponent == null || !tabComponent.isActivated() ) {
            ComponentPanel form = new ComponentPanel( this );
            tabComponent = new AbstractTab(jtpMainTabPanel , "Componentes", form);
            form.setTab( tabComponent );
            form.addComponent();
        } else {
            tabComponent.setAsSelected();
            ((ComponentPanel)tabComponent.getContentPanel()).addComponent();
        }
    }

    /**
     * Abre aba de clientes
     */
    public void actionCustomer() {
        if ( tabCustomer == null || !tabCustomer.isActivated() ) {
            CustomerPanel form = new CustomerPanel( this, jtpMainTabPanel );
            tabCustomer = new AbstractTab(jtpMainTabPanel , "Clientes", form);
            form.setTab( tabCustomer );
        } else {
            tabCustomer.setAsSelected();
        }
    }

    /**
     * Abre aba de novo cliente
     */
    public void actionAddCustomer() {
        if ( tabCustomer == null || !tabCustomer.isActivated() ) {
            CustomerPanel form = new CustomerPanel( this, jtpMainTabPanel );
            tabCustomer = new AbstractTab(jtpMainTabPanel , "Clientes", form);
            form.setTab( tabCustomer );
            form.addCustomer();
        } else {
            tabCustomer.setAsSelected();
            ((CustomerPanel)tabCustomer.getContentPanel()).addCustomer();
        }
    }

    /**
     * Abre aba de operações
     */
    public void actionOperation() {
        if ( tabOperation == null || !tabOperation.isActivated() ) {
            OperationPanel form = new OperationPanel( this );
            tabOperation = new AbstractTab(jtpMainTabPanel , "Operações", form);
            form.setTab( tabOperation );
        } else {
            tabOperation.setAsSelected();
        }
    }

    /**
     * Abre diálogo de nova operação
     */
    public void actionAddOperation() {
        NewOperationDialog dialog = new NewOperationDialog(this);
        dialog.setVisible(true);
    }

    /**
     * Abre página inicial
     */
    public void actionHomePage() {
        if ( tabStartPage == null || !tabStartPage.isActivated() ) {
            StartPagePanel form = new StartPagePanel();
            tabStartPage = new AbstractTab(jtpMainTabPanel , "Página Inicial", form);
        } else {
            tabStartPage.setAsSelected();
        }
    }

    /**
     * Abre aba de conjuntos
     */
    public void actionSets() {
        if ( tabSet == null || !tabSet.isActivated() ) {
            SetPanel form = new SetPanel( this, jtpMainTabPanel );
            tabSet = new AbstractTab(jtpMainTabPanel , "Conjuntos", form);
            form.setTab( tabSet );
        } else {
            tabSet.setAsSelected();
        }
    }

    /**
     * Abre aba de novo conjunto
     */
    public void actionAddSet() {
        if ( tabNewSet == null || !tabNewSet.isActivated() ) {
            NewSetPanel form = new NewSetPanel(this);
            tabNewSet = new AbstractTab(jtpMainTabPanel , "Novo Conjunto", form);
            form.setTab( tabNewSet );
        } else {
            tabNewSet.setAsSelected();
        }
    }

    /**
     * Abre aba de ordens de produção
     */
    public void actionOrders() {
        if ( tabOrder == null || !tabOrder.isActivated() ) {
            OrderPanel form = new OrderPanel( this, jtpMainTabPanel );
            tabOrder = new AbstractTab(jtpMainTabPanel , "Ordens de Produção", form);
            form.setTab( tabOrder );
        } else {
            tabOrder.setAsSelected();
        }
    }

    /**
     * Abre aba de nova ordem de produção
     */
    public void actionAddOrder() {
        if ( tabNewOrder == null || !tabNewOrder.isActivated() ) {
            NewOrderPanel form = new NewOrderPanel(this);
            tabNewOrder = new AbstractTab(jtpMainTabPanel , "Nova Ordem de Produção", form);
            form.setTab( tabNewOrder );
        } else {
            tabNewOrder.setAsSelected();
        }
    }

    /**
     * Abre aba de sequenciamento da produção
     */
    public void actionProductionScheduling() {
        if ( tabScheduling == null || !tabScheduling.isActivated() ) {
            SchedulingPanel form = new SchedulingPanel( this, jtpMainTabPanel );
            tabScheduling = new AbstractTab(jtpMainTabPanel , "Sequenciamento da Produção", form);
            form.setTab( tabScheduling );
        } else {
            tabScheduling.setAsSelected();
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

        jpMainToolBarPanel = new javax.swing.JPanel();
        jtbMainToolBar = new javax.swing.JToolBar();
        jbHomePage = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jbProductionScheduling = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jbOrders = new javax.swing.JButton();
        jbAddOrder = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jbSets = new javax.swing.JButton();
        jbAddSet = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jbComponents = new javax.swing.JButton();
        jbAddComponent = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jbOperation = new javax.swing.JButton();
        jbAddOperation = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jbCalendar = new javax.swing.JButton();
        jbAddCalendar = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jbCustomer = new javax.swing.JButton();
        jbAddCustomer = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jbExit = new javax.swing.JButton();
        jtpMainTabPanel = new javax.swing.JTabbedPane();
        jSeparator5 = new javax.swing.JSeparator();
        jbmMainMenuBar = new javax.swing.JMenuBar();
        jmArchive = new javax.swing.JMenu();
        jmiStartPage = new javax.swing.JMenuItem();
        jmiExit = new javax.swing.JMenuItem();
        jmConsult = new javax.swing.JMenu();
        jmiCalendar = new javax.swing.JMenuItem();
        jmiCustomer = new javax.swing.JMenuItem();
        jmiComponent = new javax.swing.JMenuItem();
        jmiSet = new javax.swing.JMenuItem();
        jmiOperation = new javax.swing.JMenuItem();
        jmiOrder = new javax.swing.JMenuItem();
        jmiScheduling = new javax.swing.JMenuItem();
        jmAdd = new javax.swing.JMenu();
        jmiAddCalendar = new javax.swing.JMenuItem();
        jmiAddCustomer = new javax.swing.JMenuItem();
        jmiAddComponent = new javax.swing.JMenuItem();
        jmiAddSet = new javax.swing.JMenuItem();
        jmiAddOperation = new javax.swing.JMenuItem();
        jmiAddOrder = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jmiAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(icon.getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpMainToolBarPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(236, 233, 216), new java.awt.Color(236, 233, 216), null, null));

        jtbMainToolBar.setFloatable(false);
        jtbMainToolBar.setRollover(true);

        jbHomePage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/home.png"))); // NOI18N
        jbHomePage.setToolTipText("Ir para Página Inicial");
        jbHomePage.setFocusable(false);
        jbHomePage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbHomePage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbHomePage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbHomePageActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbHomePage);
        jtbMainToolBar.add(jSeparator10);

        jbProductionScheduling.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/scheduling.png"))); // NOI18N
        jbProductionScheduling.setToolTipText("Consultar Sequenciamento da Produção");
        jbProductionScheduling.setFocusable(false);
        jbProductionScheduling.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbProductionScheduling.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbProductionScheduling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbProductionSchedulingActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbProductionScheduling);

        jSeparator1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtbMainToolBar.add(jSeparator1);

        jbOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/order.png"))); // NOI18N
        jbOrders.setToolTipText("Consultar Ordens de Produção");
        jbOrders.setFocusable(false);
        jbOrders.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbOrders.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOrdersActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbOrders);

        jbAddOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/order_add.png"))); // NOI18N
        jbAddOrder.setToolTipText("Adicionar Ordem de Produção");
        jbAddOrder.setFocusable(false);
        jbAddOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddOrder.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddOrderActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddOrder);
        jtbMainToolBar.add(jSeparator4);

        jbSets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/set.png"))); // NOI18N
        jbSets.setToolTipText("Consultar Conjuntos");
        jbSets.setFocusable(false);
        jbSets.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSets.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSetsActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbSets);

        jbAddSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/set_add.png"))); // NOI18N
        jbAddSet.setToolTipText("Adicionar Conjunto");
        jbAddSet.setFocusable(false);
        jbAddSet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddSet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddSetActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddSet);
        jtbMainToolBar.add(jSeparator2);

        jbComponents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/component.png"))); // NOI18N
        jbComponents.setToolTipText("Consultar Componentes");
        jbComponents.setFocusable(false);
        jbComponents.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbComponents.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbComponents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbComponentsActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbComponents);

        jbAddComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/component_add.png"))); // NOI18N
        jbAddComponent.setToolTipText("Adicionar Componente");
        jbAddComponent.setFocusable(false);
        jbAddComponent.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddComponent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddComponentActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddComponent);
        jtbMainToolBar.add(jSeparator3);

        jbOperation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/operation.png"))); // NOI18N
        jbOperation.setToolTipText("Consultar Operações");
        jbOperation.setFocusable(false);
        jbOperation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbOperation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOperationActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbOperation);

        jbAddOperation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/operation_add.png"))); // NOI18N
        jbAddOperation.setToolTipText("Adicionar Operação");
        jbAddOperation.setFocusable(false);
        jbAddOperation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddOperation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddOperationActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddOperation);
        jtbMainToolBar.add(jSeparator8);

        jbCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/calendar.png"))); // NOI18N
        jbCalendar.setToolTipText("Consultar Calendários");
        jbCalendar.setFocusable(false);
        jbCalendar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCalendar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCalendarActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbCalendar);

        jbAddCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/calendar_add.png"))); // NOI18N
        jbAddCalendar.setToolTipText("Adicionar Calendário");
        jbAddCalendar.setFocusable(false);
        jbAddCalendar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddCalendar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddCalendarActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddCalendar);
        jtbMainToolBar.add(jSeparator6);

        jbCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/customer.png"))); // NOI18N
        jbCustomer.setToolTipText("Consultar Clientes");
        jbCustomer.setFocusable(false);
        jbCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCustomer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCustomerActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbCustomer);

        jbAddCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/customer_add.png"))); // NOI18N
        jbAddCustomer.setToolTipText("Adicionar Cliente");
        jbAddCustomer.setFocusable(false);
        jbAddCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAddCustomer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddCustomerActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbAddCustomer);
        jtbMainToolBar.add(jSeparator9);

        jbExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/bar/exit.png"))); // NOI18N
        jbExit.setToolTipText("Sair do ProSchedule");
        jbExit.setFocusable(false);
        jbExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExitActionPerformed(evt);
            }
        });
        jtbMainToolBar.add(jbExit);

        javax.swing.GroupLayout jpMainToolBarPanelLayout = new javax.swing.GroupLayout(jpMainToolBarPanel);
        jpMainToolBarPanel.setLayout(jpMainToolBarPanelLayout);
        jpMainToolBarPanelLayout.setHorizontalGroup(
            jpMainToolBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtbMainToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
        );
        jpMainToolBarPanelLayout.setVerticalGroup(
            jpMainToolBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtbMainToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jtpMainTabPanel.setOpaque(true);

        jmArchive.setText("Arquivo");

        jmiStartPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/home.png"))); // NOI18N
        jmiStartPage.setText("Página Inicial");
        jmiStartPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiStartPageActionPerformed(evt);
            }
        });
        jmArchive.add(jmiStartPage);

        jmiExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jmiExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/exit.png"))); // NOI18N
        jmiExit.setText("Sair");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExitActionPerformed(evt);
            }
        });
        jmArchive.add(jmiExit);

        jbmMainMenuBar.add(jmArchive);

        jmConsult.setText("Consultar");

        jmiCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/calendar.png"))); // NOI18N
        jmiCalendar.setText("Calendários");
        jmiCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCalendarActionPerformed(evt);
            }
        });
        jmConsult.add(jmiCalendar);

        jmiCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/customer.png"))); // NOI18N
        jmiCustomer.setText("Clientes");
        jmiCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiCustomerActionPerformed(evt);
            }
        });
        jmConsult.add(jmiCustomer);

        jmiComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/component.png"))); // NOI18N
        jmiComponent.setText("Componentes");
        jmiComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiComponentActionPerformed(evt);
            }
        });
        jmConsult.add(jmiComponent);

        jmiSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/set.png"))); // NOI18N
        jmiSet.setText("Conjuntos");
        jmiSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSetActionPerformed(evt);
            }
        });
        jmConsult.add(jmiSet);

        jmiOperation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/operation.png"))); // NOI18N
        jmiOperation.setText("Operações");
        jmiOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiOperationActionPerformed(evt);
            }
        });
        jmConsult.add(jmiOperation);

        jmiOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/order.png"))); // NOI18N
        jmiOrder.setText("Ordens de Produção");
        jmiOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiOrderActionPerformed(evt);
            }
        });
        jmConsult.add(jmiOrder);

        jmiScheduling.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/scheduling.png"))); // NOI18N
        jmiScheduling.setText("Sequenciamento da Produção");
        jmiScheduling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSchedulingActionPerformed(evt);
            }
        });
        jmConsult.add(jmiScheduling);

        jbmMainMenuBar.add(jmConsult);

        jmAdd.setText("Adicionar");

        jmiAddCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/calendar_add.png"))); // NOI18N
        jmiAddCalendar.setText("Calendário");
        jmiAddCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddCalendarActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddCalendar);

        jmiAddCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/customer_add.png"))); // NOI18N
        jmiAddCustomer.setText("Cliente");
        jmiAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddCustomerActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddCustomer);

        jmiAddComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/component_add.png"))); // NOI18N
        jmiAddComponent.setText("Componente");
        jmiAddComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddComponentActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddComponent);

        jmiAddSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/set_add.png"))); // NOI18N
        jmiAddSet.setText("Conjunto");
        jmiAddSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddSetActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddSet);

        jmiAddOperation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/operation_add.png"))); // NOI18N
        jmiAddOperation.setText("Operação");
        jmiAddOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddOperationActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddOperation);

        jmiAddOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/proschedule/resources/images/menu/order_add.png"))); // NOI18N
        jmiAddOrder.setText("Ordem de Produção");
        jmiAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAddOrderActionPerformed(evt);
            }
        });
        jmAdd.add(jmiAddOrder);

        jbmMainMenuBar.add(jmAdd);

        jmHelp.setText("Ajuda");

        jmiAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jmiAbout.setText("Sobre");
        jmiAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAboutActionPerformed(evt);
            }
        });
        jmHelp.add(jmiAbout);

        jbmMainMenuBar.add(jmHelp);

        setJMenuBar(jbmMainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpMainToolBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jtpMainTabPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpMainToolBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpMainTabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExitActionPerformed
        exit();
    }//GEN-LAST:event_jbExitActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
        exit();
    }//GEN-LAST:event_jmiExitActionPerformed

    private void jbCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCalendarActionPerformed
        actionCalendar();
    }//GEN-LAST:event_jbCalendarActionPerformed

    private void jbAddCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddCalendarActionPerformed
        actionAddCalendar();
    }//GEN-LAST:event_jbAddCalendarActionPerformed

    private void jbComponentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbComponentsActionPerformed
        actionComponents();
    }//GEN-LAST:event_jbComponentsActionPerformed

    private void jbAddComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddComponentActionPerformed
        actionAddComponent();
    }//GEN-LAST:event_jbAddComponentActionPerformed

    private void jbCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCustomerActionPerformed
        actionCustomer();
    }//GEN-LAST:event_jbCustomerActionPerformed

    private void jbAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddCustomerActionPerformed
        actionAddCustomer();
    }//GEN-LAST:event_jbAddCustomerActionPerformed

    private void jbOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOperationActionPerformed
        actionOperation();
    }//GEN-LAST:event_jbOperationActionPerformed

    private void jbAddOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddOperationActionPerformed
        actionAddOperation();
    }//GEN-LAST:event_jbAddOperationActionPerformed

    private void jbHomePageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbHomePageActionPerformed
        actionHomePage();
    }//GEN-LAST:event_jbHomePageActionPerformed

    private void jbSetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSetsActionPerformed
        actionSets();
    }//GEN-LAST:event_jbSetsActionPerformed

    private void jbAddSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddSetActionPerformed
        actionAddSet();
    }//GEN-LAST:event_jbAddSetActionPerformed

    private void jbOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOrdersActionPerformed
        actionOrders();
    }//GEN-LAST:event_jbOrdersActionPerformed

    private void jbAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddOrderActionPerformed
        actionAddOrder();
    }//GEN-LAST:event_jbAddOrderActionPerformed

    private void jbProductionSchedulingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbProductionSchedulingActionPerformed
        actionProductionScheduling();
    }//GEN-LAST:event_jbProductionSchedulingActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void jmiAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAboutActionPerformed
        AboutDialog dialog = new AboutDialog(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jmiAboutActionPerformed

    private void jmiStartPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiStartPageActionPerformed
        actionHomePage();
    }//GEN-LAST:event_jmiStartPageActionPerformed

    private void jmiCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCalendarActionPerformed
        actionCalendar();
    }//GEN-LAST:event_jmiCalendarActionPerformed

    private void jmiCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiCustomerActionPerformed
        actionCustomer();
    }//GEN-LAST:event_jmiCustomerActionPerformed

    private void jmiComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiComponentActionPerformed
        actionComponents();
    }//GEN-LAST:event_jmiComponentActionPerformed

    private void jmiSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSetActionPerformed
        actionSets();
    }//GEN-LAST:event_jmiSetActionPerformed

    private void jmiOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiOperationActionPerformed
        actionOperation();
    }//GEN-LAST:event_jmiOperationActionPerformed

    private void jmiOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiOrderActionPerformed
        actionOrders();
    }//GEN-LAST:event_jmiOrderActionPerformed

    private void jmiSchedulingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSchedulingActionPerformed
        actionProductionScheduling();
    }//GEN-LAST:event_jmiSchedulingActionPerformed

    private void jmiAddCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddCalendarActionPerformed
        actionAddCalendar();
    }//GEN-LAST:event_jmiAddCalendarActionPerformed

    private void jmiAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddCustomerActionPerformed
        actionAddCustomer();
    }//GEN-LAST:event_jmiAddCustomerActionPerformed

    private void jmiAddComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddComponentActionPerformed
        actionAddComponent();
    }//GEN-LAST:event_jmiAddComponentActionPerformed

    private void jmiAddSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddSetActionPerformed
        actionAddSet();
    }//GEN-LAST:event_jmiAddSetActionPerformed

    private void jmiAddOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddOperationActionPerformed
        actionAddOperation();
    }//GEN-LAST:event_jmiAddOperationActionPerformed

    private void jmiAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAddOrderActionPerformed
        actionAddOrder();
    }//GEN-LAST:event_jmiAddOrderActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JButton jbAddCalendar;
    private javax.swing.JButton jbAddComponent;
    private javax.swing.JButton jbAddCustomer;
    private javax.swing.JButton jbAddOperation;
    private javax.swing.JButton jbAddOrder;
    private javax.swing.JButton jbAddSet;
    private javax.swing.JButton jbCalendar;
    private javax.swing.JButton jbComponents;
    private javax.swing.JButton jbCustomer;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbHomePage;
    private javax.swing.JButton jbOperation;
    private javax.swing.JButton jbOrders;
    private javax.swing.JButton jbProductionScheduling;
    private javax.swing.JButton jbSets;
    private javax.swing.JMenuBar jbmMainMenuBar;
    private javax.swing.JMenu jmAdd;
    private javax.swing.JMenu jmArchive;
    private javax.swing.JMenu jmConsult;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenuItem jmiAbout;
    private javax.swing.JMenuItem jmiAddCalendar;
    private javax.swing.JMenuItem jmiAddComponent;
    private javax.swing.JMenuItem jmiAddCustomer;
    private javax.swing.JMenuItem jmiAddOperation;
    private javax.swing.JMenuItem jmiAddOrder;
    private javax.swing.JMenuItem jmiAddSet;
    private javax.swing.JMenuItem jmiCalendar;
    private javax.swing.JMenuItem jmiComponent;
    private javax.swing.JMenuItem jmiCustomer;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOperation;
    private javax.swing.JMenuItem jmiOrder;
    private javax.swing.JMenuItem jmiScheduling;
    private javax.swing.JMenuItem jmiSet;
    private javax.swing.JMenuItem jmiStartPage;
    private javax.swing.JPanel jpMainToolBarPanel;
    private javax.swing.JToolBar jtbMainToolBar;
    private javax.swing.JTabbedPane jtpMainTabPanel;
    // End of variables declaration//GEN-END:variables

    private AbstractTab tabStartPage;
    private AbstractTab tabCalendar;
    private AbstractTab tabComponent;
    private AbstractTab tabCustomer;
    private AbstractTab tabOperation;
    private AbstractTab tabSet;
    private AbstractTab tabNewSet;
    private AbstractTab tabOrder;
    private AbstractTab tabNewOrder;
    private AbstractTab tabScheduling;
}
