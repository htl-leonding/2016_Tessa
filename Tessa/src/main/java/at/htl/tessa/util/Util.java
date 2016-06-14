package at.htl.tessa.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Created by Korti on 09.06.2016.
 */
public class Util {

    public static long randomLong(Random random, long n) {
        long bits, val;
        do {
            bits = (random.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }

    public static long calculateDays(LocalDate date) {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

}
