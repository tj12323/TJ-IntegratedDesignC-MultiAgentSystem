package MiniDidi.map.adapters.format;

import lombok.Data;

/**
 * Description:
 * <p>
 * 地图数据传输对象 (Data Transfer Object) 类。
 * 用于在Controller层与客户端之间传递地图数据，将地图网格及其维度信息进行结构化封装。
 * </p>
 * 
 * 该Dto包含：
 * @param width 地图的宽度（列数） int
 * @param height 地图的高度（行数） int
 * @param grid 地图的二维网格数据，true表示障碍物，false表示可通行区域 boolean[][]
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 16:51
 */
@Data
public class MapDto {
    private int width;
    private int height;
    private boolean[][] grid; // 地图的网格数据

    public MapDto(boolean[][] grid) {
        if (grid == null || grid.length == 0) {
            this.height = 0;
            this.width = 0;
            this.grid = new boolean[0][0];
        } else {
            this.height = grid.length;
            this.width = grid[0].length;
            this.grid = grid;
        }
    }
}
