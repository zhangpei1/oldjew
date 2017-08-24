package com.new_jew.net;

import android.content.Context;
import android.util.Log;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
/**
 * Created by zhangpei on 2016/8/10.
 */
public class Location_service {
    public static LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    public static String adress = "";
    private Context context;
    public static double Latitude = 0;
    public static double Longitude = 0;
    public static String location = "";

    public Location_service(Context context) {

        this.context = context;
        mLocationClient = new LocationClient(context);

        initLocation();//声明LocationClient类



    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                Latitude = bdLocation.getLatitude();
                Longitude = bdLocation.getLongitude();
                adress = bdLocation.getAddrStr().toString()+bdLocation.getLocationDescribe() ;
                location = String.valueOf(Longitude) + "," + String.valueOf(Latitude);
                Log.e("location",bdLocation.getLocationDescribe());
//                LatLng latLng=new LatLng(Latitude,Longitude);
//                BaiPio.getadress(latLng);

            }
        });
    }


    public void StartLocation() {

        mLocationClient.start();


    }

    public void StopLocation() {


        mLocationClient.stop();


    }

    public static String getaddress() {
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {


                adress = bdLocation.getAddrStr().toString()+bdLocation.getLocationDescribe();
                Address address1= bdLocation.getAddress();
                Log.e("adreeeee",adress);
            }
        });    //注册监听函数


        return adress;


    }


    public static String getdoubleadress() {

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                Latitude = bdLocation.getLatitude();
                Longitude = bdLocation.getLongitude();


            }
        });

        //注册监听函数
        Log.e("adreeeee", String.valueOf(Latitude) + "," + String.valueOf(Longitude));
        return String.valueOf(Longitude) + "," + String.valueOf(Latitude);

    }

}
