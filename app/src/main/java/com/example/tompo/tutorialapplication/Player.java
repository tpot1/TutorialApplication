package com.example.tompo.tutorialapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import java.util.ArrayList;
import android.util.Log;

/**
 * Created by tompo on 02/07/2017.
 */

public class Player extends SolidObject{

    public Bitmap bitmap;

    // Bob starts off not moving
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;

    public boolean isDashing = false;

    public double dragThreshold;
    public float dashLength = 500;


    // He can walk at 800 pixels per second
    float walkSpeedPerSecond = 800;

    float dashSpeedPerSecond = 800;


    public float totalDashX = 0;
    public float totalDashY = 0;

    public float dashedX = 0;
    public float dashedY = 0;

    public Player(float x, float y, float width, float length, Resources r, int bitmapID, double dragThreshold){

        this.setBoundaries(x,y,width,length);

        this.dragThreshold = dragThreshold;

        // Load Bob from his .png file
        Bitmap rawBob = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawBob, (int) width, (int) length, false);

    }

    public boolean checkDash(XY oldXY, XY newXY){
        float dx = Math.abs(oldXY.x - newXY.x);
        float dy = Math.abs(oldXY.y - newXY.y);

        double dragDist = Math.sqrt((dx * dx) + (dy * dy));

        return dragDist > dragThreshold;
    }

    public void dash(float fps) {

        if(!(this.totalDashX == 0 && this.totalDashY == 0) && isDashing){
            //XY realXY = bob.checkCollision(newXY.x, newXY.y, solidObjects);

            float dashX = (dashSpeedPerSecond * (Math.abs(totalDashX) / (Math.abs(totalDashX) + Math.abs(totalDashY)))) / fps;
            float dashY = (dashSpeedPerSecond * (Math.abs(totalDashY) / (Math.abs(totalDashX) + Math.abs(totalDashY)))) / fps;

            if(totalDashX < 0){
                dashX = -dashX;
            }
            if(totalDashY < 0){
                dashY = -dashY;
            }

            this.x += dashX;
            dashedX += dashX;

            this.y += dashY;
            dashedY += dashY;

            if(Math.abs(dashedX) >= Math.abs(totalDashX) && Math.abs(dashedY) >= Math.abs(totalDashY)){
                this.isDashing = false;

                totalDashY = 0;
                totalDashX = 0;

                dashedX = 0;
                dashedY = 0;
            }
        }

    }

    public XY checkCollision(float newX, float newY, ArrayList<SolidObject> objects){
        for(SolidObject o : objects){
            int left = Math.min((int)newX, (int)o.x);
            int right = Math.max((int)newX + (int)this.width, (int)o.x + (int)o.width);
            int top = Math.min((int)newY, (int)o.y);
            int bottom = Math.max((int)newY + (int)this.length, (int)o.y + (int)o.length);

            for (int x = left; x < right; x++) {
                for (int y = top; y < bottom; y++) {
                    if (this.contains(x,y) && o.contains(x,y)) {
                        return new XY(x,y);
                    }
                }
            }
        }
        return new XY(newX, newY);
    }

}
