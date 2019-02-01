package com.thingtek.beanServiceDao.user.entity;

import lombok.Data;

public
@Data
class UserBean {

    private String username;
    private String password;
    private Integer authority;
    private Boolean del;

}
