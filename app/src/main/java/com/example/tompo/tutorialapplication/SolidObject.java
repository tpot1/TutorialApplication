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
        vertices = new XY[] {new XY(x,y), new XY(x + width, y), new XY(x + width, y - length), new XY(x, y - length) };
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

    /*public boolean contains(float x, float y) {
        this.updateVertices();

        int n = vertices.length;
        boolean inside = false;

        float p1x = vertices[0].x;
        float p1y = vertices[0].y;

        for (int i = 0; i < n + 1; i++) {
            float p2x = vertices[i % n].x;
            float p2y = vertices[i % n].y;

            if (y > Math.min(p1y, p2y)) {
                if (y <= Math.max(p1y, p2y)) {
                    if (x <= Math.max(p1x, p2x)) {
                        if (p1y != p2y) {
                            float xints = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x;
                            if (p1x == p2x || x <=xints){
                                inside = !inside;
                            }

                        }
                    }
                }
            }
            p1x = p2x;
            p1y = p2y;
        }

        return inside;
    }*/
}
