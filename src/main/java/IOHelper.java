import lombok.SneakyThrows;
import lombok.val;

import java.io.*;

public class IOHelper {
    private static final String INPUT_CSV_FILE_NAME = "weatherHistory.csv";
    private static final String OUTPUT_CSV_FILE_NAME = "dataset.csv";

    @SneakyThrows
    public static Reader getReader(){
        val csv = ClassLoader.getSystemClassLoader().getResource(INPUT_CSV_FILE_NAME).getFile();

        return new BufferedReader(new FileReader(csv));
    }

    @SneakyThrows
    public static Writer getWriter(){
        return new BufferedWriter(new FileWriter(new File(OUTPUT_CSV_FILE_NAME)));
    }
}
