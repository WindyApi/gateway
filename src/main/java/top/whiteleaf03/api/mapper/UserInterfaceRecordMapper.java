package top.whiteleaf03.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.whiteleaf03.api.modal.UserIdAndInterfaceIdVO;

/**
 * @author WhiteLeaf03
 */
@Mapper
public interface UserInterfaceRecordMapper {
    /**
     * 查询剩余使用次数
     *
     * @param userIdAndInterfaceIdVO 用户id和接口id
     * @return 返回结果
     */
    Long selectLeftNumByUserIdAndInterfaceId(UserIdAndInterfaceIdVO userIdAndInterfaceIdVO);

    /**
     * 减少使用次数
     *
     * @param userIdAndInterfaceIdVO 用户id和接口id
     */
    void updateLeftNumByUserIdAndInterfaceId(UserIdAndInterfaceIdVO userIdAndInterfaceIdVO);
}
