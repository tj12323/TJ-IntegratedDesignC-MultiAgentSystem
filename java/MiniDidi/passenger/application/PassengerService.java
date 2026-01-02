package MiniDidi.passenger.application;

import MiniDidi.map.application.DidiMapDao;
import MiniDidi.passenger.domain.DidiPassenger;
import MiniDidi.passenger.domain.PassengerStatus;
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
 * 乘客服务类。
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/26 15:24
 */
@Service
public class PassengerService {

    private static final Logger logger = LoggerFactory.getLogger(PassengerService.class);
    private final DidiMapDao mapDao;
    private final DidiPassengerDao passengerDao;

    @Autowired
    public PassengerService(DidiMapDao mapDao, DidiPassengerDao passengerDao) {
        this.mapDao = mapDao;
        this.passengerDao = passengerDao;
    }

    /**
     * 随机生成指定数量的乘客。
     */
    public void randomPassenger(int passengerNum) {
        passengerDao.clearPassengers();
        int maxX = mapDao.selectMapCols();
        int maxY = mapDao.selectMapRows();
        for (int i = 0; i < passengerNum; i++) {
            int startX = (int) (Math.random() * maxX);
            int startY = (int) (Math.random() * maxY);
            if (mapDao.isObstacle(startX, startY)) {
                i--;
                continue;
            }
            int endX = (int) (Math.random() * maxX);
            int endY = (int) (Math.random() * maxY);
            if (mapDao.isObstacle(endX, endY)) {
                i--;
                continue;
            }
            //起点终点距离太近
            if (Math.abs(startX - endX) + Math.abs(startY - endY) < mapDao.selectMapCols() / 4) {
                i--;
                continue;
            }
            passengerDao.addPassenger(i, startX, startY, endX, endY);
            logger.info("passenger created: index:{},start position:{},{},end position:{}",i, startX, startY, endX, endY);
        }
    }

    // 采用已有数据集生成乘客
    public void createPassengersFromPreset(List<PresetData.PassengerPreset> passengerPresets) {
    passengerDao.clearPassengers();
    for (PresetData.PassengerPreset preset : passengerPresets) {
        passengerDao.addPassenger(preset.getIndex(), preset.getStartX(), preset.getStartY(), preset.getEndX(), preset.getEndY());
        logger.info("passenger created from preset: index:{},start position:{},{},end position:{},{}",
                    preset.getIndex(), preset.getStartX(), preset.getStartY(), preset.getEndX(), preset.getEndY());
        }
    }

    public void pickUpPassenger(int carIndex, int passengerIndex) {
        DidiPassenger passenger = this.getPassenger(passengerIndex);
        if(passenger.getStatus() != PassengerStatus.WAITING) {
            // logger.error("乘客 {} 状态为 {}，不能被接起", passengerIndex, passenger.getStatus());
            throw new IllegalStateException("乘客当前状态不是WAITING，无法被接起");
        }
        passengerDao.pickUpPassenger(carIndex, passengerIndex);
        // logger.info("乘客 {} 已被车辆 {} 接起", passengerIndex, carIndex);
    }

    public void dropOffPassenger(int passengerIndex) {
        passengerDao.dropOffPassenger(passengerIndex);
        // logger.info("乘客 {} 已被送达目的地", passengerIndex);
    }

    public DidiPassenger getPassenger(int passengerIndex) {
        return passengerDao.getPassengerByIndex(passengerIndex);
    }

    public List<DidiPassenger> getPassengers() {
        return passengerDao.getPassengers();
    }
}
