package com.proschedule.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Classe de utilidades para datas
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class DateUtil {
    /**
     * Converte uma String para um objeto Date. Caso a String seja vazia ou nula,
     * retorna null - para facilitar em casos onde formulários podem ter campos
     * de datas vazios.
     * @param date String no formato dd/MM/yyyy a ser formatada
     * @return Date Objeto Date ou null caso receba uma String vazia ou nula
     * @throws DateUtilException
     */
    public static Date formatDate(String date) throws DateUtilException {
        if (date == null || date.equals(""))
            return null;

        Date formatedDate = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatedDate = (java.util.Date)formatter.parse(date);
        } catch (ParseException e) {
            throw new DateUtilException(e, "Fomato de data inválido. Formato correto: dd/mm/yyyy.");
        }
        return formatedDate;
    }

    /**
     * Converte ano, mês e dia como inteiros para formato de data.
     * 
     * @param year <Integer> Ano da data
     * @param month <Integer> Mês da data
     * @param day <Integer> Dia da data
     * @return <Date> A data formatada
     */
    public static Date formatDate(Integer year, Integer month, Integer day) {
        //Verifica se os valores não estã vazios
        if (year == null || month == null || day == null ) {
            return null;
        }

        //Verifica se o ano está correto
        if ( String.valueOf(year).length() != 4 ) {
            return null;
        }

        String date;

        //Adiciona os dias
        if ( day < 10 ) {
            date = "0" + String.valueOf(day) + "/";
        } else {
            date = String.valueOf(day) + "/";
        }

        //Adiciona os meses
        if ( month < 10 ) {
            date += "0" + String.valueOf(month) + "/";
        } else {
            date += String.valueOf(month) + "/";
        }

        //Adiciona o ano
        date += String.valueOf(year);

        try {
            return DateUtil.formatDate(date);
        } catch (DateUtilException ex) {
            return null;
        }
    }

    /**
     * Transforma uma string em uma data do tipo DateTime
     * @param strDate Data de entrada no formado dd/MM/YYYY
     * @return
     */
    public static DateTime toDateTime( String strDate ) {
        String arrDate[] = strDate.split("/");

        if ( arrDate.length < 3 ) {
            return null;
        }

        String toFormat = arrDate[2] + "-" + arrDate[1] + "-" + arrDate[0];

        DateTimeFormatter parser = ISODateTimeFormat.date();
        DateTime date = parser.parseDateTime( toFormat );

        return date;
    }

    /**
     * Transforma uma data do tipo Date para DateTime
     * @param date
     * @return
     */
    public static DateTime toDateTime( Date date ) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = format.format( date );
        
        String arrDate[] = strDate.split("/");

        if ( arrDate.length < 3 ) {
            return null;
        }

        String toFormat = arrDate[2] + "-" + arrDate[1] + "-" + arrDate[0];

        DateTimeFormatter parser = ISODateTimeFormat.date();
        DateTime dateTime = parser.parseDateTime( toFormat );

        return dateTime;
    }

    /**
     * Transforma uma data em string.
     * 
     * @param date A data a ser transformada
     * @return String no formato dd-MM-yyyy
     */
    public static String toString( Date date ) {
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat dayFormat = new SimpleDateFormat("dd");

        String toString = dayFormat.format(date) + "-" + monthFormat.format(date)
                + "-" + yearFormat.format(date);

        return toString;
    }

    /**
     * Faz a comparação entre o ano, mês e dia de duas datas para
     * verificar se elas são ou não iguais.
     * 
     * @param dateOne <Date> Primeira data para comparação
     * @param dateTwo <Date> Segunda data para comparação
     * @return <boolean> True se as datas forem iguais ou false se forem diferentes.
     */
    public static boolean compareDates( Date dateOne , Date dateTwo ) {
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat dayFormat = new SimpleDateFormat("dd");

        if ( yearFormat.format( dateOne ).equals( yearFormat.format( dateTwo ) )
                && monthFormat.format( dateOne ).equals( monthFormat.format( dateTwo ) )
                && dayFormat.format( dateOne ).equals( dayFormat.format( dateTwo ) ) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna o nome do dia da semana da data informada.
     * 
     * @param date <Date> A data a ser informada
     * @return <String> O nome do dia da semana
     */
    public static String getDayOfWeekString( Date date ) {
        String[] days = new String[7];
        days[0] = "Domingo";
        days[1] = "Segunda";
        days[2] = "Terça";
        days[3] = "Quarta";
        days[4] = "Quinta";
        days[5] = "Sexta";
        days[6] = "Sábado";

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);

        int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);

        return days[ dayOfWeek - 1 ];
    }

    /**
     * Recupera o ano atual
     * @return O ano atual
     */
    public static int getCurrentYear() {
        DateTime dateTime = new DateTime();
        return dateTime.getYear();
    }
}
