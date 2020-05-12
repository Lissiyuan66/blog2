package com.lsy.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_failuremail")
@Data
public class FailureMail {

    @Id
    @GeneratedValue
    private Long id;

    private String failuremail;

    private String failuretime;

    private String failuretitle;

    private String failuretext;
}
