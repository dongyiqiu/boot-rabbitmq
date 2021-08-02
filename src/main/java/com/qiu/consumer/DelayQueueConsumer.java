package com.qiu.consumer;

import com.qiu.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class DelayQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.QUEUE_NAME)
    public void receiveDelayMessage(Message message){
        String msg = new String(message.getBody());
        log.info("localTime:{}-->message:{}",new Date().toString(),msg);


    }

}
