package com.svnavigatoru600.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Provides a set of static functions related to {@link Date}s.
 * 
 * @author Tomas Skalicky
 */
public class DateUtils {

    public static final Map<Locale, String> SHORT_DATE_FORMATS;
    public static final Map<Locale, String> MIDDLE_DATE_FORMATS;
    public static final Map<Locale, String> LONG_DATE_FORMATS;
    public static final Map<Locale, String> SHORT_DAY_FORMATS;
    public static final Map<Locale, String> LONG_MONTH_FORMATS;
    public static final String LONG_YEAR_FORMAT = "yyyy";
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+1");

    public static final Map<Locale, String> DEFAULT_DATE_TIME_FORMATS;
    /**
     * Format for the Tigra calendar.
     */
    public static final String CALENDAR_DATE_FORMAT = "d.M.yyyy";

    /**
     * Static constructor.
     */
    static {
        SHORT_DATE_FORMATS = new HashMap<Locale, String>();
        MIDDLE_DATE_FORMATS = new HashMap<Locale, String>();
        LONG_DATE_FORMATS = new HashMap<Locale, String>();
        SHORT_DAY_FORMATS = new HashMap<Locale, String>();
        LONG_MONTH_FORMATS = new HashMap<Locale, String>();
        DEFAULT_DATE_TIME_FORMATS = new HashMap<Locale, String>();

        Locale csCZLocale = new Locale("cs", "CZ");
        SHORT_DATE_FORMATS.put(csCZLocale, "d.M.yy");
        MIDDLE_DATE_FORMATS.put(csCZLocale, "d.M.yyyy");
        LONG_DATE_FORMATS.put(csCZLocale, "d. MMMM yyyy");
        SHORT_DAY_FORMATS.put(csCZLocale, "d");
        LONG_MONTH_FORMATS.put(csCZLocale, "MMMM");
        DEFAULT_DATE_TIME_FORMATS.put(csCZLocale, "d. MMMM yyyy H:mm");
    }

    /**
     * Formats the given <code>date</code> according to the given <code>format</code>. The <code>locale</code>
     * is important for localization.
     * 
     * @return Formatted <code>date</code>
     */
    public static String format(Date date, String format, Locale locale) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        formatter.setTimeZone(DateUtils.TIME_ZONE);
        return formatter.format(date);
    }

    /**
     * Gets the today date. It means that hours, minutes, seconds and so on are set to zero.
     */
    public static Date getToday() {
        Date date = new Date(); // timestamp now
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTimeZone(DateUtils.TIME_ZONE);
        cal.setTime(date); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hours to midnight
        cal.set(Calendar.MINUTE, 0); // set minutes in hour
        cal.set(Calendar.SECOND, 0); // set seconds in minute
        cal.set(Calendar.MILLISECOND, 0); // set milliseconds in second
        return cal.getTime();
    }
}
