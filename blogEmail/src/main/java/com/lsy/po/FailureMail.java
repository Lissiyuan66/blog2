package com.lsy.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "FAILURE_MAIL")
@Data
public class FailureMail {

    @Field("failure_mail")
    private String failureMail;

    @Field("failure_time")
    private String failureTime;

    @Field("failure_title")
    private String failuretitle;

    @Field("failure_text")
    private String failuretext;
}
