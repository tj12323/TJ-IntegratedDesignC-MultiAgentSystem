package MiniDidi.shared.config;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author EddieLe@AutomationLine
 * @version 1.1
 * @since 2025/10/23 12:27
 */

@Data
@NoArgsConstructor
public class PresetData {
        private int scenario;
    private List<CarPreset> cars;
    private List<PassengerPreset> passengers;

    @Data
    @NoArgsConstructor
    public static class CarPreset {
        private int index;
        private double x;
        private double y;
    }

    @Data
    @NoArgsConstructor
    public static class PassengerPreset {
        private int index;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
    }
}
