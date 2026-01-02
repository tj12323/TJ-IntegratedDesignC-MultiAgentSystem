package MiniDidi.car.application;

import java.util.List;

import MiniDidi.car.domain.DidiCar;

/**
 * Description:
 * <p>
 * 小车数据访问接口
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/23 17:02
 */
public interface DidiCarDao {

    /**
     * 加载车辆数据
     * @return 车辆数据
     */
    List<DidiCar> selectCars();

    /**
     * 添加车辆
     * @param carIndex 车辆索引
     * @param carX 车辆X坐标
     * @param carY 车辆Y坐标
     * @param carAngle 车辆角度
     * @param speed 车辆速度
     * @param qianlun_angle 前轮转向角
     */
    void insertCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle);

    /**
     * 根据车辆索引获取车辆
     * @param carIndex 车辆索引
     * @return 对应的车辆对象
     */
    DidiCar selectById(int carIndex);

    /**
     * 设置车辆位置
     * @param carIndex 车辆索引
     * @param carX 车辆X坐标
     * @param carY 车辆Y坐标
     * @param carAngle 车辆角度
     * @param speed 车辆速度
     * @param qianlun_angle 前轮转向角
     */
    void updateCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle);

    /**
     * 清空所有车辆
     */
    void deleteCars();

    /**
     * 接乘客
     * @param carIndex 车辆索引
     * @param passengerIndex 乘客索引
     */
    void pickPassenger(int carIndex, int passengerIndex);

    /**
     * 送乘客
     * @param carIndex 车辆索引
     */
    void dropPassenger(int carIndex);

}
