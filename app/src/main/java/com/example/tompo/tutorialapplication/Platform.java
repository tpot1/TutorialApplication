package com.example.tompo.tutorialapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by tompo on 02/07/2017.
 */

public class Platform extends SolidObject {

    public Bitmap bitmap;

    public Platform(float x, float y, float width, float length, Resources r, int bitmapID){

        this.setBoundaries(x,y,width,length);

        // Load Bob from his .png file
        Bitmap rawPlatform = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawPlatform, (int) width, (int) length, false);

    }


}
