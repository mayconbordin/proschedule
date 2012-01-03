package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.exceptions.OperationTypePersistenceException;
import com.proschedule.validator.util.ValidatorException;
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
public class OperationTypeFacadeTest {
    private OperationTypeFacade facade;

    public OperationTypeFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new OperationTypeFacade();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class OperationTypeFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        OperationType o = new OperationType();
        o.setDescription("TESTE");

        //Valida um componente com valores válidos
        try {
            facade.validate(o);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida componente inválido
        //Ele não pode passar no teste
        OperationType c = new OperationType();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class OperationTypeFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        OperationType o = new OperationType();
        o.setDescription("TESTE");

        try {
            facade.add(o);
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class OperationTypeFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        try {
            OperationType o = facade.list("description", "TESTE", "=").get(0);

            o.setDescription("TESTANDO");

            try {
                facade.modify(o);
            } catch ( OperationTypePersistenceException ex ) {
                fail( ex.getDetailMessage() );
            } catch ( ValidatorException ex ) {
                fail("Erro ao validar objeto.");
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of list method, of class OperationTypeFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<OperationType> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OperationTypeFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<OperationType> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OperationTypeFacade.
     */
    @Test
    public void testList_3args_1() throws Exception {
        System.out.println("list");

        int expected = 1;

        try {
            List<OperationType> list = facade.list("description", "TESTANDO", "=");
            assertEquals(expected, list.size());
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OperationTypeFacade.
     */
    @Test
    public void testList_3args_2() throws Exception {
        System.out.println("list");

        try {
            OperationType o = facade.list("description", "TESTANDO", "=").get(0);

            int expected = 1;
            int id = o.getId();

            try {
                List<OperationType> list = facade.list("id", id, "=");
                assertEquals(expected, list.size());
            } catch ( OperationTypePersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of alreadyExist method, of class OperationTypeFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        try {
            OperationType o = facade.list("description", "TESTANDO", "=").get(0);

            OperationType c = new OperationType();
            c.setId( o.getId() );

            try {
                boolean result = facade.alreadyExist(c);
                assertEquals(true, result);
            } catch ( OperationTypePersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of searchForDescription method, of class OperationTypeFacade.
     */
    @Test
    public void testSearchForDescription() throws Exception {
        System.out.println("searchForDescription");

        String description = "TESTANDO";

        try {
            boolean result = facade.searchForDescription(description);
            assertEquals(true, result);
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }
    
    /**
     * Test of remove method, of class OperationTypeFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            OperationType o = facade.list("description", "TESTANDO", "=").get(0);

            try {
            facade.remove(o);
        } catch ( OperationTypePersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

}