package MiniDidi.passenger.adapters.format;

import lombok.Data;

/**
 * Description:
 * <p>
 * 任务分配请求的数据传输对象 (Data Transfer Object) 类。
 * 用于客户端向 `POST /api/cars/{carIndex}/assignment` 接口发送请求时，在请求体中指定要分配的乘客。
 * </p>
 * 
 * 该Dto包含：
 * @param passengerId 需要被车辆接起的乘客的唯一索引 int
 * 
 * @author EddieLe@Automation
 * @version 1.1
 * @since 2025/10/26 11:24
 */
@Data
public class AssignmentRequestDto {
    private int passengerId;
}
