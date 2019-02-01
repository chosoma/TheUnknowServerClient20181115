package com.thingtek.util;

import com.thingtek.view.component.factory.MyColorFactory;
import com.thingtek.view.component.factory.MyFontFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.SortOrder;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class JfreeChartUtil {

    private static Date LastTime, NextTime;

    public static SimpleDateFormat ChartDataFormat = new SimpleDateFormat("yyyy年MM月dd日");

    public static SimpleDateFormat ChartDataFormat_M = new SimpleDateFormat("MM月dd日");

    public static SimpleDateFormat ChartDataFormat_D = new SimpleDateFormat("dd日");

    // public static Image chartImage;
    // public static Image PlotImage;
    // static {
    // try {
    // chartImage = ImageIO.read(new File("images/chart/chartImage.png"));
    // PlotImage = ImageIO.read(new File("images/chart/plotImage.png"));
    // } catch (IOException e) {
    // // e.printStackTrace();
    // }
    // }

    public static void setChartTheme() {
        StandardChartTheme theme = new StandardChartTheme("unicode") {
            // 重写apply(...)方法是为了借机消除文字锯齿.VALUE_TEXT_ANTIALIAS_OFF
            public void apply(JFreeChart chart) {
                super.apply(chart);
                chart.getRenderingHints().put(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
        };
        theme.setExtraLargeFont(MyFontFactory.FONT_18);
        theme.setLargeFont(MyFontFactory.FONT_14);
        theme.setRegularFont(MyFontFactory.FONT_12);
        theme.setSmallFont(MyFontFactory.FONT_9);
        // 网格线颜色
        Color gridColor = UIManager.getColor("Table.gridColor");
        theme.setDomainGridlinePaint(gridColor);
        theme.setRangeGridlinePaint(gridColor);
        // 设置曲线图与xy轴的距离
        theme.setAxisOffset(new RectangleInsets(1D, 1D, 1D, 1D));
        theme.setAxisLabelPaint(MyColorFactory.Color_X33);
        theme.setChartBackgroundPaint(new Color(255, 255, 255, 0));// 非数据区颜色
        // theme.setItemLabelPaint(Color_X33);// 图例字体颜色
        // new GradientPaint(0.0f, 0.0f, new Color(242, 248, 255), 0.0f, 0.0f,
        // new Color(227, 239, 255))
        theme.setPlotBackgroundPaint(MyColorFactory.Color_XF8F8FF);// 设置数据区背景
        theme.setPlotOutlinePaint(MyColorFactory.Color_X33);// 数据区边框颜色
        theme.setTitlePaint(MyColorFactory.Color_X33);// 标题颜色
        // theme.setSubtitlePaint(Color_X33);// 副标题颜色
        // theme.setLegendItemPaint(Color_X33);
        theme.setTickLabelPaint(MyColorFactory.Color_X33);//
        theme.setShadowVisible(false);// 柱状图阴影
        // theme.setDrawingSupplier(supplier)
        // TODO Auto-generated catch block
        // theme.setBarPainter(new StandardBarPainter());
        // 用于将该主题作为工厂的默认主题。
        ChartFactory.setChartTheme(theme);
    }

    public static void updateTime(Date lastTime, Date nextTime) {
        LastTime = lastTime;
        NextTime = nextTime;
    }

    // 判断时间是不是当前气象日范围内
    public static boolean isWeatherDay(Date date) {
        if (date.before(LastTime) || date.after(NextTime)) {
            return false;
        }
        return true;
    }

    private static Calendar Calendar_ST = Calendar.getInstance();

    public synchronized static String getSubTitle(TimeSeries timeSeries) {
        Calendar_ST.setTimeInMillis(timeSeries.getTimePeriod(0)
                .getFirstMillisecond());
        String subTitle = ChartDataFormat.format(Calendar_ST.getTime());
        int year = Calendar_ST.get(Calendar.YEAR);
        int month = Calendar_ST.get(Calendar.MONTH);
        int day = Calendar_ST.get(Calendar.DAY_OF_MONTH);
        Calendar_ST.setTimeInMillis(timeSeries.getTimePeriod(
                timeSeries.getItemCount() - 1).getFirstMillisecond());
        if (year != Calendar_ST.get(Calendar.YEAR)) {
            subTitle += "～" + ChartDataFormat.format(Calendar_ST.getTime());
        } else if (month != Calendar_ST.get(Calendar.MONTH)) {
            subTitle += "～" + ChartDataFormat_M.format(Calendar_ST.getTime());
        } else if (day != Calendar_ST.get(Calendar.DAY_OF_MONTH)) {
            subTitle += "～" + ChartDataFormat_D.format(Calendar_ST.getTime());
        }
        return subTitle;
    }


    public static Stroke GridLineStroke = new BasicStroke();// 将网格线改细实线

    private static Stroke stroke = new BasicStroke(2f);// 折线笔触
    // 折点形状
    public static Shape ChartShape = DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[1];


    private static TickUnits MyDateTickUnits = null, MyIntegerTickUnits = null,
            MyNumTickUnits_1 = null, MyNumTickUnits_2 = null,
            MyNumTickUnits_3 = null, MyNumTickUnits_4 = null;

    public static Marker getMarker(String labelName, double value) {
        Marker marker = new ValueMarker(value, Color.RED,
                new BasicStroke(0.5F), null, null, 1f);
        marker.setLabel(labelName);
        // marker.setLabelFont(new Font("SansSerif", 41, 14));
        marker.setLabelPaint(Color.RED);
        marker.setLabelOffset(new RectangleInsets(10, 15, 0, 0));
        return marker;
    }

    public static TickUnitSource myDateTickUnits() {
        return myDateTickUnits(TimeZone.getDefault(), Locale.getDefault());
    }

    public TickUnitSource myDateTickUnits(TimeZone zone) {
        return myDateTickUnits(zone, Locale.getDefault());
    }

    public static TickUnitSource myDateTickUnits(TimeZone zone, Locale locale) {

        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Null 'locale' argument.");
        }

        if (MyDateTickUnits == null) {
            synchronized (JfreeChartUtil.class) {
                if (MyDateTickUnits == null) {
                    MyDateTickUnits = new TickUnits();
                    // date formatters
                    DateFormat f2 = new SimpleDateFormat("HH:mm:ss", locale);
                    DateFormat f3 = new SimpleDateFormat("HH:mm", locale);
                    DateFormat f4 = new SimpleDateFormat("MM-dd,HH:mm", locale);
                    DateFormat f5 = new SimpleDateFormat("MM-dd", locale);
                    DateFormat f6 = new SimpleDateFormat("yyyy-MM", locale);
                    DateFormat f7 = new SimpleDateFormat("yyyy", locale);

                    f2.setTimeZone(zone);
                    f3.setTimeZone(zone);
                    f4.setTimeZone(zone);
                    f5.setTimeZone(zone);
                    f6.setTimeZone(zone);
                    f7.setTimeZone(zone);

                    // seconds
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.SECOND, 1,
                            DateTickUnitType.MILLISECOND, 50, f2));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.SECOND, 5,
                            DateTickUnitType.SECOND, 1, f2));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.SECOND, 10,
                            DateTickUnitType.SECOND, 1, f2));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.SECOND, 30,
                            DateTickUnitType.SECOND, 5, f2));

                    // minutes
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 1,
                            DateTickUnitType.SECOND, 5, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 2,
                            DateTickUnitType.SECOND, 10, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 5,
                            DateTickUnitType.MINUTE, 1, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 10,
                            DateTickUnitType.MINUTE, 1, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 15,
                            DateTickUnitType.MINUTE, 5, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 20,
                            DateTickUnitType.MINUTE, 5, f3));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MINUTE, 30,
                            DateTickUnitType.MINUTE, 5, f3));

                    // hours
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.HOUR,
                            1, DateTickUnitType.MINUTE, 5, f3));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.HOUR,
                            2, DateTickUnitType.MINUTE, 10, f3));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.HOUR,
                            4, DateTickUnitType.MINUTE, 30, f3));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.HOUR,
                            6, DateTickUnitType.HOUR, 1, f3));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.HOUR,
                            12, DateTickUnitType.HOUR, 1, f4));

                    // days
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.DAY,
                            1, DateTickUnitType.HOUR, 1, f5));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.DAY,
                            2, DateTickUnitType.HOUR, 1, f5));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.DAY,
                            7, DateTickUnitType.DAY, 1, f5));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.DAY,
                            15, DateTickUnitType.DAY, 1, f5));

                    // months
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MONTH, 1, DateTickUnitType.DAY, 1,
                            f6));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MONTH, 2, DateTickUnitType.DAY, 1,
                            f6));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MONTH, 3, DateTickUnitType.MONTH,
                            1, f6));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MONTH, 4, DateTickUnitType.MONTH,
                            1, f6));
                    MyDateTickUnits.add(new DateTickUnit(
                            DateTickUnitType.MONTH, 6, DateTickUnitType.MONTH,
                            1, f6));

                    // years
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            1, DateTickUnitType.MONTH, 1, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            2, DateTickUnitType.MONTH, 3, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            5, DateTickUnitType.YEAR, 1, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            10, DateTickUnitType.YEAR, 1, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            25, DateTickUnitType.YEAR, 5, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            50, DateTickUnitType.YEAR, 10, f7));
                    MyDateTickUnits.add(new DateTickUnit(DateTickUnitType.YEAR,
                            100, DateTickUnitType.YEAR, 20, f7));
                }
            }

        }
        return MyDateTickUnits;
    }

    public static TickUnitSource myNumTickUnits() {
        return myNumTickUnits(3);
    }

    public static TickUnitSource myNumTickUnits(int scale) {
        // NumberAxis.createIntegerTickUnits();
        TickUnits tickUnits;
        switch (scale) {
            case 0:
                if (MyIntegerTickUnits == null) {
                    synchronized (JfreeChartUtil.class) {
                        if (MyIntegerTickUnits == null) {
                            MyIntegerTickUnits = createTickUnits(0);
                        }
                    }
                }
                tickUnits = MyIntegerTickUnits;
                break;
            case 1:
                if (MyNumTickUnits_1 == null) {
                    synchronized (JfreeChartUtil.class) {
                        if (MyNumTickUnits_1 == null) {
                            MyNumTickUnits_1 = createTickUnits(1);
                        }
                    }
                }
                tickUnits = MyNumTickUnits_1;
                break;
            case 2:
                if (MyNumTickUnits_2 == null) {
                    synchronized (JfreeChartUtil.class) {
                        if (MyNumTickUnits_2 == null) {
                            MyNumTickUnits_2 = createTickUnits(2);
                        }
                    }
                }
                tickUnits = MyNumTickUnits_2;
                break;
            case 3:
                if (MyNumTickUnits_3 == null) {
                    synchronized (JfreeChartUtil.class) {
                        if (MyNumTickUnits_3 == null) {
                            MyNumTickUnits_3 = createTickUnits(3);
                        }
                    }
                }
                tickUnits = MyNumTickUnits_3;
                break;
            default:
                if (MyNumTickUnits_4 == null) {
                    synchronized (JfreeChartUtil.class) {
                        if (MyNumTickUnits_4 == null) {
                            MyNumTickUnits_4 = createTickUnits(4);
                        }
                    }
                }
                tickUnits = MyNumTickUnits_4;
                break;
        }
        return tickUnits;
    }

    private static TickUnits createTickUnits(int scale) {
        TickUnits tickUnits = new TickUnits();
        DecimalFormat df8 = new DecimalFormat("#,##0");
        DecimalFormat df9 = new DecimalFormat("#,###,##0");
        DecimalFormat df10 = new DecimalFormat("#,###,###,##0");
        tickUnits.add(new NumberTickUnit(1, df8, 2));
        tickUnits.add(new NumberTickUnit(10, df8, 2));
        tickUnits.add(new NumberTickUnit(100, df8, 2));
        tickUnits.add(new NumberTickUnit(1000, df8, 2));
        tickUnits.add(new NumberTickUnit(10000, df8, 2));
        tickUnits.add(new NumberTickUnit(100000, df8, 2));
        tickUnits.add(new NumberTickUnit(1000000, df9, 2));
        tickUnits.add(new NumberTickUnit(10000000, df9, 2));
        tickUnits.add(new NumberTickUnit(100000000, df9, 2));
        tickUnits.add(new NumberTickUnit(1000000000, df10, 2));
        tickUnits.add(new NumberTickUnit(10000000000.0, df10, 2));
        tickUnits.add(new NumberTickUnit(100000000000.0, df10, 2));
        tickUnits.add(new NumberTickUnit(25, df8, 5));
        tickUnits.add(new NumberTickUnit(250, df8, 5));
        tickUnits.add(new NumberTickUnit(2500, df8, 5));
        tickUnits.add(new NumberTickUnit(25000, df8, 5));
        tickUnits.add(new NumberTickUnit(250000, df8, 5));
        tickUnits.add(new NumberTickUnit(2500000, df9, 5));
        tickUnits.add(new NumberTickUnit(25000000, df9, 5));
        tickUnits.add(new NumberTickUnit(250000000, df9, 5));
        tickUnits.add(new NumberTickUnit(2500000000.0, df10, 5));
        tickUnits.add(new NumberTickUnit(25000000000.0, df10, 5));
        tickUnits.add(new NumberTickUnit(250000000000.0, df10, 5));
        tickUnits.add(new NumberTickUnit(5L, df8, 5));
        tickUnits.add(new NumberTickUnit(50L, df8, 5));
        tickUnits.add(new NumberTickUnit(500L, df8, 5));
        tickUnits.add(new NumberTickUnit(5000L, df8, 5));
        tickUnits.add(new NumberTickUnit(50000L, df8, 5));
        tickUnits.add(new NumberTickUnit(500000L, df8, 5));
        tickUnits.add(new NumberTickUnit(5000000L, df9, 5));
        tickUnits.add(new NumberTickUnit(50000000L, df9, 5));
        tickUnits.add(new NumberTickUnit(500000000L, df9, 5));
        tickUnits.add(new NumberTickUnit(5000000000L, df10, 5));
        tickUnits.add(new NumberTickUnit(50000000000L, df10, 5));
        tickUnits.add(new NumberTickUnit(500000000000L, df10, 5));
        if (scale > 0) {
            DecimalFormat df7 = new DecimalFormat("0.0");
            tickUnits.add(new NumberTickUnit(0.1, df7, 2));
            tickUnits.add(new NumberTickUnit(2.5, df7, 5));
            tickUnits.add(new NumberTickUnit(0.5, df7, 5));
            if (scale > 1) {
                DecimalFormat df6 = new DecimalFormat("0.00");
                tickUnits.add(new NumberTickUnit(0.01, df6, 2));
                tickUnits.add(new NumberTickUnit(0.25, df6, 5));
                tickUnits.add(new NumberTickUnit(0.05, df6, 5));
                if (scale > 2) {
                    DecimalFormat df5 = new DecimalFormat("0.000");
                    tickUnits.add(new NumberTickUnit(0.001, df5, 2));
                    tickUnits.add(new NumberTickUnit(0.025, df5, 5));
                    tickUnits.add(new NumberTickUnit(0.005, df5, 5));
                    if (scale > 3) {
                        DecimalFormat df4 = new DecimalFormat("0.0000");
                        tickUnits.add(new NumberTickUnit(0.0001, df4, 2));
                        tickUnits.add(new NumberTickUnit(0.0025, df4, 5));
                        tickUnits.add(new NumberTickUnit(0.0005, df4, 5));
                    }
                }
            }
        }
        return tickUnits;
    }

    public static JFreeChart create1900Chart(String title,
                                             String timeAxisLabel, String valueAxisLabel, XYDataset dataset,
                                             boolean legend, boolean tooltips) {
        return create1800Chart(title, timeAxisLabel, valueAxisLabel, null,
                dataset, null, legend, tooltips);
    }

    private static XYToolTipGenerator toolTipGenerator = new StandardXYToolTipGenerator(
            "{0}:{2} [{1}]", new SimpleDateFormat(Util.DATA_FORMAT_PATTERN), NumberFormat.getInstance());

    public static JFreeChart create1800Chart(String title,
                                             String timeAxisLabel, String valueAxisLabelL,
                                             String valueAxisLabelR, XYDataset datasetL, XYDataset datasetR,
                                             boolean legend, boolean tooltips) {
        DateAxis timeAxis = new DateAxis(timeAxisLabel);
        timeAxis.setLowerMargin(0.02); // reduce the default margins
        timeAxis.setUpperMargin(0.02);
        timeAxis.setStandardTickUnits(myDateTickUnits());// 时间刻度可调整集合
        NumberAxis valueAxisL = new NumberAxis(valueAxisLabelL);
        valueAxisL.setAutoRangeIncludesZero(false);// 自动显示零点为false
        // 点在图形中的上下边界
        // valueAxisL.setUpperMargin(0.1);
        // valueAxisL.setLowerMargin(0.1);
        XYPlot plot = new XYPlot(datasetL, timeAxis, valueAxisL, null);

        // 折线1
        XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true,
                true);
        rendererL.setSeriesShape(0, ChartShape);// 折点形状
        rendererL.setSeriesShapesFilled(0, false);// 折点填充
        rendererL.setSeriesOutlinePaint(0, MyColorFactory.Color_X33);// 折点颜色
        rendererL.setUseOutlinePaint(true);// 折点外边线显示
        if (tooltips) {
            rendererL.setSeriesToolTipGenerator(0, toolTipGenerator);// ToolTip
        }
        plot.setRenderer(0, rendererL);

        XYLineAndShapeRenderer rendererR = null;
        if (datasetR != null) {
            NumberAxis valueAxisR = new NumberAxis(valueAxisLabelR);
            valueAxisR.setAutoRangeIncludesZero(false);// 自动显示零点为false
            // 点在图形中的上下边界
            valueAxisR.setUpperMargin(0.15);
            valueAxisR.setLowerMargin(0.1);
            valueAxisR.setStandardTickUnits(myNumTickUnits(1));
            // valueAxisR.setLabelAngle(Math.PI/2);
            plot.setRangeAxis(1, valueAxisR);
            plot.setDataset(1, datasetR);
            plot.mapDatasetToRangeAxis(1, 1);
            // 折线2
            rendererR = new XYLineAndShapeRenderer(true, true);
            rendererR.setSeriesShape(0, ChartShape);// 折点形状
            rendererR.setSeriesShapesFilled(0, false);// 折点填充
            rendererR.setSeriesOutlinePaint(0, MyColorFactory.Color_X33);// 折点颜色
            rendererR.setUseOutlinePaint(true);// 折点外边线显示
            rendererR.setSeriesToolTipGenerator(0, toolTipGenerator);// ToolTip
            plot.setRenderer(1, rendererR);
        }

        // 网格线细实线
        plot.setRangeGridlineStroke(GridLineStroke);
        plot.setDomainGridlineStroke(GridLineStroke);

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
                plot, true);
        ChartFactory.getChartTheme().apply(chart);
