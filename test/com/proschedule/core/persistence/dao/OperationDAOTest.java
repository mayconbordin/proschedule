package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.core.persistence.model.Operation;
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
public class OperationDAOTest {

    private OperationDAO dao;
    private Operation operation;

    public OperationDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new OperationDAO();

        operation = new Operation();
        operation.setDescription("Teste");

        //A princípio existe dois tipos de operações distintas
        //Já estão gravadas no banco de dados.
        //1 é para conjuntos
        OperationType type = new OperationType();
        type.setId(1);

        operation.setType(type);

        //Lead Time
        LeadTime lt = new LeadTime();
        lt.setType("dias");
        lt.setValue(10.0);

        operation.setLeadTime(lt);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class OperationDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            boolean result = dao.add(operation);
            assertEquals(true, result);
        } catch (OperationPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modify method, of class OperationDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        try {
            Operation o = dao.list("description", "Teste", "=").get(0);

            o.setDescription("TESTE");

            OperationType type = new OperationType();
            type.setId(2);

            o.setType(type);

            try {
                boolean result = dao.modify(o);
                assertEquals(true, result);
            } catch (OperationPersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex);
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Nenhum resultado retornado." + "\n" + ex);
        }
    }

    /**
     * Test of list method, of class OperationDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Operation> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Operation> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationDAO.
     */
    @Test
    public void testList_String_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Operation> list = dao.list("description", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OperationPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationDAO.
     */
    @Test
    public void testList_String_Integer_String() throws Exception {
        System.out.println("list");

        try {
            int id = dao.list("description", "TESTE", "=").get(0).getId();

            try {
                List<Operation> list = dao.list("id", id, "=");

                if ( list.size() > 0 ) {
                    assertTrue(true);
                } else {
                    fail("A lista deveria ter 1 registro.");
                }
            } catch ( OperationPersistenceException e ) {
                fail(e.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class OperationDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        try {
            int id = dao.list("description", "TESTE", "=").get(0).getId();

            Operation c = new Operation();
            c.setId( id );

            try {
                boolean result = dao.alreadyExist(c);
                assertEquals(true, result);
            } catch ( OperationPersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class OperationDAO.
     */
    @Test
    public void testSearchForDescription() throws Exception {
        System.out.println("alreadyExist");

        try {
            boolean result = dao.searchForDescription("TESTE");
            assertEquals(true, result);
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOperation method, of class OperationDAO.
     */
    @Test
    public void testeGetOperation() throws Exception {
        System.out.println("getOperation");

        try {
            Operation c = dao.list("description", "TESTE", "=").get(0);

            try {
                Operation o = dao.getOperation( c.getId() );
                assertEquals(c.getDescription(), o.getDescription());
            } catch (OperationPersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }
    
    /**
     * Test of remove method, of class OperationDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            Operation c = dao.list("description", "TESTE", "=").get(0);

            try {
                boolean result = dao.remove(c);
                assertEquals(true, result);
            } catch (OperationPersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }
}