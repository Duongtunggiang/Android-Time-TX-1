package com.example.on2txgit;

public class BaoThuc {
    private String name;
    private String date;
    private String time;
    private String loop;
    private String buoi;

    public BaoThuc(String name, String date, String time, String loop, String buoi) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.loop = loop;
        this.buoi = buoi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLoop() {
        return loop;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }

    public String getBuoi() {
        return buoi;
    }

    public void setBuoi(String buoi) {
        this.buoi = buoi;
    }

    @Override
    public String toString() {
        return name+"-"+date+"-"+time+"-"+((loop.equals("Lap lai"))?"Co":"Khong")+"-"+buoi;
    }
}
