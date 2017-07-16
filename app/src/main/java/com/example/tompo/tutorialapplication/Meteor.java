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

    public Bitmap bitmap;
    private Random rand;

    public float screenLength, screenWidth;

    public float endX;
    public float meteorSpeedPerSecond;

    private float totalX, totalY;

    private float movedX = 0, movedY = 0;

    public boolean exploded = false;

    public Meteor(float screenWidth, float screenLength, Resources r, int bitmapID){

        rand = new Random();

        this.screenLength = screenLength;
        this.screenWidth = screenWidth;

        float startX = (screenWidth / 2) + rand.nextInt((int)screenWidth / 4) - rand.nextInt((int)screenWidth / 4);
        float startY = 0;
        float width = 100;

        endX = (screenWidth / 2) + rand.nextInt((int)screenWidth / 4) - rand.nextInt((int)screenWidth / 4);
        meteorSpeedPerSecond = 1000 + rand.nextInt(500);

        totalX = startX - endX;
        totalY = screenLength;

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

        float moveX = (this.meteorSpeedPerSecond * (Math.abs(totalX) / (Math.abs(totalX) + Math.abs(totalY)))) / fps;
        float moveY = (this.meteorSpeedPerSecond * (Math.abs(totalY) / (Math.abs(totalX) + Math.abs(totalY)))) / fps;

        if(totalX < 0){
            moveX = -moveX;
        }

        if(Math.abs(movedX) >= Math.abs(totalX) && Math.abs(movedY) >= Math.abs(totalY)){
            exploded = true;
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
                Log.d("TRP", "METEOR STRIKE");
            }
        }


        return new XY(this.x, this.y);
    }
}
