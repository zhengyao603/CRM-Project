package com.ssmproject.crm.settings.service;

import com.ssmproject.crm.settings.pojo.User;

import java.util.Map;
import java.util.Objects;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String, Object> map);
}
