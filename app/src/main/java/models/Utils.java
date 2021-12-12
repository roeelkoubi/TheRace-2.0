package models;

import java.time.Duration;
public class Utils {
    private Utils(){}

    public static String getTimeString(long time) {
        long minutes = time / 1000 / 60;
        long seconds = time/ 1000 %60;
        Duration d = Duration.ofMillis(time);
        long ms1 = d.toMillis() - (seconds*1000);
        return minutes + ":" + seconds + ":" + ms1;
    }
}
