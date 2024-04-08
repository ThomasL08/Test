package com.example.trailtrekker;

public class HistoryItem {
    private String title;
    private String longitude;
    private String latitude;

    public HistoryItem(String title, String longitude, String latitude) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}


