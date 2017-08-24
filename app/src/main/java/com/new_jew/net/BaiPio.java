package com.new_jew.net;


import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;


/**
 * Created by zhangpei on 2016/9/1.
 */
public class BaiPio {

    private static GeoCoder poiSearch;
    private static String Adress="";

    // 定义地图引擎管理类
    private BMapManager mapManager;
    // 定义搜索服务类
    public static  void getadress(LatLng latLng){
        poiSearch = GeoCoder.newInstance();

        poiSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                Log.e("地址code", geoCodeResult.getAddress());

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                Log.e("地址", reverseGeoCodeResult.getAddress());
                Adress = reverseGeoCodeResult.getAddress();


            }
        });
        poiSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));

    }


    public  static String reAdress(){


        return Adress;

    }

    public static void stop_poiSearch(){

        poiSearch.destroy();





    }

}
