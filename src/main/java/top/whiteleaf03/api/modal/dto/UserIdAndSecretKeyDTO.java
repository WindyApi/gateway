package top.whiteleaf03.api.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WhiteLeaf03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdAndSecretKeyDTO implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户的秘钥
     */
    private String secretKey;
}
