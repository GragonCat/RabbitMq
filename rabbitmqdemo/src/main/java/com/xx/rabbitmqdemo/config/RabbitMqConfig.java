package com.xx.rabbitmqdemo.config;

import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {

    public static final String BUSINESS_QUEUE = "com.xx.business.queue";
    public static final String BUSINESS_DEAD_LETTER_QUEUE = "com.xx.business.deadLetterQueue";
    public static final String BUSINESS_EXCHANGE = "com.xx.business.exchange";
    public static final String BUSINESS_DEAD_LETTER_EXCHANGE = "com.xx.business.deadLetterExchange";
    public static final String BUSINESS_ROUTINGKEY = "com.xx.business.routingKey";
    public static final String BUSINESS_DEAD_LETTER_ROUTINGKEY = "com.xx.business.deadLetter.routingKey";
    @Bean("businessExchange")
    public Exchange businessExchange(){
        return ExchangeBuilder.topicExchange(BUSINESS_EXCHANGE).durable(true).build();
    }
    @Bean("businessDeadLetterExchange")
    public Exchange businessDeadLetterExchange(){
        return ExchangeBuilder.directExchange(BUSINESS_DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    @Bean("businessQueue")
    public Queue businessQueue(){
        return QueueBuilder.durable(BUSINESS_QUEUE).deadLetterExchange(BUSINESS_DEAD_LETTER_EXCHANGE).deadLetterRoutingKey(BUSINESS_DEAD_LETTER_ROUTINGKEY).build();
    }

    @Bean("businessDeadLetterQueue")
    public Queue businessDeadLetterQueue(){
        return QueueBuilder.durable(BUSINESS_DEAD_LETTER_QUEUE).build();
    }
    @Bean("businessBinding")
    public Binding businessBinding(
            @Qualifier("businessQueue") Queue queue,
            @Qualifier("businessExchange")Exchange exchange
    ){
        return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_ROUTINGKEY).noargs();
    }
    @Bean("businessDeadLetterBinding")
    public Binding businessDeadLetterBinding(
            @Qualifier("businessDeadLetterQueue")Queue queue,
            @Qualifier("businessDeadLetterExchange")Exchange exchange
    ){
        return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_DEAD_LETTER_ROUTINGKEY).noargs();
    }

}
