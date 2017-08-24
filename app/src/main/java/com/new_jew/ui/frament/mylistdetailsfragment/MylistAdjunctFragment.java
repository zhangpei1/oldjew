package com.new_jew.ui.frament.mylistdetailsfragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.MylistDetalisBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.activity.Pdf_Activity;
import com.new_jew.ui.activity.PictureDetails;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/23.
 */
public class MylistAdjunctFragment extends BaseFrament implements View.OnClickListener {
    private ImageView attachment_debtor_id_card, attachment_debtor_account,
            attachment_vehicle_registration_certificate,
            attachment_vehicle_driving_license, attachment_loan_contract,
            attachment_investigation_report, attachment_authentic_act,
            attachment_other,w_image;
    private MylistDetalisBean detailsBean;
    private String str, str1, str2, str3, str4, str5, str6, str7,str8;

    @Override
    protected void initLayout() {
        attachment_debtor_id_card = (ImageView) view.findViewById(R.id.attachment_debtor_id_card);
        attachment_debtor_account = (ImageView) view.findViewById(R.id.attachment_debtor_account);
        attachment_vehicle_registration_certificate = (ImageView) view.findViewById(R.id.attachment_vehicle_registration_certificate);
        attachment_vehicle_driving_license = (ImageView) view.findViewById(R.id.attachment_vehicle_driving_license);
        attachment_loan_contract = (ImageView) view.findViewById(R.id.attachment_loan_contract);
        attachment_investigation_report = (ImageView) view.findViewById(R.id.attachment_investigation_report);
        attachment_authentic_act = (ImageView) view.findViewById(R.id.attachment_authentic_act);
        attachment_other = (ImageView) view.findViewById(R.id.attachment_other);
        w_image= (ImageView) view.findViewById(R.id.w_image);


    }

    @Override
    protected void initValue() {
        getdebtdetail(Constants.id);


    }

