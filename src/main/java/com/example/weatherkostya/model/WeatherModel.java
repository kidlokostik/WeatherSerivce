package com.example.weatherkostya.model;

import lombok.Data;

@Data
public class WeatherModel {
    private double temperature;
    private double humidity;
    private String temp;
    private String hum;

    private String city;
    private String source;
    public WeatherModel(double temp, double hum) {
        this.temperature = temp;
        this.humidity = hum;
    }
    public WeatherModel(){
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}