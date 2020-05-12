package com.lsy.dao;

import com.lsy.po.FailureMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailureMailRepository extends JpaRepository<FailureMail,Long> {

}
