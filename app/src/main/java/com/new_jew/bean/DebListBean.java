package com.new_jew.bean;

/**
 * Created by zhangpei on 2016/11/18.
 */
public class DebListBean {

    private String get_level,loan_type,demands_of_collection,collection_commission,loan_start_overdue_time,collecting_days,vehicle_possible_address,id,rating;
    private boolean vehicle_has_gps,vehicle_has_illegal;

    public String getGet_level() {
        return get_level;
    }

    public void setGet_level(String get_level) {
        this.get_level = get_level;
    }

    public String getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(String loan_type) {
        this.loan_type = loan_type;
    }

    public String getDemands_of_collection() {
        return demands_of_collection;
    }

    public void setDemands_of_collection(String demands_of_collection) {
        this.demands_of_collection = demands_of_collection;
    }

    public String getCollection_commission() {
        return collection_commission;
    }

    public void setCollection_commission(String collection_commission) {
        this.collection_commission = collection_commission;
    }

    public String getLoan_start_overdue_time() {
        return loan_start_overdue_time;
    }

    public void setLoan_start_overdue_time(String loan_start_overdue_time) {
        this.loan_start_overdue_time = loan_start_overdue_time;
    }

    public String getCollecting_days() {
        return collecting_days;
    }

    public void setCollecting_days(String collecting_days) {
        this.collecting_days = collecting_days;
    }

    public String getVehicle_possible_address() {
        return vehicle_possible_address;
    }

    public void setVehicle_possible_address(String vehicle_possible_address) {
        this.vehicle_possible_address = vehicle_possible_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isVehicle_has_gps() {
        return vehicle_has_gps;
    }

    public void setVehicle_has_gps(boolean vehicle_has_gps) {
        this.vehicle_has_gps = vehicle_has_gps;
    }

    public boolean isVehicle_has_illegal() {
        return vehicle_has_illegal;
    }

    public void setVehicle_has_illegal(boolean vehicle_has_illegal) {
        this.vehicle_has_illegal = vehicle_has_illegal;
    }

    public DebListBean(String get_level, String loan_type, String demands_of_collection, String collection_commission, String loan_start_overdue_time, String collecting_days, String vehicle_possible_address, String id, String rating, boolean vehicle_has_gps, boolean vehicle_has_illegal) {
        this.get_level = get_level;
        this.loan_type = loan_type;
        this.demands_of_collection = demands_of_collection;
        this.collection_commission = collection_commission;
        this.loan_start_overdue_time = loan_start_overdue_time;
        this.collecting_days = collecting_days;
        this.vehicle_possible_address = vehicle_possible_address;
        this.id = id;
        this.rating = rating;
        this.vehicle_has_gps = vehicle_has_gps;
        this.vehicle_has_illegal = vehicle_has_illegal;
    }
}
