package com.example.tompo.tutorialapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;


/**
 * Created by tompo on 16/07/2017.
 */

public class Meteor extends SolidObject {

    public static float width = 100;

    public Bitmap bitmap;
    private Random rand;

    public float screenLength, screenWidth;

    public float endX;
    public float meteorVerticalSpeedPerSecond;

    private float totalX, totalY;
    private static float MAX_SPEED_VARIABLE = (float) 1.2;

    private float totalFallingTime;

    private float movedX = 0, movedY = 0;

    public boolean expired = false;
    public boolean hitPlayer = false;

    public Meteor(float screenWidth, float screenLength, float playerX, float speedVariable, Resources r, int bitmapID){

        rand = new Random();

        this.screenLength = screenLength;
        this.screenWidth = screenWidth;

        float startX = (screenWidth / 2) + rand.nextInt((int)screenWidth / 4) - rand.nextInt((int)screenWidth / 4);
        float startY = -500;

        // ensures the meteor falls close to the player
        endX = playerX + rand.nextInt((int)screenWidth / 4) - rand.nextInt((int)screenWidth / 4);

        this.meteorVerticalSpeedPerSecond = 600 * Math.min(speedVariable,this.MAX_SPEED_VARIABLE);
        this.totalFallingTime = (this.screenLength - startY) / (this.meteorVerticalSpeedPerSecond);

        totalX = startX - endX;
        totalY = screenLength - startY;

        this.setBoundaries(startX,startY,width,width);

        // Load Bob from his .png file
        Bitmap rawPlatform = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawPlatform, (int) width, (int) width, false);

    }

    @Override
    public boolean contains(float x, float y){
        float dx = x - this.x;
        float dy = y - this.y;

        double d = Math.sqrt((dx * dx) + (dy * dy));

        return d < this.width;
    }

    // Move along the trajectory towards the end point
    // Check for collisions with the player as we are going
    public XY move(Player p, float fps, ArrayList<SolidObject> objects){

        float moveX = -this.totalX / totalFallingTime / fps;
        float moveY = this.meteorVerticalSpeedPerSecond / fps;

        if(Math.abs(movedX) >= Math.abs(totalX) && Math.abs(movedY) >= Math.abs(totalY)){
            expired = true;
            // Meteor has finished its descent - explode into zombies?
        }
        else {
            this.x += moveX;
            this.y += moveY;

            movedX += moveX;
            movedY += moveY;
        }


        ArrayList<XY> collisionEdges = new ArrayList<>();

        float pLeft = p.vertices[0].y - p.vertices[3].y;
        for(int i = 0; i < pLeft; i+=5){
            collisionEdges.add(new XY(p.x, p.y - i));
        }
        float pRight = p.vertices[1].y - p.vertices[2].y;
        for(int i = 0; i < pRight; i+=5){
            collisionEdges.add(new XY(p.x + p.width, p.y - i));
        }

        float pTop = p.vertices[2].x - p.vertices[3].x;
        for(int i = 0; i < pTop; i++){
            collisionEdges.add(new XY(p.x + i, p.y - p.length));
        }

        float pBot = this.vertices[1].x - this.vertices[0].x;
        for(int i = 0; i < pBot; i++){
            collisionEdges.add(new XY(p.x + i, p.y));
        }

        for (XY point : collisionEdges){
            if(this.contains(point.x, point.y)){
                //TODO - code for when meteor hits player
                hitPlayer = true;
                Log.d("TRP", "METEOR STRIKE");
            }
        }


        return new XY(this.x, this.y);
    }
}
