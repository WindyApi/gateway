package top.whiteleaf03.api.dubbo.interfaceinfo;

import top.whiteleaf03.api.modal.dto.InterfaceIdAndStatusDTO;

/**
 * @author WhiteLeaf03
 */
public interface InterfaceInfoDubboService {
    /**
     * 根据路径查询接口id和状态
     *
     * @param url 接口路径
     * @return 返回结果
     */
    InterfaceIdAndStatusDTO selectIdAndStatusByUrl(String url);
}
