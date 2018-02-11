package microtech.hxswork.com.frame_ui.main.sign;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.ItemType;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderAdapter;
import microtech.hxswork.com.frame_ui.main.order.OrderListItemType;
import microtech.hxswork.com.frame_ui.main.sign.marker.XYMarkerView;

import static microtech.hxswork.com.frame_ui.main.sign.LineChartManager.dataSet;
import static microtech.hxswork.com.frame_ui.main.sign.LineChartManager.xAxis;
import static microtech.hxswork.com.frame_ui.main.sign.LineChartManager.yAxisLeft;

/**
 * Created by microtech on 2018/1/9.
 */

public class SignAdapater extends MultipleRecyclerAdapter {

    protected SignAdapater(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SignitemType.SIGN_ITEM_0, R.layout.sign_item_layout);//血压
        addItemType(SignitemType.SIGN_ITEM_1, R.layout.sign_item_layout);//血氧
        addItemType(SignitemType.SIGN_ITEM_2, R.layout.sign_item_layout);//体温
        addItemType(SignitemType.SIGN_ITEM_3, R.layout.sign_item_layout);//脉搏
        openLoadAnimation(BaseQuickAdapter.LOADING_VIEW);
    }

    public static SignAdapater create(DataConVerter verter){
        return new SignAdapater(verter.CONVERT());
    }


    public void refresh(List<MultipleItemEntity> data) {
        getData().clear();
        setNewData(data);
        notifyDataSetChanged();
    }
    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case SignitemType.SIGN_ITEM_0://血压
                ArrayList<String> xValues = new ArrayList<>();
                List<MultipleItemEntity> list_p = item.getField(SignitemType.LIST);
                LineChart chart =   helper.getView(R.id.chart);
                AppCompatTextView sign_item_title = helper.getView(R.id.sign_item_title);//标题
                AppCompatTextView sign_z_nums = helper.getView(R.id.sign_z_nums);//标题
                AppCompatTextView sign_g_nums = helper.getView(R.id.sign_g_nums);//标题
                AppCompatTextView sign_d_nums = helper.getView(R.id.sign_d_nums);//标题
                sign_item_title.setText("近期血压");
                sign_item_title.setTextColor(Color.parseColor("#38E6FF"));
                int z=0,g=0,d=0;
                int size = list_p.size();
                for (int i =0 ; i < size;i++)
                {
                    if(list_p.get(i).getField(SignFilds.STATUS).equals("0"))
                    {
                        z++;
                    }else if(list_p.get(i).getField(SignFilds.STATUS).equals("1")){
                        g++;
                    }else if(list_p.get(i).getField(SignFilds.STATUS).equals("-1")){
                        d++;
                    }
                    xValues.add(i+"");
                }

                sign_z_nums.setText(z+"");
                sign_g_nums.setText(g+"");
                sign_d_nums.setText(d+"");

                LineChartManager.initDoubleLineChart(mContext,chart,xValues,getData(list_p,0),getData1(list_p));
                if(xValues.size() > 1) {
                    chart.getXAxis().setLabelCount(xValues.size() - 1);     //设置显示10个标签
                }else {
                    chart.getXAxis().setLabelCount(xValues.size());
                }
                LimitLine llx = new LimitLine(0, "");
                llx.setLineWidth(2f);
                llx.setLineColor(Color.parseColor("#2FDBF4"));
                xAxis.addLimitLine(llx);
                maker(mContext,list_p,chart,0);
                per();
                break;
            case SignitemType.SIGN_ITEM_1://血氧
                ArrayList<String> xValues1 = new ArrayList<>();
                List<MultipleItemEntity> list_o = item.getField(SignitemType.LIST);
                LineChart chart1 =   helper.getView(R.id.chart);
                AppCompatTextView sign_item_title1 = helper.getView(R.id.sign_item_title);//标题
                AppCompatTextView sign_z_nums1 = helper.getView(R.id.sign_z_nums);//标题
                AppCompatTextView sign_g_nums1 = helper.getView(R.id.sign_g_nums);//标题
                AppCompatTextView sign_d_nums1 = helper.getView(R.id.sign_d_nums);//标题
                sign_item_title1.setText("近期血氧");
                sign_item_title1.setTextColor(Color.parseColor("#FFCD82"));
                int z1=0,g1=0,d1=0;
                int size1 = list_o.size();
                for (int i =0 ; i < size1;i++)
                {
                    System.out.println("*****SignFilds.STATUS******"+list_o.get(i).getField(SignFilds.STATUS));
                    if(list_o.get(i).getField(SignFilds.STATUS).equals("0"))
                    {
                        z1++;
                    }else if(list_o.get(i).getField(SignFilds.STATUS).equals("1")){
                        g1++;
                    }else if(list_o.get(i).getField(SignFilds.STATUS).equals("-1")){
                        d1++;
                    }
                    xValues1.add(i+"");
                }

                sign_z_nums1.setText(z1+"");
                sign_g_nums1.setText(g1+"");
                sign_d_nums1.setText(d1+"");
                LineChartManager.initSingleLineChart(mContext,chart1,xValues1,getData(list_o,1));//单数据
                if(xValues1.size() > 1) {
                    chart1.getXAxis().setLabelCount(xValues1.size() - 1);     //设置显示10个标签
                }else {
                    chart1.getXAxis().setLabelCount(xValues1.size());
                }

                dataSet.setColor(Color.parseColor("#FFCD82"));
                dataSet.setCircleColor(Color.parseColor("#FFCD82"));
                dataSet.setLineWidth(4f);
                LimitLine ll1 = new LimitLine(0, "");
                ll1.setLineWidth(2f);
                ll1.setLineColor(Color.parseColor("#FFCD82"));
                xAxis.addLimitLine(ll1);
                maker(mContext,list_o,chart1,1);
                oxy();
                break;
            case SignitemType.SIGN_ITEM_2://体温
                ArrayList<String> xValues2 = new ArrayList<>();
                List<MultipleItemEntity> list_t = item.getField(SignitemType.LIST);
                LineChart chart2 =   helper.getView(R.id.chart);
                AppCompatTextView sign_item_title2 = helper.getView(R.id.sign_item_title);//标题
                AppCompatTextView sign_z_nums2 = helper.getView(R.id.sign_z_nums);//标题
                AppCompatTextView sign_g_nums2 = helper.getView(R.id.sign_g_nums);//标题
                AppCompatTextView sign_d_nums2 = helper.getView(R.id.sign_d_nums);//标题
                sign_item_title2.setText("近期体温");
                sign_item_title2.setTextColor(Color.parseColor("#62D4D9"));
                int z2=0,g2=0,d2=0;
                int size2 = list_t.size();
                for (int i =0 ; i < size2;i++)
                {
                    if(list_t.get(i).getField(SignFilds.STATUS).equals("0"))
                    {
                        z2++;
                    }else if(list_t.get(i).getField(SignFilds.STATUS).equals("1")){
                        g2++;
                    }else if(list_t.get(i).getField(SignFilds.STATUS).equals("-1")){
                        d2++;
                    }
                    xValues2.add(i+"");
                }
                sign_z_nums2.setText(z2+"");
                sign_g_nums2.setText(g2+"");
                sign_d_nums2.setText(d2+"");
                LineChartManager.initSingleLineChart(mContext,chart2,xValues2,getData(list_t,2));//单数据
                if(xValues2.size() > 1) {
                    chart2.getXAxis().setLabelCount(xValues2.size() - 1);     //设置显示10个标签
                }else {
                    chart2.getXAxis().setLabelCount(xValues2.size());
                }
                dataSet.setColor(Color.parseColor("#62D4D9"));
                dataSet.setCircleColor(Color.parseColor("#62D4D9"));
                dataSet.setLineWidth(4f);
                LimitLine ll2 = new LimitLine(0, "");
                ll2.setLineWidth(2f);
                ll2.setLineColor(Color.parseColor("#8CDEE2"));
                xAxis.addLimitLine(ll2);
                maker(mContext,list_t,chart2,2);
                nomber();
                break;
            case SignitemType.SIGN_ITEM_3://脉搏
                ArrayList<String> xValues3 = new ArrayList<>();
                List<MultipleItemEntity> list_m = item.getField(SignitemType.LIST);
                LineChart chart3 =   helper.getView(R.id.chart);
                AppCompatTextView sign_item_title3 = helper.getView(R.id.sign_item_title);//标题
                AppCompatTextView sign_z_nums3 = helper.getView(R.id.sign_z_nums);//标题
                AppCompatTextView sign_g_nums3 = helper.getView(R.id.sign_g_nums);//标题
                AppCompatTextView sign_d_nums3 = helper.getView(R.id.sign_d_nums);//标题
                sign_item_title3.setText("近期脉搏");
                sign_item_title3.setTextColor(Color.parseColor("#ff99cc"));
                int z3=0,g3=0,d3=0;
                int size3= list_m.size();
                for (int i =0 ; i < size3;i++)
                {

                    if(list_m.get(i).getField(SignFilds.STATUS).equals("0"))
                    {
                        z3++;
                    }else if(list_m.get(i).getField(SignFilds.STATUS).equals("1")){
                        g3++;
                    }else if(list_m.get(i).getField(SignFilds.STATUS).equals("-1")){
                        d3++;
                    }
                    xValues3.add(i+"");
                }
                sign_z_nums3.setText(z3+"");
                sign_g_nums3.setText(g3+"");
                sign_d_nums3.setText(d3+"");
                LineChartManager.initSingleLineChart(mContext,chart3,xValues3,getData(list_m,3));//单数据
                if(xValues3.size() > 1) {
                    chart3.getXAxis().setLabelCount(xValues3.size() - 1);     //设置显示10个标签
                }else {
                    chart3.getXAxis().setLabelCount(xValues3.size());
                }
                dataSet.setColor(Color.parseColor("#ff99cc"));
                dataSet.setCircleColor(Color.parseColor("#ff99cc"));
                dataSet.setLineWidth(4f);
                LimitLine ll3 = new LimitLine(0, "");
                ll3.setLineWidth(2f);
                ll3.setLineColor(Color.parseColor("#ff99cc"));
                xAxis.addLimitLine(ll3);
                maker(mContext,list_m,chart3,3);
                heart();
                break;
        }
    }

    private ArrayList<Entry> getData(List<MultipleItemEntity> list_line, int flage) {
        ArrayList<Entry> values = null;
        if (flage == 0) {
            //标识血压
            values = new ArrayList<>();
            for (int i = 0; i < list_line.size(); i++) {
                float dia_value=0.0f;
                String[] a;
                if(list_line.get(i).getField(SignFilds.VALUE)!= null)
                {
                        a = list_line.get(i).getField(SignFilds.VALUE).toString().split("/");
                        dia_value = Float.parseFloat(a[0]);
                }
                values.add(new Entry(i,dia_value));
            }
            LineChartManager.setLineName1("舒张压");
        }
        else if(flage == 1)
        {       //标识血氧
            values = new ArrayList<>();
            for (int i = 0; i < list_line.size(); i++) {
                float dia_value=0.0f;
                if(list_line.get(i).getField(SignFilds.VALUE)!= null)
                {
                    dia_value = Float.parseFloat(list_line.get(i).getField(SignFilds.VALUE).toString());
                }
                values.add(new Entry(i,dia_value));
            }
            LineChartManager.setLineName("血糖");
        }
        else if(flage == 2)//体温
        {
            values = new ArrayList<>();
            for (int i = 0; i < list_line.size(); i++) {
                float dia_value=0.0f;
                if(list_line.get(i).getField(SignFilds.VALUE)!= null)
                {
                    dia_value = Float.parseFloat(list_line.get(i).getField(SignFilds.VALUE).toString());
                }
                values.add(new Entry(i,dia_value));
            }
            LineChartManager.setLineName("体温");
        }else if(flage == 3)//脉搏
        {
            values = new ArrayList<>();
            for (int i = 0; i < list_line.size(); i++) {
                float dia_value=0.0f;
                if(list_line.get(i).getField(SignFilds.VALUE)!= null)
                {
                    dia_value = Float.parseFloat(list_line.get(i).getField(SignFilds.VALUE).toString());
                }
                values.add(new Entry(i,dia_value));
            }
            LineChartManager.setLineName("心率");
        }else if(flage == 4){//血糖
            values = new ArrayList<>();
            for (int i = 0; i < list_line.size(); i++) {
                float dia_value=0.0f;
                if(list_line.get(i).getField(SignFilds.VALUE)!= null)
                {
                    dia_value = Float.parseFloat(list_line.get(i).getField(SignFilds.VALUE).toString());
                }
                values.add(new Entry(i,dia_value));
            }
            LineChartManager.setLineName("血氧");
        }
        return values;
    }

    private ArrayList<Entry> getData1(   List<MultipleItemEntity> list_line) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < list_line.size(); i++) {
            float dia_value=0.0f;
            String[] a;
            if(list_line.get(i).getField(SignFilds.VALUE)!= null)
            {
                    a = list_line.get(i).getField(SignFilds.VALUE).toString().split("/");
                    dia_value = Float.parseFloat(a[1]);
            }
            values.add(new Entry(i,dia_value));
        }
        LineChartManager.setLineName("收缩压");
        return values;
    }


    private void per(){//血压
        LimitLine ll1 = new LimitLine(140f, "偏高预警线");
        ll1.setLineColor(Color.parseColor("#D1DDE6"));
        ll1.setTextColor(Color.parseColor("#8FACC8"));
        ll1.setTextSize(12);
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);

        LimitLine ll2 = new LimitLine(90f, "偏低预警线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setTextColor(Color.parseColor("#8FACC8"));
        ll2.setTextSize(12);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        ll2.setLineColor(Color.parseColor("#D1DDE6"));


        LimitLine ll3 = new LimitLine(115f, "");//正常线
        ll3.setLineWidth(1f);
        ll3.setLineColor(Color.parseColor("#D1DDE6"));

        LimitLine ll4 = new LimitLine(200f, "");//顶部的线
        ll4.setLineWidth(1f);
        ll4.setLineColor(Color.parseColor("#D1DDE6"));
        //偏高预警线
        yAxisLeft.setAxisMaximum(200f);
        //偏低预警线
        yAxisLeft.addLimitLine(ll1);
        yAxisLeft.addLimitLine(ll2);
        yAxisLeft.addLimitLine(ll3);
        yAxisLeft.addLimitLine(ll4);
        yAxisLeft.setAxisMinimum(0);
    }

    private void oxy(){
        LimitLine ll1 = new LimitLine(97f, "偏高预警线");
        ll1.setLineColor(Color.parseColor("#D1DDE6"));
        ll1.setTextColor(Color.parseColor("#8FACC8"));
        ll1.setTextSize(12);
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);

        LimitLine ll2 = new LimitLine(90f, "偏低预警线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setTextColor(Color.parseColor("#8FACC8"));
        ll2.setTextSize(12);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        ll2.setLineColor(Color.parseColor("#D1DDE6"));


        LimitLine ll3 = new LimitLine(93f, "");//正常线
        ll3.setLineWidth(1f);
        ll3.setLineColor(Color.parseColor("#D1DDE6"));

        LimitLine ll4 = new LimitLine(100f, "");//顶部的线
        ll4.setLineWidth(1f);
        ll4.setLineColor(Color.parseColor("#D1DDE6"));
        //偏高预警线
        yAxisLeft.setAxisMaximum(100f);
        //偏低预警线
        yAxisLeft.addLimitLine(ll1);
        yAxisLeft.addLimitLine(ll2);
        yAxisLeft.addLimitLine(ll3);
        yAxisLeft.addLimitLine(ll4);
        yAxisLeft.setAxisMinimum(70);
    }

    private void nomber()
    {
        LimitLine ll1 = new LimitLine(41f, "偏高预警线");
        ll1.setLineColor(Color.parseColor("#D1DDE6"));
        ll1.setTextColor(Color.parseColor("#8FACC8"));
        ll1.setTextSize(12);
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);

        LimitLine ll2 = new LimitLine(32f, "偏低预警线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setTextColor(Color.parseColor("#8FACC8"));
        ll2.setTextSize(12);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        ll2.setLineColor(Color.parseColor("#D1DDE6"));


        LimitLine ll3 = new LimitLine(35f, "");
        ll3.setLineWidth(1f);
        ll3.setLineColor(Color.parseColor("#D1DDE6"));

        LimitLine ll4 = new LimitLine(50f, "");
        ll4.setLineWidth(1f);
        ll4.setLineColor(Color.parseColor("#D1DDE6"));
        yAxisLeft.setAxisMaximum(45f);
        //偏高预警线
        yAxisLeft.addLimitLine(ll1);
        //偏低预警线
        yAxisLeft.addLimitLine(ll2);
        yAxisLeft.addLimitLine(ll3);
        yAxisLeft.addLimitLine(ll4);
    }

    private void heart(){//心率
        LimitLine ll1 = new LimitLine(100f, "偏高预警线");
        ll1.setLineColor(Color.parseColor("#D1DDE6"));
        ll1.setTextColor(Color.parseColor("#8FACC8"));
        ll1.setTextSize(12);
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);

        LimitLine ll2 = new LimitLine(55f, "偏低预警线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setTextColor(Color.parseColor("#8FACC8"));
        ll2.setTextSize(12);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        ll2.setLineColor(Color.parseColor("#D1DDE6"));


        LimitLine ll3 = new LimitLine(75f, "");//正常线
        ll3.setLineWidth(1f);
        ll3.setLineColor(Color.parseColor("#D1DDE6"));

        LimitLine ll4 = new LimitLine(200f, "");//顶部的线
        ll4.setLineWidth(1f);
        ll4.setLineColor(Color.parseColor("#D1DDE6"));
        //偏高预警线
        yAxisLeft.setAxisMaximum(200f);
        //偏低预警线
        yAxisLeft.addLimitLine(ll1);
        yAxisLeft.addLimitLine(ll2);
        yAxisLeft.addLimitLine(ll3);
        yAxisLeft.addLimitLine(ll4);
        yAxisLeft.setAxisMinimum(0);
    }

    private void maker(Context context,List<MultipleItemEntity> list , LineChart chart,int flage)
    {
        XYMarkerView markerView = new XYMarkerView(context,list,flage);
        markerView.setChartView(chart);
        chart.setMarker(markerView);
    }
}
