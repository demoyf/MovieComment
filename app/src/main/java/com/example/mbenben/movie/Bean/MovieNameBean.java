package com.example.mbenben.movie.Bean;

import java.io.Serializable;

/**
 * Created by alone on 2016/12/7.
 */
public class MovieNameBean implements Serializable {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
