package MiniDidi.car.application.impl;

import MiniDidi.car.application.DidiCarDao;
import MiniDidi.car.domain.DidiCar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; 
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p>
 * 小车数据访问接口实现类
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/23 15:55
 */
// logger needs recover
@Repository
public class DidiCarDaoImpl implements DidiCarDao {

    /**
     * 车辆列表
     */
    private final Map<Integer, DidiCar> cars = new ConcurrentHashMap<>();

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(DidiCarDaoImpl.class);

    /**
     * 添加车辆数据
     */
    @Override
    public void insertCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle) {
        // 创建车辆对象
        DidiCar newCar = new DidiCar(carIndex, carX, carY, carAngle, speed, qianlun_angle);
        DidiCar existingCar = cars.putIfAbsent(carIndex, newCar);
        // 检查车辆索引是否重复
        if (existingCar != null) {
            logger.error("车辆索引 {} 重复", carIndex);
            throw new IllegalArgumentException("车辆索引重复");
        }

        logger.info("successfully created，car index:{},ini pos:{},{}", carIndex, carX, carY);
    }

    /**
     * 根据车辆索引获取车辆
     *
     * @param carIndex 车辆索引
     * @return 对应的车辆对象
     */
    @Override
    public DidiCar selectById(int carIndex) {
        return cars.get(carIndex);
    }

    /**
     * 设置车辆位置
     *
     * @param carIndex 车辆索引
     * @param carX     车辆X坐标
     * @param carY     车辆Y坐标
     * @param carAngle 车辆角度
     */
    @Override
    public void updateCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if (car != null) {
            // 更新车辆位置
            car.setCarX(carX);
            car.setCarY(carY);
            car.setCarAngle(carAngle);
            car.setSpeed(speed);
            car.setQianlun_angle(qianlun_angle);
        } else {
            // logger.warn("尝试更新一个不存在的车辆，ID: {}", carIndex);
        }
    }

    /**
     * 清空所有车辆
     */
    @Override
    public void deleteCars() {
        cars.clear();
        // logger.info("所有车辆数据已清空");
    }

    /**
     * 获取所有车辆数据
     * @return 车辆数据
     */
    @Override
    public List<DidiCar> selectCars() {
        return new ArrayList<>(cars.values());
    }

    /**
     * 设置乘客
     *
     * @param carIndex    车辆索引
     * @param passengerIndex 乘客索引
     */
    @Override
    public void pickPassenger(int carIndex,int passengerIndex) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if (car == null) {
            // logger.error("车辆不存在");
            throw new IllegalArgumentException("车辆 " + carIndex + " 不存在");
        }
        if(car.isHasPassenger()){
            // logger.error("车辆已有乘客");
            throw new IllegalArgumentException("车辆已有乘客");
        }
        car.setNowPassengerIndex(passengerIndex);
        car.setHasPassenger(true);
    }

    /**
     * 乘客下车
     *
     * @param carIndex 车辆索引
     */
    @Override
    public void dropPassenger(int carIndex) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if (car == null) {
            // logger.error("车辆不存在");
            throw new IllegalArgumentException("车辆 " + carIndex + " 不存在");
        }
        if(!car.isHasPassenger()){
            // logger.error("车辆{}没有乘客", carIndex);
            throw new IllegalArgumentException("车辆没有乘客");
        }
        car.setNowPassengerIndex(-1);
        car.setHasPassenger(false);
    }
}
