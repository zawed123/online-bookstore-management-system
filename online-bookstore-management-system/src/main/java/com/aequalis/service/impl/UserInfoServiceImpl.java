package com.aequalis.service.impl;

import com.aequalis.entity.UserInfo;
import com.aequalis.repository.UserInfoRepository;
import com.aequalis.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger logger = LogManager.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserInfo getUserByUsername(String username) {
        return userInfoRepository.findByName(username).get();
    }

    @Override
    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "user added to system ";
    }

}

