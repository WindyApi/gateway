package top.whiteleaf03.api.service.InterfaceInvokeRecord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.whiteleaf03.api.mapper.InterfaceInvokeRecordMapper;
import top.whiteleaf03.api.modal.InterfaceInvokeRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author WhiteLeaf03
 */
@Slf4j
@Service
public class InterfaceInvokeRecordServiceImpl implements InterfaceInvokeRecordService {
    private final LinkedBlockingQueue<InterfaceInvokeRecord> interfaceInvokeRecordQueue;
    private final InterfaceInvokeRecordMapper interfaceInvokeRecordMapper;

    @Autowired
    public InterfaceInvokeRecordServiceImpl(InterfaceInvokeRecordMapper interfaceInvokeRecordMapper) {
        this.interfaceInvokeRecordQueue = new LinkedBlockingQueue<>();
        this.interfaceInvokeRecordMapper = interfaceInvokeRecordMapper;
    }

    /**
     * 将记录添加进队列
     *
     * @param interfaceInvokeRecord 调用记录
     */
    @Override
    public void insertInterfaceInvokeRecord(InterfaceInvokeRecord interfaceInvokeRecord) {
        try {
            interfaceInvokeRecordQueue.put(interfaceInvokeRecord);
        } catch (InterruptedException e) {
            log.error("调用记录存入队列时异常!{}", e.getMessage());
        }
    }

    /**
     * 从队列中获取记录并存进数据库
     */
    @Override
    @Scheduled(fixedRate = 60000)
    public void getInterfaceInvokeRecordsFromQueue() {
        List<InterfaceInvokeRecord> interfaceInvokeRecords = new ArrayList<>();
        interfaceInvokeRecordQueue.drainTo(interfaceInvokeRecords);
        if (interfaceInvokeRecords.isEmpty()) {
            return;
        }
        interfaceInvokeRecordMapper.insert(interfaceInvokeRecords);
    }
}
