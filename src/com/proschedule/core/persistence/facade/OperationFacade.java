package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.dao.OperationDAO;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.messages.OperationMessages;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Interface de comunicação com o  módulo de Operatione.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationFacade {
    private OperationDAO dao;
    private OperationSchedulingFacade operationSchedulingFacade;


    private Validator validator;
    private OperationMessages msg;

    /**
     * Construtor da Classe
     */
    public OperationFacade(){
        dao = new OperationDAO();
        validator = HibernateValidatorUtil.getValidator();
        msg = new OperationMessages();
    }

    /**
     * Valida um objeto operação.
     *
     * @param operation O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Operation operation) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<Operation>> constraintViolations
                = validator.validate(operation);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um operação.
     *
     * @param operation O operação a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationPersistenceException
     * @throws ValidatorException
     */
    public boolean add(Operation operation)
            throws OperationPersistenceException, ValidatorException {
        validate(operation);

        //Verifica se já não existe o registro
        if ( alreadyExist( operation ) ) {
            throw new OperationPersistenceException( msg.getAlreadyExist() );
        }

        boolean returnValue = dao.add(operation);

        if ( operationSchedulingFacade == null ) {
            operationSchedulingFacade = new OperationSchedulingFacade();
        }

        try {
            operationSchedulingFacade.createCurrentYearOperationScheduling(operation, null);
        } catch (OperationSchedulingPersistenceException ex) {
            throw new OperationPersistenceException( ex, ex.getMessage() );
        }

        return returnValue;
    }

    /**
     * Modifica um operação.
     *
     * @param operation O operação a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws OperationPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(Operation operation)
            throws OperationPersistenceException, ValidatorException {
        validate(operation);
        return dao.modify(operation);
    }

    /**
     * Remove um operação.
     *
     * @param operation operação a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws OperationPersistenceException
     */
    public boolean remove(Operation operation)
            throws OperationPersistenceException {
        try {
            if ( operationSchedulingFacade == null ) {
                operationSchedulingFacade = new OperationSchedulingFacade();
            }

            operationSchedulingFacade.removeAll(operation);
            return dao.remove(operation);
        } catch (OperationSchedulingPersistenceException ex) {
            throw new OperationPersistenceException(ex, ex.getMessage());
        }
    }

    /**
     * Lista todos os operaçãos.
     *
     * @return Lista dos operaçãos.
     * @throws OperationPersistenceException
     */
    public List<Operation> list() throws OperationPersistenceException {
        return dao.list();
    }

    /**
     * Devolve uma lista com todos operaçãos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de operaçãos ordenada
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , String order )
            throws OperationPersistenceException {
        return dao.list(field, order);
    }

    /**
     * Lista os operaçãos que estiverem de acordo com os parâmetros informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de operaçãos ou null se um operador inválido for informado
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , String value , String operator )
            throws OperationPersistenceException {
        return dao.list(field, value, operator);

    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informados.
     * É usada para campos do tipo inteiro.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationPersistenceException
     */
    public List<Operation> list( String field , Integer value , String operator )
            throws OperationPersistenceException {
        return dao.list(field, value, operator);
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informadas. É usada
     * para o campo de tipo de operação.
     *
     * @param value O tipo com a descrição preenchida a ser procurada
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationPersistenceException
     */
    public List<Operation> list( OperationType value , String operator )
            throws OperationPersistenceException {
        return dao.list(value, operator);
    }

    /**
     * Verifica se já existe um operação no banco de dados.
     *
     * @param operation operação com os dados da chave primária
     * @return True se o operação já existe ou false se não existe
     * @throws OperationPersistenceException
     */
    public boolean alreadyExist( Operation operation ) throws OperationPersistenceException {
        return dao.alreadyExist(operation);
    }

    /**
     * Verifica se existe outra operação com a mesma descrição.
     *
     * @param description A descrição a ser procurada
     * @return True se houver outro operação ou false se nada for econtrado.
     * @throws OperationPersistenceException
     */
    public boolean searchForDescription( String description ) throws OperationPersistenceException {
        return dao.searchForDescription(description);
    }

    /**
     * Recupera uma operação com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return A operação encontrada ou null se nada for encontrado
     * @throws OperationPersistenceException
     */
    public Operation getOperation( Integer id ) throws OperationPersistenceException {
        return dao.getOperation(id);
    }
}