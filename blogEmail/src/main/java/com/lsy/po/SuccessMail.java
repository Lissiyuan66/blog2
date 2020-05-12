package com.lsy.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_successmail")
@Data
public class SuccessMail {

    @Id
    @GeneratedValue
    private Long id;

    private String successmail;

    private String successtime;

    private String successtitle;

    private String successtext;
}
