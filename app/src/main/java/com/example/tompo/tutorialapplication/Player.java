package com.example.tompo.tutorialapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import java.util.ArrayList;
import java.util.Vector;

import android.util.Log;

/**
 * Created by tompo on 02/07/2017.
 */

public class Player extends SolidObject{

    public float playerWidth = 100;
    public float playerLength = 150;

    public float screenWidth;
    public float screenHeight;

    public Bitmap bitmap;

    // Bob starts off not moving
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;

    public boolean isDashing = false;

    public boolean active = false;

    public double dragThreshold;
    public float dashLength = 800;


    // He can walk at 800 pixels per second
    float walkSpeedPerSecond = 800;

    float dashSpeedPerSecond = 3200;


    public float totalDashX = 0;
    public float totalDashY = 0;

    public float dashedX = 0;
    public float dashedY = 0;

    public Player(float screenWidth, float screenHeight, Resources r, int bitmapID){

        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        float playerX = screenWidth/2;
        float playerY = screenHeight - (screenHeight/3);

        this.setBoundaries(playerX, playerY, playerWidth, playerLength);

        this.dragThreshold = (Math.sqrt((screenWidth * screenWidth) + (screenHeight * screenHeight)) / 8);

        // Load Bob from his .png file
        Bitmap rawBob = BitmapFactory.decodeResource(r, bitmapID);
        bitmap = Bitmap.createScaledBitmap(rawBob, (int) playerWidth, (int) playerLength, false);

    }

    public boolean checkDash(XY oldXY, XY newXY){
        float dx = Math.abs(oldXY.x - newXY.x);
        float dy = Math.abs(oldXY.y - newXY.y);

        double dragDist = Math.sqrt((dx * dx) + (dy * dy));

        return dragDist > dragThreshold;
    }

    public void dash(float fps, ArrayList<SolidObject> objects) {

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

            this.move(new XY(this.x + dashX, this.y), objects);
            this.move(new XY(this.x, this.y + dashY), objects);
            dashedX += dashX;
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


    // 1: determine which direction we are moving (positive or negative x? positive or negative y?)
    // 2: determine which edges we need to check when moving in this direction (i.e. if positive x, check right edge)
    // 3: find distance to be moved in x and y
    // 4: try moving entire distance and check for collisions on valid edges
    // 5: if collision occurred, gradually reduce distance until no collision or no movement
    public XY move(XY newPos, ArrayList<SolidObject> objects){

        this.active = true;

        XY validPos = newPos;

        if(validPos.x < 0){
            validPos.x = 0;
        }
        else if(validPos.x + this.playerWidth > screenWidth){
            validPos.x = screenWidth - this.playerWidth;
        }

        if(validPos.y - this.playerLength < 0){
            validPos.y = this.playerLength;
        }
        else if(validPos.y > screenHeight){
            validPos.y = screenHeight;
        }

        float dx = validPos.x - this.x;
        float dy = validPos.y - this.y;

        ArrayList<XY> collisionEdges = new ArrayList<>();

        if(dx < 0){

            float edge = this.vertices[0].y - this.vertices[3].y;
            for(int i = 0; i < edge; i+=5){
                collisionEdges.add(new XY(this.x + dx, this.y + dy - i));
            }
        }
        else if(dx > 0){

            float edge = this.vertices[1].y - this.vertices[2].y;
            for(int i = 0; i < edge; i+=5){
                collisionEdges.add(new XY(this.x + this.width + dx, this.y + dy - i));
            }
        }

        if(dy < 0){
            float edge = this.vertices[2].x - this.vertices[3].x;
            for(int i = 0; i < edge; i++){
                collisionEdges.add(new XY(this.x + dx + i, this.y - this.length + dy));
            }
        }
        else if(dy > 0){
            float edge = this.vertices[1].x - this.vertices[0].x;
            for(int i = 0; i < edge; i++){
                collisionEdges.add(new XY(this.x + dx + i, this.y + dy));
            }
        }

        for(SolidObject o : objects){
            for(XY point : collisionEdges){
                if(o.contains(point.x, point.y)){
                    return new XY(this.x, this.y);
                }
            }
        }

        this.x = validPos.x;
        this.y = validPos.y;

        this.updateVertices();

        return validPos;

    }

}
