package com.lsy.service;

import javax.mail.internet.AddressException;

/**
 * 邮件业务类 MailService
 * 已经被ActiveMQ取代
 */

public interface MailService {

    //发邮件
    void sendSimpleMail(String to, String subject, String content) throws AddressException;
}