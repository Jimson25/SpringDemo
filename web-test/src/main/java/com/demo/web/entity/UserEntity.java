package com.demo.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class UserEntity {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
    private String other;

}
