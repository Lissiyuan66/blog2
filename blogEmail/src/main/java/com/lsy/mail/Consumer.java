package com.lsy.mail;

import com.alibaba.fastjson.JSONObject;
import com.lsy.Util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private EmailUtil emailUtil;


    //监听队列、解析json
    @JmsListener(destination = "${queue}")     //监听的队列，配置文件中的queue属性
    public void receive(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        log.info("收到生产者消息：" + msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String title = jsonObject.getString("title");
        String text = jsonObject.getString("text");
        String email = jsonObject.getString("email");
        //发送邮件
        emailUtil.sendEmailUtil(title, text, email);
    }
}

