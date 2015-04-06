package com.example.jurgen.androidtestworkas;

import android.media.Image;
import android.widget.ImageView;

public class FootballTeamDaten {


    private int imgId;
    private int id;
    private String name;
    private String path;



    public FootballTeamDaten(int imgId, int id, String name) {
        this.imgId=imgId;
        this.id = id;
        this.name = name;
    }
    public FootballTeamDaten(int idBD, int id, String name,String path) {
        imgId=idBD;
        this.id = id;
        this.name = name;
        this.path = path;
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
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
