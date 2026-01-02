package MiniDidi.passenger.adapters.format;

import org.springframework.stereotype.Component;

import MiniDidi.passenger.domain.DidiPassenger;

/**
 * Description:
 * <p>
 * 乘客数据传输对象DTO类转化函数
 * </p>
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 15:24
 */
@Component
public class PassengerMapper {
    public PassengerDto toDto(DidiPassenger passenger) {
        if (passenger == null) return null;
        PassengerDto dto = new PassengerDto();
        dto.setPassengerIndex(passenger.getPassengerIndex());
        dto.setStatus(passenger.getStatus());
        dto.setNowCarIndex(passenger.getNowCarIndex());
        dto.setStartX(passenger.getStartX());
        dto.setStartY(passenger.getStartY());
        dto.setEndX(passenger.getEndX());
        dto.setEndY(passenger.getEndY());
        return dto;
    }
}
