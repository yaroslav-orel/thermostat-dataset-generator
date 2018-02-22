import lombok.SneakyThrows;
import lombok.val;

import java.io.*;

public class IOHelper {

    @SneakyThrows
    public static Reader getReader(String resourceFileName){
        val csv = ClassLoader.getSystemClassLoader().getResource(resourceFileName).getFile();

        return new BufferedReader(new FileReader(csv));
    }

    @SneakyThrows
    public static Writer getWriter(String outputFileName){
        return new BufferedWriter(new FileWriter(new File(outputFileName)));
    }
}
