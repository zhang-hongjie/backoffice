package fr.zhj2074.backoffice;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import fr.zhj2074.backoffice.exception.AppException;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Dates {

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
    private static final Locale DEFAULT_LOCALE = Locale.FRENCH;
    private static final FastDateFormat ISO_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            DEFAULT_TIMEZONE,
            DEFAULT_LOCALE);

    public static Date create(int year) {
        return create(year, 1, 1, 0, 0, 0, 0);
    }

    public static Date create(int year, int month, int day) {
        return create(year, month, day, 0, 0, 0, 0);
    }

    public static Date create(int year, int month, int day, int hour, int min, int sec) {
        return create(year, month, day, hour, min, sec, 0);
    }

    public static Date create(int year, int month, int day, int hour, int min, int sec, int ms) {
        Calendar cal = Calendar.getInstance(DEFAULT_TIMEZONE);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, ms);
        return cal.getTime();
    }

    public static String toIsoString(Date date) {
        return ISO_FORMAT.format(date);
    }

    public static Date fromIsoString(String dateStr) {
        try {
            return new StdDateFormat(DEFAULT_TIMEZONE, DEFAULT_LOCALE).parse(dateStr);
        } catch (ParseException e) {
            throw new AppException("Unparseable date: '" + dateStr + "'", e);
        }
    }
}
