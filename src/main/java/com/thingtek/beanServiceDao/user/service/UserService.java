package com.thingtek.beanServiceDao.user.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.user.dao.UserDao;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends BaseService {

    public static final String DEVELOP = "开发人员";
    public static final String USER = "用户";
    public static final String ADMIN = "管理员";
    public static final int DP = 0;
    public static final int AP = 1;
    public static final int UP = 2;

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    @Resource
    private UserDao dao;

    public UserBean checkUser(UserBean userBean) {
        try {
            UserBean user = dao.findUser(userBean.getUsername());
            return this.user = user != null ? user.getPassword().equals(userBean.getPassword()) ? user : null : null;
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    public UserBean checkUser(String username) {
        try {
            return dao.findUser(username);
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    public boolean checkPassWord(String text) {
        return text.equals(user.getPassword());
    }

    public boolean changePassword(String password) {
        UserBean userBean = new UserBean();
        userBean.setUsername(user.getUsername());
        userBean.setPassword(password);
        boolean flag;
        try {
            flag = dao.updateUser(userBean);
            if (flag) {
                user.setPassword(password);
            }
        } catch (Exception e) {
            log(e);
            flag = false;
        }
        return flag;
    }

    public boolean changeRole(UserBean user) {
        UserBean userBean = new UserBean();
        userBean.setUsername(user.getUsername());
        userBean.setAuthority(user.getAuthority());
        try {
            return dao.updateUser(userBean);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }

    public boolean deleteUser(UserBean user) {
        UserBean userBean = new UserBean();
        userBean.setUsername(user.getUsername());
        userBean.setPassword(user.getPassword());
        userBean.setDel(true);
        try {
            return dao.updateUser(userBean);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }

    public List<UserBean> query() {
        List<UserBean> users = new ArrayList<>();
        try {
            List<UserBean> result = dao.findAll(this.user.getAuthority());
            for (UserBean user : result) {
                if (user.equals(this.user)) {
                    continue;
                }
                users.add(user);
            }
        } catch (Exception e) {
            log(e);
        }
        return users;
    }

    public boolean addUser(UserBean userBean) {
        try {
            return dao.saveUser(userBean);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }
}
