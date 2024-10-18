package com.example.asus.mines.view;


import android.content.Context;

import com.example.asus.mines.MinesActivity;
import com.example.asus.mines.model.BombCell;
import com.example.asus.mines.model.Cell;
import com.example.asus.mines.tile.Img;
import com.example.asus.mines.tile.Tile;

public abstract class CellTile implements Tile {
    boolean clicked;
    boolean flagged;
    protected static MinesActivity context;

    //protected static Context ctx;
    public static void setContext(MinesActivity c) { context = c; }

    public static Tile tileOf(Cell cell) {
        if(cell instanceof BombCell)
            return new BombTile(cell);
        else
            return new EmptyTile(cell);
    }

}
