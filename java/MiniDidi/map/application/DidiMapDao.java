package MiniDidi.map.application;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * 地图数据访问接口。
 * <p>
 * 提供地图数据的加载、保存、获取和更新功能。
 * 支持从黑白位图文件加载地图信息，以及从序列化文件中加载或保存地图对象。
 * </p>
 *
 * 主要功能包括：
 * <ul>
 *     <li>从黑白位图文件解析地图数据</li>
 *     <li>从序列化文件加载地图对象</li>
 *     <li>保存地图对象到序列化文件</li>
 *     <li>获取和设置当前的地图对象</li>
 * </ul>
 *
 * @author EddieLe@Automation
 * @version 1.0
 * @since 2025/10/24 11:12
 */
public interface DidiMapDao {
    /**
     * 从文件加载地图
     * @param mapFile 地图文件
     */
    void loadMapFile(File mapFile);

    /**
     * 从地图对象序列化文件中读取地图
     *
     * @param mapObjectFile 地图对象序列化文件目录
     * @throws IOException            文件读取异常
     * @throws ClassNotFoundException 类未找到异常
     */
    void loadMapObject(File mapObjectFile) throws IOException, ClassNotFoundException;

    /**
     * 保存地图对象到文件
     * @param mapObject 地图对象序列化文件目录
     */
    void saveMapObject(File mapObject);

    /**
     * 获取地图对象
     *
     * @return 地图对象
     */
    boolean[][] getMap();

    /**
     * 设置地图对象
     *
     * @param map 地图对象
     */
    void updateMap(boolean[][] map);

    /**
     * 获取地图行数
     *
     * @return 地图行数
     */
    int selectMapRows();

    /**
     * 获取地图列数
     *
     * @return 地图列数
     */
    int selectMapCols();

    /**
     * 获取地图中指定位置是否为障碍物
     *
     * @param row 行索引
     * @param col 列索引
     * @return 是否为障碍物
     */
    boolean isObstacle(int row, int col);

    /**
     * 检查指定区域是否在动态网格中为空闲。
     * @param x 中心的X坐标
     * @param y 中心的Y坐标
     * @param radius 安全半径
     * @return 如果区域空闲则返回 true
     */
    // boolean isAreaInDynamicGridFree(int x, int y, int radius);

    /**
     * 在动态网格中标记指定区域为已占用。
     * @param x 中心的X坐标
     * @param y 中心的Y坐标
     * @param radius 安全半径
     */
    // void markAreaInDynamicGridAsOccupied(int x, int y, int radius);

    /**
     * 获取地图的反向表示
     * @return 反向地图
     */
    boolean[][] getReverseMap();

    /**
     * 反转地图
     */
    void reverseMap();

    

}
