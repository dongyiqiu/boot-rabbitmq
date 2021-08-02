package com.qiu.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmPublishConfig {
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String CONFIRM_KEY = "confirm.key";

    public static final String BACKUP_EXCHANGE = "backup.exchange";
    public static final String BACKUP_QUEUE = "backup.queue";
    public static final String WARNING_QUEUE ="warning.queue";

    @Bean
    public Queue confirmQueue(){
        //return new Queue(CONFIRM_QUEUE);
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public DirectExchange confirmExchange(){
        //return new DirectExchange(CONFIRM_EXCHANGE);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE).build();
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }
    @Bean Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Binding backupBindingToFanout(@Qualifier("warningQueue")Queue queue,@Qualifier("fanoutExchange")FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningBindToFanout(@Qualifier("backupQueue")Queue queue,@Qualifier("fanoutExchange")FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding normalBindingDirect(@Qualifier("confirmQueue")Queue queue,@Qualifier("confirmExchange")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_KEY);
    }
}
