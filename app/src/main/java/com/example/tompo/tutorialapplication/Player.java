package com.example.tompo.tutorialapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import java.util.ArrayList;

/**
 * Created by tompo on 02/07/2017.
 */

public class Player extends SolidObject{

    public Bitmap bitmap;

    // Bob starts off not moving
    boolean isMovingLeft = false;
    boolean isMovingRight = false;

    // He can walk at 150 pixels per second
    float walkSpeedPerSecond = 400;

    public Player(float x, float y, float width, float length, Resources r, int bitmapID){

        this.setBoundaries(x,y,width,length);

        // Load Bob from his .png file
        Bitmap rawBob = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawBob, (int) width, (int) length, false);

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
