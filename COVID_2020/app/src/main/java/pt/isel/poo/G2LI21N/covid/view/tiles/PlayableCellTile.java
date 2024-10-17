package pt.isel.poo.G2LI21N.covid.view.tiles;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import pt.isel.poo.G2LI21N.covid.R;
import pt.isel.poo.G2LI21N.covid.CovidActivity;
import pt.isel.poo.G2LI21N.covid.model.actors.Enemy;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;
import pt.isel.poo.G2LI21N.tile.Img;

public abstract class PlayableCellTile extends CellTile {


    private static final int PLAYER_RES         = R.drawable.nurse;
    private static final int PLAYER_DEAD        = R.drawable.dead;
    private static final int VIRUS_RES          = R.drawable.virus;
    private static final int BLUE_VIRUS_RES     = R.drawable.virus_blue;
    private static final int ENEMY              = R.drawable.enemy;
    private static final int PLATFORM           = R.drawable.platform;


    protected static Img playerImg, playerDeadImg, virusImg, blueVirusImg, enemyImg, platformImg;

    /**
     * Creates a new PlayableCellTile object
     * @param ctx Context
     * @param cell associated playable cell
     */
    PlayableCellTile(Context ctx, Cell cell) { super(ctx, cell); }

    /**
     * Creates a new PlayableCellTile object
     * @param ctx Context
     * @param cell associated playable cell
     */
    PlayableCellTile(Context ctx, Cell cell, int bgColor) { super(ctx, cell, bgColor); }

    {
        playerImg       = generateImage(playerImg, ctx, PLAYER_RES);
        playerDeadImg   = generateImage(playerDeadImg,ctx,PLAYER_DEAD);
        virusImg        = generateImage(virusImg, ctx, VIRUS_RES);
        blueVirusImg    = generateImage(blueVirusImg,ctx,BLUE_VIRUS_RES);
        enemyImg        = generateImage(enemyImg,ctx, ENEMY);
        platformImg     = generateImage(platformImg,ctx,PLATFORM);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        super.draw(canvas, side);
        if (!cell.hasActor())
            return;

        String actorName = cell.getActor().getClass().getSimpleName();
        try {
            PlayableCellTile.class
                    .getDeclaredMethod("draw" + actorName, Canvas.class, int.class)
                    .invoke(this, canvas, side);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.e(CovidActivity.APP_NAME, "Cannot find method to draw " + actorName);
            e.printStackTrace();
        }
    }

    // This methods are called by the draw method above.
    protected void drawPlayer(Canvas canvas, int side)      { playerImg.draw(canvas, side, side, paint); }

    protected void drawEnemy(Canvas canvas, int side)       { enemyImg.draw(canvas, side, side, paint); }

    protected void drawVirus(Canvas canvas, int side)       { virusImg.draw(canvas, side, side, paint); }

    protected void drawBlueVirus(Canvas canvas, int side)   { blueVirusImg.draw(canvas, side, side, paint); }

    protected void drawPlatform(Canvas canvas, int side)    { platformImg.draw(canvas, side, side, paint); }


}
