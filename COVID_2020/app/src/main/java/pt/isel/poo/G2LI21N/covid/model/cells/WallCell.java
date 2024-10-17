package pt.isel.poo.G2LI21N.covid.model.cells;

public final class WallCell extends Cell {

    static final char TYPE = 'X';

    WallCell(int l, int c) {
        super(l, c, TYPE);
    }

    @Override
    public boolean isTrash() {
        return false;
    }


}
