package com.example.yoshizumiapp;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

@TargetApi(26)
public class ScrollingActivity extends AppCompatActivity {
    private RadarChart chart;

    // ひとつひとつのテーブルデータをレイアウトに追加していく
    private void addData2View(String[][] datas, int index){
        String[] data = datas[index];

        int margin_size = 10;
        TextView data_text = new TextView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        layoutParams.setMargins(0, 0, 0, margin_size);

        data_text.setText(data[0] + " : " + data[1]);
        data_text.setTextSize(32);
        data_text.setGravity(Gravity.CENTER);
        data_text.setBackgroundColor(0xffffff);
        data_text.setLayoutParams(layoutParams);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);
        linearLayout.addView(data_text);
    }

    //　テーブルデータの内容を設定
    private void createDataView(){
        String[] dataFromMain = getIntent().getExtras().getStringArray("MainData");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(dataFromMain[0]);
        getWindow().setExitTransition(new Slide());

        /*
          Data setting
         */
        String datas[][] = new String[4][2];
        for(int i=0; i < datas.length; i++){
            datas[i][0] = "";
            datas[i][1] = "";
        }

        datas[0][0] = "学校の数";
        datas[1][0] = "駅の数";
        datas[2][0] = "犯罪発生率";
        datas[3][0] = "人口";

        datas[0][1] = dataFromMain[2] + "個";
        datas[1][1] = dataFromMain[3] + "個";
        datas[2][1] = dataFromMain[4] + "%";
        datas[3][1] = dataFromMain[5] + "人";
        // add data to view list
        for(int i=0;i<datas.length;i++){
            addData2View(datas,i);
        }
    }

    // レーダーチャートのレイアウト設定
    private void setRadarChart(){
        chart = findViewById(R.id.radarChart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.rgb(60, 65, 82));
        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.setWebAlpha(100);
//
        setData();
        chart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            String[] data = new String[]{"schoolCount", "stationCount", "crimePer", "population"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return data[(int) value % data.length];
            }
        });

        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(4, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(5f);
        yAxis.setDrawLabels(false);

        Legend l = chart.getLegend();

        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }

    /* TODO: パラメータいい感じにする */
    /* レーダーチャートのデータ設定 */
    private void setData(){
        String[] dataFromMain = getIntent().getExtras().getStringArray("MainData");
        int cnt = 4;

        ArrayList<RadarEntry> entries1 = new ArrayList<>(); // target data
        ArrayList<RadarEntry> entries2 = new ArrayList<>(); // average

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        for (int i = 0; i < cnt; i++) {
            float val1 = (float) 3.0;
            entries1.add(new RadarEntry(val1));
        }
        float menseki = (float)Float.valueOf(dataFromMain[1]);
        /* 要調整 */
        // school
        float val2 = (float) Float.valueOf(dataFromMain[2]) / menseki;
        if(val2 > 0.08){
            val2 = 5;
        }else if (val2 > 0.06){
            val2 = 4;
        }else if(val2 > 0.04){
            val2 = 3;
        }else if(val2 > 0.02){
            val2 = 2;
        }else{
            val2 = 1;
        }
        entries2.add(new RadarEntry(val2));
        // station
        val2 = (float) Float.valueOf(dataFromMain[3]) / menseki;
        if(val2 > 1.0){
            val2 = 5;
        }else if (val2 > 0.1){
            val2 = 4;
        }else if(val2 > 0.05){
            val2 = 3;
        }else if(val2 > 0.01){
            val2 = 2;
        }else{
            val2 = 1;
        }
        entries2.add(new RadarEntry(val2));
        //crime
        val2 = (float) Float.valueOf(dataFromMain[4]);
        if(val2 < 0.1){
            val2 = 5;
        }else if (val2 < 0.5){
            val2 = 4;
        }else if(val2 < 1.0){
            val2 = 3;
        }else if(val2 < 2.0){
            val2 = 2;
        }else{
            val2 = 1;
        }
        entries2.add(new RadarEntry(val2));
        // population
        val2 = (float) Float.valueOf(dataFromMain[5]) / menseki;
        if(val2 > 1000){
            val2 = 5;
        }else if (val2 > 500){
            val2 = 4;
        }else  if(val2 > 100){
            val2 = 3;
        }else if(val2 > 50){
            val2 = 2;
        }else{
            val2 = 1;
        }
        entries2.add(new RadarEntry(val2));


        RadarDataSet set1 = new RadarDataSet(entries1, "average");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, dataFromMain[0]);
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        createDataView();
        setRadarChart();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.scrolling_action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
