import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.apache.commons.csv.CSVRecord;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Double.parseDouble;
import static java.time.format.DateTimeFormatter.ofPattern;

@Data
@AllArgsConstructor
public class DateTemp {
    private static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    private LocalDateTime date;
    private double outdoorTemp;

    public static DateTemp toDateTemp(CSVRecord record){
        val timestamp = LocalDateTime.parse(record.get(0), FORMATTER);
        val outdoorTemp = Double.parseDouble(DECIMAL_FORMAT.format(parseDouble(record.get(3))));

        return new DateTemp(timestamp, outdoorTemp);
    }

}
