package MiniDidi.passenger.application.impl;

import MiniDidi.passenger.application.DidiPassengerDao;
import MiniDidi.passenger.domain.DidiPassenger;
import MiniDidi.passenger.domain.PassengerStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * <p>
 * 乘客数据访问接口实现类
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 13:45
 */
@Repository
public class DidiPassengerDaoImpl implements DidiPassengerDao {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(DidiPassengerDaoImpl.class);
    
    /**
     * 乘客哈希表
     */
    private final Map<Integer, DidiPassenger> passengers = new ConcurrentHashMap<>();

    /**
     * 获取乘客列表
     *
     * @return 乘客列表
     */
    @Override
    public List<DidiPassenger> getPassengers() {
        return new ArrayList<>(passengers.values());
    }

    /**
     * 添加乘客
     *
     * @param passengerIndex 乘客索引
     * @param startX         乘客起点X坐标
     * @param startY         乘客起点Y坐标
     * @param endX           乘客终点X坐标
     * @param endY           乘客终点Y坐标
     */
    @Override
    public void addPassenger(int passengerIndex, double startX, double startY, double endX, double endY) {
        if(passengerIndex < 0) {
            // logger.error("乘客索引不能为负数");
            throw new IllegalArgumentException("乘客索引不能为负数");
        }
        else if(passengers.containsKey(passengerIndex)) {
            // logger.error("乘客索引 {} 重复", passengerIndex);
            throw new IllegalArgumentException("乘客索引重复");
        }
        DidiPassenger passenger = new DidiPassenger(passengerIndex, startX, startY, endX, endY);
        passengers.put(passengerIndex, passenger);
        // logger.info("添加乘客成功, 乘客索引:{},乘客初始位置:{},{},乘客目的地:{},{}",passengerIndex, startX, startY, endX, endY);
    }

    /**
     * 根据乘客索引获取乘客
     *
     * @param passengerIndex 乘客索引
     * @return 对应的乘客对象
     */
    @Override
    public DidiPassenger getPassengerByIndex(int passengerIndex) {
        return passengers.get(passengerIndex);
    }

    /**
     * 接到乘客
     *
     * @param passengerIndex 乘客索引
     */
    @Override
    public void pickUpPassenger(int carIndex,int passengerIndex) {
        DidiPassenger passenger =getPassengerByIndex(passengerIndex);
        if (passenger != null) {
            passenger.setStatus(PassengerStatus.ONBOARD);
            passenger.setNowCarIndex(carIndex);
        }
    }

    /**
     * 乘客下车
     *
     * @param passengerIndex 乘客索引
     */
    @Override
    public void dropOffPassenger(int passengerIndex) {
        DidiPassenger passenger =getPassengerByIndex(passengerIndex);
        if (passenger != null) {
            passenger.setStatus(PassengerStatus.ARRIVED);
        }
    }

    /**
     * 清空所有乘客
     */
    @Override
    public void clearPassengers() {
        passengers.clear();
        // logger.info("所有乘客数据已清空");
    }
}
