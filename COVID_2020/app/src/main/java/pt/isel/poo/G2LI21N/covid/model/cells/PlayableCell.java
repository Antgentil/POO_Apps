package pt.isel.poo.G2LI21N.covid.model.cells;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.actors.Actor;

public abstract class PlayableCell extends Cell {

    PlayableCell (int l, int c, char type) {
        super(l, c, type);
    }

    @Override
    public boolean canHaveActor(Dir dir, Actor actor) { return true; }


}
