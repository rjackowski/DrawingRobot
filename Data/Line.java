package com.example.robert.drawingrobot.Data;

/**
 * Created by Rob on 28.12.2017.
 */

public class Line {
    private Location start;
    private Location end;

    private double dx;
    private double dy;
    private double sxey;
    private double exsy;
    private  double length;

    public Line(Location start, Location end) {
        this.start = start;
        this.end = end;
        dx = start.getX() - end.getX();
        dy = start.getY() - end.getY();
        sxey = start.getX() * end.getY();
        exsy = end.getX() * start.getY();
        length = Math.sqrt(dx * dx + dy * dy);
    }

    public float distance(Location p) {
        return (float)(Math.abs(dy * p.getX() - dx * p.getY() + sxey - exsy) / length);
    }
}
