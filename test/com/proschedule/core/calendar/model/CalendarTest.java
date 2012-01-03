/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.calendar.model;

import java.util.List;
import java.util.Date;
import com.proschedule.util.date.DateUtil;
import java.util.Set;
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
public class CalendarTest {
    private Calendar calendar;

    public CalendarTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        calendar = new Calendar();
        calendar.setYear(2010);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of generateDaysOfYear method, of class Calendar.
     */
    @Test
    public void testGenerateDaysOfYear() throws Exception {
        System.out.println("generateDaysOfYear");

        try {
            calendar.generateDaysOfYear(8.0, false);

            for ( Day d : calendar.getDays() ) {
                System.out.println( "Dia: " + d.getDate() + "\n" );
            }
        } catch ( Exception e ) {
            fail( "Erro em generateDaysOfYear: " + e.getMessage() );
        }
    }

    @Test
    public void testGetDay() throws Exception {
        System.out.println("getDay");

        Calendar c = new Calendar();
        c.setYear(2010);
        c.generateDaysOfYear(8.0, true);

        Date date = DateUtil.formatDate("10/10/2010");

        Day day = c.getDay( date );
        
        if ( day == null ) {
            fail("O dia está nulo.");
        } else {
            System.out.println( "DIA: " + day.getDate() );
        }   
    }

    @Test
    public void testGetDaysPerMonth() throws Exception {
        System.out.println("getDaysPerMonth");

        Calendar c = new Calendar();
        c.setYear(2010);
        c.generateDaysOfYear(8.0, true);

        List<Day> list = c.getDaysPerMonth( 6 );

        for ( Day day : list ) {
            System.out.println( day.getDate() );
        }
    }

}