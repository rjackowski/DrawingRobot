package com.example.robert.drawingrobot;

import android.util.Log;

import com.example.robert.drawingrobot.Data.Line;
import com.example.robert.drawingrobot.Data.Location;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Rob on 21.12.2017.
 */
// Class for keeping trajectory and changing into commendList
public class calculateTrajectory {

    private ArrayList<Location> locationList = new ArrayList<>();
    public ArrayList<String> commandList = new ArrayList<>();
    public void addElement(float x, float y,int marker) {
        Location temp= new Location(x,y,marker);
        locationList.add(temp);
    }
    public void clear() {
        locationList.clear();
    }

    public void calculate()
    {
        ArrayList<Location> locationListAfterOptimization;

//        String locationListString = "";
//        locationListString = "";
//        String locationListAfterOptimizationString = "";
//        for (Location s : locationList) {
//            locationListString += s.getX() + "|" + s.getY() + ";";
//        }
//        for (Location s : locationListAfterOptimization) {
//            locationListAfterOptimizationString += s.getX() + "|" + s.getY() + ";";
//        }
        locationListAfterOptimization = DouglasPeucker ( locationList , 0.00001f); // Optimization number of points using DouglasPeucker algorithm
        for(int i=0;i<locationListAfterOptimization.size();i++) { // changing locationList into commendlist
            float temp_distance = 0;
            float temp_angle = 0;
            float temp_angle2 = 0;
            float angle = 0;
            if (locationListAfterOptimization.get(i).getMarker() == 0)
                commandList.add("U");
            if (locationListAfterOptimization.get(i).getMarker() == 1)
                commandList.add("D");
            if (i == 1) {
                temp_distance = (float) Math.hypot(locationListAfterOptimization.get(i).getX() - locationListAfterOptimization.get(i - 1).getX(), locationListAfterOptimization.get(i).getY() - locationListAfterOptimization.get(i - 1).getY());
                if(temp_distance > 0.1)
                    commandList.add("F" +  round((temp_distance / 10.0f),2));
            } else if (i >= 2) {
                temp_angle = (float) Math.atan2(locationListAfterOptimization.get(i - 1).getY() - locationListAfterOptimization.get(i).getY(), locationListAfterOptimization.get(i).getX() - locationListAfterOptimization.get(i - 1).getX()) * 180.0f / 3.1415926535f;
                temp_angle2 = (float) Math.atan2(locationListAfterOptimization.get(i - 2).getY() - locationListAfterOptimization.get(i - 1).getY(), locationListAfterOptimization.get(i - 1).getX() - locationListAfterOptimization.get(i - 2).getX()) * 180.0f / 3.1415926535f;
                angle = temp_angle2 - temp_angle;
                if (angle > 180)
                    angle = -(360 - angle);
                if (angle < -180)
                    angle = 360 + angle;
                if (angle < -0.1) {
                    commandList.add("L" +  round(-angle,0));
                }
                if (angle > 0.1) {
                    commandList.add("R" +  round(angle,0));
                    Log.d("Pozycja", "R" + (int) (angle));
                }
                temp_distance = (float) Math.hypot(locationListAfterOptimization.get(i).getX() - locationListAfterOptimization.get(i - 1).getX(), locationListAfterOptimization.get(i).getY() - locationListAfterOptimization.get(i - 1).getY());
                if(temp_distance > 0.1)
                    commandList.add("F" + round( (temp_distance / 10.0f),0));
                Log.d("Pozycja", "F" + (int) (temp_distance / 10));
            }
        }

        String result="";
        for(String a : commandList) {
            result += a +";";
        }
        result="";
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    // Implement DouglasPeucker algorithm
    // https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
    public ArrayList<Location> DouglasPeucker (ArrayList<Location> locationList,float epsilon) {
        ArrayList<Location> resultList;
        ArrayList<Location> res1;
        ArrayList<Location> res2;
        float dmax = 0;
        float d = 0;
        int index = 0;
        Line line = new Line(locationList.get(0), locationList.get(locationList.size() - 1));
        for (int i = 1; i < locationList.size() - 1; i++) {
            d = line.distance(locationList.get(i));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        if(dmax > epsilon) {
            res1 = new ArrayList<>(locationList.subList(0,index+1));
            res1 = DouglasPeucker(res1, epsilon);
            res2 = new ArrayList<>(locationList.subList(index,locationList.size()-1));
            res2 = DouglasPeucker(res2, epsilon);
            resultList =  new ArrayList<>(res1);
            resultList.addAll(res2.subList(1, res2.size()));
        }
        else
           resultList = new ArrayList<>(locationList);
        return resultList;
    }
}
