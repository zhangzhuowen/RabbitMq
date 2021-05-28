package com.navinfo.config.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangZhuoWen
 * @ClassName DirectReceiver
 * @date 2021/5/8 16:26
 * @Description TODO
 */
@Slf4j
@Component
public class DirectReceiver implements ChannelAwareMessageListener {
    @Override
    @RabbitListener(queues = "item_queue")
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String msg = message.toString();
            log.info("接收到的数据是:{}",msg);

            /**
             *  重复消费
             *  为了防止消息在消费者端丢失，会采用手动回复MQ的方式来解决，同时也引出了一个问题，消费者处理消息成功，手动回复MQ时由于网络不稳定，连接断开，导致MQ没有收到消费者回复的消息，那么该条消息还会保存在MQ的消息队列，由于MQ的消息重发机制，会重新把该条消息发给和该队列绑定的消息者处理，这样就会导致消息重复消费。
             *
             *  解决方案
             *  可以在消费者端每次消费成功后将该条消息id保存到数据库，每次消费前查询该消息id，如果该条消息id已经存在那么表示已经消费过就不再消费否则就消费。本方案采用redis存储消息id，因为redis是单线程的，并且性能也非常好，提供了很多原子性的命令，本方案使用setnx命令存储消息id。
             *
             *  FastMap : job_id 存入数据库
             */
            //为true表示确认之前的所有消息  false表示只来处理着当前的消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.info("处理消息时显示异常,异常是:{},现拒绝消费当前消息且不再放回队列",e.getMessage());
            //为true会重新放回队列
            channel.basicReject(deliveryTag, true);
        }
    }

}
