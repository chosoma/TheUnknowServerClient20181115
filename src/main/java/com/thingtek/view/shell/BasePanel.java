package com.thingtek.view.shell;

import com.thingtek.beanServiceDao.data.service.DataService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.beanServiceDao.user.service.UserService;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.tablemodel.TableConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import javax.swing.*;

@EqualsAndHashCode(callSuper = true)
public @Data
abstract class BasePanel extends JPanel {

    protected Integer authority;

    @Resource
    protected Factorys factorys;
    @Resource
    protected DataService dataService;
    @Resource
    protected UnitService unitService;
    @Resource
    protected PointService pointService;
    @Resource
    protected UserService userService;
    @Resource
    protected TableConfig tableConfig;

    public abstract BasePanel init();

    public void manager(UserBean user) {
        init();
    }

    protected void errorMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "错误", JOptionPane.ERROR_MESSAGE);
    }

    protected void falseMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "失败", JOptionPane.ERROR_MESSAGE);
    }

    protected void successMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "成功", JOptionPane.INFORMATION_MESSAGE);
    }


}
