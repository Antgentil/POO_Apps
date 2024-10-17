package pt.isel.poo.G2LI21N.covid.model.cells;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.actors.Actor;


public abstract class Cell {

    private final char type;
    public final int line;
    public final int column;

    private Actor actor;

    /**
     * Constructor for each cell
     * @param l line where the cell is
     * @param c column where the cell is
     */
    public Cell(int l, int c, char type) {
        line = l;
        column = c;
        this.type = type;
    }

    /**
     * Moves the actor to this cell if the action
     * is allowed by the cell
     * @param dir Direction that the actor is taking
     * @param actor Actor to move
     * @return true if allowed to move, false otherwise
     */
    public boolean canHaveActor(Dir dir, Actor actor) { return false; }

    /**
     * Sets the actor
     * @param actor Actor to be set
     */
    public final void setActor(Actor actor) { this.actor = actor; }

    /**
     * Gets the current actor
     * @return current actor, null if none
     */
    public final Actor getActor() {
        return actor;
    }

    /**
     * Checks if there's an actor
     * @return true if there is one, false otherwise
     */
    public final boolean hasActor() { return actor != null; }

    /**
     * Gets the cell type
     * @return type of the cell
     */
    public final char getType() { return type; }

    /**
     * Checks if cell is Trash
     * @return true if cell is Trash, false otherwise
     */
    public abstract boolean isTrash();

    /**
     * Transforms a cell type into a Cell-like object
     * @param l Line of the cell
     * @param c Column of the cell
     * @param type Type of the cell
     * @return Cell-like object or null if type is invalid
     */
    public static Cell getCellByType(int l, int c, char type) {
        switch (type) {
            case FloorCell.TYPE:
                return new FloorCell(l, c);
            case WallCell.TYPE:
                return new WallCell(l, c);
            case TrashCell.TYPE:
                return new TrashCell(l, c);
            default:
                return null;
        }
    }

}
