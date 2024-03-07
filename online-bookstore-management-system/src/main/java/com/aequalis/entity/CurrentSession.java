package com.aequalis.entity;


import com.aequalis.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component("currentSession")
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentSession {
    @Autowired
    UserInfoService userInfoService;

    private UserInfo userInfo;


    public UserInfo getUserInfo() {
        if (userInfo == null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            userInfo = userInfoService.getUserByUsername(username);
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
