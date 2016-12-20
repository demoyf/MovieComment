package com.example.mbenben.movie.Bean;

import java.io.Serializable;

/**
 * Created by alone on 2016/11/23.
 */
public class CommentCriticBean implements Serializable {
    private Integer id;//id,唯一标识（不能写）
    private String critic;//发表的影评
    private int uid;//外键，用户基本信息的id
    private int pid;//发表的影评的id，外键
    private String time;//发表时间（不用写）
    private int good;//该评论被点赞的次数（默认0，不用写）
    private String name;
    private String phone;

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


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
