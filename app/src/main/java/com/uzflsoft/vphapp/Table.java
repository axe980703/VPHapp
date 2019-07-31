package com.uzflsoft.vphapp;

import java.util.HashMap;
import java.util.List;

public class Table {

    private String name;
    private HashMap<String, List<String>> elements;
    private List<Boolean> boxes;
    private List<Double> points;
    private String degree;
    private double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Boolean> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Boolean> boxes) {
        this.boxes = boxes;
    }

    public HashMap<String, List<String>> getElements() {
        return elements;
    }

    public void setElements(HashMap<String, List<String>> elements) {
        this.elements = elements;
    }

    public List<Double> getPoints() {
        return points;
    }

    public void setPoints(List<Double> points) {
        this.points = points;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void math() {
        double sum = 0, x;
        for(int i = 0; i < boxes.size(); i++) {
            x = boxes.get(i) ? 1 : 0;
            sum += x * points.get(i);
        }
        this.value = sum;
    }

    public void setBox(int pos, boolean x) {
        this.boxes.set(pos, x);
    }

    public boolean getBox(int pos) {
        return this.boxes.get(pos);
    }


}
