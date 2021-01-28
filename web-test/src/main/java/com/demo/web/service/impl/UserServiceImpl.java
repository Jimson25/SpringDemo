package com.demo.web.service.impl;

import com.demo.web.entity.UserEntity;
import com.demo.web.mapper.UserMapper;
import com.demo.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserEntity getOne(Integer id) {
        return userMapper.selectById(id);
    }
}
