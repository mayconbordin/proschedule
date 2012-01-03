package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.dao.SetDAO;
import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.messages.SetMessages;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Interface de comunicação com o  módulo de Conjunto.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetFacade {
    private SetDAO dao;
    private Validator validator;
    private SetMessages msg;

    /**
     * Construtor da Classe
     */
    public SetFacade(){
        dao = new SetDAO();
        validator = HibernateValidatorUtil.getValidator();
        msg = new SetMessages();
    }

    /**
     * Valida um objeto conjunto.
     *
     * @param set O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Set set) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<Set>> constraintViolations
                = validator.validate(set);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um conjunto.
     *
     * @param set O conjunto a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetPersistenceException
     * @throws ValidatorException
     */
    public boolean add(Set set)
            throws SetPersistenceException, ValidatorException {
        try {
            validate(set);

            //Verifica se já não existe o registro
            if ( alreadyExist( set ) ) {
                throw new SetPersistenceException( msg.getAlreadyExist() );
            }

            return dao.add(set);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um conjunto.
     *
     * @param set O conjunto a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(Set set)
            throws SetPersistenceException, ValidatorException {
        try {
            validate(set);
            return dao.modify(set);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um conjunto.
     *
     * @param set conjunto a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws SetPersistenceException
     */
    public boolean remove(Set set)
            throws SetPersistenceException {
        try {
            return dao.remove(set);
        } catch (SetPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os conjuntos.
     *
     * @return Lista dos conjuntos.
     * @throws SetPersistenceException
     */
    public List<Set> list() throws SetPersistenceException {
        try {
            return dao.list();
        } catch (SetPersistenceException ex) {
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
     * @throws SetPersistenceException
     */
    public List<Set> list( String field , String order )
            throws SetPersistenceException {
        try {
            return dao.list(field, order);
        } catch (SetPersistenceException ex) {
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
     * @throws SetPersistenceException
     */
    public List<Set> list( String field , String value , String operator )
            throws SetPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (SetPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes do conjunto dentro dos parâmetros
     * informados.
     *
     * @param value O conjunto a ser pesquisado
     * @param field O campo a ser ordenado
     * @param order A ordenação dos registros. Valores permitidos: asc, desc
     * @return Lista de detalhes do conjunto ou null se um operador inválido for informado
     * @throws SetDetailPersistenceException
     */
    public List<SetDetail> listDetails( Set value, String field, String order ) throws SetDetailPersistenceException {
        try {
            return dao.listDetails(value, field, order);
        } catch (SetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um conjunto no banco de dados.
     *
     * @param set conjunto com os dados da chave primária
     * @return True se o conjunto já existe ou false se não existe
     * @throws SetPersistenceException
     */
    public boolean alreadyExist( Set set ) throws SetPersistenceException {
        try {
            return dao.alreadyExist(set);
        } catch (SetPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return O conjunto encontrada ou null se nada for encontrado
     * @throws SetPersistenceException
     */
    public Set getSet( String id ) throws SetPersistenceException {
        return dao.getSet(id);
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto detalhe de conjunto.
     *
     * @param setDetail O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateDetail(SetDetail setDetail) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<SetDetail>> constraintViolations
                = validator.validate(setDetail);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um detalhe de conjunto.
     *
     * @param setDetail O detalhe de conjunto a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean addDetail(SetDetail setDetail)
            throws SetDetailPersistenceException, ValidatorException {
        try {
            validateDetail(setDetail);

            //Verifica se já não existe o registro
            if ( alreadyExistDetail( setDetail ) ) {
                throw new SetDetailPersistenceException( msg.getAlreadyExist() );
            }

            return dao.addDetail(setDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um detalhe de conjunto.
     *
     * @param setDetail O detalhe de conjunto a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean modifyDetail(SetDetail setDetail)
            throws SetDetailPersistenceException, ValidatorException {
        try {
            validateDetail(setDetail);
            return dao.modifyDetail(setDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um detalhe de conjunto.
     *
     * @param setDetail detalhe de conjunto a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws SetDetailPersistenceException
     */
    public boolean removeDetail(SetDetail setDetail)
            throws SetDetailPersistenceException {
        try {
            return dao.removeDetail(setDetail);
        } catch (SetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um detalhe de conjunto no banco de dados.
     *
     * @param setDetail detalhe de conjunto com os dados da chave primária
     * @return True se o detalhe de conjunto já existe ou false se não existe
     * @throws SetDetailPersistenceException
     */
    public boolean alreadyExistDetail( SetDetail setDetail ) throws SetDetailPersistenceException {
        try {
            return dao.alreadyExistDetail(setDetail);
        } catch (SetDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param key a chave primária do conjunto a ser procurado
     * @return O detalhe de conjunto encontrada ou null se nada for encontrado
     * @throws SetDetailPersistenceException
     */
    public SetDetail getSetDetail( SetDetailKey key )
            throws SetDetailPersistenceException {
        return dao.getSetDetail(key);
    }

    //--------------------------------------------------------------------------
    //Métodos de Component
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto componente de conjunto.
     *
     * @param setComponent O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateComponent(SetComponent setComponent) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        java.util.Set<ConstraintViolation<SetComponent>> constraintViolations
                = validator.validate(setComponent);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um componente de conjunto.
     *
     * @param setComponent O componente de conjunto a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetComponentPersistenceException
     * @throws ValidatorException
     */
    public boolean addComponent(SetComponent setComponent)
            throws SetComponentPersistenceException, ValidatorException {
        try {
            validateComponent(setComponent);

            //Verifica se já não existe o registro
            if ( alreadyExistComponent( setComponent ) ) {
                throw new SetComponentPersistenceException( msg.getAlreadyExist() );
            }

            return dao.addComponent(setComponent);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um componente de conjunto.
     *
     * @param setComponent O componente de conjunto a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws SetComponentPersistenceException
     * @throws ValidatorException
     */
    public boolean modifyComponent(SetComponent setComponent)
            throws SetComponentPersistenceException, ValidatorException {
        try {
            validateComponent(setComponent);
            return dao.modifyComponent(setComponent);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (SetComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um componente de conjunto.
     *
     * @param setComponent componente de conjunto a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws SetComponentPersistenceException
     */
    public boolean removeComponent(SetComponent setComponent)
            throws SetComponentPersistenceException {
        try {
            return dao.removeComponent(setComponent);
        } catch (SetComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um componente de conjunto no banco de dados.
     *
     * @param setComponent componente de conjunto com os dados da chave primária
     * @return True se o componente de conjunto já existe ou false se não existe
     * @throws SetComponentPersistenceException
     */
    public boolean alreadyExistComponent( SetComponent setComponent )
            throws SetComponentPersistenceException {
        try {
            return dao.alreadyExistComponent(setComponent);
        } catch (SetComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um componente de conjunto com o código informado.
     *
     * @param key A chave primária do componente a ser recuperado
     * @return O componente de conjunto encontrada ou null se nada for encontrado
     * @throws SetComponentPersistenceException
     */
    public SetComponent getSetComponent( SetComponentKey key )
            throws SetComponentPersistenceException {
        return dao.getSetComponent(key);
    }
}