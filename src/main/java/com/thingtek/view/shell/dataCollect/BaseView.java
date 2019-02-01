package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.user.entity.UserBean;
import com.thingtek.config.ClassConfig;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.shell.Shell;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import javax.swing.*;

@EqualsAndHashCode(callSuper = true)
public @Data
abstract class BaseView extends JPanel {

    /*
        数据显示类型
     */
    protected String iconText;
    protected boolean typeBol;

    private boolean select;

    @Resource
    protected ClassConfig classConfig;

    @Resource
    protected UnitService unitService;

    @Resource
    protected Factorys factorys;

    @Resource
    protected PointService pointService;

    @Resource
    protected Shell shell;

    public BaseView() {
//        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public BaseView init() {
        setBackground(factorys.getColorFactory().getColor("dataCollectContent"));
        return this;
    }

    public abstract void addData(BaseDataBean dataBean);

    public abstract void manager(UserBean user);

}
