package MiniDidi.car.adapters.format;

import org.springframework.stereotype.Component;

import MiniDidi.car.domain.DidiCar;

/**
 * Description:
 * <p>
 * 车辆数据传输对象DTO类转化函数
 * </p>
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/23 11:42
 */
@Component
public class CarMapper {
    public CarDto toDto(DidiCar car) {
        if (car == null) {
            return null;
        }
        CarDto dto = new CarDto();
        dto.setCarIndex(car.getCarIndex());
        dto.setCol(car.getCarX()); // col -> x
        dto.setRow(car.getCarY()); // row -> y
        dto.setAngle(car.getCarAngle());
        dto.setSpeed(car.getSpeed());
        dto.setQianlun_angle(car.getQianlun_angle());
        dto.setHasPassenger(car.isHasPassenger());
        dto.setPassengerIndex(car.getNowPassengerIndex());
        return dto;
    }
}
