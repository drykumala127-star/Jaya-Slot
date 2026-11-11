package k0;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class q {
    public static String a(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }
}
