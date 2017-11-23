package com.example.tycek.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Tycek on 23.11.2017.
 */

public class MineView extends View {

    Paint myPaint;
    int size;

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
        myPaint = new Paint();
        myPaint.setColor(Color.rgb(0,255,0));
        myPaint.setStrokeWidth(10);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        size = (dm.widthPixels / 10) - 2;
    }

    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.drawRect(j*size + 5, i*size + 5, (j+1)*size, (i+1)*size, myPaint);
            }
        }
    }
}
