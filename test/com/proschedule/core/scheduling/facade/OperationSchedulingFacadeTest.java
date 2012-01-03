package com.proschedule.core.scheduling.facade;

import com.proschedule.core.persistence.model.LeadTime;
import com.proschedule.core.persistence.model.OperationType;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.model.Calendar;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingComponentDetailPersistenceException;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.scheduling.exceptions.OperationSchedulingPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingSetDetailKey;
import com.proschedule.util.date.DateUtil;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingComponentDetailKey;
import com.proschedule.core.scheduling.model.keys.OperationSchedulingKey;
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
public class OperationSchedulingFacadeTest {
    private OperationSchedulingFacade facade;

    public OperationSchedulingFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new OperationSchedulingFacade();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class OperationSchedulingFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        try {
            OperationScheduling os = new OperationScheduling();
            facade.validate(os);
        } catch (ValidatorException ex) {
            fail(ex.getMessage() + ex.getDetailedMessage());
        }
    }

    /**
     * Test of add method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            OperationScheduling operationScheduling = new OperationScheduling();

            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );
            operationScheduling.setPrimaryKey(key);

            facade.add(operationScheduling);
        } catch (OperationSchedulingPersistenceException ex) {
            fail("Falha na gravação do sequenciamento por operação.\n" + ex.getDetailMessage());
        } catch (OperationPersistenceException ex) {
            fail("Falha ao recuperar a operação.\n" + ex.getDetailMessage());
        } catch (DayPersistenceException ex) {
            fail("Falha ao recuperar o dia.\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of modify method, of class OperationSchedulingFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<OperationScheduling> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_int() throws Exception {
        System.out.println("list");

        try {
            List<OperationScheduling> list = facade.list(2009);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_Date_Date() throws Exception {
        System.out.println("list");

        try {
            List<OperationScheduling> list = facade.list(
                    DateUtil.formatDate("05/10/2009"), DateUtil.formatDate("15/10/2009"));

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<OperationScheduling> list = facade.list("id", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_Operation_String() throws Exception {
        System.out.println("list");

        try {
            Operation o = new OperationFacade().getOperation(40);
            List<OperationScheduling> list = facade.list(o, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class OperationSchedulingFacade.
     */
    @Test
    public void testList_Day_String() throws Exception {
        System.out.println("list");

        try {
            Day d = new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") );
            List<OperationScheduling> list = facade.list(d, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        OperationScheduling c = new OperationScheduling();

        OperationSchedulingKey key = new OperationSchedulingKey();
        key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
        key.setOperation( new OperationFacade().getOperation(40) );
        c.setPrimaryKey(key);

        try {
            boolean result = facade.alreadyExist(c);
            assertEquals(true, result);
        } catch ( OperationSchedulingPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of getOperationScheduling method, of class OperationSchedulingFacade.
     */
    @Test
    public void testGetOperationScheduling() throws Exception {
        System.out.println("getOperationScheduling");

        try {
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling c = facade.getOperationScheduling( key );
            assertEquals(c.getPrimaryKey().getOperation().getId().intValue(), key.getOperation().getId().intValue());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of validateComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testValidateComponentDetail() throws Exception {
        System.out.println("validateComponentDetail");

    }

    /**
     * Test of addComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAddComponentDetail() throws Exception {
        System.out.println("addComponentDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            //Novo detalhe
            OperationSchedulingComponentDetail osd = new OperationSchedulingComponentDetail();

            //Chave do detalhe
            OperationSchedulingComponentDetailKey osdKey = new OperationSchedulingComponentDetailKey();
            osdKey.setOperation( os.getPrimaryKey().getOperation() );
            osdKey.setDay( os.getPrimaryKey().getDay() );

            //Pega a primeira ordem da lista
            Order o = new OrderFacade().list("id", "asc").get(0);
            osdKey.setOrder(o);

            //Pega o primeiro componente da ordem
            for ( OrderDetail od : o.getDetails() ) {
                Component c = od.getPrimaryKey().getComponent();
                osdKey.setComponent( c );
                break;
            }

            osd.setPrimaryKey(osdKey);

            facade.addComponentDetail(osd);

        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifyComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testModifyComponentDetail() throws Exception {
        System.out.println("modifyComponentDetail");

    }

    /**
     * Test of alreadyExistComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAlreadyExistComponentDetail() throws Exception {
        System.out.println("alreadyExistComponentDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            OperationSchedulingComponentDetailKey osdKey = null;

            //Pega o primeiro componente da ordem
            for ( OperationSchedulingComponentDetail osd : os.getComponentDetails() ) {
                osdKey = osd.getPrimaryKey();
            }

            OperationSchedulingComponentDetail osd = new OperationSchedulingComponentDetail();
            osd.setPrimaryKey(osdKey);

            boolean result = facade.alreadyExistComponentDetail(osd);
            assertEquals(true, result);

        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getOperationSchedulingComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testGetOperationSchedulingComponentDetail() throws Exception {
        System.out.println("getOperationSchedulingComponentDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            OperationSchedulingComponentDetailKey osdKey = null;

            //Pega o primeiro componente da ordem
            for ( OperationSchedulingComponentDetail osd : os.getComponentDetails() ) {
                osdKey = osd.getPrimaryKey();
            }

            OperationSchedulingComponentDetail od = facade.getOperationSchedulingComponentDetail(osdKey);

            if ( od == null ) {
                fail("Deveria ter retornado um objeto.");
            }

        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of validateSetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testValidateSetDetail() throws Exception {
        System.out.println("validateSetDetail");

    }

    /**
     * Test of addSetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAddSetDetail() throws Exception {
        System.out.println("addSetDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation(new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            //Novo detalhe
            OperationSchedulingSetDetail osd = new OperationSchedulingSetDetail();

            //Chave do detalhe
            OperationSchedulingSetDetailKey osdKey = new OperationSchedulingSetDetailKey();
            osdKey.setOperation( os.getPrimaryKey().getOperation() );
            osdKey.setDay( os.getPrimaryKey().getDay() );

            //Pega a primeira ordem da lista
            Order o = new OrderFacade().list("id", "asc").get(0);
            osdKey.setOrder(o);
            osdKey.setSet(o.getSet());

            osd.setPrimaryKey(osdKey);

            facade.addSetDetail(osd);

        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of modifySetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testModifySetDetail() throws Exception {
        System.out.println("modifySetDetail");

    }



    /**
     * Test of alreadyExistSetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testAlreadyExistSetDetail() throws Exception {
        System.out.println("alreadyExistSetDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            OperationSchedulingSetDetailKey osdKey = null;

            //Pega o primeiro componente da ordem
            for ( OperationSchedulingSetDetail osd : os.getSetDetails() ) {
                osdKey = osd.getPrimaryKey();
            }

            OperationSchedulingSetDetail osd = new OperationSchedulingSetDetail();
            osd.setPrimaryKey(osdKey);

            boolean result = facade.alreadyExistSetDetail(osd);
            assertEquals(true, result);

        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of getOperationSchedulingSetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testGetOperationSchedulingSetDetail() throws Exception {
        System.out.println("getOperationSchedulingSetDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            OperationSchedulingSetDetailKey osdKey = null;

            //Pega o primeiro componente da ordem
            for ( OperationSchedulingSetDetail osd : os.getSetDetails() ) {
                osdKey = osd.getPrimaryKey();
            }

            OperationSchedulingSetDetail od = facade.getOperationSchedulingSetDetail(osdKey);

            if ( od == null ) {
                fail("Deveria ter retornado um objeto.");
            }

        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of listComponentDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListComponentDetails() throws Exception {
        System.out.println("listComponentDetails");

        try {
            Order o = new OrderFacade().list("id", "asc").get(0);
            List<OperationSchedulingComponentDetail> list = facade.listComponentDetails(o, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingComponentDetailPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of listSetDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListSetDetails() throws Exception {
        System.out.println("listSetDetails");

        try {
            Order o = new OrderFacade().list("id", "asc").get(0);
            List<OperationSchedulingSetDetail> list = facade.listSetDetails(o, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingSetDetailPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of listComponentDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListComponentDetails_3args() throws Exception {
        System.out.println("listComponentDetails");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            Component c = null;
            Operation o = null;
            Day d = null;

            for (OperationSchedulingComponentDetail oscd : os.getComponentDetails() ) {
                c = oscd.getPrimaryKey().getComponent();
                o = oscd.getPrimaryKey().getOperation();
                d = oscd.getPrimaryKey().getDay();
                break;
            }

            List<OperationSchedulingComponentDetail> list = facade.listComponentDetails(c, o, d);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingComponentDetailPersistenceException ex ) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of listSetDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListSetDetails_3args() throws Exception {
        System.out.println("listSetDetails");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            Set s = null;
            Operation o = null;
            Day d = null;

            for (OperationSchedulingSetDetail ossd : os.getSetDetails() ) {
                s = ossd.getPrimaryKey().getSet();
                o = ossd.getPrimaryKey().getOperation();
                d = ossd.getPrimaryKey().getDay();
                break;
            }

            List<OperationSchedulingSetDetail> list = facade.listSetDetails(s, o, d);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingSetDetailPersistenceException ex ) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of listComponentDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListComponentDetails_Date_Date() throws Exception {
        System.out.println("listComponentDetails");

        Date startDate = DateUtil.formatDate("01/10/2009");
        Date endDate = DateUtil.formatDate("20/10/2009");

        try {
            List<OperationSchedulingComponentDetail> list = facade.listComponentDetails(startDate, endDate);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingComponentDetailPersistenceException ex ) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of listSetDetails method, of class OperationSchedulingFacade.
     */
    @Test
    public void testListSetDetails_Date_Date() throws Exception {
        System.out.println("listSetDetails");

        Date startDate = DateUtil.formatDate("01/10/2009");
        Date endDate = DateUtil.formatDate("20/10/2009");

        try {
            List<OperationSchedulingSetDetail> list = facade.listSetDetails(startDate, endDate);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver ao menos 1 registro.");
            }
        } catch ( OperationSchedulingSetDetailPersistenceException ex ) {
             fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    /**
     * Test of removeComponentDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testRemoveComponentDetail() throws Exception {
        System.out.println("removeComponentDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            for ( OperationSchedulingComponentDetail osd : os.getComponentDetails() ) {
                os.getComponentDetails().remove(osd);
                boolean result = facade.removeComponentDetail(osd);
                assertEquals(true, result);
            }

        } catch (OperationSchedulingComponentDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of removeSetDetail method, of class OperationSchedulingFacade.
     */
    @Test
    public void testRemoveSetDetail() throws Exception {
        System.out.println("removeSetDetail");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            for ( OperationSchedulingSetDetail osd : os.getSetDetails() ) {
                os.getSetDetails().remove(osd);
                boolean result = facade.removeSetDetail(osd);
                assertEquals(true, result);
            }

        } catch (OperationSchedulingSetDetailPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch (IndexOutOfBoundsException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of remove method, of class OperationSchedulingFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            //Recupera o sequenciamento da operação
            OperationSchedulingKey key = new OperationSchedulingKey();
            key.setDay( new CalendarFacade().getDay( DateUtil.formatDate("10/10/2009") ) );
            key.setOperation( new OperationFacade().getOperation(40) );

            OperationScheduling os = facade.getOperationScheduling( key );

            boolean result = facade.remove(os);
            assertEquals(true, result);
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex);
        }
    }

    /**
     * Test of createYearOperationsScheduling method, of class OperationSchedulingFacade.
     */
    @Test
    public void testCreateYearOperationsScheduling() throws Exception {
        System.out.println("createYearScheduling");

        try {
            Calendar calendar = new CalendarFacade().list().get(0);

            //Cria o sequenciamento para o calendário
            facade.createYearOperationsScheduling(calendar);

            //Lista o sequenciamento
            List<OperationScheduling> list = facade.list(calendar.getYear());

            if ( list.size() <= 0 ) {
                fail("Deveriam existir registros.");
            }

            //Remove tudo
            for ( OperationScheduling os : list ) {
                facade.remove(os);
            }

        } catch (CalendarPersistenceException ex) {
            fail(ex.getMessage() + ex.getDetailMessage());
        } catch (OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail(ex.getMessage() + ex.getDetailedMessage());
        }
    }

    /**
     * Test of createCurrentYearOperationScheduling method, of class OperationSchedulingFacade.
     */
    @Test
    public void testCreateCurrentYearOperationScheduling() throws Exception {
        System.out.println("createCurrentYearOperationScheduling");

        try {
            Operation o = new Operation();
            o.setDescription("TESTE");

            OperationType type = new OperationType();
            type.setId(1);
            o.setType(type);

            //Lead Time
            LeadTime lt = new LeadTime();
            lt.setType("horas");
            lt.setValue(10.0);
            o.setLeadTime(lt);
            OperationFacade oFacade = new OperationFacade();
            oFacade.add(o);

            facade.createCurrentYearOperationScheduling(o, 1998);

            //Lista o sequenciamento
            List<OperationScheduling> list = facade.list(o, "=");

            if ( list.size() <= 0 ) {
                fail("Deveriam existir registros.");
            }

            //Remove tudo
            for ( OperationScheduling os : list ) {
                facade.remove(os);
            }

            oFacade.remove(o);
        } catch(OperationPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch(OperationSchedulingPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        } catch(ValidatorException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailedMessage());
        }
    }
}