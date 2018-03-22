package com.example.robert.drawingrobot.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.robert.drawingrobot.DrawingClass;
import com.example.robert.drawingrobot.calculateTrajectory;

import java.util.ArrayList;

/**
 * Created by Rob on 31.12.2017.
 */

public class SketchSheetView extends View {

    private Paint paint;
    private Path path2;
    private  Bitmap bitmap;
    private Canvas canvas;
    private calculateTrajectory myTrajectory;
    private ArrayList<DrawingClass> DrawingClassArrayList = new ArrayList<DrawingClass>();
    private int counter;


    public SketchSheetView(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap); // plotno
        paint = new Paint(); // pedzel
        path2 = new Path();  // parametry do rysowania
        paint.setDither(true);
        paint.setColor(Color.parseColor("#0000FF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(4);
        myTrajectory = new calculateTrajectory();
        counter = 0;
        this.setBackgroundColor(Color.WHITE); // ustawianie koloru rysowania
    }

    public void resetPath() {
        path2.reset();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { // Obsługa zdarzen zwiazanych z kliknieciem

        DrawingClass pathWithPaint = new DrawingClass();
        canvas.drawPath(path2, paint);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            path2.moveTo(event.getX(), event.getY());
            path2.lineTo(event.getX(), event.getY());
            Log.d("Pozycja", "pozycja x: " + event.getX() + "y: " + event.getY());
            myTrajectory.addElement(event.getX(), event.getY(),1);
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path2.lineTo(event.getX(), event.getY());
            myTrajectory.addElement(event.getX(), event.getY(), 2);
            pathWithPaint.setPath(path2);
            pathWithPaint.setPaint(paint);
            DrawingClassArrayList.add(pathWithPaint);
            counter++;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
             counter=0;
             myTrajectory.addElement(event.getX(), event.getY(),0);
        }
        counter++;
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DrawingClassArrayList.size() > 0) {
            canvas.drawPath(
                    DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPath(),
                    DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPaint());
        }
    }

    public calculateTrajectory getTrajectory() {
        return myTrajectory;
    }
}