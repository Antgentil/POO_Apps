package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public class Platform extends Actor {

    public static final char TYPE = 'P';

    public Platform(int l, int c) {
        super(l,c,TYPE);
        personalDir = Dir.DOWN;
    }

    protected Platform(int l, int c, char type) {
       super(l,c,type);
    }

    @Override
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        Cell destination = calculateNextCell(level, from);
        if (destination == null) return false;

        if (destination.hasActor() && destination.getActor().isPlayer()) {
            Player player = (Player) destination.getActor();
            Cell thirdCell = level.getCell(destination.line + personalDir.dl, destination.column + personalDir.dc);
            // See if next Actor can be pushed or not
            if (player.move(level, personalDir, destination, thirdCell))
                return super.move(level, personalDir, from, destination);

        }

        // If next cell doesn't have an actor and platform is not in its
        // Basic position (bottom of the board)
        if (!destination.hasActor() && from.line < level.getHeight()-1) {
            return super.move(level, personalDir, from, destination);
        }

        return false;
    }

    @Override
    Dir calculateDirection(Level level, Cell from){
        Cell nextCell = level.calculateNextCell(from.line,from.column,personalDir);

        //  The direction is reverse in case of the following:
        //  Next cell is null
        //  Next cell has an actor that isn't the Player
        //  Next cell is an obstacle
        //  Next cell is the first line from the top and the Player has left the platform
        //  Player has left the platform midway through and platform returns down

        if (    nextCell == null ||
                nextCell.hasActor() && !nextCell.getActor().isPlayer() ||
                !nextCell.canHaveActor(personalDir, this)  ||
                nextCell.line == 0 && !(level.getPlayer().onPlatform(level)) ||
                nextCell.line != level.getHeight()-2 && personalDir != Dir.DOWN  && !(level.getPlayer().onPlatform(level)))
            return personalDir.opposite();

        return personalDir;
    }


    @Override
    public boolean isPlayer(){
        return false;
    }

}
