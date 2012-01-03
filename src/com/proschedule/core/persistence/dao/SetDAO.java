package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.messages.SetComponentMessages;
import com.proschedule.core.persistence.messages.SetDetailMessages;
import com.proschedule.core.persistence.messages.SetMessages;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
import com.proschedule.util.dao.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para Conjunto.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SetDAO extends AbstractDAO {
    private SetMessages msg = new SetMessages();
    private SetDetailMessages msgDetail = new SetDetailMessages();
    private SetComponentMessages msgComponent = new SetComponentMessages();

    /**
     * Adicionar um novo conjunto
     *
     * @param set O conjunto que será adicionado
     * @return True se a operação for bem sucedida
     * @throws SetPersistenceException
     */
    public boolean add(Set set) throws SetPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(set);
            t.commit();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um conjunto existente
     *
     * @param set O conjunto que será modificado
     * @return True se a operação for bem sucedida
     * @throws SetPersistenceException
     */
    public boolean modify(Set set) throws SetPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(set);
            t.commit();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um conjunto existente
     *
     * @param Set O conjunto que será removido
     * @return True se a operação for bem sucedida
     * @throws SetPersistenceException
     */
    public boolean remove(Set Set) throws SetPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(Set);
            t.commit();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os conjuntos
     *
     * @return Lista de conjuntos
     * @throws SetPersistenceException
     */
    public List<Set> list() throws SetPersistenceException {
        try {
            Criteria select = session.createCriteria( Set.class );
            return select.list();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getListError());
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
    public List<Set> list( String field , String order ) throws SetPersistenceException {
        try {
            Criteria select = session.createCriteria( Set.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos conjuntos dentro dos parâmetros
     * informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de conjuntos ou null se um operador inválido for informado
     * @throws SetPersistenceException
     */
    public List<Set> list( String field , String value , String operator ) throws SetPersistenceException {
        try {
            Criteria select = session.createCriteria( Set.class );

            if ( operator.equals("=") ) {
                select.add( Restrictions.eq(field, value) );
            } else if ( operator.equals("") ) {
                select.add( Restrictions.ne(field, value) );
            } else if ( operator.equals("like") ) {
                select.add( Restrictions.like(field, value) );
            } else {
                return null;
            }

            return select.list();
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getListError());
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
            Criteria select = session.createCriteria( SetDetail.class );
            select.add( Expression.eq("primaryKey.set", value) );

            if ( order.equals("asc") ) {
                select.addOrder( org.hibernate.criterion.Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( org.hibernate.criterion.Order.desc( field ) );
            }

            return select.list();
        } catch (HibernateException e) {
            throw new SetDetailPersistenceException(e, msgDetail.getListError());
        } catch (Exception e) {
            throw new SetDetailPersistenceException(e, msgDetail.getListError());
        }
    }

    /**
     * Verifica se um conjunto já existe com base na chave primária.
     *
     * @param set O conjunto com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro conjunto ou false se nada for
     * econtrado.
     * @throws SetPersistenceException
     */
    public boolean alreadyExist( Set set ) throws SetPersistenceException {
        try {
            Criteria select = session.createCriteria( Set.class )
                    .add( Restrictions.eq("id", set.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new SetPersistenceException(e, msg.getAlreadyExist());
        } catch (Exception e) {
            throw new SetPersistenceException(e, msg.getAlreadyExist());
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
            Set o = (Set) session.get(Set.class, id);

            if ( o == null ) {
                throw new SetPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Adicionar um novo detalhe de conjunto
     *
     * @param setDetail O detalhe de conjunto que será adicionado
     * @return True se a operação for bem sucedida
     * @throws SetDetailPersistenceException
     */
    public boolean addDetail(SetDetail setDetail) throws SetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(setDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new SetDetailPersistenceException(e, msgDetail.getAddError());
        } catch (Exception e) {
            throw new SetDetailPersistenceException(e, msgDetail.getAddError());
        }
        return true;
    }

    /**
     * Modificar um detalhe de conjunto existente
     *
     * @param setDetail O detalhe de conjunto que será modificado
     * @return True se a operação for bem sucedida
     * @throws SetDetailPersistenceException
     */
    public boolean modifyDetail(SetDetail setDetail) throws SetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(setDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new SetDetailPersistenceException(e, msgDetail.getModifyError());
        } catch (Exception e) {
            throw new SetDetailPersistenceException(e, msgDetail.getModifyError());
        }
        return true;
    }

    /**
     * Remover um detalhe de conjunto existente
     *
     * @param setDetail O detalhe de conjunto que será removido
     * @return True se a operação for bem sucedida
     * @throws SetDetailPersistenceException
     */
    public boolean removeDetail(SetDetail setDetail) throws SetDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(setDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new SetDetailPersistenceException(e, msgDetail.getRemoveError());
        } catch (Exception e) {
            throw new SetDetailPersistenceException(e, msgDetail.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um detalhe de conjunto já existe com base na chave primária.
     *
     * @param setDetail O detalhe de conjunto com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro detalhe de conjunto ou false se nada for
     * econtrado.
     * @throws SetDetailPersistenceException
     */
    public boolean alreadyExistDetail( SetDetail setDetail ) throws SetDetailPersistenceException {
        try {
            SetDetail o = (SetDetail) session.get(SetDetail.class, setDetail.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new SetDetailPersistenceException(e, msgDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new SetDetailPersistenceException(e, msgDetail.getAlreadyExist());
        }
    }

    /**
     * Recupera um detalhe do conjunto com o código informado.
     *
     * @param key chave para buscar o detalhe de conjunto
     * @return O detalhe do conjunto encontrado ou null se nada for encontrado
     * @throws SetDetailPersistenceException
     */
    public SetDetail getSetDetail( SetDetailKey key ) throws SetDetailPersistenceException {
            SetDetail o = (SetDetail) session.get(SetDetail.class, key);

            if ( o == null ) {
                throw new SetDetailPersistenceException(msgDetail.getNotFoundError());
            } else {
                return o;
            }
    }

    //--------------------------------------------------------------------------
    //Métodos do Component
    //--------------------------------------------------------------------------
    /**
     * Adicionar um componente detalhe de conjunto
     *
     * @param setComponent O componente de conjunto que será adicionado
     * @return True se a operação for bem sucedida
     * @throws SetComponentPersistenceException
     */
    public boolean addComponent(SetComponent setComponent) throws SetComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(setComponent);
            t.commit();
        } catch (HibernateException e) {
            throw new SetComponentPersistenceException(e, msgComponent.getAddError());
        } catch (Exception e) {
            throw new SetComponentPersistenceException(e, msgComponent.getAddError());
        }
        return true;
    }

    /**
     * Modificar um componente de conjunto existente
     *
     * @param setComponent O componente de conjunto que será modificado
     * @return True se a operação for bem sucedida
     * @throws SetComponentPersistenceException
     */
    public boolean modifyComponent(SetComponent setComponent) throws SetComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(setComponent);
            t.commit();
        } catch (HibernateException e) {
            throw new SetComponentPersistenceException(e, msgComponent.getModifyError());
        } catch (Exception e) {
            throw new SetComponentPersistenceException(e, msgComponent.getModifyError());
        }
        return true;
    }

    /**
     * Remover um componente de conjunto existente
     *
     * @param setComponent O componente de conjunto que será removido
     * @return True se a operação for bem sucedida
     * @throws SetComponentPersistenceException 
     */
    public boolean removeComponent(SetComponent setComponent) throws SetComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(setComponent);
            t.commit();
        } catch (HibernateException e) {
            throw new SetComponentPersistenceException(e, msgComponent.getRemoveError());
        } catch (Exception e) {
            throw new SetComponentPersistenceException(e, msgComponent.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um componente de conjunto já existe com base na chave primária.
     *
     * @param setComponent O componente de conjunto com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro componente de conjunto ou false se nada for
     * econtrado.
     * @throws SetComponentPersistenceException
     */
    public boolean alreadyExistComponent( SetComponent setComponent ) throws SetComponentPersistenceException {
        try {
            SetComponent o = (SetComponent) session.get(SetComponent.class, setComponent.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new SetComponentPersistenceException(e, msgComponent.getAlreadyExist());
        } catch (Exception e) {
            throw new SetComponentPersistenceException(e, msgComponent.getAlreadyExist());
        }
    }

    /**
     * Recupera um componente do conjunto com o código informado.
     *
     * @param key chave do componente de conjunto
     * @return O componente do conjunto encontrado ou null se nada for encontrado
     * @throws SetComponentPersistenceException
     */
    public SetComponent getSetComponent( SetComponentKey key ) throws SetComponentPersistenceException {
            SetComponent o = (SetComponent) session.get(SetComponent.class, key);

            if ( o == null ) {
                throw new SetComponentPersistenceException(msgComponent.getNotFoundError());
            } else {
                return o;
            }
    }
}