package com.proschedule.core.persistence.model;

import java.util.HashSet;
import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.model.keys.SetComponentKey;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.core.persistence.model.keys.SetDetailKey;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.model.keys.ComponentDetailKey;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
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
public class SetTest {
    private Set set;

    public SetTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            //Cria o conjunto
            set = new Set();
            set.setId("TESTE");

            ///////////////////////////////////////////////////
            //Operação
            Operation o = new Operation();
            o.setDescription("TESTE");
            OperationType type = new OperationType();
            type.setId(1);
            o.setType(type);
            LeadTime lt = new LeadTime();
            lt.setType("horas");
            lt.setValue(10.0);
            o.setLeadTime(lt);

            new OperationFacade().add(o);
            ///////////////////////////////////////////////////

            //Componente
            Component c = new Component();
            c.setId("TESTE");
            c.setRawMaterial("65 x 89 mm");

            ComponentFacade cFacade = new ComponentFacade();
            cFacade.add(c);

            //Detalhe de Componente
            ComponentDetail detail = new ComponentDetail();
            detail.setOrder(1000);
            lt = new LeadTime();
            lt.setValue(15.0);
            lt.setType("horas");
            detail.setLeadTime(lt);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);
            detail.setPrimaryKey(key);

            cFacade.addDetail(detail);
            c.getDetails().add(detail);
            ///////////////////////////////////////////////////

            //Adiciona o componente de conjunto
            SetComponent sComponent = new SetComponent();
            sComponent.setComponentQuantity(1000.0);
            
            SetComponentKey scKey = new SetComponentKey();
            scKey.setSet(set);
            scKey.setComponent(c);
            sComponent.setPrimaryKey(scKey);

            //Adiciona o componente
            set.getComponents().add(sComponent);
            ///////////////////////////////////////////////////
            
            //Calcula o lead time para componentes
            set.calculateComponentsLeadTime();

            //Remove o componente para salvar o conjunto
            set.setComponents(new HashSet());

            //Salva Conjunto
            SetFacade sFacade = new SetFacade();
            sFacade.add(set);

            //Salva o componente
            sFacade.addComponent(sComponent);
            set.getComponents().add(sComponent);

            ///////////////////////////////////////////////////
            //Adiciona o detalhe de conjunto
            SetDetail sDetail = new SetDetail();
            sDetail.setOrder(1000);
            lt = new LeadTime();
            lt.setValue(20.0);
            lt.setType("horas");
            sDetail.setLeadTime(lt);
            SetDetailKey sKey = new SetDetailKey();
            sKey.setSet(set);
            sKey.setOperation(o);
            sDetail.setPrimaryKey(sKey);

            sFacade.addDetail(sDetail);

            set.getDetails().add(sDetail);

        } catch (SetComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch ( OperationPersistenceException ex ) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto. \n" + ex.getDetailedMessage());
        }
    }

    @After
    public void tearDown() {
        //Deleta detalhe de conjunto
        SetFacade sFacade = new SetFacade();

        try {
            Set c = sFacade.getSet("TESTE");
            Operation o = new OperationFacade().list("description", "TESTE", "=").get(0);
            SetDetailKey key = new SetDetailKey();
            key.setSet(c);
            key.setOperation(o);

            SetDetail cd = sFacade.getSetDetail(key);

            boolean result = sFacade.removeDetail(cd);
            assertEquals(true, result);

        } catch (OperationPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (SetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }

        //Deleta o conjunto
        try {
            Set s = sFacade.getSet("TESTE");
            sFacade.remove(s);
        } catch ( SetPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }

        //Deleta o detalhe de componente
        ComponentFacade cFacade = new ComponentFacade();

        try {
            Component c = cFacade.getComponent("TESTE");
            Operation o = new OperationFacade().list("description", "TESTE", "=").get(0);
            ComponentDetailKey key = new ComponentDetailKey();
            key.setComponent(c);
            key.setOperation(o);

            ComponentDetail cd = cFacade.getComponentDetail(key);

            boolean result = cFacade.removeDetail(cd);
            assertEquals(true, result);

        } catch (OperationPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (ComponentPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (ComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }

        //Deleta o componente
        try {
            Component c = cFacade.getComponent("TESTE");
            cFacade.remove(c);
        } catch ( ComponentPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }

        //Deleta a operação
        OperationFacade oFacade = new OperationFacade();

        try {
            Operation o = oFacade.list("description", "TESTE", "=").get(0);
            oFacade.remove(o);
        } catch ( OperationPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch (IndexOutOfBoundsException ex) {
            fail("Erro ao recuperar objeto.");
        }
    }

    /**
     * Test of getComponentsLeadTime method, of class Set.
     */
    @Test
    public void testGetComponentsLeadTime() {
        try {
            System.out.println("getComponentsLeadTime");
            Set s = new SetFacade().getSet("TESTE");

            assertEquals(s.getId(), "TESTE");
            assertEquals(s.getDetails().size(), 1);
            assertEquals(s.getComponents().size(), 1);

            double expected = 15.0;
            assertEquals(expected, s.getComponentsLeadTime().getValue(), 0);
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of getTotalLeadTime method, of class Set.
     */
    @Test
    public void testGetTotalLeadTime() {
        try {
            System.out.println("getTotalLeadTime");
            Set s = new SetFacade().getSet("TESTE");
            
            double expected = 35.0;
            assertEquals(expected, s.getTotalLeadTime(), 0);
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

}