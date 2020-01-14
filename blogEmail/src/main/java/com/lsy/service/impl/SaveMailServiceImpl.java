package com.lsy.service.impl;

import com.lsy.po.FailureMail;
import com.lsy.po.SuccessMail;
import com.lsy.service.SaveMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class SaveMailServiceImpl implements SaveMailService {

    //这里我们注入MongoTemplate对象，就可以实现增删改查
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void SaveSuccessMail(String smail) {
        SuccessMail successMail = new SuccessMail();
        successMail.setSuccessmail(smail);
        successMail.setSuccesstime(new Date());
        //这里传入对象即可
        mongoTemplate.save(successMail);
    }

    @Override
    public void SaveFailureMail(String fmail) {
        FailureMail failureMail = new FailureMail();
        failureMail.setFailureMail(fmail);
        failureMail.setFailureTime(new Date());
        mongoTemplate.save(failureMail);
    }
}
