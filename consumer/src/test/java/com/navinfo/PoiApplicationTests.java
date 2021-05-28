package com.navinfo;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PoiApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    String ITEM_TOPIC_EXCHANGE = "item_topic_exchange";

    @Test
    void test() throws Exception{

    }


}
