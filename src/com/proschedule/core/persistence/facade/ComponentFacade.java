package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.dao.ComponentDAO;
import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.messages.ComponentMessages;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
import com.proschedule.validator.util.HibernateValidatorUtil;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * Interface de comunicação com o  módulo de Componente.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentFacade {
    private ComponentDAO dao;
    private Validator validator;
    private ComponentMessages msg;

    /**
     * Construtor da Classe
     */
    public ComponentFacade(){
        dao = new ComponentDAO();
        validator = HibernateValidatorUtil.getValidator();
        msg = new ComponentMessages();
    }

    /**
     * Valida um objeto componente.
     *
     * @param component O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validate(Component component) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<Component>> constraintViolations
                = validator.validate(component);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um componente.
     *
     * @param component O componente a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws ComponentPersistenceException
     * @throws ValidatorException
     */
    public boolean add(Component component)
            throws ComponentPersistenceException, ValidatorException {
        try {
            validate(component);

            //Verifica se já não existe o registro
            if ( alreadyExist( component ) ) {
                throw new ComponentPersistenceException( msg.getAlreadyExist() );
            }

            return dao.add(component);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um componente.
     *
     * @param component O componente a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws ComponentPersistenceException
     * @throws ValidatorException
     */
    public boolean modify(Component component)
            throws ComponentPersistenceException, ValidatorException {
        try {
            validate(component);
            return dao.modify(component);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um componente.
     *
     * @param component componente a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws ComponentPersistenceException
     */
    public boolean remove(Component component)
            throws ComponentPersistenceException {
        try {
            return dao.remove(component);
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista todos os componentes.
     *
     * @return Lista dos componentes.
     * @throws ComponentPersistenceException
     */
    public List<Component> list() throws ComponentPersistenceException {
        try {
            return dao.list();
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista com todos componentes em ordem crescente ou decrescente
     * em relação a um campo informado.
     *
     * @param field O campo em que a ordenação se dará
     * @param order Ascendendente ou descendente. Valores: asc ou desc
     * @return Lista de componentes ordenada
     * @throws ComponentPersistenceException
     */
    public List<Component> list( String field , String order )
            throws ComponentPersistenceException {
        try {
            return dao.list(field, order);
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Lista os componentes que estiverem de acordo com os parâmetros informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de componentes ou null se um operador inválido for informado
     * @throws ComponentPersistenceException
     */
    public List<Component> list( String field , String value , String operator )
            throws ComponentPersistenceException {
        try {
            return dao.list(field, value, operator);
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Devolve uma lista dos detalhes do componente dentro dos parâmetros
     * informados.
     *
     * @param value O componente a ser pesquisado
     * @param field O campo a ser ordenado
     * @param order A ordenação dos registros. Valores permitidos: asc, desc
     * @return Lista de detalhes de componente ou null se um operador inválido for informado
     * @throws ComponentDetailPersistenceException
     */
    public List<ComponentDetail> listDetails( Component value, String field, String order ) throws ComponentDetailPersistenceException {
        try {
            return dao.listDetails(value, field, order);
        } catch (ComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um componente no banco de dados.
     *
     * @param component componente com os dados da chave primária
     * @return True se o componente já existe ou false se não existe
     * @throws ComponentPersistenceException
     */
    public boolean alreadyExist( Component component ) throws ComponentPersistenceException {
        try {
            return dao.alreadyExist(component);
        } catch (ComponentPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param id O código a ser buscado no banco de dados
     * @return O componente encontrada ou null se nada for encontrado
     * @throws ComponentPersistenceException
     */
    public Component getComponent( String id ) throws ComponentPersistenceException {
        return dao.getComponent(id);
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Valida um objeto detalhe de componente.
     *
     * @param componentDetail O objeto a ser validado
     * @throws ValidatorException A exceção lançada em caso de dados inválidos
     */
    public void validateDetail(ComponentDetail componentDetail) throws ValidatorException {
        //Valida o objeto e recebe os resultados
        Set<ConstraintViolation<ComponentDetail>> constraintViolations
                = validator.validate(componentDetail);

        if ( constraintViolations.size() > 0 ) {
            throw new ValidatorException( constraintViolations );
        }
    }

    /**
     * Adiciona um detalhe de componente.
     *
     * @param componentDetail O detalhe de componente a ser adicionado.
     * @return True se a operação for realizada com sucesso.
     * @throws ComponentDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean addDetail(ComponentDetail componentDetail)
            throws ComponentDetailPersistenceException, ValidatorException {
        try {
            validateDetail(componentDetail);

            //Verifica se já não existe o registro
            if ( alreadyExistDetail( componentDetail ) ) {
                throw new ComponentDetailPersistenceException( msg.getAlreadyExist() );
            }

            return dao.addDetail(componentDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (ComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Modifica um detalhe de componente.
     *
     * @param componentDetail O detalhe de componente a ser modificado.
     * @return True se a operação for realizada com sucesso.
     * @throws ComponentDetailPersistenceException
     * @throws ValidatorException
     */
    public boolean modifyDetail(ComponentDetail componentDetail)
            throws ComponentDetailPersistenceException, ValidatorException {
        try {
            validateDetail(componentDetail);
            return dao.modifyDetail(componentDetail);
        } catch (ValidatorException ex) {
            throw ex;
        } catch (ComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Remove um detalhe de componente.
     *
     * @param componentDetail detalhe de componente a ser removido.
     * @return True se a operação for bem sucedida.
     * @throws ComponentDetailPersistenceException
     */
    public boolean removeDetail(ComponentDetail componentDetail)
            throws ComponentDetailPersistenceException {
        try {
            return dao.removeDetail(componentDetail);
        } catch (ComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Verifica se já existe um detalhe de componente no banco de dados.
     *
     * @param componentDetail detalhe de componente com os dados da chave primária
     * @return True se o detalhe de componente já existe ou false se não existe
     * @throws ComponentDetailPersistenceException
     */
    public boolean alreadyExistDetail( ComponentDetail componentDetail ) throws ComponentDetailPersistenceException {
        try {
            return dao.alreadyExistDetail(componentDetail);
        } catch (ComponentDetailPersistenceException ex) {
            throw ex;
        }
    }

    /**
     * Recupera um conjunto com o código informado.
     *
     * @param key A chave primária do conjunto a ser procurado
     * @return O detalhe de componente encontrada ou null se nada for encontrado
     * @throws ComponentDetailPersistenceException
     */
    public ComponentDetail getComponentDetail( ComponentDetailKey key )
            throws ComponentDetailPersistenceException {
        return dao.getComponentDetail(key);
    }
}