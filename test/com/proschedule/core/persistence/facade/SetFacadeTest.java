package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.Set;
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
public class SetFacadeTest {
    private SetFacade facade;

    public SetFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new SetFacade();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class SetFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        //Valida um conjunto com valores válidos
        Set set = new Set();
        set.setId("TESTE");

        LeadTime lt = new LeadTime();
        lt.setValue(10.0);
        lt.setType("dias");

        set.setComponentsLeadTime(lt);
        
        try {
            facade.validate(set);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida conjunto inválido
        //Ele não pode passar no teste
        Set c = new Set();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class SetFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        Set set = new Set();
        set.setId("TESTE");

        LeadTime lt = new LeadTime();
        lt.setValue(10.0);
        lt.setType("dias");

        set.setComponentsLeadTime(lt);

        try {
            facade.add(set);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class SetFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        Set set = facade.getSet("TESTE");
        set.getComponentsLeadTime().setValue(25.20);

        try {
            facade.modify(set);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of list method, of class SetFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Set> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class SetFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Set> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class SetFacade.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");
        
        int expected = 1;
        String id = "TESTE";

        try {
            List<Set> list = facade.list("id", id, "=");
            assertEquals(expected, list.size());
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of alreadyExist method, of class SetFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Set c = new Set();
        c.setId("TESTE");

        try {
            boolean result = facade.alreadyExist(c);
            assertEquals(true, result);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getSet method, of class SetFacade.
     */
    @Test
    public void testGetSet() throws Exception {
        System.out.println("getSet");

        try {
            Set o = facade.getSet( "TESTE" );
            assertEquals(o.getId(), "TESTE");
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of validateDetail method, of class SetFacade.
     */
    @Test
    public void testValidateDetail() throws Exception {
        System.out.println("validateDetail");

        //Valida um componente com valores válidos
        SetDetail cd = new SetDetail();
        cd.setOrder(1000);

        LeadTime lt = new LeadTime();
        lt.setValue(10.5);
        lt.setType("dias");

        cd.setLeadTime(lt);

        Operation o = new OperationFacade().list().get(0);
        Set set = facade.getSet( "TESTE" );
        SetDetailKey key = new SetDetailKey();
        key.setSet(set);
        key.setOperation(o);

        cd.setPrimaryKey(key);

        try {
            facade.validateDetail(cd);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida detalhe de componente inválido
        //Ele não pode passar no teste
        SetDetail c = new SetDetail();

        try {
            facade.validateDetail(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of addDetail method, of class SetFacade.
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
            Set set = facade.getSet("TESTE");

            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            detail.setPrimaryKey(key);

            facade.addDetail(detail);

            //Verifica se gravou
            SetDetail test = facade.getSetDetail(key);

            int expected = 1000;
            assertEquals(expected, test.getOrder().intValue());

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyDetail method, of class SetFacade.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Set set = facade.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            SetDetail cd = facade.getSetDetail(key);

            cd.setOrder(888);

            facade.modifyDetail(cd);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of alreadyExistDetail method, of class SetFacade.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Set set = facade.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(set);
            key.setOperation(o);

            SetDetail cd = new SetDetail();
            cd.setPrimaryKey(key);

            boolean result = facade.alreadyExistDetail(cd);
            assertEquals(true, result);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getSetDetail method, of class SetFacade.
     */
    @Test
    public void testGetSetDetail() throws Exception {
        System.out.println("getSetDetail");

        try {
            Set c = facade.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(c);
            key.setOperation(o);

            SetDetail cd = facade.getSetDetail(key);

            int expected = 888;
            assertEquals(expected, cd.getOrder().intValue());

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of validateComponent method, of class SetFacade.
     */
    @Test
    public void testValidateComponent() throws Exception {
        System.out.println("validateComponent");

        //Valida um componente com valores válidos
        SetComponent cd = new SetComponent();
        cd.setComponentQuantity(1000.0);
        
        Component c = new ComponentFacade().list().get(0);
        Set set = facade.getSet( "TESTE" );

        SetComponentKey key = new SetComponentKey();
        key.setSet(set);
        key.setComponent(c);

        cd.setPrimaryKey(key);

        try {
            facade.validateComponent(cd);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }
    }

    /**
     * Test of addComponent method, of class SetFacade.
     */
    @Test
    public void testAddComponent() throws Exception {
        System.out.println("addComponent");

        try {
            //Adiciona detalhe
            SetComponent sc = new SetComponent();
            sc.setComponentQuantity(1000.0);

            Component c = new ComponentFacade().list().get(0);
            Set set = facade.getSet("TESTE");

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            sc.setPrimaryKey(key);

            facade.addComponent(sc);

            //Verifica se gravou
            SetComponent test = facade.getSetComponent(key);

            double expected = 1000.0;
            assertEquals(expected, test.getComponentQuantity(), 0);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyComponent method, of class SetFacade.
     */
    @Test
    public void testModifyComponent() throws Exception {
        System.out.println("modifyComponent");

        try {
            Set set = facade.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            SetComponent cd = facade.getSetComponent(key);

            cd.setComponentQuantity(888.0);

            facade.modifyComponent(cd);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExistComponent method, of class SetFacade.
     */
    @Test
    public void testAlreadyExistComponent() throws Exception {
        System.out.println("alreadyExistComponent");

        try {
            Set set = facade.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(set);
            key.setComponent(c);

            SetComponent cd = new SetComponent();
            cd.setPrimaryKey(key);

            boolean result = facade.alreadyExistComponent(cd);
            assertEquals(true, result);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of getSetComponent method, of class SetFacade.
     */
    @Test
    public void testGetSetComponent() throws Exception {
        System.out.println("getSetComponent");

        try {
            Set s = facade.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(s);
            key.setComponent(c);

            SetComponent cd = facade.getSetComponent(key);

            double expected = 888.0;
            assertEquals(expected, cd.getComponentQuantity(), 0);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }
    
    /**
     * Test of removeComponent method, of class SetFacade.
     */
    @Test
    public void testRemoveComponent() throws Exception {
        System.out.println("removeComponent");

        try {
            Set s = facade.getSet("TESTE");
            Component c = new ComponentFacade().list().get(0);

            SetComponentKey key = new SetComponentKey();
            key.setSet(s);
            key.setComponent(c);

            SetComponent cd = facade.getSetComponent(key);

            boolean result = facade.removeComponent(cd);
            assertEquals(true, result);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of listDetails method, of class SetFacade.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        Set value = facade.getSet("TESTE");

        try {
            List<SetDetail> list = facade.listDetails(value, "order", "asc");

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
     * Test of removeDetail method, of class SetFacade.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Set c = facade.getSet("TESTE");
            Operation o = new OperationFacade().list().get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(c);
            key.setOperation(o);

            SetDetail cd = facade.getSetDetail(key);

            boolean result = facade.removeDetail(cd);
            assertEquals(true, result);

        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class SetFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        Set set = facade.getSet("TESTE");

        try {
            facade.remove(set);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }
}