package com.example.yoshizumiapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private Marker mMarker;
    private SQLiteManager DBManager;
    private TownNameListAdapter myAdapter;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String preName = "MAIN_SETTING";
    private final String dataBoolTag = "dataBPT";
    private boolean isFirstTime;


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListView listView = (ListView)adapterView;
            TownData td = (TownData)listView.getItemAtPosition(i);

            String[] data = {
                    td.getPrefecture() + td.getCityName(),
                    String.valueOf(td.getSchoolCount()),
                    String.valueOf(td.getStationCount()),
                    String.valueOf(td.getCrimePer()),
                    String.valueOf(td.getPopulation()),
            };

            LatLng sales = new LatLng(td.getX(), td.getY());

            mMarker.remove();
            mMarker = mMap.addMarker(new MarkerOptions().position(sales).title("Marker " + td.getPrefecture() + td.getCityName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sales, 8));

            Intent intent = new Intent(getApplication(), ScrollingActivity.class);
            intent.putExtra("MainData", data);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.DBManager = new SQLiteManager(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//マップ表示
        final MapFragment mapFragment = MapFragment.newInstance();

        // MapViewをMapFragmentに変更する
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView2, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng sales = new LatLng(35.6049484, 139.3587469);
                mMarker = mMap.addMarker(new MarkerOptions().position(sales).title("Marker in Salesio-SP"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sales, 8));
            }
        });

//ここまでマップ表示
//ナビゲーション
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//ここまでナビゲーション

        // 初回のみjsonファイルからデーターベース作成を行う
        sharedPreferences = getSharedPreferences(preName, MODE_PRIVATE);
        isFirstTime = sharedPreferences.getBoolean(dataBoolTag, true);
        editor = sharedPreferences.edit();
        if(isFirstTime) {
            try {
                JsonHelper jsonHelper = new JsonHelper(this.DBManager);
                jsonHelper.readJson(this.getAssets().open("cityData.json"));
            }catch (Exception e){
                e.printStackTrace();
            }
            editor.putBoolean(dataBoolTag, false).apply();
        }

        //リスト
        ListView myListView = (ListView)findViewById(R.id.list_view);
        myAdapter = new TownNameListAdapter(this, R.layout.list_layout, DBManager.queryByHighOrLowTop30());
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(onItemClickListener);
        //ここまでリスト
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /* 検索機能予定地 */
        if (id == R.id.action_settings) {
            /*
                townDatas = DBManager.queryByCityName30(cityName);
                myAdapter.removeListAllData();
                myAdapter.clear();
                for(TownData td: townDatas){
                    myAdapter.add(td);
                }
                myAdapter.notifyDataSetChanged();
            */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        ArrayList<TownData> townDatas;

        //ここからボタンを押したときの動作
        switch (id){
            case R.id.hospital:
                item.setChecked(!item.isChecked());
                Toast.makeText(this,"action_radio1",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.station:
                item.setChecked(!item.isChecked());
                Toast.makeText(this,"駅の多い町",Toast.LENGTH_SHORT).show();
                townDatas =  DBManager.queryByHighOrLowTop30("stationCount", "high");
                myAdapter.removeListAllData();
                myAdapter.clear();
                for(TownData td: townDatas){
                    myAdapter.add(td);
                }
                myAdapter.notifyDataSetChanged();
                return true;
            case R.id.jinkou:
                item.setChecked(!item.isChecked());
                Toast.makeText(this,"人口の多い町",Toast.LENGTH_SHORT).show();
                townDatas = DBManager.queryByHighOrLowTop30("population", "high");
                myAdapter.removeListAllData();
                myAdapter.clear();
                for(TownData td: townDatas){
                    myAdapter.add(td);
                }
                myAdapter.notifyDataSetChanged();
                return true;
            case R.id.school:
                item.setChecked(!item.isChecked());
                Toast.makeText(this,"学校の多い町",Toast.LENGTH_SHORT).show();
                townDatas = DBManager.queryByHighOrLowTop30("schoolCount", "high");
                myAdapter.removeListAllData();
                myAdapter.clear();
                for(TownData td: townDatas){
                    myAdapter.add(td);
                }
                myAdapter.notifyDataSetChanged();
                return true;
            case R.id.crime:
                item.setChecked(!item.isChecked());
                Toast.makeText(this,"犯罪発生率の低い町",Toast.LENGTH_SHORT).show();
                townDatas = DBManager.queryByHighOrLowTop30("crimePer", "low");
                myAdapter.removeListAllData();
                myAdapter.clear();
                for(TownData td: townDatas){
                    myAdapter.add(td);
                }
                myAdapter.notifyDataSetChanged();
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
