package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
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
public class CustomerDAOTest {
    private CustomerDAO dao;
    private Customer customer;

    public CustomerDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new CustomerDAO();

        customer = new Customer();
        customer.setId("TESTE");
        customer.setName("Clint Eastwood");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class CustomerDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            boolean result = dao.add(customer);
            assertEquals(true, result);
        } catch (CustomerPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modify method, of class CustomerDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        customer.setName("Dirty Harry");

        try {
            boolean result = dao.modify(customer);
            assertEquals(true, result);
        } catch (CustomerPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of list method, of class CustomerDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Customer> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( CustomerPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class CustomerDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Customer> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( CustomerPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class CustomerDAO.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            List<Customer> list = dao.list("id", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( CustomerPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class CustomerDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Customer c = new Customer();
        c.setId("TESTE");

        try {
            boolean result = dao.alreadyExist(c);
            assertEquals(true, result);
        } catch ( CustomerPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getCustomer method, of class CustomerDAO.
     */
    @Test
    public void testGetCustomer() throws Exception {
        System.out.println("getCustomer");

        try {
            Customer c = dao.getCustomer( "TESTE" );
            assertEquals(c.getId(), "TESTE");
        } catch (CustomerPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class CustomerDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            boolean result = dao.remove(customer);
            assertEquals(true, result);
        } catch (CustomerPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    

}