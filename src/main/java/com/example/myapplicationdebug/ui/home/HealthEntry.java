package com.example.myapplicationdebug.ui.home;

public class HealthEntry {
    private String date; // 日期
    private int steps;   // 步数
    private int heartRate; // 心率
    private float sleepQuality; // 睡眠质量

    public HealthEntry(String date, int steps, int heartRate, float sleepQuality) {
        this.date = date;
        this.steps = steps;
        this.heartRate = heartRate;
        this.sleepQuality = sleepQuality;
    }

    // Getter 和 Setter 方法
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public float getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(float sleepQuality) {
        this.sleepQuality = sleepQuality;
    }
}


