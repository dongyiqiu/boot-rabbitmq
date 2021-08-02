package com.qiu.controller;


import com.qiu.config.ConfirmPublishConfig;
import com.qiu.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg){
        //System.out.println(msg);
        log.info("localTime:{},send message to QueueA-QueueB; message:{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend("X","XA","TenSecondsQueue-->"+msg);
        rabbitTemplate.convertAndSend("X","XB","FortySecondsQueue-->"+msg);
    }

    @GetMapping("/sendExpire/{msg}/{ttl}")
    public void sendExpireMsg(@PathVariable("msg")String msg,@PathVariable("ttl")String ttl){
        log.info("localTime:{}-- ttl:{}--message:{}",new Date().toString(),ttl,msg);
        rabbitTemplate.convertAndSend("X","XC",msg,message -> {
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });

    }

    @GetMapping("/delay/{msg}/{ttl}")
    public void sendMsg(@PathVariable("msg") String msg,@PathVariable("ttl")Integer ttl){
        //System.out.println(msg);
        log.info("localTime:{},send message to delay_queue(expire{}ms); message:{}",new Date().toString(),ttl.toString(),msg);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.EXCHANGE_NAME,DelayedQueueConfig.ROUTING_KEY,msg, message -> {
            message.getMessageProperties().setDelay(ttl);
            return message;
        });
    }


    @GetMapping("/confirm/{msg}")
    public void  confirmMessage(@PathVariable("msg")String msg){
        CorrelationData correlationData = new CorrelationData("1");
        log.info("localTime:{},send message to confirm_queue; message:{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend(ConfirmPublishConfig.CONFIRM_EXCHANGE,ConfirmPublishConfig.CONFIRM_KEY+"2",msg,correlationData);

    }

}
