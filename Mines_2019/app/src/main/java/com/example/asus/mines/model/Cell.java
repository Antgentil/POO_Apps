package com.example.asus.mines.model;

public abstract class Cell {
    private boolean flagged;
    private boolean clicked;

    public Position position;
    protected Game game;

    /**
     * Constructor for each cell
     * @param pos Position of the cell
     * @param game Current game
     */
    public Cell(Position pos, Game game) {
        position = pos;
        this.game = game;
        game.createCell(this);
    }

    /**
     * Constructor for each cell
     * @param line Cell line
     * @param col Cell column
     * @param game Current game
     */
    public Cell(int line, int col, Game game) {
        this(Position.of(line,col),game);
        game.createCell(this);
    }

    /**
     * Used when 'Flag' mode is on and common to all child cells
     * @param cell Cell to be flagged
     * @param game Current game
     */
    public void canBeFlagged(Cell cell, Game game){
        if (cell.isFlagged()){
            cell.flagged = false;
            game.incrementFlagCount(1);
        }
        else if (!cell.isClicked()){
            cell.flagged = true;
            game.incrementFlagCount(-1);
        }
        game.changedCell(cell);
        game.bombUpdate(game.getBombs());
    }

    /**
     * Method to be overwritten by each child cell
     * @param cell Cell clicked
     * @param game Current game
     */
    public void canBeClicked(Cell cell, Game game) {}

    /**
     * Gets value of variable 'clicked'
     * @return True/False, indicating whether the cell
     * has been clicked on not
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Sets the cell clicked
     */
    public void setClick() { clicked = true;}

    /**
     * To help when the player wants to see where the
     * bombs are
     */
    public void reverseCellClick(){
        clicked = !clicked;
    }

    /**
     * Gets value of variable 'flagged'
     * @return True/False, indicating whether the cell
     * has been flagged on not
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Abstract method to be implemented in each child cell
     * @return True/False, indicating that the cell can or
     * can't explode
     */
    public abstract boolean canExplode();

    /**
     * Gets the position of the cell
     * @return Cell position
     */
    public Position getPosition() { return position; }

}
