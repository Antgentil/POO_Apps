package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public final class BlueVirus extends Virus {

    public static final char TYPE = 'B';

    public BlueVirus(int l, int c) { super(l,c,TYPE); }

    @Override
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        if (to != null && to.hasActor() && to.getActor().getType() == TYPE) {
            // Since the next cell contains another BlueVirus
            // We need to calculate the cell after the last
            // BlueVirus in that line or column. There can be
            // Multiple BlueVirus together
            Cell finalCell = calculateFinalCell(level,to,dir);
            // If that last cell doesn't have an actor and isn't null or wall
            // We call 'move' from parent class
            if (finalCell != null && !finalCell.hasActor())
                return super.move(level, dir, from, finalCell);
        }

        return super.move(level, dir, from, to);
    }

    private Cell calculateFinalCell(Level level, Cell firstBlue, Dir dir){
        Cell finalCell = firstBlue;
        do {
            finalCell = level.getCell(finalCell.line + dir.dl, finalCell.column + dir.dc);

        } while(finalCell != null && finalCell.hasActor() && finalCell.getActor().getType() == TYPE);

        return finalCell;
    }

}
