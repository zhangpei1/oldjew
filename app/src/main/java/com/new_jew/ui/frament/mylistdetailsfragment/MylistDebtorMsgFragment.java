
package com.new_jew.ui.frament.mylistdetailsfragment;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.MylistDetalisBean;
import com.new_jew.global.Constants;
import com.new_jew.toolkit.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangpei on 2016/11/23.
 */
public class MylistDebtorMsgFragment extends BaseFrament {
    private TextView debtor_name, debtor_gender, debtor_marital_status, debtor_id_card, debtor_cantact_phone1,
            debtor_cantact_phone2, debtor_cantact_phone3, debtor_work_company_name, debtor_work_company_address,
            debtor_home_address1, debtor_home_address2, debtor_home_address3, debtor_home_contact_1_phone,
            debtor_home_contact_2_name, debtor_home_contact_3_name, debtor_emergency_contact_1_name, debtor_emergency_contact_2_name;

    @Override
    protected void initLayout() {
        debtor_name = (TextView) view.findViewById(R.id.debtor_name);
        debtor_gender = (TextView) view.findViewById(R.id.debtor_gender);
        debtor_marital_status = (TextView) view.findViewById(R.id.debtor_marital_status);
        debtor_id_card = (TextView) view.findViewById(R.id.debtor_id_card);
        debtor_cantact_phone1 = (TextView) view.findViewById(R.id.debtor_cantact_phone1);

        debtor_cantact_phone2 = (TextView) view.findViewById(R.id.debtor_cantact_phone2);
        debtor_cantact_phone3 = (TextView) view.findViewById(R.id.debtor_cantact_phone3);
        debtor_work_company_name = (TextView) view.findViewById(R.id.debtor_work_company_name);
        debtor_work_company_address = (TextView) view.findViewById(R.id.debtor_work_company_address);

        debtor_home_address1 = (TextView) view.findViewById(R.id.debtor_home_address1);
        debtor_home_address2 = (TextView) view.findViewById(R.id.debtor_home_address2);
        debtor_home_address3 = (TextView) view.findViewById(R.id.debtor_home_address3);
        debtor_home_contact_1_phone = (TextView) view.findViewById(R.id.debtor_home_contact_1_phone);

        debtor_home_contact_2_name = (TextView) view.findViewById(R.id.debtor_home_contact_2_name);
        debtor_home_contact_3_name = (TextView) view.findViewById(R.id.debtor_home_contact_3_name);
        debtor_emergency_contact_1_name = (TextView) view.findViewById(R.id.debtor_emergency_contact_1_name);
        debtor_emergency_contact_2_name = (TextView) view.findViewById(R.id.debtor_emergency_contact_2_name);


    }

    @Override
    protected void initValue() {
        getdebtdetail(Constants.id);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_mylistdebtormsg;
    }

    void getdebtdetail(String id) {
        String url = null;
        if (Constants.type.equals("3")){


        }else if (Constants.type.equals("4")){

        }else {
            Toast.makeText(getActivity(),"没有相对应的url",Toast.LENGTH_SHORT).show();
        }

        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(UserUtil.getUserToken(getActivity(),"details").toString());
            String str = jsonObject.toString();;
            Gson gson = new Gson();
            MylistDetalisBean detailsBean = gson.fromJson(str, MylistDetalisBean.class);
            debtor_name.setText(detailsBean.getDebtor_name());
            debtor_gender.setText(detailsBean.getDebtor_gender());
            debtor_marital_status.setText(detailsBean.getDebtor_marital_status());
            debtor_id_card.setText(detailsBean.getDebtor_id_card());
            debtor_cantact_phone1.setText(detailsBean.getDebtor_cantact_phone1());
            debtor_cantact_phone2.setText(detailsBean.getDebtor_cantact_phone2());
            debtor_cantact_phone3.setText(detailsBean.getDebtor_cantact_phone3());
            debtor_work_company_name.setText(detailsBean.getDebtor_work_company_name());
            debtor_work_company_address.setText(detailsBean.getDebtor_work_company_address());
            debtor_home_address1.setText(detailsBean.getDebtor_home_address1());
            debtor_home_address2.setText(detailsBean.getDebtor_home_address2());
            debtor_home_address3.setText(detailsBean.getDebtor_home_address3());
//            String str_linkman=detailsBean.getDebtor_home_contact_1_name().toString();
//            String str_linkman_relation=detailsBean.getDebtor_home_contact_1_relation().toString();
//            String str_linkman_phone=detailsBean.getDebtor_home_contact_1_phone().toString();
//            if (str_linkman.length()==0){
//                str_linkman="无";
//            }
//            if (str_linkman_relation.length()==0){
//                str_linkman_relation="无";
//            }
//            if (str_linkman_phone.length()==0){
//                str_linkman_phone="无";
//            }

            debtor_home_contact_1_phone.setText(detailsBean.getDebtor_home_contact_1_name() + "    " + detailsBean.getDebtor_home_contact_1_relation()
                    + "     " + detailsBean.getDebtor_home_contact_1_phone());
            debtor_home_contact_2_name.setText(detailsBean.getDebtor_home_contact_2_name() + "    " + detailsBean.getDebtor_home_contact_2_relation()
                    + "     " + detailsBean.getDebtor_home_contact_2_phone());

            debtor_home_contact_3_name.setText(detailsBean.getDebtor_home_contact_3_name() + "    " + detailsBean.getDebtor_home_contact_3_relation()
                    + "     " + detailsBean.getDebtor_home_contact_3_phone());

            debtor_emergency_contact_1_name.setText(detailsBean.getDebtor_emergency_contact_1_name() + "     " + detailsBean.getDebtor_emergency_contact_1_phone());
            debtor_emergency_contact_2_name.setText(detailsBean.getDebtor_emergency_contact_2_name() + "     " + detailsBean.getDebtor_emergency_contact_2_phone());
            Log.e("detailsBean", detailsBean.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
