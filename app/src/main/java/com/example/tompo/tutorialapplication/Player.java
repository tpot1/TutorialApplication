package com.example.tompo.tutorialapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.content.res.Resources;

/**
 * Created by tompo on 02/07/2017.
 */

public class Player implements SolidObject{

    public Bitmap bitmap;
    public float x,y;

    public static float width = 140;
    public static float length = 215;

    // Bob starts off not moving
    boolean isMovingLeft = false;
    boolean isMovingRight = false;

    // He can walk at 150 pixels per second
    float walkSpeedPerSecond = 400;

    public Player(float x, float y, Resources r, int bitmapID){

        this.setBoundaries(x,y,this.width,this.length);

        // Load Bob from his .png file
        Bitmap rawBob = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawBob, (int) width, (int) length, false);

    }

    @Override
    public void setBoundaries(float x, float y, float width, float length) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
    }
}
