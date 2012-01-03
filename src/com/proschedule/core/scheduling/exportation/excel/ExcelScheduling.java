package com.proschedule.core.scheduling.exportation.excel;

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
import com.proschedule.util.string.StringUtil;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

/**
 * Exporta o sequenciamento da produção para Excel
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ExcelScheduling implements PropertyChangeListener {
    private Workbook workbook;
    private String filePath;

    private List<SheetModel> sheets;

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

    private ExcelSchedulingMessages msg;

    private ProgressMonitor progressMonitor;
    private Task task;
    private java.awt.Frame parent;

    /**
     * Construtor da Classe
     */
    public ExcelScheduling() {
        workbook = new XSSFWorkbook();
        sheets = new ArrayList();
        setComponentList = new ArrayList();

        operationFacade = new OperationFacade();
        operationSchedulingFacade = new OperationSchedulingFacade();
        setFacade = new SetFacade();
        calendarFacade = new CalendarFacade();
        orderFacade = new OrderFacade();

        msg = new ExcelSchedulingMessages();
    }

    /**
     * Inicia a criação da planilha Excel
     */
    public void create() {
        progressMonitor = new ProgressMonitor(parent,
                              "Criando a planilha eletrônica", "", 0, 100);

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
                    //Mensagem de sucesso
                    MessageDialog.sucess("Planilha eletrônica criada e salva com sucesso!", parent);
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
     * Classe da tarefa de exportação da planilha eletrônica
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

                //Cria a planilha mestre
                sheets.add( new SheetModel(workbook.createSheet("Mestre"), "mestre",
                        day.getWeek(), 4) );

                //Cancela a tarefa
                if (isCancelled()) return null;

                //Cria as planilhas das operações
                for (Operation o : operationList) {
                    if ( o.getType().getDescription().equals("Componente") ) {
                        sheets.add( new SheetModel(workbook.createSheet(
                                StringUtil.removeInvalidSheetChars( o.getDescription() ) ), "componente",
                                day.getWeek(), 4, o) );
                    } else {
                        sheets.add( new SheetModel(workbook.createSheet(
                                StringUtil.removeInvalidSheetChars( o.getDescription() ) ), "conjunto",
                                day.getWeek(), 4, o) );
                    }
                }

                //Tamanho das linhas
                int rowSize = setComponentList.size() + 2;

                //Tamanho das colunas
                int colSize = dayList.size() + 4;

                //Cancela a tarefa
                if (isCancelled()) return null;

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
                        for ( SheetModel sm : sheets ) {
                            //Cria a linha da planilha
                            Row row = null;

                            if ( sm.getSheet().getRow(i) == null ) {
                                row = sm.getSheet().createRow(i);
                            } else {
                                row = sm.getSheet().getRow(i);
                            }

                            //Cria a célula da planilha
                            Cell cell = row.createCell(j);

                            //Estilo padrão
                            cell.setCellStyle( getNormal10Style() );

                            //Criação das semanas
                            if ( i == 0 && j > 3 ) {
                                //Troca de semana
                                if ( day.getWeek() != sm.getWeek() ) {
                                    //Mescla as colunas da semana
                                    sm.getSheet().addMergedRegion(
                                            new CellRangeAddress(0, 0, sm.getStartWeek(), j-1));

                                    //Seta o novo início da coluna da semana
                                    sm.setStartWeek(j);

                                    //Seta a semana
                                    sm.setWeek(day.getWeek());
                                }

                                //Intercambia os estilos das células
                                if ( sm.getWeek()%2 == 0 ) {
                                    cell.setCellStyle( getWeekStyleOne() );
                                } else {
                                    cell.setCellStyle( getWeekStyleTwo() );
                                }

                                //Só atribui valor a primeira célula da semana
                                if ( j == sm.getStartWeek() ) {
                                    cell.setCellValue("Semana " + sm.getWeek());
                                }

                                //Faz a mesclagem da última semana
                                if ( (j+1) == colSize ) {
                                    sm.getSheet().addMergedRegion(new CellRangeAddress
                                            (0, 0, sm.getStartWeek() , j));
                                }
                            }//criação das semanas

                            //Atribuição do tamanho das colunas
                            if ( j > 3 ) {
                                //Seta o tamanho da coluna
                                sm.getSheet().setColumnWidth(j, 1100);
                            }

                            //Criação das datas
                            if ( i == 1 && j > 3 ) {
                                //Seta a data na célula
                                cell.setCellValue( day.getDate() );

                                //Modifica o estilo de acordo com o dia
                                if ( day.isWorkingDay() ) {
                                    cell.setCellStyle( getDateStyleOne() );
                                } else {
                                    cell.setCellStyle( getDateStyleTwo() );
                                }

                                //Altura da linha das datas
                                row.setHeight((short) 580);
                            }//Criação das datas

                            //Criação dos títulos
                            if ( i == 1 ) {
                                //Títulos para componentes e mestre
                                if ( !sm.getType().equals("conjunto") ) {
                                    if ( j == 0 ) {
                                        cell.setCellValue( "Conj." );
                                        cell.setCellStyle( getNormal10Style() );
                                    } else if ( j == 1 ) {
                                        cell.setCellValue( "Componente" );
                                        cell.setCellStyle( getNormal10Style() );
                                        sm.getSheet().setColumnWidth(1, 4600);
                                    } else if ( j == 2 ) {
                                        cell.setCellValue( "Qtde. \n /Conjunto" );
                                        cell.setCellStyle( getNormal10Style() );
                                        sm.getSheet().setColumnWidth(2, 4100);
                                    } else if ( j == 3 ) {
                                        cell.setCellValue( "MP" );
                                        cell.setCellStyle( getNormal10Style() );
                                        sm.getSheet().setColumnWidth(3, 4600);
                                    }
                                } else {
                                    if ( j == 0 ) {
                                        cell.setCellValue( "Conjunto" );
                                        cell.setCellStyle( getNormal10Style() );
                                        sm.getSheet().setColumnWidth(0, 4600);
                                    }
                                }
                            }//Criação dos títulos

                            //Criação da lista de conjuntos e componentes
                            if ( i > 1 ) {
                                if ( sm.getType().equals("conjunto") ) {
                                    //Altura da linha das listagens
                                    row.setHeight((short) 580);

                                    if ( i < (setList.size() + 2) ) {
                                        if ( j == 0 ) {
                                            //Código do conjunto
                                            cell.setCellValue( setList.get( i - 2 ).getId() );
                                            cell.setCellStyle( getSetStyle() );
                                            sm.getSheet().setColumnWidth(0, 4600);
                                        } else if ( j == 1 ) {
                                            cell.setCellValue( "Quantidade de conjuntos p/ soldar" );
                                            cell.setCellStyle( getComponentStyle() );
                                            sm.getSheet().setColumnWidth(1, 4600);
                                        }
                                    }
                                } else {
                                    //Altura da linha das listagens
                                    row.setHeight((short) 480);

                                    SetComponent sc = setComponentList.get(i - 2);

                                    if ( j == 0 ) {
                                        //Primeiro conjunto
                                        if ( i == 2 ) {
                                            //Código do conjunto
                                            cell.setCellValue( sc.getPrimaryKey().getSet().getId() );
                                            cell.setCellStyle( getSetComponentStyle() );

                                            sm.setCurrentSet(sc.getPrimaryKey().getSet());
                                            sm.setStartSet(2);
                                        }

                                        //Demais conjuntos, quando trocar o conjunto
                                        if ( sc.getPrimaryKey().getSet() != sm.getCurrentSet() ) {
                                            //Mescla o código do conjunto
                                            sm.getSheet().addMergedRegion(
                                                    new CellRangeAddress(
                                                    sm.getStartSet(),i-1,0,0));

                                            sm.setCurrentSet(sc.getPrimaryKey().getSet());
                                            sm.setStartSet(i);

                                            //Código do conjunto
                                            cell.setCellValue( sc.getPrimaryKey().getSet().getId() );
                                            cell.setCellStyle( getSetComponentStyle() );
                                        }
                                    } else if ( j == 1 ) {
                                        cell.setCellValue( sc.getPrimaryKey().getComponent().getId() );
                                        cell.setCellStyle( getComponentStyle() );
                                    } else if ( j == 2 ) {
                                        cell.setCellValue( sc.getComponentQuantity() );
                                        cell.setCellStyle( getComponentStyle() );
                                    } else if ( j == 3 ) {
                                        cell.setCellValue( sc.getPrimaryKey().getComponent().getRawMaterial() );
                                        cell.setCellStyle( getRawMaterialStyle() );
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

                                        if ( day.isWorkingDay() ) {
                                            cell.setCellStyle( getSchedulingStyleOne() );
                                        } else {
                                            cell.setCellStyle( getSchedulingStyleTwo() );
                                        }

                                        if ( msSum > 0 ) {
                                            cell.setCellValue(msSum);
                                        } else {
                                            cell.setCellValue("");
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

                                        if ( day.isWorkingDay() ) {
                                            cell.setCellStyle( getSchedulingStyleOne() );
                                        } else {
                                            cell.setCellStyle( getSchedulingStyleTwo() );
                                        }

                                        if ( osSum > 0 ) {
                                            cell.setCellValue(osSum);
                                        } else {
                                            cell.setCellValue("");
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

                                        if ( day.isWorkingDay() ) {
                                            cell.setCellStyle( getSchedulingStyleOne() );
                                        } else {
                                            cell.setCellStyle( getSchedulingStyleTwo() );
                                        }

                                        if ( osSum > 0 ) {
                                            cell.setCellValue(osSum);
                                        } else {
                                            cell.setCellValue("");
                                        }
                                    }
                                }
                            }

                        }//planilhas

                        //Cancela a tarefa
                        if (isCancelled()) return null;

                    }//colunas

                    setProgress( (((i*100)/rowSize) + 5) );

                    //Cancela a tarefa
                    if (isCancelled()) return null;

                }//linhas

                //Carrega as configurações gerais das planilhas
                for ( SheetModel sm : sheets ) {
                    //Cria o painel fixo
                    sm.getSheet().createFreezePane(4,2);

                    sm.getSheet().setAutobreaks(true);

                    //Seta o zoom
                    sm.getSheet().setZoom(80,100);

                    //Centraliza verticalmente o texto
                    sm.getSheet().setVerticallyCenter(true);
                }

                save();

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
            } catch (Exception ex) {
                MessageDialog.error("Ocorreu um erro de origem desconhecida.", ex.getMessage(), parent);
            }

            return null;
        }
    }

    /**
     * Recupera os detalhes da ordem de produção com o componente, conjunto e dia
     * especificados.
     * 
     * @param component O componente do detalhe da ordem
     * @param set O conjunto do detalhe da ordem
     * @param day O dia do detalhe da ordem de produção
     * @return Lista de detalhes de ordem de produção
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
     * Recupera os detalhes do sequenciamento de componentes
     * 
     * @param component O componente do sequenciamento
     * @param operation A operação do sequenciamento
     * @param day O dia do sequenciamento
     * @return lista de detalhes de sequenciamento de operações
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
     * Recupera detalhes de sequenciamento de conjuntos
     *
     * @param set O conjunto do sequenciamento
     * @param operation A operação do sequenciamento
     * @param day O dia do sequenciamento
     * @return Lista de detalhes de sequenciamento de conjuntos
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
     * Salva a planilha no caminho informado
     * 
     * @throws ExcelSchedulingException
     */
    private void save() throws ExcelSchedulingException {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File( filePath ));
            workbook.write(fos);
        } catch (IOException e) {
            throw new ExcelSchedulingException(e, msg.getIoError());
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    throw new ExcelSchedulingException(e, msg.getIoError());
                }
            }
        }
    }

    /**
     * @return Estilo de célula para semanas
     */
    private CellStyle getWeekStyleOne() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getWeekFont() );
        style.setFillForegroundColor( IndexedColors.GREEN.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para semanas
     */
    private CellStyle getWeekStyleTwo() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getWeekFont() );
        style.setFillForegroundColor( IndexedColors.LIGHT_GREEN.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para data
     */
    private CellStyle getDateStyleOne() {
        DataFormat format = workbook.createDataFormat();
        
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_BOTTOM );
        style.setRotation((short) 90);
        style.setFont( getDateFont() );
        style.setDataFormat(format.getFormat("d/m"));
        style.setFillForegroundColor( IndexedColors.WHITE.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para datas
     */
    private CellStyle getDateStyleTwo() {
        DataFormat format = workbook.createDataFormat();

        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_BOTTOM );
        style.setRotation((short) 90);
        style.setFont( getDateFont() );
        style.setDataFormat(format.getFormat("d/m"));
        style.setFillForegroundColor( IndexedColors.GREY_25_PERCENT.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para componentes
     */
    private CellStyle getSetComponentStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setRotation((short) 90);
        style.setFont( getSetComponentFont() );
        style.setFillForegroundColor( IndexedColors.WHITE.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para componente
     */
    private CellStyle getComponentStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getNormal10Font() );
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para conjunto
     */
    private CellStyle getSetStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getSetFont() );
        style.setFillForegroundColor( IndexedColors.WHITE.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para matéria prima
     */
    private CellStyle getRawMaterialStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getNormal8Font() );
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula normal fonte 10
     */
    private CellStyle getNormal10Style() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getNormal10Font() );
        style.setFillForegroundColor( IndexedColors.WHITE.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        
        return style;
    }

    /**
     * @return Estilo de célula para sequenciamento
     */
    private CellStyle getSchedulingStyleOne() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getDateFont() );
        style.setFillForegroundColor( IndexedColors.WHITE.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Estilo de célula para sequenciamento
     */
    private CellStyle getSchedulingStyleTwo() {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment( CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment( CellStyle.VERTICAL_CENTER );
        style.setFont( getDateFont() );
        style.setFillForegroundColor( IndexedColors.GREY_25_PERCENT.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    /**
     * @return Fonte para semanas
     */
    private Font getWeekFont() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        return font;
    }

    /**
     * @return Fonte para componentes de conjunto
     */
    private Font getSetComponentFont() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        return font;
    }

    /**
     * @return Fonte para conjuntos
     */
    private Font getSetFont() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)14);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        return font;
    }

    /**
     * @return Fonte para datas
     */
    private Font getDateFont() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)8);
        font.setFontName("Arial");

        return font;
    }

    /**
     * @return Fonte normal tamanho 10
     */
    private Font getNormal10Font() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");

        return font;
    }

    /**
     * @return fonte normal tamanho 8 para texto
     */
    public Font getNormal8Font() {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)8);
        font.setFontName("Arial");

        return font;
    }

    /**
     * Seta o período de sequenciamento a ser exportado
     *
     * @param startDate data inicial
     * @param endDate data final
     */
    public void setPeriod( DateTime startDate, DateTime endDate ) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
