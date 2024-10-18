package com.example.asus.mines.model;

import android.util.Log;

import com.example.asus.mines.tile.Img;

public class BombCell extends Cell  {
    private boolean explode;
    private static final String TAG = "MyActivity";

    BombCell(Position position, Game game) {
        super(position.line,position.col, game);
    }


    /**
     * Process for when a cell with bomb is clicked
     * @param cell Cell clicked
     * @param game Current game
     */
    @Override
    public void canBeClicked(Cell cell, Game game) {
        BombCell bomb = (BombCell)cell;
        if (bomb.isFlagged()) return;
        else {
            game.bombClicked = true;
            bomb.explode = true;
        }
        game.changedCell(bomb);
    }


    /**
     * Used to tell if the cell is a bomb or not
     * @return True/False, indicating that the cell can
     * or can't explode
     */
    @Override
    public boolean canExplode(){return true;}

    /**
     * Used to update status of the bomb that was clicked on
     * @return True/False, indicating that the bomb has/hasn't
     * exploded
     */
    public boolean hasExploded() { return explode; }

}
