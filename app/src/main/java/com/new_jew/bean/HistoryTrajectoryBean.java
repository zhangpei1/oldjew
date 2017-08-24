package com.new_jew.bean;

/**
 * Created by zhangpei on 2016/12/5.
 */
public class HistoryTrajectoryBean {
    private String position;
    private String detail;
    private String img1;
    private String img2;
    private String img3;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public HistoryTrajectoryBean(String position, String detail, String img1, String img2, String img3, String created, String video) {
        this.position = position;
        this.detail = detail;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.created = created;
        this.video = video;
    }

    private String created;
    private String video;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
