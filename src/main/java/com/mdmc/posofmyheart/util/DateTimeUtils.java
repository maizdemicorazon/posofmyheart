package com.mdmc.posofmyheart.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class DateTimeUtils {

    /**
     * Genera un LocalDateTime para la fecha indicada, con hora aleatoria
     * entre las 17:00 y las 22:00 (inclusive), y minutos/segundos aleatorios.
     *
     * @param date la fecha para la que se genera el LocalDateTime
     * @return LocalDateTime con hora aleatoria entre 17:00 y 22:00
     */
    public static LocalDateTime randomEveningDateTime(LocalDate date) {
        final int START_HOUR = 17;
        final int END_HOUR   = 22;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        int hour   = rnd.nextInt(START_HOUR, END_HOUR + 1);
        int minute = rnd.nextInt(0, 60);
        int second = rnd.nextInt(0, 60);

        return date.atTime(hour, minute, second);
    }

}
