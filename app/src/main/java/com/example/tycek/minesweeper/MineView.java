package com.example.tycek.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Tycek on 23.11.2017.
 */

public class MineView extends View {

    Paint myPaint;
    int size;
    long startTime = 0;
    float clickX, clickY;
    int mineX, mineY;
    Mine[][] mines;

    public MineView(Context context) {
        super(context);
        init(context);
    }

    public MineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mines = new Mine[9][9];
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(0,255,0));
        myPaint.setStrokeWidth(10);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        size = (dm.widthPixels / 9) - 5;
        System.out.println(size);

        for (int i = 0 ; i < 9 ; i++) {
            for (int j = 0 ; j < 9 ; j++) {
                mines[i][j] = new Mine(i * (size + 5), j * (size + 5), size);
            }
        }
        GenerateMines();
        GenerateNumbers();
    }

    protected void onDraw(Canvas canvas) {
        System.out.println(mines[0][0].Visited());
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(mines[i][j].Visited() && mines[i][j].getState() != -1){
                    myPaint.setColor(Color.rgb(255,0,0));
                    canvas.drawRect(mines[i][j].x, mines[i][j].y, mines[i][j].x + mines[i][j].size, mines[i][j].y + mines[i][j].size, myPaint);
                    myPaint.setColor(Color.rgb(0,0,0));
                    myPaint.setTextSize(40);
                    if(mines[i][j].getState() != 0) {
                        canvas.drawText(String.valueOf(mines[i][j].getState()),mines[i][j].x + mines[i][j].size/2-5, mines[i][j].y+ mines[i][j].size/2+5, myPaint);
                    }
                }

                else if(mines[i][j].Visited() && mines[i][j].getState() == -1){
                    myPaint.setColor(Color.rgb(0,0,0));
                    Drawable d = getResources().getDrawable(R.drawable.mine);
                    d.setBounds(mines[i][j].x, mines[i][j].y, mines[i][j].x + mines[i][j].size, mines[i][j].y + mines[i][j].size);
                    d.draw(canvas);
                }

                else if(mines[i][j].flagged()) {
                    Drawable d = getResources().getDrawable(R.drawable.vlajka);
                    d.setBounds(mines[i][j].x, mines[i][j].y, mines[i][j].x + mines[i][j].size, mines[i][j].y + mines[i][j].size);
                    d.draw(canvas);
                }

                else if(!mines[i][j].Visited()) {
                    myPaint.setColor(Color.rgb(0, 255, 0));
                    canvas.drawRect(mines[i][j].x, mines[i][j].y, mines[i][j].x + mines[i][j].size, mines[i][j].y + mines[i][j].size, myPaint);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch(touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                clickX = touchevent.getX();
                clickY = touchevent.getY();
                startTime = System.currentTimeMillis();
                mineX = (int)(clickX/size);
                mineY = (int)(clickY/size);
                System.out.println("Mina[" + mineY + "][" + mineX + "]");
                break;
            }

            case MotionEvent.ACTION_UP: {
                if(mineX < 9 && mineY < 9) {
                    if(System.currentTimeMillis() - startTime  > 200) {
                        if(mines[mineX][mineY].flagged() ){
                            mines[mineX][mineY].unflag();
                        }
                        else {
                            mines[mineX][mineY].flag();
                        }

                    }
                    else {
                        mines[mineX][mineY].visit();
                        if(mines[mineX][mineY].getState() == 0) {
                            checkForNonMines(mineX, mineY);
                        }
                    }

                    invalidate();
                }
                break;
            }
        }
        return true;
    }

    private void GenerateMines() {
        Random rand = new Random();
        int mineCount = 0;
        while(mineCount < 10) {
            int X = rand.nextInt(9);
            int Y = rand.nextInt(9);
            if (mines[X][Y].getState() != -1) {
                mines[X][Y].changeState(-1);
                System.out.println(Y + " " + X);
                mineCount++;
            }
        }
    }

    private void GenerateNumbers() {
        for(int i = 0 ; i < 9 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                if(mines[i][j].getState() != -1) {
                    mines[i][j].changeState(checkSurroundings(i,j));
                }
            }
        }
    }

    private int checkSurroundings(int MyX, int MyY) {
        int count = 0;
        int fromX = -1;
        int fromY = -1;
        int toX = 1;
        int toY = 1;

        if(MyX == 0) fromX = 0;
        if(MyX == 8) toX = 0;
        if(MyY == 0) fromY = 0;
        if(MyY == 8) toY = 0;

        for(int x2 = fromX ; x2 <= toX ; x2++ ) {
            for(int y2 = fromY ; y2 <= toY ; y2++) {
                if(x2 == 0 && y2 == 0) continue;
                if(mines[MyX+x2][MyY+y2].getState() == -1) count++;
            }
        }
        return count;
    }

    public void checkForNonMines(int MyX, int MyY) {
        int fromX = -1;
        int fromY = -1;
        int toX = 1;
        int toY = 1;

        if(MyX == 0) fromX = 0;
        if(MyX == 8) toX = 0;
        if(MyY == 0) fromY = 0;
        if(MyY == 8) toY = 0;

        for(int x2 = fromX ; x2 <= toX ; x2++ ) {
            for(int y2 = fromY ; y2 <= toY ; y2++) {
                if(x2 == 0 && y2 == 0) continue;
                else if(mines[MyX+x2][MyY+y2].getState() == 0 && mines[MyX+x2][MyY+y2].Visited() == false) {
                    //tileCount++;
                    mines[MyX+x2][MyY+y2].visit();
                    checkForNonMines(MyX+x2, MyY+y2);
                }
                else if(mines[MyX+x2][MyY+y2].getState() != 0 && mines[MyX+x2][MyY+y2].Visited() == false) {
                    mines[MyX+x2][MyY+y2].visit();
                    //tileCount++;
                }
            }
        }
    }
}
