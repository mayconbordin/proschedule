package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.messages.ComponentDetailMessages;
import com.proschedule.core.persistence.messages.ComponentMessages;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
import com.proschedule.util.dao.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Operações de banco de dados para Componente.
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ComponentDAO extends AbstractDAO {

    private ComponentMessages msg = new ComponentMessages();
    private ComponentDetailMessages msgDetail = new ComponentDetailMessages();

    /**
     * Adicionar um novo componente
     *
     * @param component O componente que será adicionado
     * @return True se a operação for bem sucedida
     * @throws ComponentPersistenceException
     */
    public boolean add(Component component) throws ComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(component);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getAddError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getAddError());
        }
        return true;
    }

    /**
     * Modificar um componente existente
     *
     * @param component O componente que será modificado
     * @return True se a operação for bem sucedida
     * @throws ComponentPersistenceException
     */
    public boolean modify(Component component) throws ComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(component);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getModifyError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getModifyError());
        }
        return true;
    }

    /**
     * Remover um componente existente
     *
     * @param Component O componente que será removido
     * @return True se a operação for bem sucedida
     * @throws ComponentPersistenceException
     */
    public boolean remove(Component Component) throws ComponentPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(Component);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getRemoveError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getRemoveError());
        }
        return true;
    }

    /**
     * Devolve uma lista com todos os componentes
     *
     * @return Lista de componentes
     * @throws ComponentPersistenceException
     */
    public List<Component> list() throws ComponentPersistenceException {
        try {
            Criteria select = session.createCriteria( Component.class );
            return select.list();
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getListError());
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
    public List<Component> list( String field , String order ) throws ComponentPersistenceException {
        try {
            Criteria select = session.createCriteria( Component.class );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }
            return select.list();
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Devolve uma lista dos componentes dentro dos parâmetros
     * informados.
     *
     * @param field O campo a ser pesquisado
     * @param value O termo a ser pesquisado no campo.
     * @param operator O operador lógico. Valores permitidos: =, <>, like
     * @return Lista de componentes ou null se um operador inválido for informado
     * @throws ComponentPersistenceException
     */
    public List<Component> list( String field , String value , String operator ) throws ComponentPersistenceException {
        try {
            Criteria select = session.createCriteria( Component.class );

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
            throw new ComponentPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getListError());
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
            Criteria select = session.createCriteria( ComponentDetail.class );
            select.add( Expression.eq("primaryKey.component", value) );

            if ( order.equals("asc") ) {
                select.addOrder( Order.asc( field ) );
            } else if ( order.equals("desc") ) {
                select.addOrder( Order.desc( field ) );
            }

            return select.list();
        } catch (HibernateException e) {
            throw new ComponentDetailPersistenceException(e, msg.getListError());
        } catch (Exception e) {
            throw new ComponentDetailPersistenceException(e, msg.getListError());
        }
    }

    /**
     * Verifica se um componente já existe com base na chave primária.
     *
     * @param component O componente com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro componente ou false se nada for
     * econtrado.
     * @throws ComponentPersistenceException
     */
    public boolean alreadyExist( Component component ) throws ComponentPersistenceException {
        try {
            Criteria select = session.createCriteria( Component.class )
                    .add( Restrictions.eq("id", component.getId()) );

            if ( select.list().size() > 0 ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new ComponentPersistenceException(e, msg.getAlreadyExist());
        } catch (Exception e) {
            throw new ComponentPersistenceException(e, msg.getAlreadyExist());
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
            Component o = (Component) session.get(Component.class, id);

            if ( o == null ) {
                throw new ComponentPersistenceException(msg.getNotFoundError());
            } else {
                return o;
            }
    }

    //--------------------------------------------------------------------------
    //Métodos do Detail
    //--------------------------------------------------------------------------
    /**
     * Adicionar um novo componentDetaile
     *
     * @param componentDetail O componentDetaile que será adicionado
     * @return True se a operação for bem sucedida
     * @throws ComponentDetailPersistenceException
     */
    public boolean addDetail(ComponentDetail componentDetail) throws ComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.save(componentDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getAddError());
        } catch (Exception e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getAddError());
        }
        return true;
    }

    /**
     * Modificar um componentDetaile existente
     *
     * @param componentDetail O componentDetaile que será modificado
     * @return True se a operação for bem sucedida
     * @throws ComponentDetailPersistenceException
     */
    public boolean modifyDetail(ComponentDetail componentDetail) throws ComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.merge(componentDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getModifyError());
        } catch (Exception e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getModifyError());
        }
        return true;
    }

    /**
     * Remover um componentDetaile existente
     *
     * @param ComponentDetail O componentDetaile que será removido
     * @return True se a operação for bem sucedida
     * @throws ComponentDetailPersistenceException
     */
    public boolean removeDetail(ComponentDetail ComponentDetail) throws ComponentDetailPersistenceException {
        try {
            Transaction t = session.beginTransaction();
            session.delete(ComponentDetail);
            t.commit();
        } catch (HibernateException e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getRemoveError());
        } catch (Exception e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getRemoveError());
        }
        return true;
    }

    /**
     * Verifica se um detalhe de componente já existe com base na chave primária.
     *
     * @param componentDetail O detalhe de componente com os dados da chave primária a
     * ser procurada.
     * @return True se houver outro detalhe de componente ou false se nada for
     * econtrado.
     * @throws ComponentDetailPersistenceException
     */
    public boolean alreadyExistDetail( ComponentDetail componentDetail ) throws ComponentDetailPersistenceException {
        try {
            ComponentDetail o = (ComponentDetail) session.get(ComponentDetail.class, componentDetail.getPrimaryKey());

            if ( o != null ) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getAlreadyExist());
        } catch (Exception e) {
            throw new ComponentDetailPersistenceException(e, msgDetail.getAlreadyExist());
        }
    }

    /**
     * Recupera um detalhe do componente com o código informado.
     *
     * @param key chave do detalhe de componente a ser procurado
     * @return O detalhe do componente encontrado ou null se nada for encontrado
     * @throws ComponentDetailPersistenceException
     */
    public ComponentDetail getComponentDetail( ComponentDetailKey key ) throws ComponentDetailPersistenceException {
            ComponentDetail o = (ComponentDetail) session.get(ComponentDetail.class, key);

            if ( o == null ) {
                throw new ComponentDetailPersistenceException(msgDetail.getNotFoundError());
            } else {
                return o;
            }
    }
}