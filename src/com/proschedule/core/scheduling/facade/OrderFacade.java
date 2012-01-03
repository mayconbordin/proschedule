package com.proschedule.core.scheduling.facade;

import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.scheduling.dao.OrderDAO;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingComponentDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.scheduling.messages.OrderDetailMessages;
import com.proschedule.core.scheduling.messages.OrderMessages;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingComponentDetailKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingSetDetailKey;
import com.proschedule.core.scheduling.model.keys.OrderDetailKey;
import com.proschedule.util.date.DateUtil;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.joda.time.DateTime;

/**
 * Interface de comunicação com o  módulo de Ordens de Produção.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OrderFacade {
    private OrderDAO dao;
    private CalendarFacade calendarFacade;
    private OperationSchedulingFacade operationSchedulingFacade;
    private ComponentFacade componentFacade;
    private SetFacade setFacade;

    private Validator validator;

    private OrderMessages msg;
    private OrderDetailMessages msgDetail;

    /**
     * Construtor da Classe
     */
    public OrderFacade(){
        dao = new OrderDAO();
        calendarFacade = new CalendarFacade();
        operationSchedulingFacade = new OperationSchedulingFacade();
        componentFacade = new ComponentFacade();
        setFacade = new SetFacade();

        validator = HibernateValidatorUtil.getValidator();
        
        msg = new OrderMessages();
        msgDetail = new OrderDetailMessages();
    }

    /**
     * Valida um objeto conjunto.
     *
     * @param order O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Order order) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<Order>> constraintViolations
                = validator.validate(order);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um conjunto.
     *
     * @param order O conjunto a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OrderPersistenceException
     * @throws ValidatorException
     * @throws OrderDetailPersistenceException
     */
    public boolean add(Order order)
            throws OrderPersistenceException, ValidatorException, OrderDetailPersistenceException {
        try {
            validate(order);

            //Verifica se já não existe o registro
            if ( alreadyExist( order ) ) {
                throw new OrderPersistenceException( msg.getAlreadyExist() );
            }

            //Calcula a data de início da produção
            calculateMasterSchedulingDate(order);

            boolean result = dao.add(order);

            //Cria o sequenciamento da ordem
            createOperationsScheduling(order);

            return result;
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um conjunto.
     *
     * @param order O conjunto a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OrderPersistenceException
     * @throws ValidatorException
     * @throws OrderDetailPersistenceException
     */
    public boolean modify(Order order)
            throws OrderPersistenceException, ValidatorException, OrderDetailPersistenceException {
        try {
            validate(order);

            //Calcula a data de início da produção
            calculateMasterSchedulingDate(order);

            boolean result = dao.modify(order);

            //Remove o sequenciamento anterior
            removeOperationsScheduling(order);

            //Recria o sequenciamento da ordem
            createOperationsScheduling(order);

            return result;
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um conjunto.
     *
     * @param order conjunto a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OrderPersistenceException
     */
    public boolean remove(Order order)
            throws OrderPersistenceException {
        try {
            //Remove o sequenciamento anterior
            removeOperationsScheduling(order);
            
            return dao.remove(order);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os conjuntos.
     *
     * @return Lista dos conjuntos.
     * @throws OrderPersistenceException
     */
    public List<Order> list() throws OrderPersistenceException {
        try {
            return dao.list();
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista com todos conjuntos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de conjuntos ordenada
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , String order )
            throws OrderPersistenceException {
        try {
            return dao.list(field, order);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista os conjuntos que estiverem de acordo com os parâmetros informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de conjuntos ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , String value , String operator )
            throws OrderPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das ordens de produção dentro dos parâmetros informados.
     * É usada para campos do tipo double.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , Double value , String operator )
            throws OrderPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das ordens de produção dentro dos parâmetros informados.
     * É usada para campos do tipo data.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( String field , Date value , String operator )
            throws OrderPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de conjunto.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( Set value , String operator ) throws OrderPersistenceException {
        try {
            return dao.list(value, operator);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de conjunto.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OrderPersistenceException
     */
    public List<Order> list( Customer value , String operator ) throws OrderPersistenceException {
        try {
            return dao.list(value, operator);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das ordens de produção com o conjunto e data informadas.
     *
     * @param set O conjunto da ordem de produção
     * @param deliveryDate A data de entrega da ordem de produção
     * @return Lista de operações
     * @throws OrderPersistenceException 
     */
    public List<Order> list( Set set, Date deliveryDate ) throws OrderPersistenceException {
        try {
            return dao.list(set, deliveryDate);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes da ordem de produção de um determinado componente
     * em uma data especifica.
     *
     * @param component O componente do detalhe da ordem de produção
     * @param set O conjunto do detalhe da ordem de produção
     * @param day A data do detalhe da ordem de produção
     * @return Lista de detalhes de ordem de produção ou null se um operador inválido for informado
     * @throws OrderDetailPersistenceException
     */
    public List<OrderDetail> listDetails( Component component, Set set, Day day ) throws OrderDetailPersistenceException {
        try {
            return dao.listDetails(component, set, day);
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes da ordem de produção de um determinado
     * período de início de sequenciamento.
     *
     * @param startDate O limite inferior da data de início de sequenciamento das ordens
     * @param endDate O limite superior da data de fim de sequenciamento das ordens
     * @return Lista de detalhes de ordem de produção ou null se um operador inválido for informado
     * @throws OrderDetailPersistenceException
     */
    public List<OrderDetail> listDetails( Date startDate, Date endDate ) throws OrderDetailPersistenceException {
        try {
            return dao.listDetails(startDate, endDate);
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um conjunto no banco de dados.
     *
     * @param order conjunto com os dados da chave primária
     * @return True se o conjunto já existe ou false se não existe
     * @throws OrderPersistenceException
     */
    public boolean alreadyExist( Order order ) throws OrderPersistenceException {
        try {
            return dao.alreadyExist(order);
        } catch (OrderPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return O conjunto encontrada ou null se nada for encontrado
     * @throws OrderPersistenceException
     */
    public Order getOrder( String id ) throws OrderPersistenceException {
        return dao.getOrder(id);
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto detalhe de conjunto.
     *
     * @param orderDetail O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateDetail(OrderDetail orderDetail) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<OrderDetail>> constraintViolations
                = validator.validate(orderDetail);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um detalhe de conjunto.
     *
     * @param orderDetail O detalhe de conjunto a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OrderDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean addDetail(OrderDetail orderDetail)
            throws OrderDetailPersistenceException, ValidatorException {
        try {
            validateDetail(orderDetail);

            //Verifica se já não existe o registro
            if ( alreadyExistDetail( orderDetail ) ) {
                throw new OrderDetailPersistenceException( msgDetail.getAlreadyExist() );
            }

            return dao.addDetail(orderDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um detalhe de conjunto.
     *
     * @param orderDetail O detalhe de conjunto a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OrderDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean modifyDetail(OrderDetail orderDetail)
            throws OrderDetailPersistenceException, ValidatorException {
        try {
            validateDetail(orderDetail);
            return dao.modifyDetail(orderDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um detalhe de conjunto.
     *
     * @param orderDetail detalhe de conjunto a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OrderDetailPersistenceException
     */
    public boolean removeDetail(OrderDetail orderDetail)
            throws OrderDetailPersistenceException {
        try {
            return dao.removeDetail(orderDetail);
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um detalhe de conjunto no banco de dados.
     *
     * @param orderDetail detalhe de conjunto com os dados da chave primária
     * @return True se o detalhe de conjunto já existe ou false se não existe
     * @throws OrderDetailPersistenceException
     */
    public boolean alreadyExistDetail( OrderDetail orderDetail ) throws OrderDetailPersistenceException {
        try {
            return dao.alreadyExistDetail(orderDetail);
        } catch (OrderDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param key A chave primária a ser procurada
     * @return O detalhe de conjunto encontrada ou null se nada for encontrado
     * @throws OrderDetailPersistenceException
     */
    public OrderDetail getOrderDetail( OrderDetailKey key )
            throws OrderDetailPersistenceException {
        return dao.getOrderDetail(key);
    }

    //--------------------------------------------------------------------------
    //Métodos de Sequenciamento
    //--------------------------------------------------------------------------
    /**
     * Calcula a data em que a produção da ordem deve começar baseado no lead time
     * total para a produção do conjunto.
     * 
     * @param order
     * @throws OrderDetailPersistenceException
     */
    public void calculateMasterSchedulingDate( Order order ) throws OrderDetailPersistenceException {
        try {
            //O tempo em horas que leva para produzir o conjunto da ordem
            double leadTime = order.getSet().getTotalLeadTime();

            //Deve ser igual ou maior ao lead time, pois é a soma de tempo acumulado
            //durante os dias a partir da data de entrega
            double leadTimeSum = 0.0;

            //O dia em que a produção deve começar, a padrão é a data de entrega
            //Ela vai retrocedendo até a soma do lead time superar ou igualar o
            //lead time necessário para produzir o conjunto
            Day startDay = calendarFacade.getDay( order.getDeliveryDate() );

            //Pega a data do dia da entrega para iniciar a percorrida dos dias
            DateTime date = DateUtil.toDateTime( startDay.getDate() );

            //Percorre os dias a partir da data de entrega,
            //indo para trás, até que a soma de tempo alcance ou ultrapasse
            //a quantidade exigida
            while (1 == 1) {
                //Verifica se neste dia há trabalho
                if ( startDay.isWorkingDay() ) {
                    //Adiciona as horas de trabalho daquele dia ao contador
                    //total de horas
                    leadTimeSum += startDay.getWorkingHours();
                }

                //Condição de parada - lead time necessário obtido
                if ( leadTimeSum >= leadTime ) {
                    break;
                }

                //Passa para o dia anterior
                date = date.minusDays(1);

                //Carrega o dia anterior
                startDay = calendarFacade.getDay( date.toDate() );
            }

            //Seta a data em que a produção irá começar
            order.setMasterScheduling(startDay);

        } catch (DayPersistenceException ex) {
            throw new OrderDetailPersistenceException(ex, msg.getCalendarNotFound());
        }
    }

    private void createComponentOperationScheduling(Operation operation, Day startDay,
            Component component, Order order) 
            throws OperationSchedulingComponentDetailPersistenceException, ValidatorException {
        //Cria a chave do detalhe de sequenciamento
        OperationSchedulingComponentDetailKey osdKey =
                new OperationSchedulingComponentDetailKey(operation, startDay, component, order);

        //Cria o detalhe de sequenciamento
        OperationSchedulingComponentDetail osd = new OperationSchedulingComponentDetail();
        osd.setPrimaryKey(osdKey);

        //Salva o novo detalhe
        operationSchedulingFacade.addComponentDetail(osd);
    }

    /**
     * Faz o sequenciamento por operação da ordem de produção informada
     *
     * @param order A ordem que será sequenciada
     * @throws OrderPersistenceException
     * @throws ValidatorException
     */
    public void createOperationsScheduling( Order order ) throws OrderPersistenceException, ValidatorException {
        try {
            Day lastDay = order.getMasterScheduling();
            DateTime lastDate = DateUtil.toDateTime( lastDay.getDate() );
            double lastDayLeadTime = 0.0;

            //
            //COMPONENTES
            //
            //Percorre cada componente do conjunto da ordem de produção
            for ( SetComponent sc : order.getSet().getComponents() ) {
                //Recupera o componente
                Component c = sc.getPrimaryKey().getComponent();

                //Começa pelo início da produção
                Day startDay = order.getMasterScheduling();

                //Data do início da produção
                DateTime date = DateUtil.toDateTime( startDay.getDate() );

                //O tempo disponível para o dia inicial de produção
                double startDayLeadTime = startDay.getWorkingHours();

                //Listagem das operações de componentes na ordem em que devem ocorrer
                ConcurrentLinkedQueue<ComponentDetail> list
                        = new ConcurrentLinkedQueue(
                            componentFacade.listDetails(c, "order", "asc")
                        );

                //Enquanto houverem operações para serem alocadas
                for (ComponentDetail cd : list) {
                    //Recupera a operação pela qual o componente passa
                    Operation o = cd.getPrimaryKey().getOperation();
                    
                    //Tempo necessário para realizar a operação atual
                    double neededLeadTime = cd.getLeadTimeValue();

                    //Indica o primeiro dia da produção, é quando deve ser marcado o registro
                    boolean firstDay = true;

                    //Percorre os dias até que o tempo necessário tenha se esgotado
                    while ( neededLeadTime > 0 ) {
                        //Se houver tempo livre e for um dia de trabalho
                        if ( startDayLeadTime > 0 && startDay.isWorkingDay() ) {
                            //Armazena temporariamente o tempo necessário restante
                            double tempNeeded = neededLeadTime;

                            //Subtrai o tempo necessário do existente no dia
                            neededLeadTime -= startDayLeadTime;

                            //Subtrai do dia o tempo de produção do componente
                            //na operação atual
                            startDayLeadTime -= tempNeeded;

                            //Só grava o sequenciamento no primeiro dia, no início
                            if ( firstDay ) {
                                createComponentOperationScheduling(o, startDay, c, order);
                            }
                        //Quado acabar o tempo livre do dia, passa para o próximo
                        } else {
                            //Passa para o próximo dia
                            date = date.plusDays(1);

                            //Carrega o próximo dia
                            startDay = calendarFacade.getDay( date.toDate() );

                            //Carrega o lead time
                            startDayLeadTime = startDay.getWorkingHours();

                            //Só seta como falso o primeiro dia se o dia possuía tempo
                            //para a produção
                            if ( neededLeadTime != cd.getLeadTimeValue() ) {
                                firstDay = false;
                            }
                        }
                    }//enquanto precisar de tempo
                }//for dos detalhes de componente

                //Precisa pegar a maior data de todas
                //Para marcar o fim da produção dos componentes e o início da
                //produção do conjunto
                if ( date.getMillis() > lastDate.getMillis() ) {
                    lastDate = date;
                    lastDay = startDay;
                    lastDayLeadTime = startDayLeadTime;
                }

            }//for componentes

            //
            //CONJUNTOS
            //
            //Listagem das operações de conjuntos na ordem em que devem ocorrer
            ConcurrentLinkedQueue<SetDetail> list = new ConcurrentLinkedQueue(
                        setFacade.listDetails(order.getSet(), "order", "asc")
                    );

            //Enquanto houverem operações para serem alocadas
            for (SetDetail sd : list) {
                //Recupera a operação pela qual o conjunto passa
                Operation o = sd.getPrimaryKey().getOperation();

                //Tempo necessário para realizar a operação atual
                double neededLeadTime = sd.getLeadTimeValue();

                //Indica o primeiro dia da produção, é quando deve ser marcado o registro
                boolean firstDay = true;

                //Percorre os dias até que o tempo necessário tenha se esgotado
                while ( neededLeadTime > 0 ) {
                    //Se houver tempo livre e for um dia de trabalho
                    if ( lastDayLeadTime > 0 && lastDay.isWorkingDay() ) {
                        //Armazena temp. o tempo necessário restante
                        double tempNeeded = neededLeadTime;

                        //Subtrai o tempo necessário do existente no dia
                        neededLeadTime -= lastDayLeadTime;

                        //Subtrai do dia o tempo de produção do componente
                        //na operação atual
                        lastDayLeadTime -= tempNeeded;

                        //Só grava o sequenciamento no primeiro dia, no início
                        if ( firstDay ) {
                            //Cria a chave do detalhe de sequenciamento
                            OperationSchedulingSetDetailKey osdKey =
                                    new OperationSchedulingSetDetailKey(o, lastDay, order.getSet(), order);

                            //Cria o detalhe de sequenciamento
                            OperationSchedulingSetDetail osd = new OperationSchedulingSetDetail();
                            osd.setPrimaryKey(osdKey);

                            //Salva o novo detalhe
                            operationSchedulingFacade.addSetDetail(osd);
                        }
                    //Quado acabar o tempo livre do dia, passa para o próximo
                    } else {
                        //Passa para o próximo dia
                        lastDate = lastDate.plusDays(1);

                        //Carrega o próximo dia
                        lastDay = calendarFacade.getDay( lastDate.toDate() );

                        //Carrega o lead time
                        lastDayLeadTime = lastDay.getWorkingHours();

                        //Só seta como falso o primeiro dia se o dia possuía tempo
                        //para a produção
                        if ( neededLeadTime != sd.getLeadTimeValue() ) {
                            firstDay = false;
                        }
                    }
                }//enquanto precisar de tempo
            }//for dos detalhes de conjunto
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        } catch (SetDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        } catch (ComponentDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        } catch (ValidatorException ex) {
            throw ex;
        } catch (DayPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        }
    }

    /**
     * Remove o sequenciamento por operação da ordem de produção informada
     *
     * @param order A ordem cujo sequenciamento será removido
     * @throws OrderPersistenceException
     */
    public void removeOperationsScheduling( Order order ) throws OrderPersistenceException {
        try {
            List<OperationSchedulingComponentDetail> oscdList = operationSchedulingFacade.listComponentDetails(order, "=");
            for (OperationSchedulingComponentDetail oscd : oscdList) {
                operationSchedulingFacade.removeComponentDetail(oscd);
            }
            List<OperationSchedulingSetDetail> ossdList = operationSchedulingFacade.listSetDetails(order, "=");
            for (OperationSchedulingSetDetail ossd : ossdList) {
                operationSchedulingFacade.removeSetDetail(ossd);
            }
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw new OrderPersistenceException(ex, ex.getMessage());
        }
    }
}