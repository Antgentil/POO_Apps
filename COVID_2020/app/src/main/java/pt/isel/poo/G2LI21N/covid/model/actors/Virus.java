package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public class Virus extends Actor {

    public static final char TYPE = '*';

    public Virus(int l, int c) {
        super(l,c,TYPE);
    }

    protected Virus(int l, int c, char type) {
       super(l,c,type);
    }

    @Override
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        if (to == null || to.hasActor()) return false;

        if (super.move(level, dir, from, to)) {
            if (to.isTrash()) {
                //level.delete(this);
                this.killActor();
                level.removeActor(to);
                level.decrementVirus();
            }

            return true;
        }

        return false;
    }

    @Override
    Dir calculateDirection(Level level, Cell from) {
        return personalDir;
    }

    @Override
    public boolean isPlayer(){
        return false;
    }


}
