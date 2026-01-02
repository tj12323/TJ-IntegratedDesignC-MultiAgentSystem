package MiniDidi.map.adapters;

import MiniDidi.map.adapters.format.MapDto;
import MiniDidi.map.adapters.format.MapMapper;
import MiniDidi.map.application.MapService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * <p>
 * 地图控制器
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 16:17
 */
@RestController
@RequestMapping("/api/maps")
//logger 注释 需要恢复
public class MapController {
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);
    private final MapService mapService;
    private final MapMapper mapMapper;

    @Autowired
    public MapController(MapService mapService, MapMapper mapMapper) {
        this.mapService = mapService;
        this.mapMapper = mapMapper;
    }

    /**
     * 获取当前仿真的地图资源
     *
     * @return 包含地图网格和维度信息的DTO
     */
    @GetMapping
    public ResponseEntity<MapDto> getMap() {
        try {
            // logger.info("请求获取地图信息...");
            boolean[][] mapGrid = mapService.getReverseMap();

            // 使用Mapper将原始数据转换为DTO
            MapDto mapDto = mapMapper.toDto(mapGrid);

            logger.info("map size: {}x{}", mapDto.getWidth(), mapDto.getHeight());
            return ResponseEntity.ok(mapDto);
        } catch (IllegalStateException e) {
            // 如果地图尚未初始化，返回一个错误码
            // logger.error("地图资源尚未准备好: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
