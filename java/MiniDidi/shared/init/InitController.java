package MiniDidi.shared.init;

import MiniDidi.car.adapters.format.CarDto;
import MiniDidi.car.adapters.format.CarMapper;
import MiniDidi.car.application.CarService;
import MiniDidi.car.domain.DidiCar;
import MiniDidi.passenger.adapters.format.PassengerDto;
import MiniDidi.passenger.adapters.format.PassengerMapper;
import MiniDidi.passenger.application.PassengerService;
import MiniDidi.passenger.domain.DidiPassenger;
import MiniDidi.shared.config.PresetData;
import MiniDidi.shared.config.PresetDataService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * <p>
 * 初始化Controller
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 10:17
 */
@RestController
@RequestMapping("/api/sim")
public class InitController {

    private static final Logger logger = LoggerFactory.getLogger(InitController.class);
    private final CarService carService;
    private final PassengerService passengerService;
    private final CarMapper carMapper;        
    private final PassengerMapper passengerMapper;
    private final PresetDataService presetDataService;
    private final int carNums;
    private final int passengerNums;

    @Autowired
    public InitController(@Value("${car.nums}") int carNums,
                          @Value("${passenger.nums}") int passengerNums,
                          CarService carService,
                          PassengerService passengerService,
                          CarMapper carMapper,
                          PassengerMapper passengerMapper,
                          PresetDataService presetDataService) {
        this.carNums = carNums;
        this.passengerNums = passengerNums;
        this.carService = carService;
        this.passengerService = passengerService;
        this.carMapper = carMapper;
        this.passengerMapper = passengerMapper;
        this.presetDataService = presetDataService;
    }

    /**
     * 重置模拟世界状态。
     * 随机生成新的车辆和乘客位置，并返回新的初始状态。
     *
     * @return 包含初始化后车辆和乘客信息的DTO
     */
    // @PostMapping("/reset")
    // public ResponseEntity<InitResponseDto> resetSimulation() {
    //     logger.info("收到重置模拟世界请求，开始重新生成数据...");

    //     carService.randomCar(carNums);
    //     passengerService.randomPassenger(passengerNums);
    //     List<DidiCar> cars = carService.getCars();
    //     List<DidiPassenger> passengers = passengerService.getPassengers();

    //     // 获取小车的初始位置
    //     List<CarDto> carDtos = cars.stream()
    //         .map(carMapper::toDto)
    //         .collect(Collectors.toList());


    //     // 获取乘客的初始位置和目的地
    //     List<PassengerDto> passengerDtos = passengers.stream()
    //         .map(passengerMapper::toDto)
    //         .collect(Collectors.toList());
        
    //     // 封装到响应DTO中并返回    
    //     InitResponseDto responseDto = new InitResponseDto(carDtos, passengerDtos);
    //     logger.info("所有状态已重置，小车和乘客的初始位置已生成");
    //     return ResponseEntity.ok(responseDto);
    // }
    /**
     * 重置模拟世界状态。
     * 按照原有数据集生成新的车辆和乘客位置，并返回新的初始状态。
     *
     * @return 包含初始化后车辆和乘客信息的DTO
     */
    @PostMapping("/reset")
    public ResponseEntity<InitResponseDto> resetSimulation() {
        logger.info("收到重置模拟世界请求，开始从预设数据生成...");

        // 1. 获取一组随机的预设数据
        PresetData presetData = presetDataService.getRandomPresetData();
        
        // 2. 根据预设数据创建小车和乘客
        carService.createCarsFromPreset(presetData.getCars());
        passengerService.createPassengersFromPreset(presetData.getPassengers());

        // 后面逻辑不变
        List<DidiCar> cars = carService.getCars();
        List<DidiPassenger> passengers = passengerService.getPassengers();

        List<CarDto> carDtos = cars.stream()
            .map(carMapper::toDto)
            .collect(Collectors.toList());

        List<PassengerDto> passengerDtos = passengers.stream()
            .map(passengerMapper::toDto)
            .collect(Collectors.toList());
        
        InitResponseDto responseDto = new InitResponseDto(carDtos, passengerDtos);
        logger.info("所有状态已重置，场景 {} 的数据已加载", presetData.getScenario());
        return ResponseEntity.ok(responseDto);
    }
}