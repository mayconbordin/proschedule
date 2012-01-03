/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.string;

/**
 *
 * @author Maycon-VM
 */
public class StringUtil {

    /**
     * 
     * @param string
     * @return
     */
    public static String removeInvalidSheetChars( String string ) {

        string = string.replace('*', '-');
        string = string.replace('?', '-');
        string = string.replace('\\', '-');
        string = string.replace('/', '-');
        string = string.replace('[', '-');
        string = string.replace(']', '-');

        return string;
    }
}
