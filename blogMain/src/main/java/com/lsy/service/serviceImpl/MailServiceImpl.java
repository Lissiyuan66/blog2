package com.lsy.service.serviceImpl;


import com.lsy.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;


/**
 * 邮件业务类 MailService
 * 已经被ActiveMQ取代
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String content) throws AddressException {
        int a = 1;
        SimpleMailMessage message = new SimpleMailMessage();
        String nick="";
        try {
            nick=javax.mail.internet.MimeUtility.encodeText("小源的博客");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setFrom(String.valueOf(new InternetAddress(nick+" <"+from+">"))); // 邮件发送者
        message.setTo(to); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(content); // 内容
        mailSender.send(message); //发邮件
        System.out.println("邮件发送成功");
    }

}