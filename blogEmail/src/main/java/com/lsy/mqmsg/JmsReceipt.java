package com.lsy.mqmsg;

import com.alibaba.fastjson.JSONObject;
import com.lsy.Util.EmailSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsReceipt {

    @Autowired
    private EmailSendUtil emailSendUtil;


    //监听队列、解析json
    @JmsListener(destination = "${queue}")     //监听的队列，配置文件中的queue属性
    public void receive(String msg) {
        if (StringUtils.isEmpty(msg)) {
            log.info("收到生产者消息为空");
            return;
        }
        log.info("收到生产者消息：" + msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //标题
        String title = jsonObject.getString("title");
        //内容
        String text = jsonObject.getString("text");
        //收件人
        String email = jsonObject.getString("email");
        //父评论昵称
        String fname = jsonObject.getString("fname");
        //地址
        String url = jsonObject.getString("url");
        //子评论昵称
        String zname = jsonObject.getString("zname");
        //父评论内容
        String ftext = jsonObject.getString("ftext");
        if (fname == null&&ftext==null&&zname!=null) { //通知管理员评论
            emailSendUtil.sendHtmlMail(title, text, email, "管理员", url, zname, "无");
        } else if (fname == null && ftext == null){ //通知新博客
            emailSendUtil.sendHtmlAdminMail(title,text,email,url);
        } else { //通知评论人
            //发送邮件
            emailSendUtil.sendHtmlMail(title, text, email, fname, url, zname, ftext);
        }
    }
}

