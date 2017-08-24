package com.new_jew.ui.collectoractivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.Accessory_Adpter;
import com.new_jew.bean.CollectionRecordBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.ui.activity.PictureDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by zhangpei on 2016/9/12.
 */
public class Accessory_Activity extends BaseActivity {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    private GridView photo_grid;
    private JCVideoPlayerStandard play_video;
    private int id = 0;
    private int position = 0;
    private ArrayList<Accessory_Adpter> mlist;
    private AccessoryAdpter adpter;
    private ArrayList<String> flist = new ArrayList<>();

    @Override
    protected void initLayout() {
        x.view().inject(this);
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        photo_grid = (GridView) this.findViewById(R.id.photo_grid);
        play_video = (JCVideoPlayerStandard) this.findViewById(R.id.custom_videoplayer_standard);

        mlist = new ArrayList<>();

    }

    @Override
    protected void initValue() {
        if (Constants.type.equals("3")){
            get_process(Constants.id);

        }else {

            getdetails();
        }
        adpter = new AccessoryAdpter(mlist, getApplicationContext());
        photo_grid.setAdapter(adpter);
        title_text.setText("催记详情");

    }

    @Override
    protected void initListener() {

        photo_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Accessory_Activity.this, PictureDetails.class);
                intent.putExtra("path", flist.get(position));
                intent.putExtra("id", 1);
                Log.e("aaaaaaaaaaaa", flist.get(position));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_accessory;
    }

    void getdetails() {
        dialog.show();
        RequestParams params = null;
            params = new RequestParams(Api.staff_car_orders.staff_car_orders + Constants.id + "/collection_records/");

        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();
                Log.e("result", result.toString());
                try {
                    JSONArray jsonArray = new JSONArray(result.toString());
                    if (jsonArray.length() == 0) {

                        return;
                    }
                    JSONObject mjs1 = new JSONObject(jsonArray.get(id).toString());

                    if (!mjs1.getString("img1").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img1"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img1")));
                    }
                    if (!mjs1.getString("img2").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img2"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img2")));
                    }
                    if (!mjs1.getString("img3").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img3"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img3")));
                    }

                    adpter.notifyDataSetChanged();
                    if (!mjs1.getString("video").equals("null")) {
                        play_video.setVisibility(View.VISIBLE);
                        Uri uri = Uri.parse(Api.pic + mjs1.getString("video").toString());
                        play_video.setUp(Api.pic + mjs1.getString("video")
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, " ");
                    } else {
                        play_video.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    class AccessoryAdpter extends BaseAdapter {
        private ArrayList<Accessory_Adpter> mlist;
        private Context context;
        private LayoutInflater inflate;

        public AccessoryAdpter(ArrayList<Accessory_Adpter> mlist, Context context) {
            this.mlist = mlist;
            this.context = context;
            this.inflate = getLayoutInflater().from(context);
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHold hold = null;
            if (convertView == null) {
                hold = new ViewHold();
                convertView = inflate.inflate(R.layout.layout_add_picture, null);
                hold.image = (ImageView) convertView.findViewById(R.id.add_picture);
                convertView.setTag(hold);

            } else {
                hold = (ViewHold) convertView.getTag();


            }
            Load_image.Setimage(mlist.get(position).getUrl(), hold.image);
            return convertView;
        }

        class ViewHold {


            private ImageView image;

        }
    }

    void get_process(String id_order) {
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + id_order  + "/process/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                   Log.e("result",result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

//                    collection_day.setText(String.valueOf(TimeUtil.getformatdata(jsonObject.getString("collecting_day")))+"天");
//                    surplus_days.setText("剩余"+String.valueOf(jsonObject.getInt("collecting_days")-TimeUtil.getformatdata(jsonObject.getString("collecting_day")))+"天");
                    JSONArray ajson = new JSONArray(jsonObject.getString("collection_records"));

                    JSONObject mjs1 = new JSONObject(ajson.get(id).toString());

                    if (!mjs1.getString("img1").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img1"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img1")));
                    }
                    if (!mjs1.getString("img2").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img2"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img2")));
                    }
                    if (!mjs1.getString("img3").equals("null")) {
                        flist.add(Api.pic + mjs1.getString("img3"));
                        mlist.add(new Accessory_Adpter(Api.pic + mjs1.getString("img3")));
                    }

                    adpter.notifyDataSetChanged();
                    if (!mjs1.getString("video").equals("null")) {
                        play_video.setVisibility(View.VISIBLE);
                        Uri uri = Uri.parse(Api.pic + mjs1.getString("video").toString());
                        play_video.setUp(Api.pic + mjs1.getString("video")
                                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, " ");
                    } else {
                        play_video.setVisibility(View.GONE);
                    }



                    adpter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
