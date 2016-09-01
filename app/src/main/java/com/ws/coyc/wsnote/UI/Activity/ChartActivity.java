package com.ws.coyc.wsnote.UI.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.InfoOver;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.DateUtils;
import com.ws.coyc.wsnote.Utils.MyDate;

import java.util.ArrayList;
import java.util.Date;

public class ChartActivity extends AppCompatActivity {

    private LineChart mChart;


    private String[] x_day;
    private String[] y_count;
    private String[] y_get_moeny;
    private String[] y_all_out;

    private LineDataSet line_count;
    private LineDataSet line_get_money;
    private LineDataSet line_all_out;

    private ArrayList<InfoOver> infoOvers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initData();
        initView();
    }

    private void initView() {
        mChart = (LineChart) findViewById(R.id.chart1);

        //在chart上的右下角加描述
        mChart.setDescription("");
        mChart.setDescriptionTextSize(30);
        mChart.setGridBackgroundColor(Color.rgb(255, 255, 255));

        //设置Y轴前后倒置
//        mChart.setInvertYAxisEnabled(false);
//        //设置高亮显示
//        mChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mChart.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        mChart.setDragEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setScaleXEnabled(true);
        //设置是否能扩大扩小
        mChart.setPinchZoom(false);
        mChart.setMaxVisibleValueCount(7);
        // 设置背景颜色

//         mChart.setBackgroundColor(Color.GRAY);
        //设置点击chart图对应的数据弹出标注
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // define an offset to change the original position of the marker
        // (optional)
//        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
//        mv.setMinimumHeight(80);
//        // set the marker to the chart
//        mChart.setMarkerView(mv);
//        // enable/disable highlight indicators (the lines that indicate the
//        // highlighted Entry)
//        mChart.setHighlightIndicatorEnabled(false);
        //设置字体格式，如正楷
        Typeface tf = Typeface.DEFAULT;
        mChart.setDescriptionTypeface(tf);

//        LimitLine ll1 = new LimitLine(95f, "警戒值 95%");
//        ll1.setLineWidth(2f);
////        ll1.setLineColor(Color.rgb(0,0,0));
////        ll1.enableDashedLine(10f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
//        ll1.setTextSize(15f);
//        ll1.setTypeface(tf);

        XAxis xl = mChart.getXAxis();
//      xl.setAvoidFirstLastClipping(true);
//      xl.setAdjustXLabels(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTypeface(tf); // 设置字体
        xl.setTextSize(15f); // 设置字体大小
        xl.setSpaceBetweenLabels(0); // 设置数据之间的间距'

        YAxis yl = mChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//      yl.setAxisMaxValue(220f);
//        yl.addLimitLine(ll1);
        yl.setTypeface(tf); // 设置字体
        yl.setTextSize(15f); // s设置字体大小
        yl.setAxisMinValue(0f);
        yl.setStartAtZero(false);

//      yl.setLabelCount(5); // 设置Y轴最多显示的数据个数

        YAxis y2 = mChart.getAxisRight();
        y2.setEnabled(false);
//        y2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        y2.setTypeface(tf); // 设置字体
//        y2.setTextSize(15f); // s设置字体大小
//        y2.setAxisMinValue(0f);
//        y2.setStartAtZero(false);

        String [] xx = {"1yue","2yue","3yue","4yue","5yue","6yue"};
        String [] yy = {"1","2","3","4","5","6"};

        notifyDataChanged(xx,yy);
    }

    private void initData() {

        infoOvers = DataManager.getInstance().infos_over;

        //init x_day
        Date day = DataManager.getInstance().startDate;
        Date endday = DataManager.getInstance().endDate;
        ArrayList<MyDate> myStrings = new ArrayList<>();
        while (day.getTime()<=endday.getTime())
        {
            myStrings.add(new MyDate(day,DateUtils.getMouth(day)+"-"+DateUtils.getDay(day)));
            day = DateUtils.getNextDay(day);
        }
        int size = myStrings.size();
        x_day = new String[size];
        y_count = new String[size];
        y_get_moeny = new String[size];
        y_all_out = new String[size];
        int serach_p = 0;
        for(int i = 0;i<size;i++)
        {
            x_day[i] = myStrings.get(i).day;
            //init line_y
            int size_info = infoOvers.size();
            int day_of_count = 0;
            int day_of_get_money = 0;
            int day_of_all_out = 0;
            for(int k = serach_p;k<size_info;k++)
            {
                if(DateUtils.isTheSameDay(infoOvers.get(k).dateStart,myStrings.get(i).date) )
                {
                    day_of_count++;
                    day_of_get_money += (infoOvers.get(k).all_out_money-infoOvers.get(k).all_in_money);
                    day_of_all_out += infoOvers.get(k).all_out_money;
                }else
                {
                    serach_p = k;
                    break;
                }
            }
            y_count[i] = day_of_count+"";
            y_get_moeny[i] = day_of_get_money+"";
            y_all_out[i] = day_of_all_out+"";
        }

    }


    private void notifyDataChanged(String[] xx, String[] yy) {
        Typeface tf = Typeface.DEFAULT;
        setData();



        //从X轴进入的动画
        mChart.animateX(1000);
        //设置最小的缩放
        mChart.setScaleMinima(0.5f, 1f);
        //设置视口
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);
//        l.setForm(Legend.LegendForm.CIRCLE);  //设置图最下面显示的类型
//        l.setTypeface(tf);
//        l.setTextSize(30);
//        l.setTextColor(Color.rgb(244, 117, 117));
//        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        l.setYOffset(100);
//        l.setFormSize(20f); // set the size of the legend forms/shapes

        // 刷新图表
        mChart.invalidate();
    }



    private void setData() {

        ArrayList<String> x_day = new ArrayList<String>();
        for (int i = 0; i < this.x_day.length; i++) {
            x_day.add(this.x_day[i]);
        }

        ArrayList<Entry> y_count = getEntries(this.y_count);
        ArrayList<Entry> y_get_money = getEntries(this.y_get_moeny);
        ArrayList<Entry> y_all_out = getEntries(this.y_all_out);

        line_count = getLineDataSet(y_count);
        line_get_money = getLineDataSet(y_get_money);
        line_all_out = getLineDataSet(y_all_out);

        LineData data = new LineData(x_day);

        data.addDataSet(line_count);
        data.addDataSet(line_get_money);
        data.addDataSet(line_all_out);

        // set data
        mChart.setData(data);
    }

    @NonNull
    private ArrayList<Entry> getEntries(String[] y) {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < y.length; i++) {
            yVals.add(new Entry(Integer.parseInt(y[i]), i));
        }
        return yVals;
    }

    @NonNull
    private LineDataSet getLineDataSet(ArrayList<Entry> yVals) {
        LineDataSet set1 = new LineDataSet(yVals, "xxx");
        set1.setDrawCubic(false);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(false);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
//        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(244, 117, 117));    //设置曲线的颜色
        set1.setValueTextSize(15f);
        return set1;
    }
}
