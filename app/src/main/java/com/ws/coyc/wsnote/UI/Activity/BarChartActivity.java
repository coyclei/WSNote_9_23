package com.ws.coyc.wsnote.UI.Activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.Utils.MyColor;


import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    private BarChart mBarChart_count;
    private BarChart mBarChart_money;
    private BarChart mBarChart_all_out;

    private BarData mBarData_count;
    private BarData mBarData_money;
    private BarData mBarData_all_out;

    private CheckBox box_count;
    private CheckBox box_get_money;
    private CheckBox box_all_out;



    private ArrayList<Person> persons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        persons = DataManager.getInstance().persons;

        box_count = (CheckBox) findViewById(R.id.cb_count);
        box_get_money = (CheckBox) findViewById(R.id.cb_money);
        box_all_out = (CheckBox) findViewById(R.id.cb_all_out);

        mBarChart_count = (BarChart) findViewById(R.id.chart_count);
        mBarChart_money = (BarChart) findViewById(R.id.chart_money);
        mBarChart_all_out = (BarChart) findViewById(R.id.chart_all_out);


        box_count.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mBarChart_count.setVisibility(View.VISIBLE);
                    mBarChart_money.setVisibility(View.INVISIBLE);
                    mBarChart_all_out.setVisibility(View.INVISIBLE);
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
                    mBarChart_count.setVisibility(View.INVISIBLE);
                    mBarChart_money.setVisibility(View.VISIBLE);
                    mBarChart_all_out.setVisibility(View.INVISIBLE);
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
                    mBarChart_count.setVisibility(View.INVISIBLE);
                    mBarChart_money.setVisibility(View.INVISIBLE);
                    mBarChart_all_out.setVisibility(View.VISIBLE);
                    box_count.setChecked(false);
                    box_get_money.setChecked(false);
                }
            }
        });



        mBarData_count = getBarData_count(persons.size());
        mBarData_money = getBarData_money(persons.size());
        mBarData_all_out = getmBarData_all_out(persons.size());
        showBarChart(mBarChart_count, mBarData_count);
        showBarChart(mBarChart_money, mBarData_money);
        showBarChart(mBarChart_all_out, mBarData_all_out);
    }

    private void showBarChart(BarChart barChart, BarData barData) {

        barChart.setDrawBorders(false);  ////是否在折线图上添加边框

        barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
//        barChart.setScaleEnabled(true);// 是否可以缩放
        barChart.setScaleYEnabled(false);

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(false);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(18f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

//      X轴设定
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);

        YAxis yl = barChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yl.setTextSize(15f); // s设置字体大小
        yl.setAxisMinValue(0f);
        yl.setStartAtZero(false);

        YAxis y2 = barChart.getAxisRight();
        y2.setEnabled(false);


        barChart.animateX(1500); // 立即执行的动画,x轴
    }

    private BarData getBarData_count(int count) {
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xValues.add(persons.get(i).name);
        }

        ArrayList<BarEntry> yValues_count = new ArrayList<>();

        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues_count.add(new BarEntry(persons.get(i).count, i));
        }
        // y轴的数据集合
        BarDataSet barDataSet_count = getBarDataSet(yValues_count,"总单数", MyColor.coffe_5);

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet_count); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value+"";
            }
        });
        return barData;
    }

    private BarData getBarData_money(int count) {
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xValues.add(persons.get(i).name);
        }

        ArrayList<BarEntry> yValues_getmoney = new ArrayList<>();

        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues_getmoney.add(new BarEntry(persons.get(i).all_prise_out-persons.get(i).all_prise_in, i));
        }
        // y轴的数据集合
        BarDataSet barDataSet_get_money = getBarDataSet(yValues_getmoney,"利润",MyColor.red_5);

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet_get_money); //

        BarData barData = new BarData(xValues, barDataSets);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


                return (int)value+"";
            }
        });
        return barData;
    }


    private BarData getmBarData_all_out(int count) {
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xValues.add(persons.get(i).name);
        }

        ArrayList<BarEntry> yValues_all_out = new ArrayList<>();

        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues_all_out.add(new BarEntry(persons.get(i).all_prise_out, i));
        }
        // y轴的数据集合
        BarDataSet barDataSet_all_out = getBarDataSet(yValues_all_out,"销售额",MyColor.giren_5);

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet_all_out); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                return (int)value+"";
            }
        });
        return barData;
    }





    @NonNull
    private BarDataSet getBarDataSet(ArrayList<BarEntry> yValues_count,String text,int color) {
        BarDataSet barDataSet = new BarDataSet(yValues_count, text);
        barDataSet.setValueTextSize(13);
        barDataSet.setColor(color);
        return barDataSet;
    }
}
