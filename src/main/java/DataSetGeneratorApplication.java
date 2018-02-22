import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

public class DataSetGeneratorApplication {

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup val reader = IOHelper.getReader();
        @Cleanup val writer = IOHelper.getWriter();

        @Cleanup val csvParser = CSVHelper.getCSVParser(reader);
        @Cleanup val csvPrinter = CSVHelper.getCSVPrinter(writer);

        CSVHelper.generateCSV(csvParser, csvPrinter);
    }

}
