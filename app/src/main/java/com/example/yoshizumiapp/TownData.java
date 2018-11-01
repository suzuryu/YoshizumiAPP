package com.example.yoshizumiapp;

public class TownData
{
    private String  cityName;
    private int     schoolCount;
    private String  prefecture;
    private int     stationCount;
    private Double  crimePer;
    private int     population;
    private Double  x;
    private Double  y;

    TownData(){
        this.cityName = "NO NAME";
        this.schoolCount = 0;
        this.prefecture = "NO PREFECTURE";
        this.stationCount = 0;
        this.crimePer = 0.0;
        this.population = 0;
        this.x = 0.0;
        this.y = 0.0;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getCityName() {
        return cityName;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getPopulation() { return population; }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setSchoolCount(int schoolCount) {
        this.schoolCount = schoolCount;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public int getStationCount() {
        return stationCount;
    }

    public Double getCrimePer() {
        return crimePer;
    }

    public int getSchoolCount() {
        return schoolCount;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public void setCrimePer(Double crimePer) {
        this.crimePer = crimePer;
    }

    public void setStationCount(int stationCount) {
        this.stationCount = stationCount;
    }
}
