package com.example.tompo.tutorialapplication;

/**
 * Created by tompo on 02/07/2017.
 */


public abstract class SolidObject{

    public float x, y, width, length;

    public XY[] vertices;

    public void setBoundaries(float x, float y, float width, float length) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;

        updateVertices();
    }

    public void updateVertices(){
        vertices = new XY[] {new XY(x,y), new XY(x + width, y), new XY(x + width, y + length), new XY(x, y + length) };
    }

    public boolean contains(float x, float y){
        updateVertices();

        int i;
        int j;
        boolean result = false;
        for (i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            if ((vertices[i].y > y) != (vertices[j].y > y) &&
                    (x < (vertices[j].x - vertices[i].x) * (y - vertices[i].y) / (vertices[j].y-vertices[i].y) + vertices[i].x)) {
                result = !result;
            }
        }
        return result;
    }

}
