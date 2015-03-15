package com.example.jurgen.androidtestworkas;

import android.media.Image;
import android.widget.ImageView;

public class FootballTeam {


    private int imgId;
    private int id;
    private String name;

    public FootballTeam( int imgId ,int id, String name) {

        this.imgId=imgId;
        this.id = id;
        this.name = name;
    }
    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
