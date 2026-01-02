package MiniDidi.car.domain;

import lombok.Data;

/**
 * Description:
 * <p>
 * 车辆数据类
 * </p>
 *
 * @author WeijiaDou@Studyline
 * @version 1.0
 * @since 2025/10/12 10:39
 */
@Data
public class DidiCar {
    private int carIndex;

    private int nowPassengerIndex;

    boolean hasPassenger;
    /**
     * 车辆位置，行是Y，列是X
     */
    private double carX;
    private double carY;

    private double carAngle;
    private double speed;
    private double qianlun_angle;

    public DidiCar(int carIndex, double carX, double carY, double carAngle, double speed, double qianlun_angle){
        this.carIndex = carIndex;
        this.carX = carX;
        this.carY = carY;
        this.carAngle = carAngle;
        this.speed = speed;
        this.qianlun_angle = qianlun_angle;
        this.nowPassengerIndex = -1;
    }
}
