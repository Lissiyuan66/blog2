package com.lsy.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;

@Document(collection = "FAILURE_MAIL")
@Data
public class FailureMail {
    /*@Id
    @Field("mail_id")
    private Long id;*/

    @Field("failure_mail")
    private String failureMail;

    @Field("failure_time")
    private Date failureTime;
}
