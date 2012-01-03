package com.proschedule.core.scheduling.view.scheduling;

import com.proschedule.core.scheduling.exportation.excel.*;
import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingComponentDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.core.scheduling.facade.OrderFacade;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.util.date.DateUtil;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.table.CellSpan;
import com.proschedule.util.table.ColoredCell;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import org.joda.time.DateTime;

/**
 * Classe que constrói as tabelas de sequenciamento da produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class TableScheduling implements PropertyChangeListener {
    private List<SchedulingTableControl> tables;

    private DateTime startDate;
    private DateTime endDate;

    private OperationFacade operationFacade;
    private OperationSchedulingFacade operationSchedulingFacade;
    private SetFacade setFacade;
    private CalendarFacade calendarFacade;
    private OrderFacade orderFacade;

    private List<Set> setList;
    private List<SetComponent> setComponentList;
    private List<Operation> operationList;
    private List<Day> dayList;
    private List<OrderDetail> orderDetailList;
    private List<OperationSchedulingComponentDetail> componentSchedulingList;
    private List<OperationSchedulingSetDetail> setSchedulingList;

    private ProgressMonitor progressMonitor;
    private Task task;
    private java.awt.Frame parent;

    private SchedulingPanel panel;

    private Color lightGreen = new Color(153, 255, 153);
    private Color darkGreen = new Color(0, 153, 0);
    private Color gray = new Color(192, 192, 192);
    private Color darkOrange = new Color(234, 148, 39);

    /**
     * Construtor da Classes
     * @param panel O painel de sequenciamento que receberá as tabelas
     */
    public TableScheduling(SchedulingPanel panel) {
        tables = new ArrayList();
        setComponentList = new ArrayList();

        operationFacade = new OperationFacade();
        operationSchedulingFacade = new OperationSchedulingFacade();
        setFacade = new SetFacade();
        calendarFacade = new CalendarFacade();
        orderFacade = new OrderFacade();

        this.panel = panel;
    }

    /**
     * Inicia a recuperação dos dados do sequenciamento e criação das tabelas
     */
    public void create() {
        progressMonitor = new ProgressMonitor(parent,
                              "Criando as tabelas de sequenciamento", "", 0, 100);

        progressMonitor.setProgress(0);
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress") ) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            String message =
                String.format("%d%% Completado.\n", progress);
            progressMonitor.setNote(message);
            if (progressMonitor.isCanceled() || task.isDone()) {
                if (progressMonitor.isCanceled()) {
                    task.cancel(true);
                } else {
                    //Limpa as abas
                    panel.getJtpScheduling().removeAll();

                    int index;
                    
                    //Cria as abas mestre e de operações p/ componentes
                    for ( SchedulingTableControl stm : tables ) {
                        if ( stm.getType().equals("mestre") ) {
                            panel.getJtpScheduling().add(stm.getTableName(), stm.createScrollPane());
                            index = panel.getJtpScheduling().getTabCount() - 1;
                            panel.getJtpScheduling().setForegroundAt(index, darkGreen);
                        } else if ( !stm.getType().equals("conjunto") ) {
                            panel.getJtpScheduling().add(stm.getTableName(), stm.createScrollPane());
                            index = panel.getJtpScheduling().getTabCount() - 1;
                            panel.getJtpScheduling().setForegroundAt(index, Color.blue);
                        }
                    }

                    for ( SchedulingTableControl stm : tables ) {                        
                        if ( stm.getType().equals("conjunto") ) {
                            panel.getJtpScheduling().add(stm.getTableName(), stm.createScrollPane());
                            index = panel.getJtpScheduling().getTabCount() - 1;
                            panel.getJtpScheduling().setForegroundAt(index, darkOrange);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(java.awt.Frame parent) {
        this.parent = parent;
    }

    /**
     * @return the sheets
     */
    public List<SchedulingTableControl> getTables() {
        return tables;
    }

    /**
     * Classe que efetua a tarefa de recuperar dados do sequenciamento e montar
     * as tabelas
     */
    class Task extends SwingWorker<Void, Void> {        
        @Override
        public Void doInBackground() {
            try {
                setProgress(0);

                setList = setFacade.list("id", "asc");
                operationList = operationFacade.list();
                dayList = calendarFacade.listDays(startDate.toDate(), endDate.toDate());
                orderDetailList = orderFacade.listDetails(startDate.toDate(), endDate.toDate());

                componentSchedulingList = operationSchedulingFacade.listComponentDetails(startDate.toDate(), endDate.toDate());
                setSchedulingList = operationSchedulingFacade.listSetDetails(startDate.toDate(), endDate.toDate());
                setProgress(5);

                for ( Set set : setList ) {
                    for ( SetComponent sc : set.getComponents() ) {
                        setComponentList.add(sc);
                    }
                }

                if ( dayList.size() <= 0 ) {
                    throw new ExcelSchedulingException("Não existem registros para este período.");
                }

                //Data inicial
                Day day = dayList.get(0);

                //Tamanho das linhas
                int rowSize = setComponentList.size() + 2;

                //Tamanho das colunas
                int colSize = dayList.size() + 4;

                //Cria a planilha mestre
                getTables().add( new SchedulingTableControl("Mestre", "mestre",
                        day.getWeek(), 0, rowSize, colSize) );

                //Cria as planilhas das operações
                for (Operation o : operationList) {
                    if ( o.getType().getDescription().equals("Componente") ) {
                        getTables().add( new SchedulingTableControl(o.getDescription(), "componente",
                                day.getWeek(), 0, o, rowSize, colSize) );
                    } else {
                        getTables().add( new SchedulingTableControl(o.getDescription(), "conjunto",
                                day.getWeek(), 0, o, rowSize, colSize) );
                    }
                }

                //Percorre as linhas
                for ( int i = 0; i < rowSize ; i++ ) {
                    //Percorre as colunas
                    for ( int j = 0; j < colSize ; j++ ) {
                        //Dia atual da planilha
                        day = null;

                        //Atribuição dos dias
                        if ( j > 3 ) {
                            //Recupera o dia atual
                            day = dayList.get( j - 4 );
                        }

                        //Percorre cada uma das planilhas
                        for ( SchedulingTableControl sm : getTables() ) {
                            CellSpan cellSpan = null;
                            ColoredCell cellColor = null;

                            if ( j < 4 ) {
                                cellSpan =(CellSpan) sm.getSidebarTableModel()
                                        .getCellAttribute();
                                cellColor = (ColoredCell) sm.getSidebarTableModel()
                                        .getCellAttribute();
                            } else {
                                cellSpan =(CellSpan) sm.getDataTableModel()
                                        .getCellAttribute();
                                cellColor = (ColoredCell) sm.getDataTableModel()
                                        .getCellAttribute();
                            }
                            
                            
                            //Criação das semanas
                            if ( i == 0 && j > 3 ) {
                                //Troca de semana
                                if ( day.getWeek() != sm.getWeek() ) {
                                    //Mescla as colunas da semana
                                    cellSpan.combine(getInterval(0, 0),
                                            getInterval(sm.getStartWeek(), j - 1 - 4));

                                    //Seta o novo início da coluna da semana
                                    sm.setStartWeek(j - 4);

                                    //Seta a semana
                                    sm.setWeek(day.getWeek());
                                }

                                //Intercambia os estilos das células
                                if ( sm.getWeek()%2 == 0 ) {
                                    cellColor.setBackground(darkGreen, i, j - 4);
                                } else {
                                    cellColor.setBackground(lightGreen, i, j - 4);
                                }

                                //Só atribui valor a primeira célula da semana
                                //if ( j == sm.getStartWeek() ) {
                                    sm.getDataTableModel().setValueAt("Semana "
                                            + sm.getWeek(), i, j - 4);
                                //}

                                //Faz a mesclagem da última semana
                                if ( (j+1) == colSize ) {
                                    cellSpan.combine(getInterval(0, 0),
                                            getInterval(sm.getStartWeek(), j - 4));
                                }
                            }//criação das semanas

                            //Atribuição do tamanho das colunas
                            if ( j > 3 ) {
                                //Seta o tamanho da coluna
                                //sm.getColumn(j).setPreferredWidth(60);
                            }

                            //Criação das datas
                            if ( i == 1 && j > 3 ) {
                                //Seta a data na célula
                                sm.getDataTableModel().setValueAt(DateUtil
                                        .toString(day.getDate()), i, j - 4);

                                //Modifica o estilo de acordo com o dia
                                if ( !day.isWorkingDay() ) {
                                    cellColor.setBackground(gray, i, j - 4);
                                }
                            }//Criação das datas

                            //Criação dos títulos
                            if ( i == 1 ) {
                                //Títulos para componentes e mestre
                                if ( !sm.getType().equals("conjunto") ) {
                                    if ( j == 0 ) {
                                        sm.getSidebarTableModel()
                                                .setValueAt("Conjunto", i, j);
                                    } else if ( j == 1 ) {
                                        sm.getSidebarTableModel()
                                                .setValueAt("Componente", i, j);
                                    } else if ( j == 2 ) {
                                        sm.getSidebarTableModel()
                                                .setValueAt("Qtde./Conjunto", i, j);
                                    } else if ( j == 3 ) {
                                        sm.getSidebarTableModel()
                                                .setValueAt("MP", i, j);
                                    }
                                } else {
                                    if ( j == 0 ) {
                                        sm.getSidebarTableModel()
                                                .setValueAt("Conjunto", i, j);
                                    }
                                }
                            }//Criação dos títulos

                            //Criação da lista de conjuntos e componentes
                            if ( i > 1 ) {
                                if ( sm.getType().equals("conjunto") ) {
                                    if ( i < (setList.size() + 2) ) {
                                        if ( j == 0 ) {
                                            //Código do conjunto
                                            sm.getSidebarTableModel()
                                                    .setValueAt(setList.get( i - 2 ).getId(), i, j);
                                        } else if ( j == 1 ) {
                                            sm.getSidebarTableModel()
                                                    .setValueAt("Qtde. de conjuntos p/ soldar", i, j);
                                            //sm.getColumn(j).setPreferredWidth(160);
                                        }
                                    }
                                } else {
                                    SetComponent sc = setComponentList.get(i - 2);

                                    if ( j == 0 ) {
                                        //Primeiro conjunto
                                        if ( i == 2 ) {
                                            //Código do conjunto
                                            sm.getSidebarTableModel()
                                                    .setValueAt(sc.getPrimaryKey().getSet().getId(), i, j);

                                            sm.setCurrentSet(sc.getPrimaryKey().getSet());
                                            sm.setStartSet(2);
                                        }

                                        //Demais conjuntos, quando trocar o conjunto
                                        if ( sc.getPrimaryKey().getSet() != sm.getCurrentSet() ) {
                                            //Mescla o código do conjunto
                                            cellSpan.combine(getInterval(sm.getStartSet(), i-1),
                                                    getInterval(0, 0));

                                            sm.setCurrentSet(sc.getPrimaryKey().getSet());
                                            sm.setStartSet(i);

                                            //Código do conjunto
                                            sm.getSidebarTableModel()
                                                    .setValueAt(sc.getPrimaryKey().getSet().getId(), i, j);
                                        }
                                    } else if ( j == 1 ) {
                                        sm.getSidebarTableModel()
                                                    .setValueAt(sc.getPrimaryKey().getComponent().getId(), i, j);
                                    } else if ( j == 2 ) {
                                        sm.getSidebarTableModel()
                                                    .setValueAt(sc.getComponentQuantity(), i, j);
                                    } else if ( j == 3 ) {
                                        sm.getSidebarTableModel()
                                                    .setValueAt(sc.getPrimaryKey().getComponent().getRawMaterial(), i, j);
                                    }
                                }
                            }

                            //Preenchimento da planilha com conteúdo
                            if ( i > 1 ) {
                                if ( sm.getType().equals("mestre") ) {
                                    if ( j > 3 ) {
                                        SetComponent sc = setComponentList.get(i - 2);

                                        List<OrderDetail> msDetails = getOrderDetail(
                                                sc.getPrimaryKey().getComponent(),
                                                sc.getPrimaryKey().getSet(), day);

                                        double msSum = 0.0;

                                        for ( OrderDetail od : msDetails ) {
                                            msSum += od.getComponentQuantity();
                                        }

                                        if ( !day.isWorkingDay() ) {
                                            cellColor.setBackground(gray, i, j - 4);
                                        }

                                        if ( msSum > 0 ) {
                                            sm.getDataTableModel().setValueAt(msSum, i, j - 4);
                                        } else {
                                            sm.getDataTableModel().setValueAt("", i, j - 4);
                                        }
                                    }
                                } else if ( sm.getType().equals("componente") ) {
                                    if ( j > 3 ) {
                                        SetComponent sc = setComponentList.get(i - 2);

                                        List<OperationSchedulingComponentDetail> osDetails
                                                = getComponentSchedulingDetails(
                                                sc.getPrimaryKey().getComponent(), sm.getOperation(), day);

                                        double osSum = 0.0;

                                        for ( OperationSchedulingComponentDetail od : osDetails ) {
                                            osSum += od.getPrimaryKey().getOrder()
                                                    .getOrderDetail( sc.getPrimaryKey()
                                                    .getComponent() ).getComponentQuantity();
                                        }

                                        if ( !day.isWorkingDay() ) {
                                            cellColor.setBackground(gray, i, j - 4);
                                        }

                                        if ( osSum > 0 ) {
                                            sm.getDataTableModel().setValueAt(osSum, i, j - 4);
                                        } else {
                                            sm.getDataTableModel().setValueAt("", i, j - 4);
                                        }
                                    }
                                } else if ( sm.getType().equals("conjunto") ) {
                                    if ( j > 3 && i < (setList.size() + 2) ) {
                                        Set set = setList.get( i - 2 );

                                        List<OperationSchedulingSetDetail> osDetails
                                                = getSetSchedulingDetails(
                                                set, sm.getOperation(), day);

                                        double osSum = 0.0;

                                        for ( OperationSchedulingSetDetail od : osDetails ) {
                                            osSum += od.getPrimaryKey().getOrder().getSetQuantity();
                                        }

                                        if ( !day.isWorkingDay() ) {
                                            cellColor.setBackground(gray, i, j - 4);
                                        }

                                        if ( osSum > 0 ) {
                                            sm.getDataTableModel().setValueAt(osSum, i, j - 4);
                                        } else {
                                            sm.getDataTableModel().setValueAt("", i, j - 4);
                                        }
                                    }
                                }
                            }

                        }//planilhas
                    }//colunas

                    setProgress( (((i*100)/rowSize) + 5) );

                }//linhas

                setProgress(100);
            } catch (ExcelSchedulingException ex) {
                MessageDialog.error(ex.getMessage(), ex.getDetailMessage(), parent);
            } catch (SetPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (DayPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (OperationPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (OrderDetailPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (OperationSchedulingComponentDetailPersistenceException ex) {
                MessageDialog.error(ex, parent);
            } catch (OperationSchedulingSetDetailPersistenceException ex) {
                MessageDialog.error(ex, parent);
            }

            return null;
        }

        @Override
        public void done() {

        }
    }

    /**
     * Recupera os detalhes de ordens de produção
     * 
     * @param component Componente do detalhamento
     * @param set Conjunto do detalhamento
     * @param day Dia do detalhamento
     * @return Lista de detalhes de ordens de produção
     */
    private List<OrderDetail> getOrderDetail( Component component, Set set, Day day ) {
        List<OrderDetail> odList = new ArrayList();

        for ( OrderDetail od : orderDetailList ) {
            if ( od.getPrimaryKey().getComponent().getId().equals( component.getId() )
                    && od.getPrimaryKey().getOrder().getSet().getId().equals( set.getId() )
                    && DateUtil.compareDates(od.getPrimaryKey().getOrder().getMasterScheduling().getDate(), day.getDate())) {
                odList.add(od);
            }
        }

        return odList;
    }

    /**
     * Recupera detalhes de sequenciamento da produção para componentes
     * 
     * @param component Componente do detalhamento
     * @param operation Operação do detalhamento
     * @param day Dia do detalhamento
     * @return Lista de detalhes de sequenciamento para componentes
     */
    private List<OperationSchedulingComponentDetail> getComponentSchedulingDetails
            (Component component, Operation operation, Day day) {
        List<OperationSchedulingComponentDetail> oscdList = new ArrayList();

        for ( OperationSchedulingComponentDetail oscd : componentSchedulingList ) {
            if ( oscd.getPrimaryKey().getComponent().getId().equals( component.getId() )
                    && oscd.getPrimaryKey().getOperation().getId().intValue() == operation.getId().intValue()
                    && DateUtil.compareDates(oscd.getPrimaryKey().getDay().getDate(), day.getDate())) {
                oscdList.add(oscd);
            }
        }

        return oscdList;
    }

    /**
     * Recupera detalhes de sequenciamento da produção para conjuntos
     * 
     * @param set Conjunto do detalhamento
     * @param operation Operação do detalhamento
     * @param day Dia do detalhamento
     * @return Lista de detalhes de sequenciamento para conjuntos
     */
    private List<OperationSchedulingSetDetail> getSetSchedulingDetails
            (Set set, Operation operation, Day day) {
        List<OperationSchedulingSetDetail> ossdList = new ArrayList();

        for ( OperationSchedulingSetDetail ossd : setSchedulingList ) {
            if ( ossd.getPrimaryKey().getSet().getId().equals( set.getId() )
                    && ossd.getPrimaryKey().getOperation().getId().intValue() == operation.getId().intValue()
                    && DateUtil.compareDates(ossd.getPrimaryKey().getDay().getDate(), day.getDate())) {
                ossdList.add(ossd);
            }
        }

        return ossdList;
    }

    /**
     * Devolve intervalo numérico entre dois números inteiros
     * 
     * @param start Valor inicial
     * @param end Valor final
     * @return Array dos valores dentro do intervalo
     */
    private int[] getInterval(int start, int end) {
        int[] array = new int[ (end-start) + 1 ];
        int count = 0;

        for ( int i = start; i <= end; i++) {
            array[count] = i;
            count++;
        }

        return array;
    }

    /**
     * Seta o período a ser exibido pelo sequenciamento
     * @param startDate Data inicial
     * @param endDate Data final
     */
    public void setPeriod( DateTime startDate, DateTime endDate ) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
