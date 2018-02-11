package microtech.hxswork.com.frame_ui.main.sign;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * Created by microtech on 2017/10/23.
 */

public class LineChartManager {

    private static String lineName = null;
    private static String lineName1 = null;
    public  static XAxis xAxis;
    public  static  YAxis yAxisLeft;
    public static  LineDataSet dataSet;

    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue) {
        initDataStyle(context,mLineChart);

        //设置折线的样式
         dataSet = new LineDataSet(yValue, lineName);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        dataSet.setColor(Color.parseColor("#38E6FF"));
//        dataSet.setCircleColor(Color.parseColor("#38E6FF"));
        dataSet.setHighLightColor(Color.parseColor("#00000000"));
        dataSet.setLineWidth(3f);
        // 显示坐标点的小圆点
        dataSet.setDrawCircles(true);
        // 显示定位线
        dataSet.setValueTextSize(0f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);
//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(1000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }
    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     * @param yValue1 另一条折线在y轴的值
     */
    public static void initDoubleLineChart(Context context, LineChart mLineChart, final ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {

        initDataStyle(context,mLineChart);

        LineDataSet dataSet = new LineDataSet(yValue, lineName);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(Color.parseColor("#38E6FF"));
        dataSet.setCircleColor(Color.parseColor("#38E6FF"));
        dataSet.setHighLightColor(Color.parseColor("#00000000"));
        dataSet.setLineWidth(3f);
        // 显示坐标点的小圆点
        dataSet.setDrawCircles(true);
        // 显示定位线
        dataSet.setValueTextSize(0f);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawValues(false);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet1.setValueTextSize(0f);
        //dataSet1.enableDashedLine(10f, 10f, 0f);//将折线设置为曲线
        dataSet1.setColor(Color.parseColor("#50BEFF"));
        dataSet1.setCircleColor(Color.parseColor("#50BEFF"));
        dataSet1.setDrawValues(false);
        dataSet1.setDrawCircles(true);
        dataSet1.setHighLightColor(Color.parseColor("#00000000"));
        dataSet1.setLineWidth(3f);
        dataSet1.setCircleRadius(3f);
        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet);
        dataSets.add(dataSet1);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(1000);
        mLineChart.animateX(2000);
        mLineChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param mLineChart
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {

        mLineChart.getDescription().setEnabled(false);
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawGridBackground(false);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setForm(Legend.LegendForm.LINE);
        //设置x轴的样式
        xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1);
        //xAxis.setLabelRotationAngle(-20f);//X轴显示的角度
        xAxis.setAxisLineColor(Color.parseColor("#D1DDE6"));
        xAxis.setTextColor(Color.parseColor("#898EA6"));
        //xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        //设置是否显示x轴
        xAxis.setEnabled(true);
        //设置左边y轴的样式
        yAxisLeft = mLineChart.getAxisLeft();
        // 不显示y轴
        yAxisLeft.setDrawAxisLine(false);
        //y轴最大
        // 不从y轴发出横向直线
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisLineColor(Color.parseColor("#66CDAA"));
        yAxisLeft.setTextColor(Color.parseColor("#898EA6"));
        yAxisLeft.setAxisLineWidth(5);
        // 设置y轴数据的位置
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisLeft.setLabelCount(10, false);
    }

    /**
     * @Description:设置折线的名称
     * @param name
     */
    public static void setLineName(String name){
        lineName = name;
    }

    /**
     * @Description:设置另一条折线的名称
     * @param name
     */
    public static void setLineName1(String name){
        lineName1 = name;
    }
}
