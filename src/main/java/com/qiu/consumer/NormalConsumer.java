package com.qiu.consumer;

import com.qiu.config.ConfirmPublishConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class NormalConsumer {
    @RabbitListener(queues = ConfirmPublishConfig.CONFIRM_QUEUE)
    public void receiveMsg(Message message){
        log.info("localTime:{} NormalQueueGetMessage:{}",new Date().toString(),new String(message.getBody()));
    }
}
