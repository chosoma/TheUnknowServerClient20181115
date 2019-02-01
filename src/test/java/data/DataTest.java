package data;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.WaterDataBean;
import com.thingtek.beanServiceDao.data.service.DataService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataTest {
    private String config = "appcontext/applicationContext.xml";
    private ApplicationContext ac = new ClassPathXmlApplicationContext(config);

    @Test
    public void saveData() {
        DataService dataService = ac.getBean(DataService.class);

        BaseDataBean[] data = new BaseDataBean[31];
        for (int i = 0; i < data.length; i++) {
            data[i] = new WaterDataBean();
            data[i].setUnit_type((byte) 1);
            data[i].setUnit_num((byte) (i + 1));
            data[i].setValue1((float) (Math.floor(Math.random() * 10 + 100)));
            data[i].setValue2((float) (Math.floor(Math.random() * 10 + 100)));
            data[i].setValue3((float) (Math.floor(Math.random() * 10 + 100)));
        }
        dataService.saveData(data);


    }

}
