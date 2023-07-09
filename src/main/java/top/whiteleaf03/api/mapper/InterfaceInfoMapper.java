package top.whiteleaf03.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.whiteleaf03.api.modal.InterfaceIdAndStatus;

/**
 * @author WhiteLeaf03
 */
@Mapper
public interface InterfaceInfoMapper {
    /**
     * 根据路径查询接口id和状态
     *
     * @param url 接口路径
     * @return 返回结果
     */
    InterfaceIdAndStatus selectIdAndStatusByUrl(String url);
}
