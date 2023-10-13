package top.whiteleaf03.api.dubbo.userinterfacerecord;

import top.whiteleaf03.api.modal.dto.UserIdAndInterfaceIdDTO;

/**
 * @author WhiteLeaf03
 */
public interface UserInterfaceRecordDubboService {
    /**
     * 查询剩余使用次数
     *
     * @param userIdAndInterfaceIdDTO 用户id和接口id
     * @return 返回结果
     */
    Long selectLeftNumByUserIdAndInterfaceId(UserIdAndInterfaceIdDTO userIdAndInterfaceIdDTO);

    /**
     * 减少使用次数
     *
     * @param userIdAndInterfaceIdDTO 用户id和接口id
     */
    void updateLeftNumByUserIdAndInterfaceId(UserIdAndInterfaceIdDTO userIdAndInterfaceIdDTO);
}
