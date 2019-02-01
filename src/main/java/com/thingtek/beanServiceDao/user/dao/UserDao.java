package com.thingtek.beanServiceDao.user.dao;

import com.thingtek.beanServiceDao.user.entity.UserBean;


import java.util.List;

public interface UserDao {

    UserBean findUser(UserBean userBean) throws Exception;

    UserBean findUser(String username) throws Exception;

    boolean saveUser(UserBean userBean) throws Exception;

    boolean updateUser(UserBean userBean) throws Exception;

    List<UserBean> findAll(int authority) throws Exception;

}
