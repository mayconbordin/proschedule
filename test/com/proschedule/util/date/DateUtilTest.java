/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.date;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
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
public class DateUtilTest {

    public DateUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of formatDate method, of class DateUtil.
     */
    @Test
    public void testFormatDate_String() throws Exception {
        System.out.println("formatDate");
        
    }

    /**
     * Test of formatDate method, of class DateUtil.
     */
    @Test
    public void testFormatDate_3args() {
        System.out.println("formatDate");
        
    }

    /**
     * Test of compareDates method, of class DateUtil.
     */
    @Test
    public void testCompareDates() {
        System.out.println("compareDates");
        
    }

    /**
     * Test of getDayOfWeekString method, of class DateUtil.
     */
    @Test
    public void testGetDayOfWeekString() {
        System.out.println("getDayOfWeekString");
        
    }

    /**
     * Test of toDateTime method, of class DateUtil.
     */
    @Test
    public void testToDateTime_String() {
        System.out.println("toDateTime");

        DateTime dt = DateUtil.toDateTime("10/10/2010");

        if ( !dt.toString().equals( "2010-10-10T00:00:00.000-03:00" ) ) {
            fail("Data incorreta.");
        }

        System.out.println( dt.toString() );
    }

    /**
     * Test of toDateTime method, of class DateUtil.
     */
    @Test
    public void testToDateTime_Date() {
        try {
            System.out.println("toDateTime");

            Date date = DateUtil.formatDate("10/10/2010");

            DateTime dt = DateUtil.toDateTime(date);
            if (!dt.toString().equals("2010-10-10T00:00:00.000-03:00")) {
                fail("Data incorreta.");
            }
            System.out.println(dt.toString());
        } catch (Exception ex) {
            fail("Erro ao parsear data.");
        }
    }

}