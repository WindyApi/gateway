package top.whiteleaf03.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.whiteleaf03.api.modal.InterfaceInvokeRecord;

import java.util.List;

/**
 * @author WhiteLeaf03
 */
@Mapper
public interface InterfaceInvokeRecordMapper {
    /**
     * 新增接口调用记录
     *
     * @param interfaceInvokeRecords 插入的记录
     */
    void insert(List<InterfaceInvokeRecord> interfaceInvokeRecords);
}
