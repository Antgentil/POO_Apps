package pt.isel.poo.G2LI21N.covid.view.tiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import pt.isel.poo.G2LI21N.covid.model.actors.Player;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public class FloorCellTile extends PlayableCellTile {

    private static final int BACKGROUND_COLOR = Color.CYAN;

    FloorCellTile(Context ctx, Cell cell) { super(ctx, cell, BACKGROUND_COLOR); }


    @Override
    protected void drawPlayer(Canvas canvas, int side) {
        playerImg.draw(canvas, side, side, paint);
        if( ( (Player) cell.getActor() ).isDead() )
            playerDeadImg.draw(canvas, side, side, paint);
    }
}
