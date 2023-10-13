package top.whiteleaf03.api.dubbo.user;

import top.whiteleaf03.api.modal.dto.UserIdAndSecretKeyDTO;

/**
 * @author WhiteLeaf03
 */
public interface UserDubboService {
    /**
     * 根据ak获取用户id和sk
     *
     * @param accessKey 用户ak
     * @return 返回结果
     */
    UserIdAndSecretKeyDTO selectUserIdAndSecretKeyByAccessKey(String accessKey);
}
