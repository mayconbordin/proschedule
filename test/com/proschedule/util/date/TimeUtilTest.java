/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.date;

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
public class TimeUtilTest {

    public TimeUtilTest() {
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
     * Test of parseDouble method, of class TimeUtil.
     */
    @Test
    public void testParseDouble() {
        System.out.println("parseDouble");
        double hours = 2.5;
        double time[] = TimeUtil.parseDouble(hours);

        assertEquals(time[0], 2.0, 0);
        assertEquals(time[1], 30.0, 0);
        assertEquals(time[2], 0.0, 0);

        System.out.println(time[0] + " horas " + time[1] + " minutos " + time[2] + " segundos");
    }

}