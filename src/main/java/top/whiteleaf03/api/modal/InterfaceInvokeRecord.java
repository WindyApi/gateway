package top.whiteleaf03.api.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author WhiteLeaf03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceInvokeRecord {
    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 创建时间
     */
    private Date createTime;

    public InterfaceInvokeRecord(Long userId, Long interfaceId) {
        this.userId = userId;
        this.interfaceId = interfaceId;
    }
}
