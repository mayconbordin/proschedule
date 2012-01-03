/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.core.persistence.model.OperationType;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maycon Bordin
 */
public class OperationTypeDAOTest {

    private OperationTypeDAO dao;

    public OperationTypeDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new OperationTypeDAO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class OperationTypeDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        OperationType operationType = new OperationType();
        operationType.setDescription("Teste");

        try {
            boolean result = dao.add(operationType);
            assertEquals(true, result);
        } catch (OperationTypePersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modify method, of class OperationTypeDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        try {
            OperationType o = dao.list("description", "Teste", "=").get(0);
            o.setDescription("TESTE");

            try {
                boolean result = dao.modify(o);
                assertEquals(true, result);
            } catch (OperationTypePersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex);
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Nenhum resultado retornado." + "\n" + ex);
        }
    }

    /**
     * Test of list method, of class OperationTypeDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<OperationType> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationTypePersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationTypeDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<OperationType> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationTypePersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationTypeDAO.
     */
    @Test
    public void testList_3args_1() throws Exception {
        System.out.println("list");

        try {
            List<OperationType> list = dao.list("description", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OperationTypePersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationTypeDAO.
     */
    @Test
    public void testList_3args_2() throws Exception {
        System.out.println("list");

        try {
            int id = dao.list("description", "TESTE", "=").get(0).getId();

            try {
                List<OperationType> list = dao.list("id", id, "=");

                if ( list.size() > 0 ) {
                    assertTrue(true);
                } else {
                    fail("A lista deveria ter 1 registro.");
                }
            } catch ( OperationTypePersistenceException e ) {
                fail(e.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class OperationTypeDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        try {
            int id = dao.list("description", "TESTE", "=").get(0).getId();

            OperationType c = new OperationType();
            c.setId( id );

            try {
                boolean result = dao.alreadyExist(c);
                assertEquals(true, result);
            } catch ( OperationTypePersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

    /**
     * Test of searchForDescription method, of class OperationTypeDAO.
     */
    @Test
    public void testSearchForDescription() throws Exception {
        System.out.println("searchForDescription");

        try {
            boolean result = dao.searchForDescription("TESTE");
            assertEquals(true, result);
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of remove method, of class OperationTypeDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            OperationType c = dao.list("description", "TESTE", "=").get(0);

            try {
                boolean result = dao.remove(c);
                assertEquals(true, result);
            } catch (OperationTypePersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

}