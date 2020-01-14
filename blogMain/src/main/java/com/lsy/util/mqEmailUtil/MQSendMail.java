package com.lsy.util.mqEmailUtil;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import javax.jms.Queue;


@Component
@EnableScheduling
@Slf4j
public class MQSendMail {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    //每隔5秒向消息队列发送消息
    //@Scheduled(fixedDelay = 5000)
    //异步发送
    @Async
    public void send(String title,String text,String email){
        JSONObject jsonObject = new JSONObject();
        //用户名，封装邮件内容时使用
        jsonObject.put("title",title);
        //文本内容
        jsonObject.put("text",text);
        //指定发送到哪个邮箱 (邮件接收者邮箱)
        jsonObject.put("email",email);
        //将发送的消息转换为Json字符串
        String msg = jsonObject.toJSONString();
        log.info("生产者向消费者发送内容：" +  msg);
        jmsMessagingTemplate.convertAndSend(queue,msg);
    }
}

