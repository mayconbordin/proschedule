package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.dao.OperationTypeDAO;
import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.core.persistence.messages.OperationTypeMessages;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Interface de comunicação com o  módulo de OperationType.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class OperationTypeFacade {
    private OperationTypeDAO dao;
    private Validator validator;
    private OperationTypeMessages msg;

    /**
     * Construtor da Classe
     */
    public OperationTypeFacade(){
        dao = new OperationTypeDAO();
        validator = HibernateValidatorUtil.getValidator();
        msg = new OperationTypeMessages();
    }

    /**
     * Valida um objeto tipo de operação.
     *
     * @param operationType O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(OperationType operationType) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<OperationType>> constraintViolations
                = validator.validate(operationType);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um tipo de operação.
     *
     * @param operationType O tipo de operação a ser adicionado.
     * @return True se a tipo de operação for realizada com sucesso.
     * @throws OperationTypePersistenceException
     * @throws ValidatorException
     */
    public boolean add(OperationType operationType)
            throws OperationTypePersistenceException, ValidatorException {
        try {
            validate(operationType);

            //Verifica se já não existe o registro
            if ( alreadyExist( operationType ) ) {
                throw new OperationTypePersistenceException( msg.getAlreadyExist() );
            }

            return dao.add(operationType);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um tipo de operação.
     *
     * @param operationType O tipo de operação a ser modificado.
     * @return True se a tipo de operação for realizada com sucesso.
     * @throws OperationTypePersistenceException
     * @throws ValidatorException
     */
    public boolean modify(OperationType operationType)
            throws OperationTypePersistenceException, ValidatorException {
        try {
            validate(operationType);
            return dao.modify(operationType);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um tipo de operação.
     *
     * @param operationType tipo de operação a ser removido.
     * @return True se a tipo de operação for bem sucedida.
     * @throws OperationTypePersistenceException
     */
    public boolean remove(OperationType operationType)
            throws OperationTypePersistenceException {
        try {
            return dao.remove(operationType);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os tipo de operaçãos.
     *
     * @return Lista dos tipo de operaçãos.
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list() throws OperationTypePersistenceException {
        try {
            return dao.list();
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista com todos tipo de operaçãos em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de tipo de operaçãos ordenada
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , String order )
            throws OperationTypePersistenceException {
        try {
            return dao.list(field, order);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista os tipo de operaçãos que estiverem de acordo com os parâmetros informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de tipo de operaçãos ou null se um operador inválido for informado
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , String value , String operator )
            throws OperationTypePersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista das operações dentro dos parâmetros informados.
     * É usada para campos do tipo inteiro.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, >, <, >=, <=
     * @return Lista de operações ou null se um operador inválido for informado
     * @throws OperationTypePersistenceException
     */
    public List<OperationType> list( String field , Integer value , String operator )
            throws OperationTypePersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um tipo de operação no banco de dados.
     *
     * @param operationType tipo de operação com os dados da chave primária
     * @return True se o tipo de operação já existe ou false se não existe
     * @throws OperationTypePersistenceException
     */
    public boolean alreadyExist( OperationType operationType ) throws OperationTypePersistenceException {
        try {
            return dao.alreadyExist(operationType);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se existe outro tipo de operação com a mesma descrição.
     *
     * @param description A descrição a ser procurada
     * @return True se houver outro tipo de operação ou false se nada for econtrado.
     * @throws OperationTypePersistenceException
     */
    public boolean searchForDescription( String description ) throws OperationTypePersistenceException {
        try {
            return dao.searchForDescription(description);
        } catch (OperationTypePersistenceException ex) {
            throw ex;
        }
    }
}