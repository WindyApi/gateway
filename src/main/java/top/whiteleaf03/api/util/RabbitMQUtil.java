package top.whiteleaf03.api.util;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.whiteleaf03.api.modal.dto.InterfaceInvokeRecordDTO;

@Component
public class RabbitMQUtil {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQUtil(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue("interface_invoke_record");
    }

    public void sentRecord(InterfaceInvokeRecordDTO interfaceInvokeRecordDTO) {
        rabbitTemplate.convertAndSend("interface_invoke_record", JSONUtil.toJsonStr(interfaceInvokeRecordDTO));
    }
}
