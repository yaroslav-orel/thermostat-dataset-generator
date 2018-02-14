import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.collect.Streams.stream;
import static java.lang.Double.parseDouble;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;

public class DataSetGenerator {

    private static final String CSV_FILE_PATH = "weatherHistory.csv";
    private static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    @SneakyThrows
    public static void main(String[] args) {
        val csv = ClassLoader.getSystemClassLoader().getResource(CSV_FILE_PATH).getFile();
        @Cleanup val reader = new BufferedReader(new FileReader(csv));
        @Cleanup val csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withSkipHeaderRecord());

        val dataSet = stream(csvParser)
                .map(DataSetGenerator::toDataSetRow)
                .collect(toList());

        dataSet.stream().forEach(System.out::println);
    }

    private static DataSetRow toDataSetRow(CSVRecord record){
        val timestamp = LocalDateTime.parse(record.get(0), FORMATTER);
        val outdoorTemp = parseDouble(DECIMAL_FORMAT.format(parseDouble(record.get(3))));
        val indoorTemp = chooseIndoorTemp(timestamp, outdoorTemp);

        return new DataSetRow(timestamp, outdoorTemp, indoorTemp);
    }

    private static boolean isHome(LocalDateTime timestamp){
        int hour = timestamp.getHour();
        return !(hour > 9 && hour < 19);
    }

    private static Double chooseIndoorTemp(LocalDateTime timestamp, Double outdoorTemp){
        Double indoorTemp = isHome(timestamp) ? 23D : 10D;
        if(outdoorTemp > indoorTemp) indoorTemp = outdoorTemp;

        return indoorTemp;
    }
}
