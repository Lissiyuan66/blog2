package com.lsy.service;

import com.lsy.po.User;


public interface UserService {

    User checkUser(String username, String password);
}
