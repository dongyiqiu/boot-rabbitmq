package com.qiu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {

    public static final String QUEUE_NAME = "delayed.queue";
    public static final String ROUTING_KEY = "delayed.routingKey";
    public static final String EXCHANGE_NAME = "delayed.exchange";

    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(EXCHANGE_NAME,"x-delayed-message",
                true,false,arguments);
    }
    @Bean
    public Queue delayedQueue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding normalQueueBindingDelayedExchange(@Qualifier("delayedQueue")Queue queue,@Qualifier("delayedExchange")CustomExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();

    }

}
