package pt.isel.poo.G2LI21N.covid.view.tiles;

import android.content.Context;
import android.graphics.Color;

import pt.isel.poo.G2LI21N.tile.Img;
import pt.isel.poo.G2LI21N.covid.R;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public final class TrashCellTile extends CellTile {

    private static final int BACKGROUND_COLOR = Color.CYAN;

    private static final int RESID = R.drawable.trash;
    private static Img image;

    TrashCellTile(Context ctx, Cell cell) {
        super(ctx, cell, RESID);
        setBackgroundColor(BACKGROUND_COLOR);
        image = generateImage(image, ctx, RESID);
        setTileBackgroundImage(image);
    }

}
