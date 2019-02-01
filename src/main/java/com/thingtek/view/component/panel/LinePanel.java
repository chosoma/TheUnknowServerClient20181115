package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.data.basedata.entity.BaseDataBean;
import com.thingtek.beanServiceDao.unit.baseunit.entity.BaseUnitBean;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LinePanel extends BaseGhaph {

    public LinePanel() {
        setLayout(new BorderLayout());
        init();
    }

    private TimeSeriesCollection dataset;
    private XYLineAndShapeRenderer renderer;

    private void init() {
        dataset = new TimeSeriesCollection();
        Font font = new Font("微软雅黑", Font.PLAIN, 12);

        DateAxis timeAxis = new DateAxis();
//        timeAxis.setLowerMargin(0.02);  // reduce the default margins
//        timeAxis.setUpperMargin(0.02);
        timeAxis.setDateFormatOverride(new SimpleDateFormat("YYYY-MM-dd"));
        timeAxis.setLabelFont(font);
        timeAxis.setTickLabelFont(font);

        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setAutoRangeIncludesZero(true);  // override default
        valueAxis.setLabelFont(font);
        valueAxis.setTickLabelFont(font);
        valueAxis.setNumberFormatOverride(new DecimalFormat("#0.00"));

        XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, null);
        plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));//图片区与坐标轴的距离
        plot.setOutlinePaint(null);
        plot.setInsets(new RectangleInsets(0, 0, 0, 0));//坐标轴与最外延的距离
        XYToolTipGenerator toolTipGenerator = StandardXYToolTipGenerator.getTimeSeriesInstance();
        XYURLGenerator urlGenerator = new StandardXYURLGenerator();
        renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setBaseToolTipGenerator(toolTipGenerator);
        renderer.setURLGenerator(urlGenerator);
//        plot.setRenderer(renderer);
        /*设置颜色*/
/*        renderer.setSeriesPaint(0, new Color(236, 236, 0));//A
        renderer.setSeriesPaint(1, new Color(50, 153, 102));//B
        renderer.setSeriesPaint(2, new Color(236, 0, 0));//C
        renderer.setSeriesPaint(3, new Color(0, 0, 0));//C*/
        renderer.setBaseShapesVisible(false);
//        renderer.setBaseShapesVisible(true);
        renderer.setBaseItemLabelsVisible(false);//设置值显示 true 显示 false 不显示
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_LEFT));
        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        renderer.setBaseItemLabelFont(new Font("Dialog", Font.BOLD, 14));
        renderer.setSeriesStroke(0, new BasicStroke(5));
        plot.setRenderer(renderer);
        JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.setTextAntiAlias(true);
        chart.setAntiAlias(true);//设置抗锯齿
        chart.setPadding(new RectangleInsets(5, 5, 5, 5));
        chart.setNotify(true);
        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void clear() {
        dataset.removeAllSeries();
        System.gc();
    }

    public void addData(Map<BaseUnitBean, List<BaseDataBean>> chartDatas) {
//        System.out.println("addData0:"+new Date());
        clear();
//        System.out.println("addData1:"+new Date());
        Set<Map.Entry<BaseUnitBean, List<BaseDataBean>>> entrySet = chartDatas.entrySet();
        for (Map.Entry<BaseUnitBean, List<BaseDataBean>> entry : entrySet) {
            TimeSeries timeSeries = new TimeSeries(entry.getKey().getUnit_name());
            List<BaseDataBean> datalist = entry.getValue();
            for (BaseDataBean data : datalist) {
                timeSeries.add(new Second(data.getInserttime()), data.getValue1());
            }
            /*Iterator<BaseDataBean> iterator = datalist.iterator();
            while (iterator.hasNext()){
                BaseDataBean data = iterator.next();
                iterator.remove();
                timeSeries.add(new Second(data.getInserttime()), data.getValue1());
            }*/
            dataset.addSeries(timeSeries);
        }
        for (int i = 0; i < chartDatas.size(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(1.0f));
        }
        repaint();
//        System.out.println("addData2:"+new Date());
    }

    public void delete(Map<BaseUnitBean, java.util.List<Date>> dates) {
        Set<Map.Entry<BaseUnitBean, java.util.List<Date>>> entrySet = dates.entrySet();
        for (Map.Entry<BaseUnitBean, java.util.List<Date>> entry : entrySet) {
            TimeSeries timeSeries = dataset.getSeries(entry.getKey().getUnit_name());
            for (Date date : entry.getValue()) {
                timeSeries.delete(new Second(date));
            }
        }
    }

    public void setLight(Set<BaseUnitBean> units) {
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(1.0f));
        }
        for (BaseUnitBean unit : units) {
            int index = dataset.getSeriesIndex(unit.getUnit_name());
            renderer.setSeriesStroke(index, new BasicStroke(10f));
        }

    }


}
