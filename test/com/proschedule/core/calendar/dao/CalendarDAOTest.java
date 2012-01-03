package com.proschedule.core.calendar.dao;

import com.proschedule.core.calendar.exceptions.DayPersistenceException;
import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.util.date.DateUtil;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import com.proschedule.core.calendar.model.Day;
import java.util.Set;
import java.util.HashSet;
import com.proschedule.core.calendar.model.Calendar;
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
public class CalendarDAOTest {

    private Calendar calendar;

    private CalendarDAO dao = new CalendarDAO();

    public CalendarDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        //Cria novo calendário
        calendar = new Calendar();
        calendar.setYear(2020);

        try {
            //Cria um dia
            Day day = new Day();
            day.setCalendar(calendar);
            day.setDate(DateUtil.formatDate("10/10/2020"));
            day.calcWeek();
            day.setWorkingDay(true);
            day.setWorkingHours(8.0);

            Set<Day> days = new HashSet();
            days.add(day);

            //Adiciona os dias ao calendário
            calendar.setDays(days);
        } catch (Exception ex) {
            Logger.getLogger(CalendarDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {

    }

    /**
     * Adiciona um calendário ao banco de dados
     */
    @Test
    public void testAdd() throws Exception {
        System.out.println("add");
        
        try {
            boolean result = dao.add(calendar);
            assertEquals(true, result);
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of modify method, of class CalendarDAO.
     */
    @Test
    public void testModify() throws Exception {
        System.out.println("modify");

        for ( Day day : calendar.getDays() ) {
            day.setWeek(30);
            break;
        }

        try {
            boolean result = dao.modify(calendar);
            assertEquals(true, result);
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }
    
    /**
     * Test of list method, of class CalendarDAO.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = dao.list();

            if ( list.size() > 0 ) {
                assertTrue(true);
            } else {
                assertFalse(true);
            }
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of list method, of class CalendarDAO.
     */
    @Test
    public void testList_String_Integer() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = dao.list("year",2020,"=");

            for ( Calendar c : list ) {
                System.out.println( "Calendário:" + c.getYear() + "\n" );
            }
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of alreadyExist method, of class CalendarDAO.
     */
    @Test
    public void testAlreadyExist() throws Exception {
        System.out.println("alreadyExist");

        Calendar c = new Calendar();
        c.setYear(2020);

        try {
            boolean result = dao.alreadyExist(c);
            assertEquals(true, result);
        } catch ( CalendarPersistenceException ex ) {
            fail( ex.getDetailMessage() );
        }
    }

    /**
     * Test of list method, of class CalendarDAO.
     */
    @Test
    public void testList_String_String() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = dao.list("year", "asc");

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
     * Test of list method, of class CalendarDAO.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");

        try {
            List<Calendar> list = dao.list("year", 2020, "=");

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
     * Test of listDays method, of class CalendarDAO.
     */
    @Test
    public void testListDays() throws Exception {
        System.out.println("listDays");

        try {
            Date startDate = DateUtil.formatDate("01/10/2020");
            Date endDate = DateUtil.formatDate("20/10/2020");

            List<Day> list = dao.listDays(startDate, endDate);

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
     * Test of getCalendar method, of class CalendarDAO.
     */
    @Test
    public void testGetCalendar() throws Exception {
        System.out.println("getCalendar");

        try {
            Calendar c = dao.getCalendar( 2020 );

            if ( c == null ) {
                fail("O dia deveria existir");
            }

            assertEquals(c.getYear().intValue(), 2020);
        } catch ( CalendarPersistenceException e ) {
            fail(e.getMessage() + "\n" + e.getDetailMessage());
        }
    }

    /**
     * Test of getDay method, of class CalendarDAO.
     */
    @Test
    public void testGetDay() throws Exception {
        System.out.println("getDay");

        try {
            Day day = dao.getDay( DateUtil.formatDate("10/10/2020") );

            if ( day == null ) {
                fail("O dia deveria existir");
            }

            assertEquals(DateUtil.formatDate("10/10/2020"), day.getDate());
        } catch ( DayPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }

    /**
     * Test of remove method, of class CalendarDAO.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");

        try {
            boolean result = dao.remove(calendar);
            assertEquals(true, result);
        } catch ( CalendarPersistenceException e ) {
            fail(e.getDetailMessage());
        }
    }
}