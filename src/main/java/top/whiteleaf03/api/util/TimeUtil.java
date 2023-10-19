package top.whiteleaf03.api.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.whiteleaf03.api.modal.dto.InterfaceInvokeRecordDTO;

@Slf4j
@Component
public class TimeUtil {
    private static final ThreadLocal<InterfaceInvokeRecordDTO> threadLocalStorage = new ThreadLocal<>();
    private final RabbitMQUtil rabbitMQUtil;

    @Autowired
    public TimeUtil(RabbitMQUtil rabbitMQUtil) {
        this.rabbitMQUtil = rabbitMQUtil;
    }

    public void saveRecord(InterfaceInvokeRecordDTO interfaceInvokeRecordDTO) {
        threadLocalStorage.set(interfaceInvokeRecordDTO);
        log.info("开始计时");
    }

    public void endTiming() {
        InterfaceInvokeRecordDTO interfaceInvokeRecordDTO = threadLocalStorage.get();
        threadLocalStorage.remove();
        if (ObjectUtil.isNull(interfaceInvokeRecordDTO)) {
            return;
        }
        Long useTime = Math.max(System.currentTimeMillis() - interfaceInvokeRecordDTO.getUseTime(), 1L);
        log.info("计时结束，共耗时{}ms", useTime);
        interfaceInvokeRecordDTO.setUseTime(useTime);
        rabbitMQUtil.sentRecord(interfaceInvokeRecordDTO);
    }
}
