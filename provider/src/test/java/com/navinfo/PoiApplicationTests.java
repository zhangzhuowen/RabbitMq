package com.navinfo;


import com.navinfo.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


@SpringBootTest
class PoiApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    void test() throws Exception{
        rabbitTemplate.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE,"item.3","zzw");
        Thread.sleep(2000);

    }


    @Test
    void testCompare() throws Exception{
        String oldContact = "122-3".split("-")[1];





    }


}
