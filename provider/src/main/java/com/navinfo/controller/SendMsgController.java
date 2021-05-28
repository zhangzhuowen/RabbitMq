package com.navinfo.controller;

import com.navinfo.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangZhuoWen
 * @ClassName SendMsgController
 * @date 2021/5/7 16:47
 * @Description TODO
 */
@RestController
@Slf4j
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送的消息怎么样才算失败或成功？如何确认？
     * 当消息无法路由到队列时，确认消息路由失败。消息成功路由时，当需要发送的队列都发送成功后，进行确认消息，对于持久化队列意味着写入磁盘，对于镜像队列意味着所有镜像接收成功
     *
     *
     * @param msg
     * @param key
     * @return
     */
    @GetMapping("sendMsg")
    public String sendMsg(@RequestParam String msg,@RequestParam String key){
        log.info("sendMsg");
        rabbitTemplate.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE,key,msg);
        return "success";
    }

}
