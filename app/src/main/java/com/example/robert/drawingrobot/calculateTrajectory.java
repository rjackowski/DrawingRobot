package com.example.robert.drawingrobot;

import android.util.Log;

import com.example.robert.drawingrobot.Data.Line;
import com.example.robert.drawingrobot.Data.Location;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rob on 21.12.2017.
 */

public class calculateTrajectory {
    // klasa do przechowywania trajektorii i obliczania jej

        private ArrayList<Location> LocationList = new ArrayList<>();
        public ArrayList<String> CommandList = new ArrayList<>();
        public void addElement(float x, float y,int marker) {
            Location temp= new Location(x,y,marker);
            LocationList.add(temp);
        }
        public void clear() {
            LocationList.clear();
        }

        public void calculate()
        {
            int numberOfIteration = 10;
            int averageCounter = 0;
            Float a, b;
            Float sumOfY = 0.0f;
            Float sumOfX = 0.0f;
            Float regressionUp = 0.0f;
            Float regressionDown = 0.0f;
            Location tempLocation ;
            ArrayList<Location> LocationListAfterRegression = new ArrayList<>();
            ArrayList<Location> AveragePosition  = new ArrayList<>();
             // Linear regression method
            for (int i =0; i < LocationList.size(); i++) {
                sumOfX += LocationList.get(i).getX();
                sumOfY += LocationList.get(i).getY();
                if(i % numberOfIteration == 0) { //we look for average positions for every part of points
                    Location temp = new Location(sumOfX / numberOfIteration, sumOfY / numberOfIteration);
                    AveragePosition.add(temp);
                    sumOfY = 0.0f;
                    sumOfX = 0.0f;
                }
            }

//            averageCounter  = 0;
//            for (int i =0; i < LocationList.size(); i++) {
//                if(i == 0) {
//                    LocationListAfterRegression.add(LocationList.get(i)); // first point of locationList is firstPoint of new array
//                }
//                regressionUp += (LocationList.get(i).getX()- AveragePosition.get(averageCounter).getX()) * (LocationList.get(i).getY()- AveragePosition.get(averageCounter).getY());
//                regressionDown +=   (float)Math.pow((LocationList.get(i).getX()- AveragePosition.get(averageCounter).getX()),2);
//                if(i%numberOfIteration == 0 && i!=0){ //we look for average positions for every part of points
//                    a = regressionUp /regressionDown;
//                    b = AveragePosition.get(averageCounter).getY() - AveragePosition.get(averageCounter).getX() * a;
//                    tempLocation = new Location();
//                    tempLocation.setX(LocationList.get(i).getX());
//                    tempLocation.setY(a*LocationList.get(i).getX()+b);
//                    tempLocation.setMarker(LocationList.get(i).getMarker());
//                    averageCounter++;
//                    LocationListAfterRegression.add(tempLocation);
//                }
//            }


                LocationListAfterRegression = DouglasPeucker(LocationList, 0.1f);
                Log.d("Epsilon", " Size1: " + LocationListAfterRegression.size() );
                LocationListAfterRegression = DouglasPeucker(LocationListAfterRegression,0.1f);
                Log.d("Epsilon", " Size2: " + LocationListAfterRegression.size() );


//
//            for(int i=0;i<LocationListAfterRegression.size();i++) {
//                float temp_distance = 0;
//                float temp_angle = 0;
//                float temp_angle2 = 0;
//                float kat = 0;
//                if (LocationListAfterRegression.get(i).getMarker() == 0)
//                    CommandList.add("U");
//                if (LocationListAfterRegression.get(i).getMarker() == 1)
//                    CommandList.add("D");
//                if (i == 1) {
//                    temp_distance = (float) Math.hypot(LocationListAfterRegression.get(i).getX() - LocationListAfterRegression.get(i - 1).getX(), LocationListAfterRegression.get(i).getY() - LocationListAfterRegression.get(i - 1).getY());
//                    CommandList.add("F" +  (temp_distance / 10));
//                    Log.d("Pozycja", "F" + (int) (temp_distance / 10));
//                } else if (i >= 2) {
//                    temp_angle = (float) Math.atan2(LocationListAfterRegression.get(i - 1).getY() - LocationListAfterRegression.get(i).getY(), LocationListAfterRegression.get(i).getX() - LocationListAfterRegression.get(i - 1).getX()) * 180.0f / 3.1415926535f;
//                    temp_angle2 = (float) Math.atan2(LocationListAfterRegression.get(i - 2).getY() - LocationListAfterRegression.get(i - 1).getY(), LocationListAfterRegression.get(i - 1).getX() - LocationListAfterRegression.get(i - 2).getX()) * 180.0f / 3.1415926535f;
//                    kat = temp_angle2 - temp_angle;
//                    if (kat > 180)
//                        kat = -(360 - kat);
//                    if (kat < -180)
//                        kat = 360 + kat;
//                    if (kat < -1) {
//                        CommandList.add("L" +  (-kat));
//                        Log.d("Pozycja", "L" + (int) (-kat));
//                    }
//                    if (kat > 1) {
//                        CommandList.add("R" +  (kat));
//                        Log.d("Pozycja", "R" + (int) (kat));
//                    }
//                    temp_distance = (float) Math.hypot(LocationListAfterRegression.get(i).getX() - LocationListAfterRegression.get(i - 1).getX(), LocationListAfterRegression.get(i).getY() - LocationListAfterRegression.get(i - 1).getY());
//                    CommandList.add("F" +  (temp_distance / 10));
//                    Log.d("Pozycja", "F" + (int) (temp_distance / 10));
//                }
//            }
            String listLocationString = "";
            listLocationString = "";
            String listLocationWithRegressionString = "";
            for (Location s : LocationList) {
                listLocationString += s.getX() + "|" + s.getY() + ";";
            }
            for (Location s : LocationListAfterRegression) {
                listLocationWithRegressionString += s.getX() + "|" + s.getY() + ";";
            }


//            Location temp = new Location();
//            LocationList.clear();
//            for(int i =0; i<1; i++){
//                temp = new Location(100, 100);
//                LocationList.add(temp);
//                temp = new Location(150, 150);
//                LocationList.add(temp);
//                temp = new Location(200, 100);
//                LocationList.add(temp);
//                temp = new Location(200, 200);
//                LocationList.add(temp);
//                temp = new Location(150, 150);
//                LocationList.add(temp);
//                temp = new Location(100, 200);
//                LocationList.add(temp);
//              //  temp = new Location(222, 167);
//                //LocationList.add(temp);
//
//                //temp = new Location(30, 100);
//                //LocationList.add(temp);
//            }




//            Location temp = new Location();
//            LocationList.clear();
//            for(int i =0; i<1; i++){
//            temp = new Location(200, 100);
//            LocationList.add(temp);
//                temp = new Location(150, 180);
//                LocationList.add(temp);
//                temp = new Location(100, 100);
//                LocationList.add(temp);
//                temp = new Location(69, 200);
//                LocationList.add(temp);
//            temp = new Location(150, 180);
//            LocationList.add(temp);
//                temp = new Location(212, 200);
//                LocationList.add(temp);
//            temp = new Location(200, 100);
//            LocationList.add(temp);
//        }

            listLocationString = "";
            for (Location s : LocationList) {
                listLocationString += s.getX() + "|" + s.getY() + ";";
            }

//            Location temp = new Location();
//            LocationList.clear();
//            for(int i =0; i<50; i++){
//            temp = new Location(200, 200);
//            LocationList.add(temp);
//                temp = new Location(250, 150);
//                LocationList.add(temp);
//                temp = new Location(210, 100);
//                LocationList.add(temp);
//                temp = new Location(155, 60);
//                LocationList.add(temp);
//            temp = new Location(100, 100);
//            LocationList.add(temp);
//                temp = new Location(60, 150);
//                LocationList.add(temp);
//            temp = new Location(100, 200);
//            LocationList.add(temp);
//        }






            for(int i=0;i<LocationList.size();i++) {
                float temp_distance = 0;
                float temp_angle = 0;
                float temp_angle2 = 0;
                double test_angle = 0;
                float kat = 0;
                if (LocationList.get(i).getMarker() == 0)
                    CommandList.add("U");
                if (LocationList.get(i).getMarker() == 1)
                    CommandList.add("D");
                if (i == 1) {
                    temp_distance = (float) Math.hypot(LocationList.get(i).getX() - LocationList.get(i - 1).getX(), LocationList.get(i).getY() - LocationList.get(i - 1).getY());
                    if(temp_distance > 0.1)
                        CommandList.add("F" +  round((temp_distance / 10.0f),2));
                } else if (i >= 2) {
                   // if(LocationList.get(i).getX() != LocationList.get(i-1).getX() )
                  //  temp_angle = ( LocationList.get(i).getY() - LocationList.get(i-1).getY() ) / (LocationList.get(i).getX() - LocationList.get(i - 1).getX())* 180.0f / 3.1415926535f;

                    temp_angle = (float) Math.atan2(LocationList.get(i - 1).getY() - LocationList.get(i).getY(), LocationList.get(i).getX() - LocationList.get(i - 1).getX()) * 180.0f / 3.1415926535f;
                    test_angle =   Math.atan2(200-150,(200-200))*180.0 /3.14;
                    test_angle =   Math.atan2(200-200,(200-150))*180.0 /3.14;
                    temp_angle2 = (float) Math.atan2(LocationList.get(i - 2).getY() - LocationList.get(i - 1).getY(), LocationList.get(i - 1).getX() - LocationList.get(i - 2).getX()) * 180.0f / 3.1415926535f;
                    kat = temp_angle2 - temp_angle;
                    if (kat > 180)
                        kat = -(360 - kat);
                    if (kat < -180)
                        kat = 360 + kat;
                    if (kat < -0.1) {
                        CommandList.add("L" +  round(-kat,2));
                        Log.d("Pozycja", "L" + (int) (-kat));
                    }
                    if (kat > 0.1) {
                        CommandList.add("R" +  round(kat,2));
                        Log.d("Pozycja", "R" + (int) (kat));
                    }
                    temp_distance = (float) Math.hypot(LocationList.get(i).getX() - LocationList.get(i - 1).getX(), LocationList.get(i).getY() - LocationList.get(i - 1).getY());
                    if(temp_distance > 0.1)
                        CommandList.add("F" + round( (temp_distance / 10.0f),2));
                    Log.d("Pozycja", "F" + (int) (temp_distance / 10));
                }
            }

            String result="";
            result ="";
            LocationList.clear();
            for(String x : CommandList) {
                result+= x + ";";
            }
            CommandList.clear();
            Log.d("Pozycja", "F");
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
