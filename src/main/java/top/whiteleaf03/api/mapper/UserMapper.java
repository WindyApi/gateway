package top.whiteleaf03.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.whiteleaf03.api.modal.UserIdAndSecretKeyVO;

/**
 * @author WhiteLeaf03
 */
@Mapper
public interface UserMapper {
    /**
     * 根据ak获取用户的id和sk
     *
     * @param accessKey accessKey
     * @return 返回用户的id和sk
     */
    UserIdAndSecretKeyVO selectIdAndSecretKeyByAccessKey(String accessKey);
}
