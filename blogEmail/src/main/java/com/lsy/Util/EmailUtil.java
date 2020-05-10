package com.lsy.Util;

import com.lsy.service.SaveMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Component
@Slf4j
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Value("${spring.mail.username}") //发送方邮箱
    private String toEmail;

    @Autowired
    private SaveMailService saveMailService;

/*    public void sendEmailUtil(String title, String text, String Email) {
        //发送消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人
        String nick = "";
        try {
            nick = javax.mail.internet.MimeUtility.encodeText("小源的博客");
            //邮件标题
            message.setFrom(String.valueOf(new InternetAddress(nick + " <" + toEmail + ">")));
            //主题(标题)
            message.setSubject(title);
            //内容
            message.setText(text);
            //接收者
            message.setTo(Email);
            javaMailSender.send(message);
            //保存成功列表到mongodb
            //saveMailService.SaveSuccessMail(Email,title,text);
            log.info("邮件发送完成,收件人:" + Email);
        } catch (Exception e) {
            //保存失败列表到mongodb
            //saveMailService.SaveFailureMail(Email,title,text);
            log.warn("发送失败，收件人是：" + Email);
        }
    }*/

    //发送评论通知邮件
    public void sendHtmlMail(String title, String text, String email,String fname,String url,String zname,String ftext) {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("text", text);
        context.setVariable("fname", fname);
        context.setVariable("url", url);
        context.setVariable("zname", zname);
        context.setVariable("title",title);
        context.setVariable("ftext",ftext);
        //解析模板
        String emailContent = templateEngine.process("comment", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String nick = javax.mail.internet.MimeUtility.encodeText("小源的博客");
            //邮件标题
            helper.setFrom(String.valueOf(new InternetAddress(nick + " <" + toEmail + ">")));
            helper.setTo(email);
            helper.setSubject("您在【"+title+"】的评论有了新的回复");
            helper.setText(emailContent, true);
            javaMailSender.send(message);
            log.info("评论通知邮件发送成功");
        } catch (Exception e) {
            log.error("评论通知邮件发送失败", e);
        }
    }

    //发送新帖通知邮件
    public void sendHtmlAdminMail(String title, String text, String email,String url) {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("text", text);
        context.setVariable("url", url);
        context.setVariable("title",title);
        String emailContent = templateEngine.process("newpost", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String nick = javax.mail.internet.MimeUtility.encodeText("小源的博客");
            //邮件标题
            helper.setFrom(String.valueOf(new InternetAddress(nick + " <" + toEmail + ">")));
            helper.setTo(email);
            helper.setSubject("小源的博客发了新帖子【"+title+"】");
            helper.setText(emailContent, true);
            javaMailSender.send(message);
            log.info("新帖通知邮件发送成功");
        } catch (Exception e) {
            log.error("新帖通知邮件发送失败", e);
        }
    }


}
