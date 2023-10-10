package top.whiteleaf03.api.service.InterfaceInvokeRecord;

import top.whiteleaf03.api.modal.InterfaceInvokeRecordVO;

/**
 * @author WhiteLeaf03
 */
public interface InterfaceInvokeRecordService {
    /**
     * 将记录添加进队列
     *
     * @param interfaceInvokeRecordVO 调用记录
     */
    void insertInterfaceInvokeRecord(InterfaceInvokeRecordVO interfaceInvokeRecordVO);

    /**
     * 从队列中获取记录并存进数据库
     */
    void getInterfaceInvokeRecordsFromQueue();
}