    @Override
    protected void initListener() {
        attachment_debtor_id_card.setOnClickListener(this);
        attachment_debtor_account.setOnClickListener(this);
        attachment_vehicle_registration_certificate.setOnClickListener(this);
        attachment_vehicle_driving_license.setOnClickListener(this);
        attachment_loan_contract.setOnClickListener(this);
        attachment_investigation_report.setOnClickListener(this);
        attachment_authentic_act.setOnClickListener(this);
        attachment_other.setOnClickListener(this);
        w_image.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_mylistadjunct;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_debtor_id_card:
                if (str != "null") {
                    if (str.substring(str.length() - 3, str.length()).equals("pdf")) {
                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }

                break;
            case R.id.attachment_debtor_account:
                if (str1 != "null") {
                    if (str1.substring(str1.length() - 3, str1.length()).equals("pdf")) {
                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str1);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str1);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }

                break;
            case R.id.attachment_vehicle_registration_certificate:
                if (str2 != "null") {
                    if (str2.substring(str2.length() - 3, str2.length()).equals("pdf")) {
                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str2);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str2);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.attachment_vehicle_driving_license:
                if (str3 != "null") {

                    if (str3.substring(str3.length() - 3, str3.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str3);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str3);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.attachment_loan_contract:
                if (str4 != "null") {
                    if (str4.substring(str4.length() - 3, str4.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str4);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str4);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.attachment_investigation_report:
                if (str5 != "null") {
                    if (str5.substring(str5.length() - 3, str5.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str5);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str5);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.attachment_authentic_act:
                if (str6 != "null") {
                    if (str6.substring(str6.length() - 3, str6.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str6);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str6);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.attachment_other:
                if (str7 != "null") {
                    if (str7.substring(str7.length() - 3, str7.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str7);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str7);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.w_image:
                if (str8 != "null") {
                    if (str8.substring(str8.length() - 3, str8.length()).equals("pdf")) {

                        Intent intent = new Intent(getActivity(), Pdf_Activity.class);
                        intent.putExtra("path", str8);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getActivity(), PictureDetails.class);
                        intent.putExtra("path", str8);
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }

                }
                break;

        }

    }

    void getdebtdetail(String id) {
        dialog.show();
        String url = null;
        if (Constants.type.equals("3")) {
            url = Api.my_car_orders.my_car_orders;

        } else if (Constants.type.equals("4")) {
            url = Api.staff_car_orders.staff_car_orders;
        } else {
            Toast.makeText(getActivity(), "没有相对应的url", Toast.LENGTH_SHORT).show();
        }


        RequestParams params = new RequestParams(url + id + "/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                try {
                    dialog.dismiss();
                    Log.e("附件", result.toString());
                    JSONObject json = null;

                    json = new JSONObject(result.toString());

                    str = json.getString("attachment_debtor_id_card").toString();
//                    str1 = json.getString("attachment_debtor_id_card").toString();
//                    str2 = json.getString("attachment_debtor_id_card").toString();
                    str1 = json.getString("attachment_debtor_account").toString();
                    str2 = json.getString("attachment_vehicle_registration_certificate").toString();
                    str3 = json.getString("attachment_vehicle_driving_license").toString();
                    str4 = json.getString("attachment_loan_contract").toString();
                    str5 = json.getString("attachment_investigation_report").toString();
                    str6 = json.getString("attachment_authentic_act").toString();
                    str7 = json.getString("attachment_other").toString();
                    JSONObject power=new JSONObject(json.getString("power_of_attorney"));
                    str8=power.getString("power_of_attorney").toString();
                    if (str != "null") {
                        if (str.substring(str.length() - 3, str.length()).equals("pdf")) {
                            attachment_debtor_id_card.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {
                            Load_image.Setimagem(str, attachment_debtor_id_card);
                        }
                    } else {
                        attachment_debtor_id_card.setBackgroundResource(R.drawable.no_file_icon);
                    }

                    if (str1 != "null") {
                        if (str1.substring(str1.length() - 3, str1.length()).equals("pdf")) {
                            attachment_debtor_account.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str1, attachment_debtor_account);
                        }
                    } else {
                        attachment_debtor_account.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str2 != "null") {
                        if (str2.substring(str2.length() - 3, str2.length()).equals("pdf")) {
                            attachment_vehicle_registration_certificate.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str2, attachment_vehicle_registration_certificate);
                        }
                    } else {
                        attachment_vehicle_registration_certificate.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str3 != "null") {
                        if (str3.substring(str3.length() - 3, str3.length()).equals("pdf")) {
                            attachment_vehicle_driving_license.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str3, attachment_vehicle_driving_license);
                        }
                    } else {
                        attachment_vehicle_driving_license.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str4 != "null") {
                        if (str4.substring(str4.length() - 3, str4.length()).equals("pdf")) {
                            attachment_loan_contract.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str4, attachment_loan_contract);
                        }
                    } else {
                        attachment_loan_contract.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str5 != "null") {
                        if (str5.substring(str5.length() - 3, str5.length()).equals("pdf")) {
                            attachment_investigation_report.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str5, attachment_investigation_report);
                        }
                    } else {
                        attachment_investigation_report.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str6 != "null") {
                        if (str.substring(str6.length() - 3, str6.length()).equals("pdf")) {
                            attachment_authentic_act.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str6, attachment_authentic_act);
                        }
                    } else {
                        attachment_authentic_act.setBackgroundResource(R.drawable.no_file_icon);
                    }


                    if (str7 != "null") {
                        if (str7.substring(str7.length() - 3, str7.length()).equals("pdf")) {
                            attachment_other.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str7, attachment_other);
                        }
                    } else {
                        attachment_other.setBackgroundResource(R.drawable.no_file_icon);
                    }

                    if (str8 != "null") {
                        if (str8.substring(str8.length() - 3, str8.length()).equals("pdf")) {
                            w_image.setBackgroundResource(R.drawable.pdf_file_icon);
                        } else {

                            Load_image.Setimagem(str8, w_image);
                        }
                    } else {
                        w_image.setBackgroundResource(R.drawable.no_file_icon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                dialog.dismiss();

            }
        });
    }


}
