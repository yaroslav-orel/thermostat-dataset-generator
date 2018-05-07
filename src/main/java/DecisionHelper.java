import lombok.experimental.var;
import lombok.val;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.concurrent.ThreadLocalRandom.current;

public class DecisionHelper {

    public static Double chooseIndoorTemp(DateTemp dateTemp) {
        var desiredTemp = getDesiredTemp(dateTemp.getDate());

        if (dateTemp.getOutdoorTemp() >= desiredTemp)
            desiredTemp = dateTemp.getOutdoorTemp() + current().nextDouble(1, 3);

        return desiredTemp;
    }

    public static Double getDesiredTemp(LocalDateTime date) {
        if (date.getDayOfWeek().equals(SATURDAY) || date.getDayOfWeek().equals(SUNDAY))
            return 23D;

        switch (getDayPeriod(date)) {
            case NIGHT:
                return 20D;
            case MORNING:
                return 20D + current().nextDouble(1, 3);
            case WORKING_HOURS:
                return 10D;
            default:
                return 23D;
        }
    }

    //TODO Figure out the way to calculate this correctly
    public static Double calculateEnergyConsumption(Double outdoorTemp, Double indoorTemp) {
        val difference = Math.abs(indoorTemp - outdoorTemp);

        if (difference < 1)
            return 0.1D;
        else if (difference >= 1 && difference < 3)
            return difference * 0.50;
        if (difference >= 3 && difference < 5)
            return difference * 0.75;
        else
            return difference * 0.85;
    }

    public static DayPeriod getDayPeriod(LocalDateTime date) {
        val dayHour = date.getHour();

        if (dayHour < 6)
            return DayPeriod.NIGHT;
        if (dayHour >= 6 && dayHour < 10)
            return DayPeriod.MORNING;
        if (dayHour >= 10 && dayHour < 19)
            return DayPeriod.WORKING_HOURS;

        return DayPeriod.EVENING;
    }
}
