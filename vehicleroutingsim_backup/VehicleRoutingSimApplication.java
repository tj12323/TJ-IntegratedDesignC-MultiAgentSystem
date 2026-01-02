package edu.tongji.vehicleroutingsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import edu.tongji.vehicleroutingsim.backup.VehicleRoutingSimApplication;
import edu.tongji.vehicleroutingsim.service.MapService;

/**
 * Description:
 * <p>
 * 本文件为SpringBoot启动入口
 * </p>
 *
 * @author EddieLe@AutomationLine
 * @version 1.1
 * @since 2025/10/22 15:02
 */
@SpringBootApplication
// 开启AOP
@EnableAspectJAutoProxy
public class VehicleRoutingSimApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        ConfigurableApplicationContext context = SpringApplication.run(VehicleRoutingSimApplication.class, args);

        // 获取 Spring 容器中的 Bean
        MapService mapService = context.getBean(MapService.class);

        // 调用初始化方法
        if(!mapService.loadMapObject()){
            mapService.loadMapFile();
            mapService.reverseMap();
            mapService.saveMapObject();
            mapService.loadMapObject();
        }
    }
}
