package com.qiu.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    //inject
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate.setConfirmCallback(this);
    }

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        /**
         * 1. data
         * 2. if success
         * 3. cause
         * 4. 此回调只判断是否交换机收到，不确保队列收到
         */
        String id = correlationData!=null?correlationData.getId():"";
        if(ack){
            log.info("exchange received data! id:{}",id);
        }else {
            log.info("exchange do not receive! id:{},cause:{}",id,cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        // only exc when failed to delivery to queue;
        log.info("message:{}--exchange{}--cause:{}--routingKey:{}",returnedMessage.getMessage(),returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }
}
