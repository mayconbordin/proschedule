package com.proschedule.core.persistence.facade;

import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.validator.util.ValidatorException;
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
public class OperationFacadeTest {

    private OperationFacade facade;

    public OperationFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new OperationFacade();

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class OperationFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        Operation o = new Operation();
        o.setDescription("Teste");

        //A princípio existe dois tipos de operações distintas
        //Já estão gravadas no banco de dados.
        //1 é para conjuntos
        OperationType type = new OperationType();
        type.setId(1);

        o.setType(type);

        //Lead Time
        LeadTime lt = new LeadTime();
        lt.setType("dias");
        lt.setValue(10.0);

        o.setLeadTime(lt);

        //Valida um componente com valores válidos
        try {
            facade.validate(o);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida componente inválido
        //Ele não pode passar no teste
        Operation c = new Operation();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class OperationFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        Operation o = new Operation();
        o.setDescription("Teste");

        //A princípio existe dois tipos de operações distintas
        //Já estão gravadas no banco de dados.
        //1 é para conjuntos
        OperationType type = new OperationType();
        type.setId(1);

        o.setType(type);

        //Lead Time
        LeadTime lt = new LeadTime();
        lt.setType("dias");
        lt.setValue(10.0);

        o.setLeadTime(lt);

        try {
            facade.add(o);
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class OperationFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        try {
            Operation o = facade.list("description", "Teste", "=").get(0);

            o.setDescription("TESTE");

            try {
                facade.modify(o);
            } catch ( OperationPersistenceException ex ) {
                fail( ex.getDetailMessage() );
            } catch ( ValidatorException ex ) {
                fail("Erro ao validar objeto.");
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of list method, of class OperationFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Operation> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OperationFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Operation> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OperationFacade.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            Operation o = facade.list("description", "TESTE", "=").get(0);

            int expected = 1;
            int id = o.getId();

            try {
                List<Operation> list = facade.list("id", id, "=");
                assertEquals(expected, list.size());
            } catch ( OperationPersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of alreadyExist method, of class OperationFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        try {
            Operation o = facade.list("description", "TESTE", "=").get(0);

            Operation c = new Operation();
            c.setId( o.getId() );

            try {
                boolean result = facade.alreadyExist(c);
                assertEquals(true, result);
            } catch ( OperationPersistenceException ex ) {
                fail( ex.getDetailMessage() );
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of searchForDescription method, of class OperationFacade.
     */
    @Test
    public void testSearchForDescription() throws Exception {
        System.out.println("searchForDescription");

        String description = "TESTE";

        try {
            boolean result = facade.searchForDescription(description);
            assertEquals(true, result);
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOperation method, of class OperationFacade.
     */
    @Test
    public void testeGetOperation() throws Exception {
        System.out.println("getOperation");

        try {
            Operation c = facade.list("description", "TESTE", "=").get(0);

            try {
                Operation o = facade.getOperation( c.getId() );
                assertEquals(c.getDescription(), o.getDescription());
            } catch (OperationPersistenceException ex) {
                fail(ex.getMessage() + "\n" + ex.getDetailMessage());
            }
        } catch (IndexOutOfBoundsException ex ) {
            fail("Erro ao recuperar objeto.\n" + ex.getMessage());
        }
    }

    /**
     * Test of remove method, of class OperationFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            OperationSchedulingFacade osFacade = new OperationSchedulingFacade();

            Operation o = facade.list("description", "TESTE", "=").get(0);

            //Lista o sequenciamento
            List<OperationScheduling> list = osFacade.list(o, "=");

            if ( list.size() <= 0 ) {
                fail("Deveriam existir registros.");
            }

            //Remove tudo
            for ( OperationScheduling os : list ) {
                osFacade.remove(os);
            }

            facade.remove(o);
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }
}