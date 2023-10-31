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
public class InterfaceIdAndStatusDTO implements Serializable {
    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 接口状态
     * true 上线
     * false 离线
     */
    private Boolean status;
}
