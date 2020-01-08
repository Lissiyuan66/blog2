package com.lsy.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;

//这里指定我们MongoDB的名字，Spring会帮我们自动创建数据库
@Document(collection = "SUCCESS_MAIL")
@Data
public class SuccessMail {

    /*@Id
    @Field("mail_id")
    private Long id;*/

    //无需设定主键，Mongo会帮我们自动生成Object类型的主键
    //这里是字段名，下面同理
    @Field("success_mail")
    private String successmail;

    @Field("success_time")
    private Date successtime;
}
