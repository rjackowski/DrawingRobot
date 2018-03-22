package com.example.robert.drawingrobot.Data;

/**
 * Created by Rob on 21.12.2017.
 */

public class Location { // class to keep location points of trajectory and information about marker
    private float x;
    private float y;
    private  int marker;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;

    }

    public Location() {

    }
    public Location(float x, float y, int marker) {
        this(x,y);
        this.marker = marker;
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }
}
