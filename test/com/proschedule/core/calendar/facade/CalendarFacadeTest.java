package com.proschedule.core.calendar.facade;

import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.scheduling.model.OperationScheduling;
import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.util.date.DateUtil;
import com.proschedule.core.calendar.dao.CalendarDAO;
import com.proschedule.core.calendar.model.Day;
import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.calendar.model.Calendar;
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
public class CalendarFacadeTest {

    private CalendarFacade facade;
    private Calendar calendar;

    public CalendarFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        facade = new CalendarFacade();
        calendar = new Calendar();
        calendar.setYear(2020);
        calendar.generateDaysOfYear(8.0, false);
    }

    @After
    public void tearDown() {

    }

    /**
     * Test of validate method, of class CalendarFacade.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");

        try {
            facade.validate(calendar);
        } catch ( ValidatorException e ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of add method, of class CalendarFacade.
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");

        try {
            facade.add(calendar);
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of modify method, of class CalendarFacade.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        for ( Day day : calendar.getDays() ) {
            day.setWorkingDay(false);
            break;
        }

        try {
            facade.modify(calendar);
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        } catch ( ValidatorException ex ) {
            fail("Erro ao validar objeto.");
        }
    }

    /**
     * Test of list method, of class CalendarFacade.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list_0args");

        try {
            List<Calendar> list = facade.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                assertFalse(true);
            }
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class CalendarFacade.
     */
    @Test
    public void testList_String_Integer() throws Exception {
        System.out.println("list_String_Integer");

        int expected = 1;
        int year = 2020;

        try {
            List<Calendar> list = facade.list("year", year, "=");
            assertEquals(expected, list.size());
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of alreadyExist method, of class CalendarFacade.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Calendar c = new Calendar();
        c.setYear(2020);

        try {
            boolean result = facade.alreadyExist(c);
            assertEquals(true, result);
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class CalendarFacade.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = facade.list("year", "asc");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria ter ao menos um calendário");
            }
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class CalendarFacade.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = facade.list("year", 2020, "=");

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver um calendário");
            }
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of listDays method, of class CalendarFacade.
     */
    @Test
    public void testListDays() throws Exception {
        System.out.println("listDays");

        try {
            Date startDate = DateUtil.formatDate("01/10/2020");
            Date endDate = DateUtil.formatDate("20/10/2020");

            List<Day> list = facade.listDays(startDate, endDate);

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                fail("Deveria haver um calendário");
            }
        } catch ( DayPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of getCalendar method, of class CalendarFacade.
     */
    @Test
    public void testGetCalendar() throws Exception {
        System.out.println("getCalendar");

        try {
            Calendar c = facade.getCalendar( 2020 );

            if ( c == null ) {
                fail("O dia deveria existir");
            }

            assertEquals(c.getYear().intValue(), 2020);
        } catch ( CalendarPersistenceException e ) {
            fail(e.getMessage() + "\n" + e.getDetailMessage());
        }
    }

    /**
     * Test of getDay method, of class CalendarFacade.
     */
    @Test
    public void testGetDay() throws Exception {
        System.out.println("getDay");

        try {
            Day day = facade.getDay( DateUtil.formatDate("10/10/2020") );

            if ( day == null ) {
                fail("O dia deveria existir");
            }

            assertEquals(DateUtil.formatDate("10/10/2020"), day.getDate());
        } catch ( DayPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class CalendarFacade.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        OperationSchedulingFacade osFacade = new OperationSchedulingFacade();

        try {
            for ( OperationScheduling os : osFacade.list(2020) ) {
                osFacade.remove(os);
            }

            facade.remove(calendar);
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of checkCurrentYearCalendar method, of class CalendarFacade.
     */
    @Test
    public void testCheckCurrentYearCalendar() {
        System.out.println("checkCurrentYearCalendar");
        try {
            boolean result = facade.checkCurrentYearCalendar();

            //Existe calendário para o ano corrente
            assertTrue(result);
        } catch (CalendarPersistenceException ex) {
            fail(ex.getMessage() + "\n" + ex.getDetailMessage());
        }
    }

    

    
}