package com.xx.rabbitmqdemo.business.controller;

import com.xx.rabbitmqdemo.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProducerController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("message/{msg}")
    public String product(@PathVariable String msg){
        System.out.println("前端输入信息："+msg);
        rabbitTemplate.convertAndSend(RabbitMqConfig.BUSINESS_EXCHANGE,RabbitMqConfig.BUSINESS_ROUTINGKEY,msg);
        return "ok";
    }

}
