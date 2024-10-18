package com.example.asus.mines.view;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.asus.mines.R;
import com.example.asus.mines.model.Cell;
import com.example.asus.mines.model.EmptyCell;
import com.example.asus.mines.tile.Img;


import static android.graphics.Color.*;

public class EmptyTile extends CellTile {
    private EmptyCell empty;
    private int adjacentBombCount;
    Paint paint=new Paint();
    Img img;
    private static final String TAG = "MyActivity";

    public EmptyTile(Cell cell) {
        empty = (EmptyCell) cell;
        this.adjacentBombCount = empty.getCount();
        this.clicked = empty.isClicked();
        this.flagged = empty.isFlagged();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        if(!clicked&&!flagged){
            paint.setColor(GRAY);
            //canvas.drawRect((float)(side) ,(float)(side), (float)(side), (float)(side), paint);
            canvas.drawRect((float)(side/200) ,(float)(side), (float)(side), (float)(side/200), paint);
        }
        if(clicked){
            // Log.v(TAG, "line "+side+"  adjacentBombCount :"+adjacentBombCount +"********** *********************");
            //paint.setColor(Color.BLUE);

            switch(adjacentBombCount){
                case 1:
                    paint.setColor(0xFF1ff0ff); //LIGHT_BLUE
                    break;
                case 2:
                    paint.setColor(GREEN);
                    break;
                case 3:
                    paint.setColor(RED);
                    break;
                case 4:
                    paint.setColor(BLUE);
                    break;
                case 5:
                    paint.setColor(MAGENTA);
                    break;
                case 6:
                    paint.setColor(BLACK);
                    break;
                case 7:
                    paint.setColor(0xFFffb700); // TOAST_ORANGE
                    break;
                case 8:
                    paint.setColor(0xFF0d820d); // DARK_GREEN
                    break;
                default:
                    paint.setColor(BLACK);
                    break;
            }

            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            if(adjacentBombCount!=0)
                canvas.drawText(""+adjacentBombCount,side/2,(side/2)+15,paint);
        }
        if(flagged){
            img=new Img(context,R.drawable.flag);
            paint.setColor(GRAY);
            canvas.drawRect((float)(side/200) ,(float)(side), (float)(side), (float)(side/200), paint);
            img.draw(canvas,side,side,paint);
        }
    }
    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}