//		rendererL.setSeriesPaint(0, XY_line);// 折线的颜色
        rendererL.setSeriesStroke(0, stroke);// 折线笔触
        if (rendererR != null) {
            rendererR.setSeriesPaint(0, MyColorFactory.TemperatureColor);// 折线的颜色
            rendererR.setSeriesStroke(0, stroke);// 折线笔触
        }
        return chart;
    }


    static StandardCategoryItemLabelGenerator categoryItemLabelGenerator = new StandardCategoryItemLabelGenerator();

    public static JFreeChart createRainChart(String title,
                                             String categoryAxisLabel, String valueAxisLabel,
                                             CategoryDataset dataset, PlotOrientation orientation,
                                             boolean legend, boolean tooltips) {

        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        CategoryAxis categoryAxis = new CategoryAxis(categoryAxisLabel);
        ValueAxis valueAxis = new NumberAxis(valueAxisLabel);
        valueAxis.setUpperMargin(0.15);
        // 设置工程值刻度可调整集合
        valueAxis.setStandardTickUnits(JfreeChartUtil.myNumTickUnits(1));

        BarRenderer renderer = new BarRenderer();
        if (orientation == PlotOrientation.HORIZONTAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT);
            renderer.setBasePositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
            renderer.setBaseNegativeItemLabelPosition(position2);
        } else if (orientation == PlotOrientation.VERTICAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
            renderer.setBasePositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER);
            renderer.setBaseNegativeItemLabelPosition(position2);
        }
        // renderer.setDrawBarOutline(true);
        renderer.setSeriesItemLabelGenerator(0, categoryItemLabelGenerator);
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setMaximumBarWidth(0.06);
        if (tooltips) {
            renderer.setSeriesToolTipGenerator(0,
                    new StandardCategoryToolTipGenerator("{1}时,{0}：{2} mm",
                            NumberFormat.getInstance()));
        }
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis,
                renderer);
        plot.setOrientation(orientation);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        plot.setRangeGridlineStroke(GridLineStroke);
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
                plot, legend);
        ChartFactory.getChartTheme().apply(chart);
        renderer.setSeriesPaint(0, MyColorFactory.rainColor);
        return chart;

    }


    public static ChartPanel getChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart, ChartPanel.DEFAULT_WIDTH,
                ChartPanel.DEFAULT_HEIGHT, 260, 180, 1920, 1200,
                ChartPanel.DEFAULT_BUFFER_USED, false, true, true, false, true);
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }

}
