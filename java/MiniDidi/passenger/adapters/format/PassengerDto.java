package MiniDidi.passenger.adapters.format;

import MiniDidi.passenger.domain.PassengerStatus;
import lombok.Data;

/**
 * Description:
 * <p>
 * 乘客数据传输对象 (Data Transfer Object)类
 * 用于在Controller层与客户端之间传递乘客数据，实现内外模型分离
 * 本DTO包含乘客model的相关信息
 * </p>
 * 
 * 该Dto包含：
 * @param passengerIndex 乘客的唯一索引 int
 * @param status 乘客的当前状态 (WAITING, ONBOARD, ARRIVED) PassengerStatus枚举
 * @param nowCarIndex 搭乘的车辆索引，若未上车则为-1 int
 * @param startX 乘客的起点横坐标x double
 * @param startY 乘客的起点纵坐标y double
 * @param endX 乘客的目的地横坐标x double
 * @param endY 乘客的目的地纵坐标y double
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/24 15:17
 */

@Data
public class PassengerDto {
    private int passengerIndex;
    private PassengerStatus status;
    private int nowCarIndex;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
}