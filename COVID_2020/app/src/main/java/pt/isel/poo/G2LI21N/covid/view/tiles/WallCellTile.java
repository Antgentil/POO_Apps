package pt.isel.poo.G2LI21N.covid.view.tiles;

import android.content.Context;

import pt.isel.poo.G2LI21N.covid.R;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;
import pt.isel.poo.G2LI21N.tile.Img;

public class WallCellTile extends CellTile {

    private static final int RESID = R.drawable.wall;
    private static Img image;


    WallCellTile(Context ctx, Cell cell) {
        super(ctx, cell);
        image = generateImage(image, ctx, RESID);
        setTileBackgroundImage(image);
    }

}
