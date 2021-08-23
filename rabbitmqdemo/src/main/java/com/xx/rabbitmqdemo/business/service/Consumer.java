package com.xx.rabbitmqdemo.business.service;


import com.rabbitmq.client.Channel;
import com.xx.rabbitmqdemo.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Consumer {

    @RabbitListener(queues = {RabbitMqConfig.BUSINESS_QUEUE})
    public void listen(Message message, Channel channel) throws IOException, InterruptedException {
        String msg = new String(message.getBody(),"UTF-8");
        Exception exception = null;
        TimeUnit.SECONDS.sleep(1);
        boolean ack = true;
        if(msg.contains("0")){
            try{
                throw new RuntimeException("dead letter exception");
            }
            catch (RuntimeException e){
                ack = false;
                exception = e;
            }
        }
        if(!ack){
            log.error("消费者消费：{} 信息发生：{} 异常，产生死信",message,exception.getMessage(),exception);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }else{
            log.info("消费者消费：{} 信息成功",msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
    }

    @RabbitListener(queues = {RabbitMqConfig.BUSINESS_DEAD_LETTER_QUEUE})
    public void deadLetterListen(Message message,Channel channel) throws IOException, InterruptedException {
        String msg = new String(message.getBody(),"UTF-8");
        log.info("死信队列消费者消费信息：{}",message);
        TimeUnit.SECONDS.sleep(3);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
