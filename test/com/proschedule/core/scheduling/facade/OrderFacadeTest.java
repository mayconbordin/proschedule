package com.proschedule.core.scheduling.facade;

import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.util.date.DateUtil;
import com.proschedule.core.persistence.facade.CustomerFacade;
import com.proschedule.core.persistence.model.Customer;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.keys.OrderDetailKey;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OrderFacadeTest {
    private OrderFacade facade;

    public OrderFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new OrderFacade();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class OrderFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        //Valida um conjunto com valores válidos
        Order order = new Order();
        order.setId("TESTE");
        order.setSetQuantity(1000.0);
        order.setCustomer( new CustomerFacade().list().get(0) );
        order.setDeliveryDate( DateUtil.formatDate("10/10/1999"));
        order.setSet( new SetFacade().list().get(0) );

        try {
            facade.validate(order);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida conjunto inválido
        //Ele não pode passar no teste
        Order c = new Order();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class OrderFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        Order order = new Order();
        order.setId("TESTE");
        order.setSetQuantity(1000.0);
        order.setCustomer( new CustomerFacade().list().get(0) );
        order.setDeliveryDate( DateUtil.formatDate("10/10/1999"));
        order.setSet( new SetFacade().list().get(0) );

        try {
            facade.add(order);
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class OrderFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        Order order = facade.getOrder("TESTE");
        order.setSetQuantity(888.0);

        try {
            facade.modify(order);
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Order> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_3args_1() throws Exception {
        System.out.println("list");

        int expected = 1;
        String id = "TESTE";

        try {
            List<Order> list = facade.list("id", id, "=");
            assertEquals(expected, list.size());
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_3args_2() throws Exception {
        System.out.println("list");

        int expected = 1;
        double value = 888.0;

        try {
            List<Order> list = facade.list("setQuantity", value, "=");
            assertEquals(expected, list.size());
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_3args_3() throws Exception {
        System.out.println("list");

        int expected = 1;
        Date value = DateUtil.formatDate("10/10/1999");

        try {
            List<Order> list = facade.list("deliveryDate", value, "=");
            assertEquals(expected, list.size());
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_Set_String() throws Exception {
        System.out.println("list");

        Set value = new SetFacade().list().get(0);

        try {
            List<Order> list = facade.list(value, "=");

            if ( list.size() <= 0 ) {
                fail("Deveria ter ao menos um registro");
            }
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_Customer_String() throws Exception {
        System.out.println("list");

        Customer value = new CustomerFacade().list().get(0);

        try {
            List<Order> list = facade.list(value, "=");

            if ( list.size() <= 0 ) {
                fail("Deveria ter ao menos um registro");
            }
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class OrderFacade.
     */
    @Test
    public void testList_Set_Date() throws Exception {
        System.out.println("list");

        Set set = new SetFacade().list().get(0);

        Date deliveryDate = DateUtil.formatDate("10/10/1999");

        try {
            List<Order> list = facade.list(set, deliveryDate);

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
     * Test of alreadyExist method, of class OrderFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Order o = new Order();
        o.setId("TESTE");

        try {
            boolean result = facade.alreadyExist(o);
            assertEquals(true, result);
        } catch ( OrderPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOrder method, of class OrderFacade.
     */
    @Test
    public void testGetOrder() throws Exception {
        System.out.println("getOrder");

        try {
            Order o = facade.getOrder( "TESTE" );
            assertEquals(o.getId(), "TESTE");
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of validateDetail method, of class OrderFacade.
     */
    @Test
    public void testValidateDetail() throws Exception {
        System.out.println("validateDetail");

        Order o = facade.getOrder("TESTE");

        //Valida um componente com valores válidos
        OrderDetail detail = new OrderDetail();
        detail.setComponentQuantity(1000.0);

        OrderDetailKey key = new OrderDetailKey();
        key.setComponent( new ComponentFacade().list().get(0) );
        key.setOrder(o);

        try {
            facade.validateDetail(detail);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

//        //Valida detalhe de ordem inválido
//        //Ele não pode passar no teste
//        OrderDetail od = new OrderDetail();
//
//        try {
//            facade.validateDetail(od);
//            fail("Objeto inválido passou na validação.");
//        } catch ( ValidatorException e ) {
//            assertTrue(true);
//        }
    }

    /**
     * Test of addDetail method, of class OrderFacade.
     */
    @Test
    public void testAddDetail() throws Exception {
        System.out.println("addDetail");

        try {
            Order o = facade.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = new OrderDetail();
                od.setPrimaryKey(key);
                od.setComponentQuantity( o.getSetQuantity() * c.getComponentQuantity() );

                facade.addDetail(od);
            }

            //Verifica se foi salvo
            Order test = facade.getOrder( "TESTE" );

            assertEquals(o.getSet().getComponents().size(), test.getDetails().size());

        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto.\n" + e.getDetailedMessage());
        }
    }

    /**
     * Test of modifyDetail method, of class OrderFacade.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Order o = facade.getOrder( "TESTE" );

            for ( OrderDetail od : o.getDetails() ) {
                od.setComponentQuantity(200.0);
                facade.modifyDetail(od);
            }

            //Verifica se foi salvo
            Order test = facade.getOrder("TESTE");
            double expected = 200.0;

            for ( OrderDetail od : test.getDetails() ) {
                assertEquals(expected, od.getComponentQuantity(), 0);
            }

        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto.\n" + e.getDetailedMessage());
        }
    }

    /**
     * Test of alreadyExistDetail method, of class OrderFacade.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Order o = facade.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = new OrderDetail();
                od.setPrimaryKey(key);

                boolean result = facade.alreadyExistDetail(od);
                assertEquals(true, result);
            }
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getOrderDetail method, of class OrderFacade.
     */
    @Test
    public void testGetOrderDetail() throws Exception {
        System.out.println("getOrderDetail");

        try {
            Order o = facade.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = facade.getOrderDetail(key);

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
     * Test of listDetails method, of class OrderFacade.
     */
    @Test
    public void testListDetails_Date_Date() throws Exception {
        System.out.println("listDetails");

        Date startDate = DateUtil.formatDate("01/10/1999");
        Date endDate = DateUtil.formatDate("20/10/1999");

        try {
            List<OrderDetail> list = facade.listDetails(startDate, endDate);

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
     * Test of listDetails method, of class OrderFacade.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        try {
            Order o = facade.getOrder( "TESTE" );

            Component component = null;

            for ( SetComponent sc : o.getSet().getComponents() ) {
                component = sc.getPrimaryKey().getComponent();
                break;
            }

            List<OrderDetail> list = facade.listDetails(component, o.getSet(), o.getMasterScheduling());

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
     * Test of removeDetail method, of class OrderFacade.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Order o = facade.getOrder( "TESTE" );

            for ( SetComponent c : o.getSet().getComponents() ) {
                OrderDetailKey key = new OrderDetailKey();
                key.setComponent( c.getPrimaryKey().getComponent() );
                key.setOrder(o);

                OrderDetail od = facade.getOrderDetail(key);

                boolean result = facade.removeDetail(od);
                assertEquals(true, result);
            }
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of remove method, of class OrderFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        Order order = facade.getOrder( "TESTE" );

        try {
            boolean result = facade.remove(order);
            assertEquals(true, result);
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of calculateMasterSchedulingDate method, of class OrderFacade.
     */
    @Test
    public void testCalculateMasterSchedulingDate() {
        try {
            System.out.println("calculateMasterSchedulingDate");

            Order order = facade.getOrder("2");
            facade.calculateMasterSchedulingDate(order);

            Date expected = DateUtil.formatDate("08/10/2009");
            assertEquals( order.getMasterScheduling().getDate(), expected);
            
        } catch (OrderDetailPersistenceException ex) {
           fail(ex.getMessage());
           fail(ex.getDetailMessage());
        } catch (OrderPersistenceException ex) {
           fail(ex.getMessage());
           fail(ex.getDetailMessage());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of createOperationsScheduling method, of class OrderFacade.
     */
    @Test
    public void testCreateOperationsScheduling() throws Exception {
        System.out.println("createOperationsScheduling");
        
    }
}