package com.lsy.service.impl;

import com.lsy.dao.FailureMailRepository;
import com.lsy.dao.SuccessMailRepository;
import com.lsy.po.FailureMail;
import com.lsy.po.SuccessMail;
import com.lsy.service.SaveMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class SaveMailServiceImpl implements SaveMailService {

    @Autowired
    private FailureMailRepository failureMailRepository;

    @Autowired
    private SuccessMailRepository successMailRepository;


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
        successMailRepository.save(successMail);
    }

    @Override
    public void SaveFailureMail(String fmail,String title,String text) {
        //修改时区
        SimpleDateFormat fm = new SimpleDateFormat("MMM d yyyy hh:mma", Locale.CHINA);
        fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        FailureMail failureMail = new FailureMail();
        failureMail.setFailuremail(fmail);
        failureMail.setFailuretime(fm.format(new Date()));
        failureMail.setFailuretitle(title);
        failureMail.setFailuretext(text);
        failureMailRepository.save(failureMail);
    }
}
