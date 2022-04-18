package com.example.pingponggame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {
    Context context;
    float ballX, ballY;
    Velocity velocity = new Velocity(25, 32);
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float textSize = 120;
    float paddleX, paddleY;
    float oldX, paddleOldX;
    int point = 0;
    int life = 3;
    Bitmap ball, paddle;
    int dWith, dHeight;
    MediaPlayer mpHit, mpMiss;
    Random random;
    SharedPreferences sharedPreferences;
    Boolean audioState;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle2);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        mpHit = MediaPlayer.create(context, R.raw.hit);
        mpMiss = MediaPlayer.create(context, R.raw.miss);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.BLUE);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWith = size.x;
        dHeight = size.y;
        random = new Random();
        ballX = random.nextInt();
        paddleY = (dHeight * 4) / 5;
        paddleX = dWith / 2 - paddle.getWidth() / 2;

        sharedPreferences = context.getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        ballX += velocity.getX();
        ballY += velocity.getY();

        if ((ballX >= dWith - ball.getWidth()) || ballX <= 0) {
            velocity.setX(velocity.getX() * -1);
        }
        if (ballY <= 0) {
            velocity.setY(velocity.getY() * -1);
        }
        if (ballY > paddleY + paddle.getHeight()) {
            ballX = 1 + random.nextInt(dWith - ball.getWidth() - 1);
            ballY  = 0;
            if (mpMiss != null && audioState) {
                mpMiss.start();
            }
            velocity.setX(xVelocity());
            velocity.setY(32);
            life--;
            if (life == 0)
            {
                Intent intent = new Intent(context, GameOver.class);
                intent.putExtra("points", point);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
        if (((ballX + ball.getWidth())>= paddleX)
            && (ballX <= paddleX + paddle.getWidth())
            && (ballY + ball.getHeight() >= paddleY)
            && (ballY + ball.getHeight() <= paddleY + paddle.getHeight()))
        {
            if (mpHit != null && audioState)
            {
                mpHit.start();
            }
            velocity.setX(velocity.getX() +1);
            velocity.setY((velocity.getY() + 1)* -1);
            point++;
        }
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);
        canvas.drawText(""+point, 20, textSize, textPaint);
        if (life == 2)
        {
            healthPaint.setColor(Color.YELLOW);
        }else if (life == 1)
        {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWith-200, 30, dWith-200 + 60*life, 80, healthPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= paddleY)
        {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN)
            {
                oldX = event.getX();
                paddleOldX  = paddleX;
            }
            if (action == MotionEvent.ACTION_MOVE)
            {
                float shift = oldX - touchX;
                float newPaddeX = paddleOldX - shift;
                if (newPaddeX <= 0)
                    paddleX =0;
                else  if(newPaddeX >= dWith - paddle.getWidth())
                    paddleX = dWith - paddle.getWidth();
                else
                    paddleX = newPaddeX;
            }
        }
        return true;
    }

    private int xVelocity() {
        int values[] = {-35, -30, -25, 25, 30, 35};
        int index = random.nextInt(6);
        return values[index];
    }
}
