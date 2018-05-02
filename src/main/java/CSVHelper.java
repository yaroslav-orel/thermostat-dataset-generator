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

    @SneakyThrows
    public static void printRecord(CSVPrinter printer, List<String> row){
        printer.printRecord(row);
    }
}
