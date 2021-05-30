package com.example.todolist.model;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String name;
    private String time;
    private String category;
    private int status;

    public Task(int id, String name, String time, String category, int status) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.category = category;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Task() {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
