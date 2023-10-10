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
public class InterfaceIdAndStatusVO {
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
