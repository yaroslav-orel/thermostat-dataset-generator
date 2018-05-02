import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.collect.Streams.stream;
import static java.lang.Double.parseDouble;
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
        val isSomeoneHome = DecisionHelper.isSomeoneHome(dateTemp.getDate());
        val indoorTemp = DecisionHelper.chooseIndoorTemp(dateTemp.getOutdoorTemp(), isSomeoneHome);
        val energyConsumption = DecisionHelper.calculateEnergyConsumption(dateTemp.getOutdoorTemp(), indoorTemp, isSomeoneHome);

        return asList(
                dateTemp.getDate().toString(),
                Double.toString(dateTemp.getOutdoorTemp()),
                Double.toString(indoorTemp),
                Double.toString(energyConsumption)
        );
    }

}
