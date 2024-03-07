package com.aequalis.service;

import com.aequalis.entity.UserInfo;
import com.aequalis.repository.UserInfoRepository;
import com.aequalis.service.impl.UserInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserInfoRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoServiceImpl userService;

    @Test
    public void testAddUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("testUser");
        userInfo.setPassword("password123");
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        String result = userService.addUser(userInfo);
        assertEquals("user added to system ", result);
    }
}