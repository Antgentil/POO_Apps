package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public final class Player extends Actor {

    public static final char TYPE = '@';

    public Player(int l, int c) {super(l,c,TYPE);}

    @Override
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        if (to == null) return false;
        if (to.hasActor() && to.getActor().getType() != Enemy.TYPE) {
            Actor toActor = to.getActor();
            Cell other = level.getCell(to.line + dir.dl, to.column + dir.dc);
            // See if next Actor can be pushed or not
            if (!toActor.move(level, dir, to, other)) return false;
        }
        if(to.isTrash() || to.hasActor() && to.getActor().getType() == Enemy.TYPE){
            this.killActor();
            return false;
        }
        if (!to.isTrash() && super.move(level, dir, from, to)) {
            level.updatePlayerCell(to);
            return true;
        }

        return false;
    }

    @Override
    Dir calculateDirection(Level level, Cell from) {
        return personalDir;
    }

    /**
     * Checks whether the Player is on top
     * Of a platform or not
     * @param level Level where the Player is
     * @return True if Player is on a platform, false otherwise
     */
    public boolean onPlatform (Level level) {
        Cell player = level.getPlayerCell();
        Cell below = level.calculateNextCell(player.line,player.column,Dir.DOWN);
        if (below == null) return false;
        return below.hasActor() && below.getActor().getType() == Platform.TYPE;
    }

    @Override
    public boolean isPlayer(){
        return true;
    }

}
