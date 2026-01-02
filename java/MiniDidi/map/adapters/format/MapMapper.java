package MiniDidi.map.adapters.format;

import org.springframework.stereotype.Component;

/**
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 13:51
 */

@Component
public class MapMapper {
    public MapDto toDto(boolean[][] mapGrid) {
        return new MapDto(mapGrid);
    }
}
