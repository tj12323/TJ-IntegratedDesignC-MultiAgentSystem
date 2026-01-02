package MiniDidi.car.adapters.format;

import lombok.Data;

/**
 * Description:
 * <p>
 * 车辆数据传输对象 (Data Transfer Object)类
 * 用于在Controller层与客户端之间传递车辆数据，实现内外模型分离。
 * 本DTO包含车辆model的相关信息
 * </p>
 * 
 * 该Dto包含：
 * @param carIndex 车辆索引 int
 * @param col 车辆横坐标x double
 * @param row 车辆纵坐标y double
 * @param angle 车辆方向角 double
 * @param hasPassenger 反映车上是否有乘客 bool
 * @param passengerIndex 车辆上乘客索引 int
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/23 11:22
 */
@Data
public class CarDto {
    private int carIndex;
    private double col; 
    private double row; 
    private double angle;
    private double speed;
    private double qianlun_angle;
    private boolean hasPassenger;
    private int passengerIndex;
}
