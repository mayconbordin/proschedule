package com.proschedule.util.date;

/**
 *
 * @author Maycon Bordin
 */
public class TimeUtil {

    /**
     * Converte um número flutuante correspondente ao número de horas em um array
     * de horas minutos e segundos.
     * 
     * @param hoursValue Valor das horas a ser convertido
     * @return Array com as horas (índice 0), minutos (índice 1) e segundos (índice 2)
     */
    public static double[] parseDouble( double hoursValue ) {
        double hours = Math.floor( hoursValue ); //get the total whole hours
        double minutes = hoursValue * 60; //60 minutes in an hour or 205.2 minutes
        minutes = minutes - (hours * 60); // 60 minutes * 3 hours = 25.2 minutes left
        double seconds = minutes * 60; //60 seconds in a minute or 1512 seconds
        minutes = Math.floor(minutes); // total whole minutes
        seconds = seconds - (minutes * 60); // total seconds minus the whole minutes or 1512 - 1500 = 12 seconds

        double time[] = { hours, minutes, seconds };

        return time;
    }
}
