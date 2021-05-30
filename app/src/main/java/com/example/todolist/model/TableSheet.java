package com.example.todolist.model;

import java.util.ArrayList;

public class TableSheet {
    private int day;
    private ArrayList<String> classes;

    public TableSheet() {
    }

    public TableSheet(int day, ArrayList<String> classes) {
        this.day = day;
        this.classes = classes;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }
}
