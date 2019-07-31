package com.uzflsoft.vphapp;

public class DataModel {

    String name;
    String status;
    double points;
    int visibility;
    int position;


    public DataModel(String name, String status, double points) {
        this.name = name;
        this.status = status;
        this.points = points;
        if(points > 0)
            this.visibility = 1;
        else
            this.visibility = 0;

    }

    public int getPosition() { return position; }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public double getPoints() {
        return points;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setPosition(int position) { this.position = position; }

}