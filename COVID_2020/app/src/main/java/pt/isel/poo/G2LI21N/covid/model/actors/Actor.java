package pt.isel.poo.G2LI21N.covid.model.actors;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

public abstract class Actor {

    private final char type;
    private int line;
    private int column;

    private boolean dead;
    Dir personalDir;

    Actor(int l, int c, char type) {
        this.type   = type;
        line   = l;
        column = c;
    }

    /**
     * Moves the actor into a desired cell
     * @param level Level where the actor will be moved
     * @param dir Direction to move the actor
     * @param from Where the actor is currently
     * @param to Where the actor will go
     * @return true if the actor moves, false otherwise
     */
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        Actor actor = from.getActor();
        if (to.canHaveActor(dir, actor)) {
            to.setActor(actor);
            this.setPosition(to.line, to.column);
            from.setActor(null);
            level.updateCell(from);
            level.updateCell(to);
            return true;
        }
        return false;
    }

    /**
     *
     * @param level Level where the actor will be moved
     * @param from Where the actor is currently
     * @return Cell where the actor wants go
     */
    Cell calculateNextCell(Level level, Cell from) {
        personalDir = calculateDirection(level,from);
        return level.calculateNextCell(from.line,from.column, personalDir);
    }

    /**
     * To be overwritten by child classes
     * @param level Level where the actor will be moved
     * @param from Where the actor is currently
     * @return Direction where to move next
     */
    abstract Dir calculateDirection(Level level, Cell from);

    /**
     * Checks if actor is dead
     * @return True if dead, false otherwise
     */
    public boolean isDead() { return dead; }

    /**
     * Kills current actor
     */
    void killActor() {dead = true;}


    /**
     * Method to be overwritten by all Actors
     * @return true if Actor is the Player, false otherwise
     */
    public abstract boolean isPlayer();

    /**
     * Gets the actor type
     * @return actor type
     */
    public final char getType() { return type; }

    private void setPosition(int line, int column) {this.line = line; this.column = column;}

    public int getL() { return line; }
    public int getC() { return column; }

}
