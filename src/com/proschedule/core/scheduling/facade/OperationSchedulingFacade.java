package com.proschedule.core.scheduling.facade;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.dao.OperationSchedulingDAO;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingComponentDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.scheduling.messages.OperationSchedulingComponentDetailMessages;
import com.proschedule.core.scheduling.messages.OperationSchedulingMessages;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingComponentDetailKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingSetDetailKey;
import com.proschedule.util.date.DateUtil;
import com.proschedule.util.date.DateUtilException;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.joda.time.DateTime;

/**
 * Classe para controle do sequenciamento da produção
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationSchedulingFacade {
    private OperationSchedulingDAO dao;
    private OperationFacade operationFacade;
    private CalendarFacade calendarFacade;

    private Validator validator;
    private OperationSchedulingMessages msg;
    private OperationSchedulingComponentDetailMessages msgDetail;

    /**
     * Construtor da Classe
     */
    public OperationSchedulingFacade(){
        dao = new OperationSchedulingDAO();
        operationFacade = new OperationFacade();

        validator = HibernateValidatorUtil.getValidator();

        msg = new OperationSchedulingMessages();
        msgDetail = new OperationSchedulingComponentDetailMessages();
    }

    /**
     * Valida um objeto sequenciamento por operação.
     *
     * @param operationScheduling O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(OperationScheduling operationScheduling) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<OperationScheduling>> constraintViolations
                = validator.validate(operationScheduling);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um sequenciamento por operação.
     *
     * @param operationScheduling O sequenciamento por operação a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingPersistenceException
     * @throws ValidatorException
     */
    public boolean add(OperationScheduling operationScheduling)
            throws OperationSchedulingPersistenceException, ValidatorException {
        try {
            validate(operationScheduling);

            //Verifica se já não existe o registro
            if ( alreadyExist( operationScheduling ) ) {
                throw new OperationSchedulingPersistenceException( msg.getAlreadyExist() );
            }

            return dao.add(operationScheduling);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um sequenciamento por operação.
     *
     * @param operationScheduling O sequenciamento por operação a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(OperationScheduling operationScheduling)
            throws OperationSchedulingPersistenceException, ValidatorException {
        try {
            validate(operationScheduling);
            return dao.modify(operationScheduling);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um sequenciamento por operação.
     *
     * @param operationScheduling sequenciamento por operação a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OperationSchedulingPersistenceException
     */
    public boolean remove(OperationScheduling operationScheduling)
            throws OperationSchedulingPersistenceException {
        try {
            return dao.remove(operationScheduling);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove todo o sequenciamento de uma operação.
     *
     * @param operation A operação cujo sequenciamento será removido
     * @return True se tudo ocorrer bem
     * @throws OperationSchedulingPersistenceException
     */
    public boolean removeAll( Operation operation ) throws OperationSchedulingPersistenceException {
        try {
            return dao.removeAll(operation);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os sequenciamento por operaçãos.
     *
     * @return Lista dos sequenciamento por operaçãos.
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list() throws OperationSchedulingPersistenceException {
        try {
            return dao.list();
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista o sequenciamento por operações do ano informado.
     * 
     * @param year O ano a ser procurado
     * @return Lista do sequenciamento por operações do ano informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list( int year ) throws OperationSchedulingPersistenceException {
        try {
            Date startDate = DateUtil.formatDate("01/01/" + year);
            Date endDate;

            DateTime dt = DateUtil.toDateTime("01/12/" + year);
            DateTime last = dt.dayOfMonth().withMaximumValue();

            endDate = last.toDate();

            return list(startDate, endDate);
        } catch (DateUtilException ex) {
            throw new OperationSchedulingPersistenceException(ex, ex.getMessage());
        }
    }

    /**
     * Devolve uma lista com todos sequenciamento por operaçãos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de sequenciamento por operaçãos ordenada
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list( String field , String order )
            throws OperationSchedulingPersistenceException {
        try {
            return dao.list(field, order);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

     /**
     * Devolve uma lista dos sequenciamentos de operações dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de operações ou null se um operador inválido for informado
      * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Date startDate , Date endDate )
            throws OperationSchedulingPersistenceException {
        try {
            return dao.list(startDate, endDate);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos de operações de acordo com a operação.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Operation value , String operator )
            throws OperationSchedulingPersistenceException {
        try {
            return dao.list(value, operator);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos de operações de acordo com o dia informado.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingPersistenceException
     */
    public List<OperationScheduling> list(Day value , String operator )
            throws OperationSchedulingPersistenceException {
        try {
            return dao.list(value, operator);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos componentes de determinada
     * ordem de produção.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingComponentDetailPersistenceException 
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails(Order value , String operator )
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            return dao.listComponentDetails(value, operator);
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos conjuntos de determinada
     * ordem de produção.
     *
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails(Order value , String operator )
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            return dao.listSetDetails(value, operator);
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos componentes de
     * determinado componente em determinada operação em determinado dia.
     *
     * @param component O componente a ser buscado no sequenciamento
     * @param operation A operação a ser buscada no sequenciamento
     * @param day O dia a ser buscado no sequenciamento
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails
            ( Component component, Operation operation, Day day )
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            return dao.listComponentDetails(component, operation, day);
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos sequenciamentos por operação dos conjuntos de
     * determinado conjunto em determinada operação em determinado dia.
     *
     * @param set O conjunto do detalhamento
     * @param operation A operação a ser buscada no sequenciamento
     * @param day O dia a ser buscado no sequenciamento
     * @return Lista de sequenciamentos de operações ou null se um operador inválido for informado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails
            ( Set set, Operation operation, Day day )
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            return dao.listSetDetails(set, operation, day);
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes de componente dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de detalhes de componente
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public List<OperationSchedulingComponentDetail> listComponentDetails
            (Date startDate , Date endDate )
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            return dao.listComponentDetails(startDate, endDate);
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes de conjunto dentro do período informado.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de detalhes de conjunto
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public List<OperationSchedulingSetDetail> listSetDetails
            (Date startDate , Date endDate )
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            return dao.listSetDetails(startDate, endDate);
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um sequenciamento por operação no banco de dados.
     *
     * @param operationScheduling sequenciamento por operação com os dados da chave primária
     * @return True se o sequenciamento por operação já existe ou false se não existe
     * @throws OperationSchedulingPersistenceException
     */
    public boolean alreadyExist( OperationScheduling operationScheduling ) throws OperationSchedulingPersistenceException {
        try {
            return dao.alreadyExist(operationScheduling);
        } catch (OperationSchedulingPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um sequenciamento por operação com o código informado.
     *
     * @param key A chave primária a ser procurada
     * @return O sequenciamento por operação encontrada ou null se nada for encontrado
     * @throws OperationSchedulingPersistenceException
     */
    public OperationScheduling getOperationScheduling( OperationSchedulingKey key ) throws OperationSchedulingPersistenceException {
        return dao.getOperationScheduling(key);
    }

    //--------------------------------------------------------------------------
    //Métodos do ComponentDetail
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<OperationSchedulingComponentDetail>> constraintViolations
                = validator.validate(operationSchedulingDetail);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento por operação a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingComponentDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean addComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail)
            throws OperationSchedulingComponentDetailPersistenceException, ValidatorException {
        try {
            validateComponentDetail(operationSchedulingDetail);

            //Verifica se já não existe o registro
            if ( alreadyExistComponentDetail( operationSchedulingDetail ) ) {
                throw new OperationSchedulingComponentDetailPersistenceException( msgDetail.getAlreadyExist() );
            }

            return dao.addComponentDetail(operationSchedulingDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento por operação a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingComponentDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean modifyComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail)
            throws OperationSchedulingComponentDetailPersistenceException, ValidatorException {
        try {
            validateComponentDetail(operationSchedulingDetail);
            return dao.modifyComponentDetail(operationSchedulingDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail detalhe de sequenciamento por operação a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean removeComponentDetail(OperationSchedulingComponentDetail operationSchedulingDetail)
            throws OperationSchedulingComponentDetailPersistenceException {
        try {
            return dao.removeComponentDetail(operationSchedulingDetail);
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um detalhe de sequenciamento por operação no banco de dados.
     *
     * @param operationSchedulingDetail detalhe de sequenciamento por operação com os dados da chave primária
     * @return True se o detalhe de sequenciamento por operação já existe ou false se não existe
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public boolean alreadyExistComponentDetail( OperationSchedulingComponentDetail operationSchedulingDetail ) throws OperationSchedulingComponentDetailPersistenceException {
        try {
            return dao.alreadyExistComponentDetail(operationSchedulingDetail);
        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um sequenciamento por operação com o código informado.
     *
     * @param key A chave primária a ser procurada
     * @return O detalhe de sequenciamento por operação encontrada ou null se nada for encontrado
     * @throws OperationSchedulingComponentDetailPersistenceException
     */
    public OperationSchedulingComponentDetail getOperationSchedulingComponentDetail( OperationSchedulingComponentDetailKey key )
            throws OperationSchedulingComponentDetailPersistenceException {
        return dao.getOperationSchedulingComponentDetail(key);
    }

    //--------------------------------------------------------------------------
    //Métodos do SetDetail
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateSetDetail(OperationSchedulingSetDetail operationSchedulingDetail) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<OperationSchedulingSetDetail>> constraintViolations
                = validator.validate(operationSchedulingDetail);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento por operação a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingSetDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean addSetDetail(OperationSchedulingSetDetail operationSchedulingDetail)
            throws OperationSchedulingSetDetailPersistenceException, ValidatorException {
        try {
            validateSetDetail(operationSchedulingDetail);

            //Verifica se já não existe o registro
            if ( alreadyExistSetDetail( operationSchedulingDetail ) ) {
                throw new OperationSchedulingSetDetailPersistenceException( msgDetail.getAlreadyExist() );
            }

            return dao.addSetDetail(operationSchedulingDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail O detalhe de sequenciamento por operação a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationSchedulingSetDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean modifySetDetail(OperationSchedulingSetDetail operationSchedulingDetail)
            throws OperationSchedulingSetDetailPersistenceException, ValidatorException {
        try {
            validateSetDetail(operationSchedulingDetail);
            return dao.modifySetDetail(operationSchedulingDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um detalhe de sequenciamento por operação.
     *
     * @param operationSchedulingDetail detalhe de sequenciamento por operação a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean removeSetDetail(OperationSchedulingSetDetail operationSchedulingDetail)
            throws OperationSchedulingSetDetailPersistenceException {
        try {
            return dao.removeSetDetail(operationSchedulingDetail);
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um detalhe de sequenciamento por operação no banco de dados.
     *
     * @param operationSchedulingDetail detalhe de sequenciamento por operação com os dados da chave primária
     * @return True se o detalhe de sequenciamento por operação já existe ou false se não existe
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public boolean alreadyExistSetDetail( OperationSchedulingSetDetail operationSchedulingDetail ) throws OperationSchedulingSetDetailPersistenceException {
        try {
            return dao.alreadyExistSetDetail(operationSchedulingDetail);
        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um sequenciamento por operação com o código informado.
     *
     * @param key A chave primária a ser procurada
     * @return O detalhe de sequenciamento por operação encontrada ou null se nada for encontrado
     * @throws OperationSchedulingSetDetailPersistenceException
     */
    public OperationSchedulingSetDetail getOperationSchedulingSetDetail( OperationSchedulingSetDetailKey key )
            throws OperationSchedulingSetDetailPersistenceException {
        return dao.getOperationSchedulingSetDetail(key);
    }

    //--------------------------------------------------------------------------
    //Métodos do Sequenciamento
    //--------------------------------------------------------------------------
    /**
     * Cria a base de sequenciamento para cada operação em um determinado calendário
     * 
     * @param calendar O calendário para a criação do sequenciamento das operações
     * @throws OperationSchedulingPersistenceException
     * @throws ValidatorException
     */
    public void createYearOperationsScheduling( Calendar calendar ) throws OperationSchedulingPersistenceException, ValidatorException {
        try {
            //Pega a lista com todas as operações
            List<Operation> operations = operationFacade.list();

            //Percorre a lista de operações, pois deve criar o sequenciamento
            //para cada operação em todos os dias do ano
            for ( Operation o : operations ) {
                //Percorre cada dia do ano
                for ( Day day : calendar.getDays() ) {
                    //Cria a chave primária do sequenciamento por operação
                    OperationSchedulingKey osKey = new OperationSchedulingKey();
                    osKey.setOperation(o);
                    osKey.setDay(day);

                    //Cria o sequenciamento por operação com a chave criada
                    OperationScheduling os = new OperationScheduling();
                    os.setPrimaryKey(osKey);

                    //Adiciona o sequenciamento
                    add(os);
                }
            }

        } catch (OperationPersistenceException ex) {
            throw new OperationSchedulingPersistenceException(ex, ex.getMessage());
        }
    }

    /**
     * Cria o sequenciamento de base para determinada operação em determinado ano
     * 
     * @param operation A operação a ser sequenciada
     * @param year O ano em que será feito o sequenciamento
     * @throws OperationSchedulingPersistenceException
     * @throws ValidatorException
     */
    public void createCurrentYearOperationScheduling( Operation operation, Integer year ) throws OperationSchedulingPersistenceException, ValidatorException {
        try {
            if ( calendarFacade == null ) {
                calendarFacade = new CalendarFacade();
            }

            if ( year == null ) {
                DateTime dt = new DateTime();
                year = dt.getYear();
            }

            
            Calendar calendar = calendarFacade.getCalendar( year );

            //Percorre cada dia do ano
            for ( Day day : calendar.getDays() ) {
                //Cria a chave primária do sequenciamento por operação
                OperationSchedulingKey osKey = new OperationSchedulingKey();
                osKey.setOperation( operation );
                osKey.setDay(day);

                //Cria o sequenciamento por operação com a chave criada
                OperationScheduling os = new OperationScheduling();
                os.setPrimaryKey(osKey);

                //Adiciona o sequenciamento
                add(os);
            }

        } catch (CalendarPersistenceException ex) {
            throw new OperationSchedulingPersistenceException(ex, ex.getMessage());
        }
    }
}