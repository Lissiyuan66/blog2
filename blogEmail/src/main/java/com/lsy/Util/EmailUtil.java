package com.lsy.Util;

import com.lsy.service.SaveMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.internet.InternetAddress;


@Component
@Slf4j
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") //发送方邮箱
    private String toEmail;

    @Autowired
    private SaveMailService saveMailService;

    public void sendEmailUtil(String title, String text, String Email) {
        //发送消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人
        String nick="";
        try {
            nick=javax.mail.internet.MimeUtility.encodeText("小源的博客");
            //邮件标题
            message.setFrom(String.valueOf(new InternetAddress(nick+" <"+toEmail+">")));
            //主题(标题)
            message.setSubject(title);
            //内容
            message.setText(text);
            //接收者
            message.setTo(Email);
            javaMailSender.send(message);
            //保存成功列表到mongodb
            saveMailService.SaveSuccessMail(Email,title,text);
            log.info("邮件发送完成,收件人:" + Email);
        } catch (Exception e) {
            //保存失败列表到mongodb
            saveMailService.SaveFailureMail(Email,title,text);
            log.warn("发送失败，收件人是："+Email);
        }
    }
}
