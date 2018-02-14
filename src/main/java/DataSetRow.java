import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DataSetRow {
    final LocalDateTime timestamp;
    final Double outdoorTemp;
    Double indoorTemp;
    Double energyConsumption;
}
