package com.example.robert.drawingrobot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class autoMode extends Activity {
    CanvasView  cns ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_auto_mode);
        setContentView(new CanvasView(this));
    }

    class CanvasView extends View {
        private float x,y;

        public CanvasView(Context context){
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            Paint pedzel = new Paint();
            pedzel.setARGB(255,255,255,255);
            canvas.drawRect(x-50, y-50, x+50, y+50, pedzel);
            canvas.drawLine(50, 60, 20, 60, pedzel);
        }

        public void getXY(float tx, float ty) {
            x=tx;
            y=ty;
        }

        public boolean onTouchEvent(MotionEvent event) {
            Log.d("dotyk", event.getX() + " " + event.getY());

            cns.getXY(event.getX(),event.getY());
            return true;
        }

    }


}
