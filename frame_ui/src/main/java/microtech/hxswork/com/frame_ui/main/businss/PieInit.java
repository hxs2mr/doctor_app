package microtech.hxswork.com.frame_ui.main.businss;

import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import microtech.hxswork.com.frame_core.util.SAToast;

import static microtech.hxswork.com.frame_core.init.Frame.getApplicationContext;

/**
 * Created by microtech on 2017/12/11.
 */

public class PieInit  {

    public static void initReportChart(final PieChart mChart, PieData pieData) {

        mChart.setUsePercentValues(true);

        mChart.setExtraOffsets(5,10,5,5);  //设置间距
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setRotationAngle(90); // 初始旋转角度
        mChart.setRotationEnabled(true); // 可以手动旋转

        mChart.setCenterTextColor(Color.parseColor("#37BBFB"));
        mChart.setDrawHoleEnabled(true);//空心猿

        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);

        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        // 设置饼图是否接收点击事件，默认为true
        mChart.setTransparentCircleRadius(61f);
        mChart.setTouchEnabled(true);  //设置是否响应点击触摸

        mChart.setDrawCenterText(true);  //设置是否绘制中心区域文字

        mChart.setDrawEntryLabels(false);  //设置是否绘制标签

        mChart.setHighlightPerTapEnabled(true);  //设置是否高亮显示触摸的区域

        mChart.setData(pieData);  //设置数据

        Legend mLegend = mChart.getLegend(); //设置比例图
        mLegend.setEnabled(false);
        mChart.setDrawMarkerViews(true);  //设置是否绘制标记

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);  //设置动画效果
    }

}
