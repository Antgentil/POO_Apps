package pt.isel.poo.G2LI21N.covid.model.cells;

public final class TrashCell extends PlayableCell {

    static final char TYPE = 'V';

    TrashCell(int l, int c) {
        super(l, c, TYPE);
    }

    @Override
    public boolean isTrash() {
        return true;
    }

}
