import java.time.LocalDateTime;

public class DecisionHelper {

    private static boolean isHome(LocalDateTime timestamp){
        int hour = timestamp.getHour();
        return !(hour > 9 && hour < 19);
    }

    public static Double chooseIndoorTemp(LocalDateTime timestamp, Double outdoorTemp){
        Double indoorTemp = isHome(timestamp) ? 23D : 10D;
        if(outdoorTemp > indoorTemp) indoorTemp = outdoorTemp;

        return indoorTemp;
    }

    public static Double calculateEnergyConsumption(Double outdoorTemp, Double indoorTemp) {
        return Math.abs(indoorTemp - outdoorTemp) * 0.75;
    }
}
