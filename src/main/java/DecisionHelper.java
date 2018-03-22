import lombok.val;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.concurrent.ThreadLocalRandom.current;

public class DecisionHelper {

    public static boolean isSomeoneHome(LocalDateTime timestamp){
        val dayOfWeek = timestamp.getDayOfWeek();
        if(dayOfWeek.equals(SATURDAY) || dayOfWeek.equals(SUNDAY))
            return true;
        else {
            int hour = timestamp.getHour();
            return !(hour > 9 && hour < 19);
        }
    }

    public static Double chooseIndoorTemp(Double outdoorTemp, boolean isSomeoneHome){
        Double indoorTemp = isSomeoneHome ? 23D : 10D;
        if(outdoorTemp >= indoorTemp)
            indoorTemp = outdoorTemp + current().nextInt(2, 5);

        return indoorTemp;
    }

    public static Double calculateEnergyConsumption(Double outdoorTemp, Double indoorTemp, boolean isSomeoneHome) {
        if (isSomeoneHome) return Math.abs(indoorTemp - outdoorTemp) * 0.75;
        else return 0D;
    }
}
