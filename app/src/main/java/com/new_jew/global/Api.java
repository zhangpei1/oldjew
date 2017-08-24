package com.new_jew.global;

/**
 * Created by zhangpei on 2016/11/8.
 */
public class Api {
    //            public static final String pic = "http://192.168.3.3";//本地
//    public static final String domain_name = "http://192.168.3.3/api/";//本地
    public static final String pic = "https://www.jew315.com";//云
    public static final String domain_name = "https://www.jew315.com/api/";//云
    public static final String users = "users";

    //注册
    public interface Register {

        String Register = domain_name + users + "/reg/";


    }

    public interface LogIn {

        String LogIn = domain_name + users + "/token/";

    }

    public interface Verify {
        String Verify = domain_name + "telephone_verify/";


    }

    public interface Me {

        String Me = domain_name + users + "/me/";

    }

    public interface my_profile {

        String my_profile = domain_name + users + "/my_profile/";

    }

    public interface debt {

        String debt = domain_name + "car_debts/";

    }

    public interface my_car_orders {

        String my_car_orders = domain_name + "my_car_orders/";

    }

    public interface collection_company_staffs {

        String collection_company_staffs = domain_name + "collection_company_staffs/";

    }

    public interface messages {

        String messages = domain_name + "messages/";
    }

//催收员

    public interface staff_car_orders {
        String staff_car_orders = domain_name + "staff_car_orders/";

    }

    //修改密码
    public interface password {
        String password = domain_name + users + "password/";

    }

    //申请延期
    public interface car_oders {
        String car_oders = domain_name + "car_orders/";


    }

    //更新
    public interface app_update {

        String app_update = domain_name + "app_update/";
    }
}
