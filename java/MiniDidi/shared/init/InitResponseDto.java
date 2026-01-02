package MiniDidi.shared.init;

import lombok.Data;
import java.util.List;

import MiniDidi.car.adapters.format.CarDto;
import MiniDidi.passenger.adapters.format.PassengerDto;


/**
 * Description:
 * <p>
 * 乘客数据传输对象 (Data Transfer Object)类
 * 用于封装对 `/api/simulation/reset` 请求的响应数据，将新生成的车辆和乘客列表一次性返回给客户端。
 * </p>
 * 
 * 该Dto包含：
 * @param cars 初始化后生成的车辆列表 List<CarDto>
 * @param passengers 初始化后生成的乘客列表 List<PassengerDto>
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 15:48
 */
@Data
public class InitResponseDto {
    private List<CarDto> cars;
    private List<PassengerDto> passengers;

    public InitResponseDto(List<CarDto> cars, List<PassengerDto> passengers) {
        this.cars = cars;
        this.passengers = passengers;
    }
}
