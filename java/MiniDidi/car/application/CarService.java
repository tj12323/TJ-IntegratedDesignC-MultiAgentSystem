package MiniDidi.car.application;

import MiniDidi.car.domain.DidiCar;
import MiniDidi.map.application.DidiMapDao;
import MiniDidi.passenger.application.PassengerService;
import MiniDidi.shared.config.PresetData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Description:
 * <p>
 * 小车服务类
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/23 15:32
 */
@Service
// logger注释 需要恢复
public class CarService {

    private final DidiCarDao didiCarDao;
    private final DidiMapDao mapDao;
    private final PassengerService passengerService;

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    public CarService(DidiCarDao didiCarDao, DidiMapDao mapDao, PassengerService passengerService) {
        this.didiCarDao = didiCarDao;
        this.mapDao = mapDao;
        this.passengerService = passengerService;
    }

    /**
     * 设置小车位置
     *
     * @param carIndex 小车索引
     * @param carX     小车X坐标
     * @param carY     小车Y坐标
     * @param carAngle 小车角度
     */
    public void setCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle){
        didiCarDao.updateCar(carIndex, carX, carY, carAngle, speed, qianlun_angle);
        // logger.info("car position updated: index:{},position:{},{},angle:{}", carIndex, carX, carY, carAngle);
    }

    /**
     * 获取指定ID的车辆。
     *
     * @param carIndex 车辆索引
     * @return 对应的 DidiCar 对象
     * @throws NoSuchElementException 如果找不到指定ID的车辆，抛出无元素错误
     */
    public DidiCar getCarByIndex(int carIndex) {
        DidiCar car = didiCarDao.selectById(carIndex);
        if (car == null) {
            // 5. 抛出更具描述性的异常，Controller层可以捕获并转换为404
            throw new NoSuchElementException("ID为 " + carIndex + " 的车辆不存在");
        }
        return car;
    }

    /**
     * 获取所有车辆的列表。
     * 方法签名和实现不变，因为Controller中仍然需要获取所有车辆的内部模型列表来进行转换。
     * @return 所有车辆的列表
     */
    public List<DidiCar> getCars() {
        return didiCarDao.selectCars();
    }

    /**
     * 获取车辆上乘客的索引。
     * @param carIndex 车辆索引
     * @return 乘客索引, 如果没有乘客则返回-1
     */
    public int getCarPassengerIndex(int carIndex){
        return getCarByIndex(carIndex).getNowPassengerIndex();
    }

    /**
     * 采用已有数据集生成小车
     */
    public void randomCar(int carNum) {
        didiCarDao.deleteCars();
        int maxX = mapDao.selectMapCols() - 1;
        int maxY = mapDao.selectMapRows() - 1;
        for (int i = 0; i < carNum; i++) {
            // 随机生成小车位置(0,maxX) (0,maxY)
            double carX = Math.random() * maxX;
            double carY = Math.random() * maxY;
            if (mapDao.isObstacle((int) carX, (int) carY)) {
                i--;
                continue;
            }
            didiCarDao.insertCar(i, carX, carY, 0, 0, 0);
            logger.info("car position created: index:{},position:{},{},angle:{}", i, carX, carY, 0);
        }
    }

        public void createCarsFromPreset(List<PresetData.CarPreset> carPresets) {
        didiCarDao.deleteCars();
        for (PresetData.CarPreset preset : carPresets) {
            // 这里我们不再校验障碍物，因为预设数据我们认为是有效的
            didiCarDao.insertCar(preset.getIndex(), preset.getX(), preset.getY(), 0, 0, 0);
            logger.info("car position created from preset: index:{},position:{},{},angle:{}", 
                        preset.getIndex(), preset.getX(), preset.getY(), 0);
        }
    }

    /**
     * 车辆接到乘客
     * 这是一个事务性操作，会同时更新车辆和乘客的状态。
     * @param carIndex       执行任务的车辆ID
     * @param passengerIndex 被分配的乘客ID
     */
    public void assignPassengerToCar(int carIndex, int passengerIndex) {
        // 乘客上车，更新乘客状态
        passengerService.pickUpPassenger(carIndex, passengerIndex);
        
        // 更新车辆自身的状态，包含对车辆是否空闲的校验。
        try {
            didiCarDao.pickPassenger(carIndex, passengerIndex);
        } catch (Exception e) {
            // 如果车辆状态更新失败，抛出异常
            // logger.error("为车辆分配乘客失败，详情: {}", e.getMessage());
            throw e; // 抛出异常
        }
        // logger.info("车辆 {} 成功接到了乘客 {}", carIndex, passengerIndex);
    }

    /**
     * 车辆完成送客
     * @param carIndex 执行任务的车辆ID
     */
    public void completeAssignment(int carIndex) {
        // logger.info("车辆 {} 开始完成其当前任务", carIndex);
        
        DidiCar car = this.getCarByIndex(carIndex); // 确保车辆存在
        if (!car.isHasPassenger()) {
            throw new IllegalStateException("车辆 " + carIndex + " 没有乘客，无法完成任务");
        }
        int passengerIndex = car.getNowPassengerIndex();
        
        // 更新乘客状态
        passengerService.dropOffPassenger(passengerIndex);
        
        // 更新车辆状态
        didiCarDao.dropPassenger(carIndex);
        
        // logger.info("车辆 {} 已成功送达乘客 {}", carIndex, passengerIndex);
    }
}
