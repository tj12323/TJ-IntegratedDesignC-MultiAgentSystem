package MiniDidi.passenger.adapters;

import MiniDidi.car.application.CarService;
import MiniDidi.passenger.adapters.format.PassengerDto;
import MiniDidi.passenger.adapters.format.PassengerMapper;
import MiniDidi.passenger.application.PassengerService;
import MiniDidi.passenger.domain.DidiPassenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Description:
 * <p>
 * 乘客数据控制器
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 16:17
 */

@RestController
@RequestMapping("api/passengers")
public class PassengerController {
    
    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);
    private final CarService carService;
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @Autowired
    public PassengerController(CarService carService, PassengerService passengerService, PassengerMapper passengerMapper) {
        this.carService = carService;
        this.passengerService = passengerService;
        this.passengerMapper = passengerMapper;
    }

    /**
     * 获取乘客信息
     *
     * @param passengerIndex 乘客索引
     * @return 乘客信息
     */
    @GetMapping("/{PassengerIndex}")
    public ResponseEntity<PassengerDto> getPassengerById(@PathVariable int passengerIndex) {
        // logger.info("请求获取乘客 {} 的信息", passengerIndex);

        try {
            DidiPassenger passenger = passengerService.getPassenger(passengerIndex);
            PassengerDto passengerDto = passengerMapper.toDto(passenger);
            return ResponseEntity.ok(passengerDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有乘客信息
     *
     * @return 乘客信息DTO
     */
    @GetMapping
    public ResponseEntity<List<PassengerDto>> getAllPassengers() {
        // logger.info("请求获取所有乘客的信息");
        List<DidiPassenger> allPassengers = passengerService.getPassengers();

        List<PassengerDto> allPassengerDtos = allPassengers.stream()
                .map(passengerMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allPassengerDtos);
    }
}
