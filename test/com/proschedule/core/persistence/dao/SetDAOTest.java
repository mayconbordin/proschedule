package com.proschedule.core.persistence.dao;

import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
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
public class SetDAOTest {

    private SetDAO dao;

    public SetDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = new SetDAO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class SetDAO.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        //Conjunto
        Set set = new Set();
        set.setId("TESTE");

        //Lead Time
        LeadTime lt = new LeadTime();
        lt.setValue(10.0);
        lt.setType("dias");

        set.setComponentsLeadTime(lt);

        try {
            boolean result = dao.add(set);
            assertEquals(true, result);
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modify method, of class SetDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        Set set = dao.getSet("TESTE");

        //Lead Time
        LeadTime lt = new LeadTime();
        lt.setValue(20.0);
        lt.setType("horas");

        set.setComponentsLeadTime(lt);

        try {
            boolean result = dao.modify(set);
            assertEquals(true, result);
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of list method, of class SetDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Set> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( SetPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class SetDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Set> list = dao.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( SetPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class SetDAO.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            List<Set> list = dao.list("id", "TESTE", "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("A lista deveria ter 1 registro.");
            }
        } catch ( SetPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class SetDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Set c = new Set();
        c.setId("TESTE");

        try {
            boolean result = dao.alreadyExist(c);
            assertEquals(true, result);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOperation method, of class SetDAO.
     */
    @Test
    public void testGetSet() throws Exception {
        System.out.println("getOperation");

        try {
            Set c = dao.getSet( "TESTE" );
            assertEquals(c.getId(), "TESTE");
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of addDetail method, of class SetDAO.
     */
    @Test
    public void testAddDetail() throws Exception {
        System.out.println("addDetail");

        try {
            //Adiciona detalhe
            SetDetail detail = new SetDetail();
            detail.setOrder(1000);

            LeadTime lt = new LeadTime();
            lt.setValue(10.5);
            lt.setType("dias");

            detail.setLeadTime(lt);

            Operation o = new OperationFacade().list().get(0);
            Set set = dao.getSet("TESTE");
            
            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            detail.setPrimaryKey(key);

            dao.addDetail(detail);

            //Verifica se gravou
            SetDetail test = dao.getSetDetail(key);

            int expected = 1000;
            assertEquals(expected, test.getOrder().intValue());

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyDetail method, of class SetDAO.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Set set = dao.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            SetDetail cd = dao.getSetDetail(key);

            cd.setOrder(888);

            dao.modifyDetail(cd);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of alreadyExistDetail method, of class SetDAO.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Set set = dao.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            SetDetail cd = new SetDetail();
            cd.setPrimaryKey(key);

            boolean result = dao.alreadyExistDetail(cd);
            assertEquals(true, result);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getSetDetail method, of class SetDAO.
     */
    @Test
    public void testGetSetDetail() throws Exception {
        System.out.println("getSetDetail");

        try {
            Set c = dao.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(c);
            key.setOperation(o);

            SetDetail cd = dao.getSetDetail(key);

            int expected = 888;
            assertEquals(expected, cd.getOrder().intValue());

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of addComponent method, of class SetDAO.
     */
    @Test
    public void testAddComponent() throws Exception {
        System.out.println("addComponent");

        try {
            //Adiciona detalhe
            SetComponent sc = new SetComponent();
            sc.setComponentQuantity(1000.0);

            Component c = new ComponentFacade().list().get(0);
            Set set = dao.getSet("TESTE");

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            sc.setPrimaryKey(key);

            dao.addComponent(sc);

            //Verifica se gravou
            SetComponent test = dao.getSetComponent(key);

            double expected = 1000.0;
            assertEquals(expected, test.getComponentQuantity(), 0);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyComponent method, of class SetDAO.
     */
    @Test
    public void testModifyComponent() throws Exception {
        System.out.println("modifyComponent");

        try {
            Set set = dao.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            SetComponent cd = dao.getSetComponent(key);

            cd.setComponentQuantity(888.0);

            dao.modifyComponent(cd);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExistComponent method, of class SetDAO.
     */
    @Test
    public void testAlreadyExistComponent() throws Exception {
        System.out.println("alreadyExistComponent");

        try {
            Set set = dao.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            SetComponent cd = new SetComponent();
            cd.setPrimaryKey(key);

            boolean result = dao.alreadyExistComponent(cd);
            assertEquals(true, result);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of getSetComponent method, of class SetDAO.
     */
    @Test
    public void testGetSetComponent() throws Exception {
        System.out.println("getSetComponent");

        try {
            Set s = dao.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(s);
            key.setComponent(c);

            SetComponent cd = dao.getSetComponent(key);

            double expected = 888.0;
            assertEquals(expected, cd.getComponentQuantity(), 0);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of removeComponent method, of class SetDAO.
     */
    @Test
    public void testRemoveComponent() throws Exception {
        System.out.println("removeComponent");

        try {
            Set s = dao.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(s);
            key.setComponent(c);

            SetComponent cd = dao.getSetComponent(key);

            boolean result = dao.removeComponent(cd);
            assertEquals(true, result);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of listDetails method, of class SetDAO.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        Set value = dao.getSet("TESTE");

        try {
            List<SetDetail> list = dao.listDetails(value, "order", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( SetDetailPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of removeDetail method, of class SetDAO.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Set c = dao.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(c);
            key.setOperation(o);

            SetDetail cd = dao.getSetDetail(key);

            boolean result = dao.removeDetail(cd);
            assertEquals(true, result);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class SetDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        Set set = dao.getSet( "TESTE" );

        try {
            boolean result = dao.remove(set);
            assertEquals(true, result);
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

}