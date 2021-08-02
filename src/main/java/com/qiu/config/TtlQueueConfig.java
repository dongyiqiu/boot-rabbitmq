package com.qiu.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TtlQueueConfig {
    // exchange
    public static final String NORMAL_EXCHANGE = "X";
    public static final String DEAD_EXCHANGE = "Y";
    //normal queue
    public static final String TEN_SECOND_QUEUE = "QA";
    public static final String FORTY_SECOND_QUEUE = "QB";
    public static final String NO_EXPIRE_QUEUE="QC";
    //dead queue
    public static final String DEAD_QUEUE = "QD";



    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");

        return QueueBuilder.durable(NO_EXPIRE_QUEUE)
                .withArguments(arguments)
                .build();
    }



    //normal exchange
    @Bean("NORMAL_EXCHANGE")
    public DirectExchange directExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    @Bean("DEAD_EXCHANGE")
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    //queue 10 seconds
    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",10000);
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");

        return QueueBuilder.durable(TEN_SECOND_QUEUE)
                .withArguments(arguments)
                .build();
    }


    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",40000);
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        //arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(FORTY_SECOND_QUEUE)
                .withArguments(arguments)
                .build();
    }



    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE)
                .build();
    }

    @Bean
    public Binding TenBindingNormal(@Qualifier("queueA")Queue queue,@Qualifier("NORMAL_EXCHANGE")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("XA");
    }


    @Bean
    public Binding NoExpireBindingNormal(@Qualifier("queueC")Queue queue,@Qualifier("NORMAL_EXCHANGE")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("XC");
    }

    @Bean
    public Binding FortyBindingNormal(@Qualifier("queueB")Queue queue,@Qualifier("NORMAL_EXCHANGE")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("XB");
    }

    @Bean
    public Binding deadQueueBindingDead(@Qualifier("queueD")Queue queue,@Qualifier("DEAD_EXCHANGE")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("YD");
    }
}
