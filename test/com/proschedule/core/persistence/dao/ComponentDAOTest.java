package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
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
public class ComponentDAOTest {

    private ComponentDAO dao;
    private Component component;
    private ComponentDetail detail;

    public ComponentDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new ComponentDAO();

        //Componente
        component = new Component();
        component.setId("TESTE");
        component.setRawMaterial("320 x 450 mm");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class ComponentDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            boolean result = dao.add(component);
            assertEquals(true, result);
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modify method, of class ComponentDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        component.setRawMaterial("20 x 20 cm");

        try {
            boolean result = dao.modify(component);
            assertEquals(true, result);
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of list method, of class ComponentDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Component> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( ComponentPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class ComponentDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Component> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( ComponentPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class ComponentDAO.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            List<Component> list = dao.list("id", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( ComponentPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class ComponentDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Component c = new Component();
        c.setId("TESTE");

        try {
            boolean result = dao.alreadyExist(c);
            assertEquals(true, result);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOperation method, of class ComponentDAO.
     */
    @Test
    public void testGetComponent() throws Exception {
        System.out.println("getOperation");

        try {
            Component c = dao.getComponent( "TESTE" );
            assertEquals(c.getId(), "TESTE");
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of addDetail method, of class ComponentDAO.
     */
    @Test
    public void testAddDetail() throws Exception {
        System.out.println("addDetail");
        try {
            //Adiciona detalhe
            detail = new ComponentDetail();
            detail.setOrder(1000);

            LeadTime lt = new LeadTime();
            lt.setValue(10.5);
            lt.setType("dias");

            detail.setLeadTime(lt);

            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(component);
            key.setOperation(o);

            detail.setPrimaryKey(key);

            dao.addDetail(detail);

            //Verifica se gravou
            ComponentDetail test = dao.getComponentDetail(key);

            int expected = 1000;
            assertEquals(expected, test.getOrder().intValue());

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyDetail method, of class ComponentDAO.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Component c = dao.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = dao.getComponentDetail(key);

            cd.setOrder(888);

            dao.modifyDetail(cd);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of alreadyExistDetail method, of class ComponentDAO.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Component c = dao.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = new ComponentDetail();
            cd.setPrimaryKey(key);

            boolean result = dao.alreadyExistDetail(cd);
            assertEquals(true, result);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getComponentDetail method, of class ComponentDAO.
     */
    @Test
    public void testGetComponentDetail() throws Exception {
        System.out.println("getComponentDetail");

        try {
            Component c = dao.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = dao.getComponentDetail(key);

            int expected = 888;
            assertEquals(expected, cd.getOrder().intValue());

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of listDetails method, of class ComponentDAO.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        try {
            Component c = dao.getComponent("TESTE");
            List<ComponentDetail> list = dao.listDetails(c, "order", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( ComponentDetailPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of removeDetail method, of class ComponentDAO.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Component c = dao.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = dao.getComponentDetail(key);

            boolean result = dao.removeDetail(cd);
            assertEquals(true, result);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class ComponentDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            boolean result = dao.remove(component);
            assertEquals(true, result);
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }
}