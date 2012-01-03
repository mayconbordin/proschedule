package com.proschedule.core.scheduling.dao;

import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.core.persistence.facade.CustomerFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.util.date.DateUtil;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.keys.OrderDetailKey;
import java.util.Date;
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
public class OrderDAOTest {
    private OrderDAO dao;

    public OrderDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new OrderDAO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class OrderDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            //Nova ordem
            Order o = new Order();
            o.setId("TESTE");
            o.setDeliveryDate( DateUtil.formatDate("10/10/2010") );
            o.setSetQuantity(111.0);

            Day day = new CalendarFacade().getDay( DateUtil.formatDate("10/10/1999") );

            o.setMasterScheduling(day);

            //Conjunto e Cliente
            Set s = new SetFacade().list().get(0);
            Customer c = new CustomerFacade().list().get(0);

            o.setCustomer(c);
            o.setSet(s);

            //Adiciona a ordem
            dao.add(o);

            Order test = dao.getOrder("TESTE");

            assertEquals("TESTE", test.getId());
            assertEquals(111.0, test.getSetQuantity(), 0);

        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of modify method, of class OrderDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        try {
            Order o = dao.getOrder("TESTE");
            o.setSetQuantity(5000.0);

            dao.modify(o);

            //Verifica
            Order test = dao.getOrder("TESTE");

            assertEquals("TESTE", test.getId());
            assertEquals(5000.0, test.getSetQuantity(), 0);
        } catch (OrderPersistenceException ex) {
            fail("Erro na ordem de produção.\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_3args_1() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = dao.list("id", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_3args_2() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = dao.list("setQuantity", 5000.0, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_3args_3() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = dao.list("deliveryDate", DateUtil.formatDate("10/10/2010"), "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_Set_String() throws Exception {
        System.out.println("list");

        Set s = new SetFacade().list().get(0);

        try {
            List<Order> list = dao.list(s, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_Customer_String() throws Exception {
        System.out.println("list");

        Customer c = new CustomerFacade().list().get(0);

        try {
            List<Order> list = dao.list(c, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OrderDAO.
     */
    @Test
    public void testList_Set_Date() throws Exception {
        System.out.println("list");

        Set set = new SetFacade().list().get(0);

        Date deliveryDate = DateUtil.formatDate("10/10/2010");

        try {
            List<Order> list = dao.list(set, deliveryDate);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( OrderPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class OrderDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Order c = new Order();
        c.setId("TESTE");

        try {
            boolean result = dao.alreadyExist(c);
            assertEquals(true, result);
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOrder method, of class OrderDAO.
     */
    @Test
    public void testGetOrder() throws Exception {
        System.out.println("getOrder");

        try {
            Order c = dao.getOrder( "TESTE" );
            assertEquals(c.getId(), "TESTE");
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of addDetail method, of class OrderDAO.
     */
    @Test
    public void testAddDetail() throws Exception {
        System.out.println("addDetail");

        try {
            Order o = dao.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = new OrderDetail();
                od.setPrimaryKey(key);
                od.setComponentQuantity( o.getSetQuantity() * c.getComponentQuantity() );

                dao.addDetail(od);
            }

            //Verifica se foi salvo
            Order test = dao.getOrder( "TESTE" );

            assertEquals(o.getSet().getComponents().size(), test.getDetails().size());

        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }  catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyDetail method, of class OrderDAO.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Order o = dao.getOrder( "TESTE" );

            for ( OrderDetail od : o.getDetails() ) {
                od.setComponentQuantity(200.0);
                dao.modifyDetail(od);
            }

            //Verifica se foi salvo
            Order test = dao.getOrder("TESTE");
            double expected = 200.0;

            for ( OrderDetail od : test.getDetails() ) {
                assertEquals(expected, od.getComponentQuantity(), 0);
            }

        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of alreadyExistDetail method, of class OrderDAO.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Order o = dao.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = new OrderDetail();
                od.setPrimaryKey(key);

                boolean result = dao.alreadyExistDetail(od);
                assertEquals(true, result);
            }
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getOrderDetail method, of class OrderDAO.
     */
    @Test
    public void testGetOrderDetail() throws Exception {
        System.out.println("getOrderDetail");

        try {
            Order o = dao.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = dao.getOrderDetail(key);

                double expected = 200.0;
                assertEquals(expected, od.getComponentQuantity(), 0);
            }
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of listDetails method, of class OrderDAO.
     */
    @Test
    public void testListDetails_Date_Date() throws Exception {
        System.out.println("listDetails");

        Date startDate = DateUtil.formatDate("01/10/1999");
        Date endDate = DateUtil.formatDate("20/10/1999");

        try {
            List<OrderDetail> list = dao.listDetails(startDate, endDate);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }

        } catch ( OrderDetailPersistenceException ex) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }

    }

    /**
     * Test of listDetails method, of class OrderDAO.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        try {
            Order o = dao.getOrder( "TESTE" );

            Component component = null;

            for ( SetComponent sc : o.getSet().getComponents() ) {
                component = sc.getPrimaryKey().getComponent();
                break;
            }

            List<OrderDetail> list = dao.listDetails(component, o.getSet(), o.getMasterScheduling());

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }

            for ( OrderDetail od : list ) {
                assertEquals(od.getPrimaryKey().getComponent().getId(), component.getId());
                assertEquals(od.getPrimaryKey().getOrder().getMasterScheduling().getDate(), o.getMasterScheduling().getDate());
            }

        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch ( OrderDetailPersistenceException ex) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of removeDetail method, of class OrderDAO.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Order o = dao.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = dao.getOrderDetail(key);

                boolean result = dao.removeDetail(od);
                assertEquals(true, result);
            }
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of remove method, of class OrderDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        Order order = dao.getOrder( "TESTE" );

        try {
            boolean result = dao.remove(order);
            assertEquals(true, result);
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }
}