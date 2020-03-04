package com.example.rehanabdullah.jump.model;

/**
 * Created by rehanabdullah on 10/04/2016.
 */

import android.annotation.SuppressLint;
import android.graphics.Canvas;

import com.example.rehanabdullah.jump.controller.GameActivity;



public class GameLoop extends Thread {

    private GameActivity.GameView view;
    static final long FPS = 10;
    private boolean running = false;
    boolean isPaused;

    public GameLoop(GameActivity.GameView view) {
        this.view = view;
    }



    public void setRunning(boolean run) {
        running = run;
    }

    public void setPause(int i) {
        synchronized (view.getHolder()) {

            if(i==0) {
                isPaused=false;
            }
            if(i==1) {
                isPaused = true;
            }
        }
    }


    @SuppressLint("WrongCall")
    @Override

    public void run() {
        long ticksPS = 100;
        long startTime = 0;
        long sleepTime;
        while (running) {
            //pause and resume

            if (isPaused) {
                try {
                    this.sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                Canvas c = null;
                startTime = System.currentTimeMillis();
                try {

                    c = view.getHolder().lockCanvas();

                    synchronized (view.getHolder()) {
                        view.onDraw(c);
                    }

                }
                finally {
                    if (c != null) {
                        view.getHolder().unlockCanvasAndPost(c);
                    }
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);

            try {

                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            }
            catch (Exception e) {

            }

        }

    }

}