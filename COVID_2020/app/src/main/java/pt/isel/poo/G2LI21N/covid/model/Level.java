package pt.isel.poo.G2LI21N.covid.model;

import java.io.PrintWriter;
import java.util.ArrayList;

import pt.isel.poo.G2LI21N.covid.model.actors.*;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;
import pt.isel.poo.G2LI21N.covid.model.cells.FloorCell;

public class Level {

    private final int levelNumber;
    private final int height;
    private final int width;

    private Cell nurseCell;
    public Player playerNurse;
    //private ArrayList<Cell> actorsCell;
    private Actor [] actors;
    private Cell[][] board;
    private Observer observer;
    private int virus;
    private int numActors = 0;
    private boolean manIsDead, levelOver;


    /**
     * Constructor for each level
     * @param levelNumber Level number
     * @param height Height of the level
     * @param width Width of the level
     */
    Level(int levelNumber, int height, int width) {
        this.levelNumber    = levelNumber;
        this.height         = height;
        this.width          = width;
        board               = new Cell[height][width];
    }

    /**
     * Resets the current level
     */
    void reset() {
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j) board[j][i] = null;

        virus = 0;
        numActors = 0;
        manIsDead = false;
        levelOver = false;
        nurseCell = null;
    }


    /**
     * Puts a cell or an actor on the given position
     * @param l Line
     * @param c Column
     * @param type Type of the box/actor
     */
    void put (int l, int c, char type) {
        Cell cell = Cell.getCellByType(l, c, type);
        // if the cell == null then the type corresponds to an actor
        if (cell == null) {
            cell = board[l][c];
            if (cell == null) {
                cell = new FloorCell(l, c);
                board[l][c] = cell;
            }

            Actor actor = null;
            switch (type) {
                case Player.TYPE:
                    actor = new Player(l,c);
                    playerNurse = (Player) actor;
                    nurseCell = cell;
                    ++numActors;
                    break;

                case Virus.TYPE:
                    actor = new Virus(l,c);
                    addVirus();
                    ++numActors;
                    break;

                case BlueVirus.TYPE:
                    actor = new BlueVirus(l,c);
                    addVirus();
                    ++numActors;
                    break;

                case Enemy.TYPE:
                    actor = new Enemy(l,c);
                    ++numActors;
                    break;

                case Platform.TYPE:
                    actor = new Platform(l,c);
                    ++numActors;
                    break;
            }

            cell.setActor(actor);
            return;
        }

        if (board[l][c] == null) board[l][c] = cell;
    }

    /**
     * Creates an array with all the Actors of the game
     */
    void listOfActors(){
        actors = new Actor [numActors];
        int pos = 0;
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                if (board[j][i].hasActor()) actors [pos++] = board[j][i].getActor();
    }

//=============================================================================================================


    public void gravity() {
        Dir fall = Dir.DOWN;
        for(Actor actor: actors) {
            if (levelOver) return;
            // There is no need to make the Player fall
            // If they are on top of a platform
            if (actor.isPlayer()) {
                Player player = (Player) actor;
                if (!player.onPlatform(this)) moveActor(player, fall);
            }
            else moveActor(actor, fall);
        }
    }

    /**
     * Moves the actors of the game
     * @param actor Actor that is called to move
     * @param dir Direction where to go
     */
    public void moveActor(Actor actor, Dir dir) {
        if (actor == null || dir == null || actor.isDead()) return;
        Cell actorCell = getCell(actor.getL(), actor.getC());
        if (actorCell == null) return;

        Cell nextCell = calculateNextCell(actorCell.line, actorCell.column, dir);
        //if (nextCell == null) return;

        actor.move(this, dir, actorCell, nextCell);
        updateObserver();
    }


//=============================================================================================================

   /**
     * Removes actor from a cell
     * @param cell Cell to remove the actor
     */
    public void removeActor(Cell cell) {
        if (cell.hasActor()) {
            cell.setActor(null);
            updateCell(cell);
        }
    }

    /**
     * Updates game status
     */
    private void updateObserver(){
        manIsDead = playerNurse.isDead();
        if (manIsDead) {
            levelOver = true;
            observer.onPlayerDead (playerNurse, getPlayerCell());
        }
        else if (virus == 0)
            levelWon();
    }

    private void addVirus() { ++virus; }

    public void updateCell(Cell cell) { observer.onCellUpdated(cell); }

    public void levelWon() {levelOver = true; observer.onLevelWin();}

    public void decrementVirus() { --virus; }

    public void setObserver(Observer observer) { this.observer = observer; }

    public int getNumber() { return levelNumber; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getRemainingVirus() { return virus; }

    public Cell getPlayerCell() { return nurseCell; }

    public void updatePlayerCell(Cell to) {
        if (to != null) nurseCell = to;
    }

    public Cell calculateNextCell(int line, int col, Dir dir){
        int nextL = line + dir.dl;
        int nextC = col + dir.dc;
        return getCell(nextL, nextC);
    }

    public Cell getCell(int l, int c) {
        if (c >= width || c < 0 || l >= height || l < 0) return null;
        return board[l][c];
    }

    public Player getPlayer(){
        return playerNurse;
    }


    /**
     * Saves this level current state into a PrintWriter
     * The level is saved in the same fashion Loader works
     * @param out PrintWriter to save the level on
     */
    void saveState(PrintWriter out) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Cell cell = board[y][x];
                char type = cell.getType();
                if (cell.hasActor())
                    out.print(cell.getActor().getType());
                else
                    out.print(type);
            }
            out.println();
        }
    }


    public interface Observer {

        /**
         * Called when a cell gets updated
         * e.g an actor moves into it
         * @param cell Cell that is updated
         */
        void onCellUpdated(Cell cell);

        /**
         * Called when a player gets killed
         * @param player Player that was killed
         * @param cell Cell where Player was killed
         */
        void onPlayerDead(Player player, Cell cell);

        /**
         * Called when a level is won
         */
        void onLevelWin();
    }

}
