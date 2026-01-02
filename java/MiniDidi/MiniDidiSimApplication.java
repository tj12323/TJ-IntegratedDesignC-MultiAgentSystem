package MiniDidi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import MiniDidi.map.application.MapService;

/**
 * Description:
 * <p>
 * 本文件为SpringBoot启动入口
 * </p>
 *
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/22 15:02
 */
@SpringBootApplication
// 开启AOP
@EnableAspectJAutoProxy
public class MiniDidiSimApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(MiniDidiSimApplication.class, args);
    }

    /**
     * 使用CommandLineRunner在应用启动后执行初始化任务。
     * 添加@Bean注解会执行它的run方法。
     * @param mapService Spring会自动从容器中注入MapService实例
     * @return 将bmp地图转变为二维数组
     */
    @Bean
    public CommandLineRunner initMapData(MapService mapService) {
        return args ->{

            // 调用地图初始化方法
            if(!mapService.loadMapObject()){
                mapService.loadMapFile();
                mapService.reverseMap();
                mapService.saveMapObject();
                mapService.loadMapObject();
            }
        };
    }
}
