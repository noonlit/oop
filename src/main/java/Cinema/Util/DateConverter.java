package Cinema.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    public static final String dateTimeFormat = "dd/MM/yyyy 'at' HH:mm";
    public static final String timeFormat = "HH:mm";

    public static final String dateTimeFormatExample = "15/04/2020 at 18:00";
    public static final String timeFormatExample = "18:00";

    /**
     * Converts the given datetime string to a LocalDateTime object.
     *
     * @param dateTimeString The string, e.g. "15/04/2020 at 18:00"
     * @return The LocalDateTime object.
     */
    public static LocalDateTime toDateTimeFromString(String dateTimeString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Converts the given time string to a LocalDateTime object.
     *
     * @param timeString The string, e.g. "08:30"
     * @return The LocalDateTimeObject.
     * @throws ParseException
     */
    public static LocalDateTime toTimeFromString(String timeString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);

        Date date = format.parse(timeString);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Returns a string representation of the given date object.
     *
     * @param date The date
     * @return The string representation.
     */
    public static String dateTimeToString(LocalDateTime date)
    {
        return date.toString();
    }
}
