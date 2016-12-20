package com.example.mbenben.movie.Movie;

import java.io.Serializable;

/**
 * Created by alone on 2016/11/11.
 */
public class PublishCritic implements Serializable {
    private Integer id;//id,唯一标识(不需要写，也不能有)
    private String critic;//影评
    private int uid;//对应用户的id，不用写
    private int good;//被点赞次数（可以不写，系统默认为0）
    private String title;//影评题目
    private int isPrivate;    //是否私有。1-是，0-否
    private String picture;//图片地址（可以为空）？？？？？
    private String time;//发表时间，实际上是到达数据库的时间
    private int height;
    private String name;
    private String headpicture;//用户的头像
    public String getHeadpicture() {
        return headpicture;
    }

    public void setHeadpicture(String headpicture) {
        this.headpicture = headpicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String phone;
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCritic() {
        return critic;
    }

    public void setCritic(String critic) {
        this.critic = critic;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
