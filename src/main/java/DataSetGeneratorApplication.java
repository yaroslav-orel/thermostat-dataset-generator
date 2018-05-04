import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import one.util.streamex.EntryStream;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Streams.stream;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class DataSetGeneratorApplication {

    private static final String INPUT_CSV_FILE_NAME = "weatherHistory.csv";
    private static final String OUTPUT_CSV_FILE_NAME = "dataset2.csv";

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup val reader = IOHelper.getReader(INPUT_CSV_FILE_NAME);
        @Cleanup val writer = IOHelper.getWriter(OUTPUT_CSV_FILE_NAME);

        @Cleanup val csvParser = CSVHelper.getCSVParser(reader);
        @Cleanup val csvPrinter = CSVHelper.getCSVPrinter(writer);

        val rawDS = stream(csvParser)
                .map(DateTemp::toDateTemp)
                .map(DataSetGeneratorApplication::toDataSetRow)
                .collect(Collectors.toList());

        val smoothedDS = smoothenDataset(rawDS);

        smoothedDS.forEach(row -> CSVHelper.printRecord(csvPrinter, row));
        csvPrinter.flush();
    }

    private static List<List<String>> smoothenDataset(List<List<String>> rawDS) {
        EntryStream.of(rawDS).forKeyValue((index, value) -> smothenPeriodChange(rawDS, index, value));

        return rawDS;
    }

    public static List<String> toDataSetRow(DateTemp dateTemp){
        val indoorTemp = DecisionHelper.chooseIndoorTemp(dateTemp);
        val energyConsumption = DecisionHelper.calculateEnergyConsumption(dateTemp.getOutdoorTemp(), indoorTemp);

        return asList(
                dateTemp.getDate().toString(),
                Double.toString(dateTemp.getOutdoorTemp()),
                Double.toString(indoorTemp),
                Double.toString(energyConsumption)
        );
    }

    private static void smothenPeriodChange(List<List<String>> ds, Integer index, List<String> current) {
        if (index == 0)
            return;

        val previous = ds.get(index - 1);

        val prevIndoorTemp = Double.parseDouble(previous.get(2));
        val curIndoorTemp = Double.parseDouble(current.get(2));
        val difference = Math.abs(prevIndoorTemp - curIndoorTemp);

        if (difference > 5) {
            val smoothedTemp = prevIndoorTemp > curIndoorTemp ?
                    prevIndoorTemp - (difference / 2D) :
                    prevIndoorTemp + (difference / 2D);
            current.set(2, Double.toString(smoothedTemp));

            System.out.println(format("Smoothed indoor temp on date %s from %s to %s because previous one was %s",
                    current.get(0),
                    curIndoorTemp,
                    smoothedTemp,
                    prevIndoorTemp));
        }
    }
}
