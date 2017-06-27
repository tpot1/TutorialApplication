package com.example.tompo.tutorialapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.DisplayMetrics;

import java.util.Stack;

public class SimpleGameEngine extends Activity {

    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    // GameView class will go here

    // More SimpleGameEngine methods will go here


    // Here is our implementation of GameView
    // It is an inner class.
    // Note how the final closing curly brace }
    // is inside SimpleGameEngine

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        long fps;

        // This is used to help calculate the fps
        private long timeThisFrame;

        // Declare an object of type Bitmap
        Bitmap bitmapBob;

        // Bob starts off not moving
        boolean isMovingLeft = false;
        boolean isMovingRight = false;

        // He can walk at 150 pixels per second
        float walkSpeedPerSecond = 150;

        // He starts 10 pixels from the left
        float bobXPosition = 10;


        float screenHeight, screenWidth;

        Stack<Integer> touchEventStack;

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Load Bob from his .png file
            bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);


            // Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenHeight = displayMetrics.heightPixels;
            screenWidth = displayMetrics.widthPixels;

            // Initialise touch event stack
            touchEventStack = new Stack<>();
        }

        @Override
        public void run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        // In later projects we will have dozens (arrays) of objects.
        // We will also do other things like collision detection.
        public void update() {

            // If bob is moving (the player is touching the screen)
            // then move him to the right based on his target speed and the current fps.
            if(isMovingRight){
                bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
            }
            else if(isMovingLeft){
                bobXPosition = bobXPosition - (walkSpeedPerSecond / fps);
            }

        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                // Make the drawing surface our canvas object
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.argb(255,  26, 128, 182));

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));

                // Make the text a bit bigger
                paint.setTextSize(45);

                // Display the current fps on the screen
                canvas.drawText("FPS:" + fps, 20, 40, paint);

                // Draw bob at bobXPosition, 200 pixels
                canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);

                // Draw everything to the screen
                // and unlock the drawing surface
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started theb
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }


        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            // get pointer index from the event object
            int pointerIndex = motionEvent.getActionIndex();

            // get pointer ID
            int pointerId = motionEvent.getPointerId(pointerIndex);


            switch (motionEvent.getActionMasked()) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    touchEventStack.push(pointerIndex);

                    // Determine direction and set isMoving so Bob is moved in the update method
                    if (motionEvent.getX(pointerIndex) > screenWidth/2){
                        isMovingRight = true;
                        isMovingLeft = false;
                    }
                    else{
                        isMovingLeft = true;
                        isMovingRight = false;
                    }


                    break;

                case MotionEvent.ACTION_POINTER_DOWN:

                    touchEventStack.push(pointerIndex);

                    // Determine direction and set isMoving so Bob is moved in the update method
                    if (motionEvent.getX(pointerIndex) > screenWidth/2){
                        isMovingRight = true;
                        isMovingLeft = false;
                    }
                    else{
                        isMovingLeft = true;
                        isMovingRight = false;
                    }


                    break;

                case MotionEvent.ACTION_POINTER_UP:

                    this.remove(pointerIndex);

                    int activePointerIndex = touchEventStack.peek();

                    if (motionEvent.getX(activePointerIndex) > screenWidth/2){
                        isMovingRight = true;
                        isMovingLeft = false;
                    }
                    else{
                        isMovingLeft = true;
                        isMovingRight = false;
                    }
                    break;



                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    // Set isMoving so Bob does not move
                    isMovingLeft = false;
                    isMovingRight = false;
                    break;

            }
            return true;
        }

        private Stack<Integer> remove(int id){

            Stack<Integer> tmpStack = new Stack<>();

            if (touchEventStack.isEmpty())
                return null;

            else {
                for (int i = 0; i < touchEventStack.size(); i++){
                    Integer element = touchEventStack.pop();
                    if(element != id){
                        tmpStack.push(element);
                    }
                }

                for (int i = 0; i < tmpStack.size(); i++){
                    touchEventStack.push(tmpStack.pop());
                }

                return touchEventStack;
            }
        }

    }
    // This is the end of our GameView inner class

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

}
