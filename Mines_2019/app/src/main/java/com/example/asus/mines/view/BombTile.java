package com.example.asus.mines.view;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.asus.mines.R;
import com.example.asus.mines.model.BombCell;
import com.example.asus.mines.model.Cell;
import com.example.asus.mines.tile.Img;

import static android.graphics.Color.GRAY;

public class BombTile extends CellTile {
    private BombCell bomb;
    private boolean explode;
    Paint paint=new Paint();
    Img img;

    private static Img bombON   = new Img(context,R.drawable.bombx);
    private static Img bombOFF  = new Img(context,R.drawable.bomb);

    public BombTile(Cell cell) {
        bomb = (BombCell) cell;
        this.clicked = bomb.isClicked();
        this.flagged = bomb.isFlagged();
        this.explode = bomb.hasExploded();
        if(explode)
            img= bombON;
        else
            img = bombOFF;
    }

    @Override
    public void draw(Canvas canvas, int side) {

        if(!clicked&&!flagged){
            paint.setColor(GRAY);
            //canvas.drawRect((float)(side) ,(float)(side), (float)(side), (float)(side), paint);
            canvas.drawRect((float)(side/200) ,(float)(side), (float)(side), (float)(side/200), paint);
        }
        if(clicked){
            img.draw(canvas,side,side,paint);
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
