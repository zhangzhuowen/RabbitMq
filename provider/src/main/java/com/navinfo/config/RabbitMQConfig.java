package com.navinfo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangZhuoWen
 * @ClassName RabbitMQConfig
 * @date 2021/5/7 16:32
 * @Description TODO
 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    //交换机名称
    public static final String ITEM_TOPIC_EXCHANGE = "item_topic_exchange";

    public static final String ITEM_QUEUE = "item_queue";


    //声明交换机
     @Bean("itemTopicExchange")
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("itemQueue")
    public Queue itemQueue() {
        return QueueBuilder.durable(ITEM_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemQueue") Queue queue,
                                     @Qualifier("itemTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }

    @Bean("myTemplate")
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        System.out.println("=====================          createRabbitTemplate            =====================");
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                log.info("ConfirmCallback相关数据：{}",correlationData);
                log.info("ConfirmCallback确认情况：{}",ack);
                log.info("ConfirmCallback原因：{}",cause);
            }
        });

        //通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("ReturnCallback消息：{}",message);
                log.info("ReturnCallback回应码：{}",replyCode);
                log.info("ReturnCallback回应信息：{}",replyText);
                log.info("ReturnCallback交换机：{}",exchange);
                log.info("ReturnCallback路由键：{}",routingKey);
            }
        });

        return rabbitTemplate;
    }
}
