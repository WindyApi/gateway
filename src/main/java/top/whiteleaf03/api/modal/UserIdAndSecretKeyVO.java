package top.whiteleaf03.api.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WhiteLeaf03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdAndSecretKeyVO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户的秘钥
     */
    private String secretKey;
}
