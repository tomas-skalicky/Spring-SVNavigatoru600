package com.svnavigatoru600.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Maps;

/**
 * Provides a set of static functions related to {@link Date Dates}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class DateUtils {

    /**
     * d.M.yy
     */
    public static final Map<Locale, String> SHORT_DATE_FORMATS;
    /**
     * d.M.yyyy
     */
    public static final Map<Locale, String> MIDDLE_DATE_FORMATS;
    /**
     * d. MMMM yyyy
     */
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
        SHORT_DATE_FORMATS = Maps.newHashMap();
        MIDDLE_DATE_FORMATS = Maps.newHashMap();
        LONG_DATE_FORMATS = Maps.newHashMap();
        SHORT_DAY_FORMATS = Maps.newHashMap();
        LONG_MONTH_FORMATS = Maps.newHashMap();
        DEFAULT_DATE_TIME_FORMATS = Maps.newHashMap();

        final Locale csCZLocale = new Locale("cs", "CZ");
        SHORT_DATE_FORMATS.put(csCZLocale, "d.M.yy");
        MIDDLE_DATE_FORMATS.put(csCZLocale, "d.M.yyyy");
        LONG_DATE_FORMATS.put(csCZLocale, "d. MMMM yyyy");
        SHORT_DAY_FORMATS.put(csCZLocale, "d");
        LONG_MONTH_FORMATS.put(csCZLocale, "MMMM");
        DEFAULT_DATE_TIME_FORMATS.put(csCZLocale, "d. MMMM yyyy H:mm");
    }

    private DateUtils() {
    }

    /**
     * Formats the given <code>date</code> according to the given <code>format</code>. The <code>locale</code> is
     * important for localization.
     *
     * @return Formatted <code>date</code>
     */
    public static String format(final Date date, final String format, final Locale locale) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        formatter.setTimeZone(DateUtils.TIME_ZONE);
        return formatter.format(date);
    }

    /**
     * Gets the today date. It means that hours, minutes and so on are set to zero. In other words, the returned
     * {@link Date} could be printed out in the format:
     * <p>
     * <code>YYYY-MM-DD</code> 00:00:00.000
     * <p>
     * where <code>YYYY-MM-DD</code> represents today.
     */
    public static Date getToday() {
        return DateUtils.getDay(0);
    }

    /**
     * Gets the yesterday date. It means that hours, minutes and so on are set to zero. In other words, the returned
     * {@link Date} could be printed out in the format:
     * <p>
     * (<code>YYYY-MM-DD</code> - 1) 00:00:00.000
     * <p>
     * where <code>YYYY-MM-DD</code> represents today.
     */
    public static Date getYesterday() {
        return DateUtils.getDay(-1);
    }

    /**
     * Gets the tomorrow date. It means that hours, minutes and so on are set to zero. In other words, the returned
     * {@link Date} could be printed out in the format:
     * <p>
     * (<code>YYYY-MM-DD</code> + 1) 00:00:00.000
     * <p>
     * where <code>YYYY-MM-DD</code> represents today.
     */
    public static Date getTomorrow() {
        return DateUtils.getDay(1);
    }

    /**
     * Gets the date of particular day. It means that hours, minutes and so on are set to zero. In other words, the
     * returned {@link Date} could be printed out in the format:
     * <p>
     * (<code>YYYY-MM-DD</code> + <code>dayOffsetFromToday</code>) 00:00:00.000
     * <p>
     * where <code>YYYY-MM-DD</code> represents today.
     *
     * @param dayOffsetFromToday
     *            The number of days from today specifies the particular day. Can be both positive and negative.
     */
    public static Date getDay(final int dayOffsetFromToday) {
        final Date now = new Date();
        return DateUtils.getDay(now, dayOffsetFromToday);
    }

    /**
     * Gets the date of particular day. It means that hours, minutes and so on are set to zero. In other words, the
     * returned {@link Date} could be printed out in the format:
     * <p>
     * (<code>YYYY-MM-DD</code> + <code>dayOffset</code>) 00:00:00.000
     * <p>
     * where <code>YYYY-MM-DD</code> represents the day of the given <code>date</code>.
     *
     * @param date
     *            Date on which we apply the given <code>dayOffset</code> to get the particular day.
     * @param dayOffset
     *            The number of days from <code>date</code>. Can be both positive and negative.
     */
    public static Date getDay(final Date date, final int dayOffset) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(DateUtils.TIME_ZONE);
        calendar.setTime(date);

        // Sets time exactly to midnight, i.e. 00:00:00.000
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        return calendar.getTime();
    }
}
