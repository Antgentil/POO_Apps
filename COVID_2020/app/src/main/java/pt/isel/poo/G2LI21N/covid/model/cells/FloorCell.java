package pt.isel.poo.G2LI21N.covid.model.cells;

public class FloorCell extends PlayableCell {

    static final char TYPE = '.';

    public FloorCell(int l, int c) { super(l, c, TYPE); }

    protected FloorCell(int l, int c, char type) { super(l, c, type); }

    @Override
    public boolean isTrash() {
        return false;
    }
}
