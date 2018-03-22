import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.google.common.collect.Streams.stream;
import static java.lang.Double.parseDouble;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;

public class CSVHelper {
    private static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    @SneakyThrows
    public static void generateCSV(CSVParser parser, CSVPrinter printer){
        stream(parser)
                .map(CSVHelper::toDataSetRow)
                .forEach(row -> printRecord(printer, row));

        printer.flush();
    }

    @SneakyThrows
    public static CSVPrinter getCSVPrinter(Writer writer){
        return new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Timestamp", "Outdoor Temp", "Indoor Temp", "Energy Consumption"));
    }

    @SneakyThrows
    public static CSVParser getCSVParser(Reader reader){
        return new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withSkipHeaderRecord());
    }

    public static void printRecord(CSVPrinter printer, List<String> row){
        try {
            printer.printRecord(row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> toDataSetRow(CSVRecord record){
        val timestamp = LocalDateTime.parse(record.get(0), FORMATTER);
        val outdoorTemp = Double.parseDouble(DECIMAL_FORMAT.format(parseDouble(record.get(3))));

        val isSomeoneHome = DecisionHelper.isSomeoneHome(timestamp);
        val indoorTemp = DecisionHelper.chooseIndoorTemp(outdoorTemp, isSomeoneHome);
        val energyConsumption = DecisionHelper.calculateEnergyConsumption(outdoorTemp, indoorTemp, isSomeoneHome);

        return asList(
                timestamp.toString(),
                Double.toString(outdoorTemp),
                Double.toString(indoorTemp),
                Double.toString(energyConsumption)
        );
    }
}
