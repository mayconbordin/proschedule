package com.proschedule.util.dao;

import com.proschedule.hibernate.util.HibernateUtil;
import org.hibernate.Session;

/**
 * Classe abstrata para classes DAO. Provê a session para
 * as operações com o Hibernate.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public abstract class AbstractDAO {
    /**
     * Hibernate Session
     */
    public Session session;

    /**
     * Construtor da Classe
     */
    public AbstractDAO() {
        session = HibernateUtil.getSession();
    }
}
