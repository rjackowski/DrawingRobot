package com.example.robert.drawingrobot;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Rob on 21.12.2017.
 */

public class DrawingClass {

    Path DrawingClassPath;
    Paint DrawingClassPaint;

    public Path getPath() {
        return DrawingClassPath;
    }
    public void setPath(Path path) {
        this.DrawingClassPath = path;
    }
    public Paint getPaint() {
        return DrawingClassPaint;
    }
    public void setPaint(Paint paint) {
        this.DrawingClassPaint = paint;
    }
}