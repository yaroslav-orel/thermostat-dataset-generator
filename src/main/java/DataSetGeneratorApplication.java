import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

public class DataSetGeneratorApplication {
    private static final String INPUT_CSV_FILE_NAME = "weatherHistory.csv";
    private static final String OUTPUT_CSV_FILE_NAME = "dataset.csv";

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup val reader = IOHelper.getReader(INPUT_CSV_FILE_NAME);
        @Cleanup val writer = IOHelper.getWriter(OUTPUT_CSV_FILE_NAME);

        @Cleanup val csvParser = CSVHelper.getCSVParser(reader);
        @Cleanup val csvPrinter = CSVHelper.getCSVPrinter(writer);

        CSVHelper.generateCSV(csvParser, csvPrinter);
    }

}
