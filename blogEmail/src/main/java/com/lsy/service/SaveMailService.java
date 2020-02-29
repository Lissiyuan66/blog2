package com.lsy.service;


public interface SaveMailService {
    //保存发送成功列表到mongodb
    void SaveSuccessMail(String smail,String title,String text);
    //保存发送失败列表到mongodb
    void SaveFailureMail(String fmail,String title,String text);
}
