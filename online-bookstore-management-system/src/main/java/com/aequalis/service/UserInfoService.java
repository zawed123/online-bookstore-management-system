package com.aequalis.service;

import com.aequalis.entity.UserInfo;

public interface UserInfoService {

    public UserInfo getUserByUsername(String username);

    public String addUser(UserInfo userInfo);
}
