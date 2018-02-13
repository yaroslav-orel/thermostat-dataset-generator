import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.collect.Streams.stream;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;

public class DataSetGenerator {

    private static final String CSV_FILE_PATH = "weatherHistory.csv";
    private static final DateTimeFormatter formatter = ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

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
    }

    private static DataSetRow toDataSetRow(CSVRecord record){
        return new DataSetRow(
                LocalDateTime.parse(record.get(0), formatter),
                Double.parseDouble(record.get(3))
                );
    }
}
