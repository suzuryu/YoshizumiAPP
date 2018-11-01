package com.example.yoshizumiapp;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(26)
public class ScrollingActivity extends AppCompatActivity {

    // ひとつひとつのテーブルデータをレイアウトに追加していく
    private void addData2View(String[][] datas, int index){
        String[] data = datas[index];
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);

        int margin_size = 10;
        TextView data_text = new TextView(this);
        LinearLayout vert_linear = new LinearLayout(this);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        layoutParams.setMargins(0, 10, 0, 10);

        vert_linear.setOrientation(LinearLayout.HORIZONTAL);
        vert_linear.setLayoutParams(layoutParams);
        vert_linear.setGravity(Gravity.CENTER);
        vert_linear.setBackgroundColor(0xff0000);

        //data_text.setElevation(10);

        data_text.setText(data[0] + " : " + data[1]);
        data_text.setTextSize(32);
        data_text.setGravity(Gravity.CENTER);
        data_text.setLayoutParams(layoutParams);

        vert_linear.addView(data_text);
        linearLayout.addView(vert_linear);
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

        datas[0][1] = dataFromMain[1] + "つ";
        datas[1][1] = dataFromMain[2] + "つ";
        datas[2][1] = dataFromMain[3] + "%";
        datas[3][1] = dataFromMain[4] + "人";
        // add data to view list
        for(int i=0;i<datas.length;i++){
            addData2View(datas,i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        createDataView();

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
