import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import java.util.List;

import static com.google.common.collect.Streams.stream;
import static java.util.Arrays.asList;

public class DataSetGeneratorApplication {

    private static final String INPUT_CSV_FILE_NAME = "weatherHistory.csv";
    private static final String OUTPUT_CSV_FILE_NAME = "dataset1.csv";

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup val reader = IOHelper.getReader(INPUT_CSV_FILE_NAME);
        @Cleanup val writer = IOHelper.getWriter(OUTPUT_CSV_FILE_NAME);

        @Cleanup val csvParser = CSVHelper.getCSVParser(reader);
        @Cleanup val csvPrinter = CSVHelper.getCSVPrinter(writer);

        stream(csvParser)
                .map(DateTemp::toDateTemp)
                .map(DataSetGeneratorApplication::toDataSetRow)
                .forEach(row -> CSVHelper.printRecord(csvPrinter, row));

        csvPrinter.flush();
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

}
