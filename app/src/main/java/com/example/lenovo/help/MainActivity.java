package com.example.lenovo.help;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private ImageButton button1;
    private ImageButton button_dail;
    /*private TextView name;
    private TextView pho;*/
    private boolean isFirstLocate=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*注册一个定位监听器，当获取到信息时就会回调到这个定位监听器*/
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.bmapView);
        positionText=(TextView)findViewById(R.id.position_text_view);
        baiduMap=mapView.getMap();
        button1=(ImageButton)findViewById(R.id.help_button);
        button_dail=(ImageButton)findViewById(R.id.dial_button);
        baiduMap.setMyLocationEnabled(true);

        //获取header的控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        /*创建一个list集合以添加没有被授权的服务*/
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        /*将List转换成数组，一次性申请授权*/
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            requestLocation();
        }


        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView=(NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();
        navView.setCheckedItem(R.id.nav_contact);
        navView.setCheckedItem(R.id.layout);
        de.hdodenhof.circleimageview.CircleImageView nav_m=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.nav_icon2);
        nav_m.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,RunActivity.class);
                startActivity(intent);
            }
        });
        button_dail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:110"));
                startActivity(intent);
            }
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.layout:{
                    Intent intent = new Intent(MainActivity.this, Log_In.class);
                    startActivity(intent);
                        break;}
                    default:
                        ;}
                    mDrawerLayout.closeDrawers();
                    return true;

            }
        });
        /*SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        name=(TextView)findViewById(R.id.username);
        String s=pref.getString("name","");
        name.setText(s);*/

    }
    /*public boolean onCreateOptionsMnu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }*/

   private void navigateTo(BDLocation location){
       if(isFirstLocate){
           LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
           MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
           baiduMap.animateMapStatus(update);
           update=MapStatusUpdateFactory.zoomTo(16f);
           baiduMap.animateMapStatus(update);
           isFirstLocate=false;
       }
       MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
       locationBuilder.latitude(location.getLatitude());
       locationBuilder.longitude(location.getLongitude());
       MyLocationData locationData=locationBuilder.build();
       baiduMap.setMyLocationData(locationData);
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            default:
        }
        return true;
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);//每五秒更新一次位置
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    /*在活动呗销毁的时候，停止定位*/
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有的权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
           if(location.getLocType()==BDLocation.TypeGpsLocation||location.getLocType()==BDLocation.TypeNetWorkLocation){
               navigateTo(location);
           }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
