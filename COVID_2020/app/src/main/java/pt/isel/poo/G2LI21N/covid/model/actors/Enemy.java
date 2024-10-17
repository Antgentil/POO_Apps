package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public class Enemy extends Actor {

    public static final char TYPE = 'E';

    public Enemy(int l, int c) {
        super(l,c,TYPE);
        personalDir = Dir.RIGHT;
    }

    protected Enemy(int l, int c, char type) {
       super(l,c,type);
    }

    @Override
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        Cell destination = calculateNextCell(level, from);
        if(destination == null) return false;

        if(destination.hasActor() && destination.getActor().isPlayer()){
            Player nurse = (Player) destination.getActor();
            nurse.killActor();
            return false;
        }

        return !destination.hasActor() && super.move(level, personalDir, from, destination);

    }


    @Override
    Dir calculateDirection(Level level, Cell from){
        Cell nextCell = level.calculateNextCell(from.line,from.column,personalDir);

        //  The direction is reverse in case of the following:
        //  Next cell is null
        //  Next cell has an actor that isn't the Player
        //  Next cell has an obstacle

        if (    nextCell == null ||
                nextCell.hasActor() && !nextCell.getActor().isPlayer() ||
                !nextCell.canHaveActor(personalDir, this))
            return personalDir.opposite();

        return personalDir;
    }

    @Override
    public boolean isPlayer(){
        return false;
    }


}
