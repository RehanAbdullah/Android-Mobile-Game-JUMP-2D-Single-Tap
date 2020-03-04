package com.example.rehanabdullah.jump.controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.example.rehanabdullah.jump.model.GameLoop;
import com.example.rehanabdullah.jump.R;

public class GameActivity extends Activity {



    // Concurrent Thread
    GameLoop gameLoopThread;

    // On create method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //phone state
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(),PhoneStateListener.LISTEN_CALL_STATE);
        //for no title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));
    }

    public class GameView extends SurfaceView{


//        Bitmap Declarations
        Bitmap bmp;
        Bitmap pause;

        Bitmap background;
        Bitmap spanner;

        Bitmap note1,note2;

        Bitmap powerimg;


        Bitmap car1,car2,car3;


        Bitmap coin;
        Bitmap exit;

        private SurfaceHolder holder;
        private int x = 0;
        private int y=0;
        private int z=0;
        private int delay=0;
        private int getx;
        private int gety;
        private int sound=1;

        int show=0;
        int SX;
        int sy;
        int cSpeed=0;
        int kSpeed=0;
        int GAMEOVER=0;

        int score=0;
        int health=100;
        int reset=0;
        int pausecount=0;
        int volume;
        int power=0;
        int powerrun=0;
        int shieldrun=0;


        // Game View inside MainActivity
        @SuppressWarnings("deprecation")
        @SuppressLint("NewApi")
        public GameView(Context context)
        {
            super(context);

            gameLoopThread = new GameLoop(this);
            holder = getHolder();

            holder.addCallback(new SurfaceHolder.Callback() {
                @SuppressWarnings("deprecation")
                @Override
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    //for stoping the game
                    gameLoopThread.setRunning(false);
                    gameLoopThread.getThreadGroup().interrupt();
                }

                @SuppressLint("WrongCall")
                @Override
                public void surfaceCreated(SurfaceHolder holder)
                {
                    gameLoopThread.setRunning(true);
                    gameLoopThread.start();

                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
                }
            });

            //getting the screen size
            Display display = getWindowManager().getDefaultDisplay();

            SX = display.getWidth();
            sy = display.getHeight();;
            cSpeed=SX/2;
            kSpeed=SX/2;
            powerrun=(3*SX/4);
            shieldrun=SX/8;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            car1=BitmapFactory.decodeResource(getResources(), R.drawable.car1);
            car2=BitmapFactory.decodeResource(getResources(), R.drawable.car2);
            car3=BitmapFactory.decodeResource(getResources(), R.drawable.car3);
            coin=BitmapFactory.decodeResource(getResources(), R.drawable.coin);
            exit=BitmapFactory.decodeResource(getResources(), R.drawable.exit);
            spanner=BitmapFactory.decodeResource(getResources(), R.drawable.spanner);
            note1=BitmapFactory.decodeResource(getResources(), R.drawable.note1);

            pause=BitmapFactory.decodeResource(getResources(), R.drawable.pause);

            powerimg=BitmapFactory.decodeResource(getResources(), R.drawable.power);
            note2=BitmapFactory.decodeResource(getResources(), R.drawable.note2);

            exit=Bitmap.createScaledBitmap(exit, 25,25, true);
            pause=Bitmap.createScaledBitmap(pause, 25,25, true);
            powerimg=Bitmap.createScaledBitmap(powerimg, 25,25, true);
            note2=Bitmap.createScaledBitmap(note2, SX,sy, true);
            car1=Bitmap.createScaledBitmap(car1, SX/9,sy/7, true);
            car2=Bitmap.createScaledBitmap(car2, SX/9,sy/7, true);
            car3=Bitmap.createScaledBitmap(car3, SX/9,sy/7, true);
            coin=Bitmap.createScaledBitmap(coin, SX/16,sy/24, true);
            background=Bitmap.createScaledBitmap(background, 2*SX,sy, true);

            //health dec
            note1=Bitmap.createScaledBitmap(note1, SX,sy, true);

//           Import sound from here
        }

        // on touch method

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                show=1;

                getx=(int) event.getX();
                gety=(int) event.getY();
                //exit
                if(getx<25&&gety<25) {
                    //high score
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
                    Editor editor = pref.edit();
                    editor.putInt("score", score);
                    editor.commit();
                    System.exit(0);

                }
                // restart game
                if(getx>91&&gety<25) {
                    if(health<=0) {
                        gameLoopThread.setPause(0);
                        health=100;
                        score=0;

                    }
                }
                //pause game
                if((getx>(SX-25) && gety <25 && pausecount==0)) {

                    gameLoopThread.setPause(1);
                    pausecount=1;
                }

                else if(getx>(SX-25) && gety<25 && pausecount==1) {
                    gameLoopThread.setPause(0);
                    pausecount=0;
                }
            }

            return true;
        }


        @SuppressLint("WrongCall")
        @Override
        public void onDraw(Canvas canvas)
        {

            //volume
            SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
            Editor editor = pref.edit();
            volume=pref.getInt("vloume", 0);
            if(volume==0) {
                sound=0;
            }

            canvas.drawColor(Color.BLACK);

            //background moving
            z=z-10;
            if(z==-SX) {
                z=0;
                canvas.drawBitmap(background, z, 0, null);

            }
            else {
                canvas.drawBitmap(background, z, 0, null);
            }

            //running car

            x+=5;
            if(x==20) {
                x=5;
            }

            if(show==0) {
                if(x%2==0) {
                    canvas.drawBitmap(car3, SX/16, 15*sy/18, null);

                }
                else {
                    canvas.drawBitmap(car1, SX/16, 15*sy/18, null);

                }


                //spanner hit
                if(kSpeed==20) {
                    kSpeed=SX;
                    health-=25;
                    canvas.drawBitmap(note1, 0, 0, null);
                }

                //power take
                if(powerrun==30) {
                    powerrun=3*SX;
                    health+=25;
                    canvas.drawBitmap(note2, 0, 0, null);
                }
            }
            //power
            powerrun=powerrun-10;
            canvas.drawBitmap(powerimg, powerrun, 15*sy/18, null);

            if(powerrun<0) {
                powerrun=3*SX/4;
            }

            //spanner
            kSpeed=kSpeed-50; // Spanner Speed
            canvas.drawBitmap(spanner, kSpeed, 15*sy/18, null);

            if(kSpeed<0) {
                kSpeed=SX;
            }

            // for jump
            if(show==1) {
                if(sound==1) {
//                    jump.start();
                }

                canvas.drawBitmap(car2, SX/16, 3*sy/4, null);
                //score
                if(cSpeed<=SX/10 && cSpeed>=SX/18) {
                    if(sound==1) {
//                        takecoin.start();

                    }
                    cSpeed=SX/2;
                    score+=10;

                }



                // jump-hold
                delay+=1;
                if(delay==3) {
                    show=0;
                    delay=0;
                }
            }

            //for coins
            cSpeed=cSpeed-25;
            if(cSpeed==-SX/2) {
                cSpeed=SX/2;
                canvas.drawBitmap(coin, cSpeed, 3*sy/4, null);

            }
            else {
                canvas.drawBitmap(coin, cSpeed, 3*sy/4, null);
            }




            //score
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setAntiAlias(true);
            paint.setFakeBoldText(true);
            paint.setTextSize(15);
            paint.setTextAlign(Align.LEFT);
            canvas.drawText("Score :"+score, 3*SX/4, 20, paint);
            //exit
            canvas.drawBitmap(exit, 0, 0, null);
            if(sound==1) {

            }
            else {
            }
            //health
            Paint myPaint = new Paint();
            myPaint.setColor(Color.RED);
            myPaint.setStrokeWidth(10);
            myPaint.setAntiAlias(true);
            myPaint.setFakeBoldText(true);
            canvas.drawText("Health :"+health, 0, (sy/8)-5, myPaint);
            canvas.drawRect(0, sy/8, health, sy/8+10, myPaint);

            //game over
            if(health<=0) {
                GAMEOVER=1;
//                mp1.stop();

                //high score
                editor.putInt("score", score);
                editor.commit();

                canvas.drawText("GAMEOVER OVER", SX/2, sy/2, myPaint);
                canvas.drawText("YOUR SCORE : "+score, SX/2, sy/4, myPaint);
                canvas.drawText("Restart", 91, 25, myPaint);
                gameLoopThread.setPause(1);
                canvas.drawBitmap(background, SX, sy, null);
            }
            // restart

            if(reset==1) {
                gameLoopThread.setPause(0);
                health=100;
                score=0;
            }

            canvas.drawBitmap(pause, (SX-25), 0, null);
        }

    }

    //phone state
    public class TeleListener extends PhoneStateListener {
        public void onCallStateChanged(int state,String incomingNumber) {
            if(state==TelephonyManager.CALL_STATE_RINGING) {
                System.exit(0);
            }
        }

    }


}

