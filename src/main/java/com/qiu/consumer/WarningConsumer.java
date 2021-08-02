package com.qiu.consumer;

import com.qiu.config.ConfirmPublishConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningConsumer {
    @RabbitListener(queues = ConfirmPublishConfig.WARNING_QUEUE)
    public void backupQueueListen(Message message){
        log.error("message:{}--",new String(message.getBody()));
    }
}
