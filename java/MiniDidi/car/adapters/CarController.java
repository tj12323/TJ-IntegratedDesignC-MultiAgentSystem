package MiniDidi.car.adapters;

import MiniDidi.car.adapters.format.CarDto;
import MiniDidi.car.adapters.format.CarMapper;
import MiniDidi.car.application.CarService;
import MiniDidi.car.domain.DidiCar;
import MiniDidi.map.application.MapService;
import MiniDidi.passenger.adapters.format.AssignmentRequestDto;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Description:
 * <p>
 * 车辆相关功能接口。
 * 接口说明：
 * 总体功能API为"/api/cars"
 * 若需查询所有车辆属性，直接GET"/api/cars"即可，会返回包含所有车辆信息的DTO列表。
 * Dto数据结构详见对应文件注释。
 * 
 * 若需查询单个车辆属性，请直接按照车辆索引序号carIndex访问API，即GET"/api/cars/{carIndex}"
 * 返回值为该车辆属性DTO类
 * 
 * 
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/22 18:39
 */
@RestController
@RequestMapping("/api/cars")
// logger info 暂时注释 需要恢复
public class CarController {


    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final MapService mapService;
    private final CarMapper carMapper;

    @Autowired
    public CarController(CarService carService, MapService mapService, CarMapper carMapper) {
        this.carService = carService;
        this.mapService = mapService;
        this.carMapper = carMapper;
    }


    /**
     * 更新指定ID车辆的位置和角度信息
     *
     * @param carIndex 从URL路径中获取的车辆索引
     * @param carDto   从请求体中获取的车辆更新数据
     * carDto内需要上传的小车参数包含：
     * @param row      行(Y坐标)
     * @param col      列(X坐标)
     * @param angle    角度
     * @return 更新后的车辆信息 (DTO)
     */
    @PutMapping("/{carIndex}") 
    public ResponseEntity<?> updateCar(@PathVariable int carIndex, @RequestBody CarDto carDto) {
        // logger.info("请求更新小车 {} 的位置，数据: {}", carIndex, carDto);

        // 校验，要求路径中的ID和请求体中的ID一致
        if (carDto.getCarIndex() != carIndex) {
            return ResponseEntity.badRequest().body("URL中的carIndex与请求体中的不匹配");
        }

        // 调用Service层，传递DTO中的数据
        carService.setCar(carIndex, carDto.getRow(), carDto.getCol(), carDto.getAngle(), carDto.getSpeed(), carDto.getQianlun_angle());
        
        // 检查障碍物
        boolean isObstacle = mapService.isObstacle(carDto.getRow(), carDto.getCol());
        // logger.info("用户已更新小车 {} 的位置, 是否在障碍物上: {}", carIndex, isObstacle);
        
        // 返回更新后的资源
        try {
            DidiCar updatedCar = carService.getCarByIndex(carIndex);
            CarDto updatedCarDto = carMapper.toDto(updatedCar);
            return ResponseEntity.ok(updatedCarDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/update")
    public String updateCarInfo(
            @RequestParam("carIndex") int carIndex,
            @RequestParam("row") double row,
            @RequestParam("col") double col,
            @RequestParam("angle") double angle, HttpServletRequest request) {
        carService.setCar(carIndex, row, col, angle, 0, 0);
        boolean error = mapService.isObstacle(row, col);
        // logger.info("ip地址为：" + request.getRemoteAddr() + "的用户已设置小车" + carIndex + "的位置");
        // 返回数据的逻辑
        return "小车位置已设置：索引:" + carIndex + ",位置:" + row + "," + col + ",角度:" + angle + ",是否有障碍物:" + error;
    }




    /**
     * 指定车辆接到特定乘客任务流程
     *
     * @param carIndex      执行任务的车辆ID
     * @param assignmentDto 包含待分配乘客ID的请求体
     * @return 更新后的车辆信息
     */
    @PostMapping("/{carIndex}/assignment") // URL: POST /api/cars/0/assignment
    public ResponseEntity<?> createAssignment(@PathVariable int carIndex, @RequestBody AssignmentRequestDto assignmentDto) {
        try {
            int passengerId = assignmentDto.getPassengerId();
            // logger.info("API请求：为车辆 {} 分配乘客 {}", carIndex, passengerId);
            
            carService.assignPassengerToCar(carIndex, passengerId);
            
            // 成功后，返回更新后的车辆资源
            DidiCar updatedCar = carService.getCarByIndex(carIndex);
            CarDto updatedCarDto = carMapper.toDto(updatedCar);
            return ResponseEntity.ok(updatedCarDto);

        } catch (NoSuchElementException e) {
            // 车辆或乘客不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            // 业务规则冲突 (例如，车已有乘客或乘客已被接)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * 乘客送达任务流程
     *
     * @param carIndex 执行任务的车辆ID
     * @return 成功则返回 204 No Content
     */
    @DeleteMapping("/{carIndex}/assignment")
    public ResponseEntity<Void> completeAssignment(@PathVariable int carIndex) {
        try {
            // logger.info("车辆 {} 的当前任务已完成", carIndex);
            
            carService.completeAssignment(carIndex);
            
            // 对于成功的DELETE操作，返回 204 No Content
            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    /**
     * 获取指定ID的车辆资源
     *
     * @param carIndex 从URL路径中获取的车辆索引
     * @return 车辆信息 (DTO)
     */
    @GetMapping("/{carIndex}")
    public ResponseEntity<CarDto> getCarById(@PathVariable int carIndex) {
        // logger.info("1111");
        
        try {
            // 按索引查询车辆
            DidiCar car = carService.getCarByIndex(carIndex);
            // logger.info("0000");
            // 使用ResponseEntity封装响应，控制HTTP状态码
            CarDto carDto = carMapper.toDto(car);
            return ResponseEntity.ok(carDto);
        } catch (IllegalArgumentException e) {
            // 如果车辆不存在，返回 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有车辆信息
     * @return 包含所有车辆信息的DTO列表
     */
    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        // logger.info("get information");

        // 从Service层获取内部模型列表
        List<DidiCar> allCars = carService.getCars();

        // 将内部模型列表 转换为 DTO 列表
        List<CarDto> allCarDtos = allCars.stream()
                                        .map(carMapper::toDto) // 复用转换方法
                                        .collect(Collectors.toList());

        // 返回封装好的DTO列表
        return ResponseEntity.ok(allCarDtos);
    }

}