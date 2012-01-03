package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.model.Customer;
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
public class CustomerFacadeTest {

    private CustomerFacade facade;
    private Customer customer;

    public CustomerFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new CustomerFacade();

        customer = new Customer();
        customer.setId("TESTE");
        customer.setName("Stanley Kubrick");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class CustomerFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

         //Valida um componente com valores válidos
        try {
            facade.validate(customer);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida componente inválido
        //Ele não pode passar no teste
        Customer c = new Customer();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class CustomerFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            facade.add(customer);
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class CustomerFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        customer.setName("Alfred Hitchcock");

        try {
            facade.modify(customer);
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of list method, of class CustomerFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Customer> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class CustomerFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Customer> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class CustomerFacade.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        int expected = 1;
        String id = "TESTE";

        try {
            List<Customer> list = facade.list("id", id, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
            
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of alreadyExist method, of class CustomerFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Customer c = new Customer();
        c.setId("TESTE");

        try {
            boolean result = facade.alreadyExist(c);
            assertEquals(true, result);
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getCustomer method, of class CustomerFacade.
     */
    @Test
    public void testGetCustomer() throws Exception {
        System.out.println("getCustomer");

        try {
            Customer c = facade.getCustomer( "TESTE" );
            assertEquals(c.getId(), "TESTE");
        } catch (CustomerPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class CustomerFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            facade.remove(customer);
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    

}