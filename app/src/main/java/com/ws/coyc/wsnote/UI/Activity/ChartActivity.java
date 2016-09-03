package com.ws.coyc.wsnote.UI.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.InfoOver;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.DateUtils;
import com.ws.coyc.wsnote.Utils.MyColor;
import com.ws.coyc.wsnote.Utils.MyDate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartActivity extends AppCompatActivity {

    private LineChart mChart_count;
    private LineChart mChart_get_money;
    private LineChart mChart_all_out;


    private String[] x_day;
    private String[] y_count;
    private String[] y_get_moeny;
    private String[] y_all_out;

    private int all_get = 0;
    private int all_out = 0;


    private LineDataSet line_count;
    private LineDataSet line_get_money;
    private LineDataSet line_all_out;

    private CheckBox box_count;
    private CheckBox box_get_money;
    private CheckBox box_all_out;

    private TextView tv_count;
    private TextView tv_all_out;
    private TextView tv_all_get;
    private TextView tv_arv_day_get;
    private TextView tv_arv_count_get;
    private TextView pre_get_and_out;

    private ArrayList<InfoOver> infoOvers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initData();
        initView();
    }

    private void initView() {
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_all_out = (TextView) findViewById(R.id.tv_all_out);
        tv_all_get = (TextView) findViewById(R.id.tv_all_get);
        tv_arv_day_get = (TextView) findViewById(R.id.tv_arv_day_get);
        tv_arv_count_get = (TextView) findViewById(R.id.tv_arv_count_get);
        pre_get_and_out = (TextView) findViewById(R.id.pre_get_and_out);


        if(x_day.length>0)
        {
            tv_count.setText(x_day.length+"天总账单数 ： "+infoOvers.size()+"");
            tv_all_out.setText("总销售额 : "+all_out+"");
            tv_all_get.setText("总利润 : "+all_get+"");
            tv_arv_day_get.setText("平均每天利润 : "+(all_get/x_day.length)+"");
            if(infoOvers.size()!=0)
            {
                tv_arv_count_get.setText("平均每单利润 : "+(all_get/infoOvers.size())+"");
            }

            float price= (float)all_get/(float)all_out;
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(price);//format 返回的是字符串


            pre_get_and_out.setText("利润率（利润/总额） : "+p+"");
        }


        box_count = (CheckBox) findViewById(R.id.cb_count);
        box_get_money = (CheckBox) findViewById(R.id.cb_money);
        box_all_out = (CheckBox) findViewById(R.id.cb_all_out);


        mChart_count = (LineChart) findViewById(R.id.chart_count);
        mChart_get_money = (LineChart) findViewById(R.id.chart_money);
        mChart_all_out = (LineChart) findViewById(R.id.chart_all_out);




        initChart(mChart_count);
        initChart(mChart_get_money);
        initChart(mChart_all_out);

        box_count.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mChart_count.setVisibility(View.VISIBLE);
                    mChart_get_money.setVisibility(View.INVISIBLE);
                    mChart_all_out.setVisibility(View.INVISIBLE);
                    box_get_money.setChecked(false);
                    box_all_out.setChecked(false);
                }
            }
        });
        box_get_money.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mChart_count.setVisibility(View.INVISIBLE);
                    mChart_get_money.setVisibility(View.VISIBLE);
                    mChart_all_out.setVisibility(View.INVISIBLE);
                    box_count.setChecked(false);
                    box_all_out.setChecked(false);
                }
            }
        });
        box_all_out.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mChart_count.setVisibility(View.INVISIBLE);
                    mChart_get_money.setVisibility(View.INVISIBLE);
                    mChart_all_out.setVisibility(View.VISIBLE);
                    box_count.setChecked(false);
                    box_get_money.setChecked(false);
                }
            }
        });



        notifyDataChanged();
    }

    private void initChart(LineChart mChart_count) {
        //在chart上的右下角加描述
        mChart_count.setDescription("");
        mChart_count.setDescriptionTextSize(30);
        mChart_count.setGridBackgroundColor(Color.rgb(255, 255, 255));
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mChart_count.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        mChart_count.setDragEnabled(true);
        mChart_count.setScaleYEnabled(false);
        mChart_count.setScaleXEnabled(true);
        mChart_count.setDragOffsetX(20);
        //设置是否能扩大扩小
        mChart_count.setPinchZoom(false);
//        mChart_count.setMaxVisibleValueCount(7);
        // 设置背景颜色

        //设置字体格式，如正楷
        Typeface tf = Typeface.DEFAULT;
        mChart_count.setDescriptionTypeface(tf);

        XAxis xl = mChart_count.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTypeface(tf); // 设置字体
        xl.setTextSize(15f); // 设置字体大小
        xl.setSpaceBetweenLabels(0); // 设置数据之间的间距'

        YAxis yl = mChart_count.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yl.setTypeface(tf); // 设置字体
        yl.setTextSize(15f); // s设置字体大小
        yl.setAxisMinValue(0f);
        yl.setStartAtZero(false);

        YAxis y2 = mChart_count.getAxisRight();
        y2.setEnabled(false);
    }

    private void initData() {

        infoOvers = DataManager.getInstance().infos_over;

        all_out = 0;
        all_get = 0;
        //init x_day
        Date day = DataManager.getInstance().startDate;
        Date endday = DataManager.getInstance().endDate;
        l.l(DateUtils.ConverToString_YMDHM(endday));
        ArrayList<MyDate> myStrings = new ArrayList<>();
        Date temp_date = new Date();
        if(endday.getTime()<temp_date.getTime())
        {
            temp_date = endday;
        }

        while (day.getTime()<=temp_date.getTime())
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
            l.l("oiiii"+x_day[i]);
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
            all_out+=day_of_all_out;
            all_get+=day_of_get_money;
        }




    }


    private void notifyDataChanged() {
        setData();
        refrahChart(mChart_count);
        refrahChart(mChart_get_money);
        refrahChart(mChart_all_out);

    }

    private void refrahChart(LineChart mChart_count) {
        //从X轴进入的动画
        mChart_count.animateX(1000);
        //设置最小的缩放
        mChart_count.setScaleMinima(0.5f, 1f);
        //设置视口
        // get the legend (only possible after setting data)
        Legend l = mChart_count.getLegend();
        l.setEnabled(false);
        // 刷新图表
        mChart_count.invalidate();
    }


    private void setData() {

        ArrayList<String> x_day = new ArrayList<>();
        for (int i = 0; i < this.x_day.length; i++) {
            x_day.add(this.x_day[i]);
        }

        ArrayList<Entry> y_count = getEntries(this.y_count);
        ArrayList<Entry> y_get_money = getEntries(this.y_get_moeny);
        ArrayList<Entry> y_all_out = getEntries(this.y_all_out);


        line_count = getLineDataSet(y_count);
        line_get_money = getLineDataSet(y_get_money, MyColor.red_5,true);
        line_all_out = getLineDataSet(y_all_out,MyColor.coffe_5,true);


        LineData data_count = new LineData(x_day);
        data_count.addDataSet(line_count);
        data_count.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value+"";
            }
        });

        LineData data_money = new LineData(x_day);
        data_money.addDataSet(line_get_money);
        data_money.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


                return (int)value+"";
            }
        });

        LineData data_all_get = new LineData(x_day);
        data_all_get.addDataSet(line_all_out);
        data_all_get.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value+"";
            }
        });

        // set data
        mChart_count.setData(data_count);
        mChart_get_money.setData(data_money);
        mChart_all_out.setData(data_all_get);


        Legend l = mChart_get_money.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);  //设置图最下面显示的类型
        l.setTextSize(30);
        l.setTextColor(Color.rgb(244, 117, 117));
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setYOffset(100);
        l.setFormSize(20f); // set the size of the legend forms/shapes
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
    private LineDataSet getLineDataSet(ArrayList<Entry> yVals,int color,boolean fill) {
        LineDataSet set1 = new LineDataSet(yVals, "");
        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(fill);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setHighLightColor(color);
        set1.setColor(color);    //设置曲线的颜色
        set1.setValueTextSize(15f);
        return set1;
    }
    @NonNull
    private LineDataSet getLineDataSet(ArrayList<Entry> yVals) {
        LineDataSet set1 = new LineDataSet(yVals, "");
        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(244, 117, 117));    //设置曲线的颜色
        set1.setValueTextSize(15f);
        return set1;
    }





}
