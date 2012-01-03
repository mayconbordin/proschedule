/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.core.calendar.model;

import com.proschedule.util.date.DateUtil;
import java.util.Date;
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
public class DayTest {

    private Day day;

    public DayTest() {
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
            day = new Day();
            day.setDate(DateUtil.formatDate("01/01/2010"));
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calcWeek method, of class Day.
     */
    @Test
    public void testCalcWeek() {
        System.out.println("calcWeek");

        day.calcWeek();

        Integer expectedWeek = 1;

        assertEquals(expectedWeek, day.getWeek());
    }

}