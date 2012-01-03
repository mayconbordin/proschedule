package com.proschedule.core.persistence.facade;

import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.model.Component;
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
public class ComponentFacadeTest {

    private ComponentFacade facade;
    private Component component;

    public ComponentFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new ComponentFacade();

        component = new Component();
        component.setId("TESTE");
        component.setRawMaterial("65 x 89 mm");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class ComponentFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        //Valida um componente com valores válidos
        try {
            facade.validate(component);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida componente inválido
        //Ele não pode passar no teste
        Component c = new Component();

        try {
            facade.validate(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of add method, of class ComponentFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            facade.add(component);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class ComponentFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        component.setRawMaterial("8799 x 52 x 256 mm");

        try {
            facade.modify(component);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of list method, of class ComponentFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Component> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class ComponentFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Component> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class ComponentFacade.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        int expected = 1;
        String id = "TESTE";

        try {
            List<Component> list = facade.list("id", id, "=");
            assertEquals(expected, list.size());
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of alreadyExist method, of class ComponentFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Component c = new Component();
        c.setId("TESTE");

        try {
            boolean result = facade.alreadyExist(c);
            assertEquals(true, result);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getComponent method, of class ComponentFacade.
     */
    @Test
    public void testGetComponent() throws Exception {
        System.out.println("getOperation");

        try {
            Component o = facade.getComponent( "TESTE" );
            assertEquals(o.getRawMaterial(), "8799 x 52 x 256 mm");
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of validateDetail method, of class ComponentFacade.
     */
    @Test
    public void testValidateDetail() throws Exception {
        System.out.println("validateDetail");

        //Valida um componente com valores válidos
        ComponentDetail cd = new ComponentDetail();
        cd.setOrder(1000);

        LeadTime lt = new LeadTime();
        lt.setValue(10.5);
        lt.setType("dias");

        cd.setLeadTime(lt);

        Operation o = new OperationFacade().list().get(0);
        ComponentDetailKey key = new ComponentDetailKey();
        key.setComponent(component);
        key.setOperation(o);

        cd.setPrimaryKey(key);

        try {
            facade.validateDetail(cd);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto: " + e.getDetailedMessage());
        }

        //Valida detalhe de componente inválido
        //Ele não pode passar no teste
        ComponentDetail c = new ComponentDetail();

        try {
            facade.validateDetail(c);
            fail("Objeto inválido passou na validação.");
        } catch ( ValidatorException e ) {
            assertTrue(true);
        }
    }

    /**
     * Test of addDetail method, of class ComponentFacade.
     */
    @Test
    public void testAddDetail() throws Exception {
        System.out.println("addDetail");

        try {
            //Adiciona detalhe
            ComponentDetail detail = new ComponentDetail();
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

            facade.addDetail(detail);

            //Verifica se gravou
            ComponentDetail test = facade.getComponentDetail(key);

            int expected = 1000;
            assertEquals(expected, test.getOrder().intValue());

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyDetail method, of class ComponentFacade.
     */
    @Test
    public void testModifyDetail() throws Exception {
        System.out.println("modifyDetail");

        try {
            Component c = facade.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = facade.getComponentDetail(key);

            cd.setOrder(888);

            facade.modifyDetail(cd);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of alreadyExistDetail method, of class ComponentFacade.
     */
    @Test
    public void testAlreadyExistDetail() throws Exception {
        System.out.println("alreadyExistDetail");

        try {
            Component c = facade.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = new ComponentDetail();
            cd.setPrimaryKey(key);

            boolean result = facade.alreadyExistDetail(cd);
            assertEquals(true, result);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getComponentDetail method, of class ComponentFacade.
     */
    @Test
    public void testGetComponentDetail() throws Exception {
        System.out.println("getComponentDetail");

        try {
            Component c = facade.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = facade.getComponentDetail(key);

            int expected = 888;
            assertEquals(expected, cd.getOrder().intValue());

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of listDetails method, of class ComponentFacade.
     */
    @Test
    public void testListDetails() throws Exception {
        System.out.println("listDetails");

        try {
            Component c = facade.getComponent("TESTE");
            List<ComponentDetail> list = facade.listDetails(c, "order", "asc");

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
     * Test of removeDetail method, of class ComponentFacade.
     */
    @Test
    public void testRemoveDetail() throws Exception {
        System.out.println("removeDetail");

        try {
            Component c = facade.getComponent("TESTE");
            Operation o = new OperationFacade().list().get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = facade.getComponentDetail(key);

            boolean result = facade.removeDetail(cd);
            assertEquals(true, result);

        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class ComponentFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            facade.remove(component);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    

}