package com.lsy.service.impl;

import com.lsy.po.FailureMail;
import com.lsy.po.SuccessMail;
import com.lsy.service.SaveMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class SaveMailServiceImpl implements SaveMailService {

    //这里我们注入MongoTemplate对象，就可以实现增删改查
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void SaveSuccessMail(String smail,String title,String text) {
        //修改时区
        SimpleDateFormat fm = new SimpleDateFormat("MMM d yyyy hh:mma", Locale.CHINA);
        fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SuccessMail successMail = new SuccessMail();
        successMail.setSuccessmail(smail);
        successMail.setSuccesstime(fm.format(new Date()));
        successMail.setSuccesstitle(title);
        successMail.setSuccesstext(text);
        //这里传入对象即可
        mongoTemplate.save(successMail);
    }

    @Override
    public void SaveFailureMail(String fmail,String title,String text) {
        //修改时区
        SimpleDateFormat fm = new SimpleDateFormat("MMM d yyyy hh:mma", Locale.CHINA);
        fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        FailureMail failureMail = new FailureMail();
        failureMail.setFailureMail(fmail);
        failureMail.setFailureTime(fm.format(new Date()));
        failureMail.setFailuretitle(title);
        failureMail.setFailuretext(text);
        mongoTemplate.save(failureMail);
    }
}
