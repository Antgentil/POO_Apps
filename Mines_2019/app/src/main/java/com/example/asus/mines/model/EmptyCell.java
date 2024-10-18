package com.example.asus.mines.model;

public class EmptyCell extends Cell {
    private int adjacentBombCount;

    EmptyCell(int line, int col, Game game, int count) {
        super(line, col, game);
        this.adjacentBombCount = count;
    }

    /**
     * Process for when a cell without bomb is clicked
     * @param cell Cell clicked
     * @param game Current game
     */
    @Override
    public void canBeClicked(Cell cell, Game game) {
        EmptyCell empty = (EmptyCell) cell;
        if (empty.isFlagged() || empty.isClicked()) return;
        if (empty.getCount() > 0)
            updateStatus(empty, game);
        else {
            updateStatus(empty, game);
            floodFill(cell.position.line, cell.position.col, game);
        }
    }

    /**
     * Updates the game and the cell
     * @param cell Cell to be updated
     * @param game Current game
     */
    private void updateStatus(Cell cell, Game game){
        game.countDownToVictory();
        cell.setClick();
        game.changedCell(cell);
    }

    /**
     * Used to tell if the cell is a bomb or not
     * @return True/False, indicating that the cell can
     * or can't explode
     */
    @Override
    public boolean canExplode(){return false;}

    /**
     * Gets the value of variable 'adjacentBombCount'
     * @return Number of bomb cells adjacent to the cell
     */
    public int getCount() { return adjacentBombCount;}

    /**
     * Recursive method used to automatically click on every
     * adjacent cell of an EmptyCell where 'adjacentBombCount'
     * is zero (0)
     * @param l Cell line
     * @param c Cell column
     * @param game Current game
     */
    private void floodFill(int l, int c, Game game){
        Cell next;
        if (l > 0)  {
            next = game.arena.get(l-1,c);
            next.canBeClicked(next, game);
        }
        if (l > 0 && c > 0) {
            next = game.arena.get(l-1,c-1);
            next.canBeClicked(next, game);
        }
        if (c > 0){
            next = game.arena.get(l,c-1);
            next.canBeClicked(next, game);
        }
        if (l < game.getLines() - 1) {
            next = game.arena.get(l+1,c);
            next.canBeClicked(next, game);
        }
        if (c < game.getCols() - 1)  {
            next = game.arena.get(l,c+1);
            next.canBeClicked(next, game);
        }
        if (l < game.getLines() - 1 && c < game.getCols() - 1){
            next = game.arena.get(l+1,c+1);
            next.canBeClicked(next, game);
        }
        if (l > 0 && c < game.getCols()-1){
            next = game.arena.get(l-1,c+1);
            next.canBeClicked(next, game);
        }
        if (l < game.getLines() - 1 && c > 0){
            next = game.arena.get(l+1,c-1);
            next.canBeClicked(next, game);
        }
    }


    /**
     * Used to update the 'adjacentBombCount' of the cells
     * that have bombs as neighbour cells
     * @param game Current game
     */
    static void setAdjacentCells(Game game){
        int line, col;
        int linesLimit = game.getLines()-1, colsLimit = game.getCols()-1;

        for(BombCell bomb : game.bombList) {
            int bombLine = bomb.position.line;
            int bombCol  = bomb.position.col;

            if (bombCol > 0){
                line = bombLine;
                col  = bombCol - 1;
                incrementCount(line, col, game);
            }
            if (bombCol < colsLimit){
                line = bombLine;
                col  = bombCol + 1;
                incrementCount(line, col, game);
            }
            if (bombLine < linesLimit){
                line = bombLine+1;
                col  = bombCol;
                incrementCount(line, col, game);
            }
            if (bombLine > 0){
                line = bombLine-1;
                col  = bombCol;
                incrementCount(line, col, game);
            }
            if (bombLine > 0 && bombCol > 0){
                line = bombLine-1;
                col  = bombCol-1;
                incrementCount(line, col, game);
            }
            if (bombLine > 0 && bombCol < colsLimit){
                line = bombLine-1;
                col  = bombCol+1;
                incrementCount(line, col, game);
            }
            if (bombLine < linesLimit && bombCol < colsLimit){
                line = bombLine+1;
                col  = bombCol+1;
                incrementCount(line, col, game);
            }
            if (bombLine < linesLimit && bombCol > 0){
                line = bombLine+1;
                col  = bombCol-1;
                incrementCount(line, col, game);
            }
        }
    }

    /**
     * Method called to help 'setAdjacentCells' method in
     * incrementing the count for each EmptyCell
     * @param l Cell line
     * @param c Cell column
     * @param game Current game
     */
    private static void incrementCount(int l, int c, Game game){
        Cell adjacent = game.arena.get(l, c);
        if (!adjacent.canExplode()) {
            EmptyCell empty = (EmptyCell) adjacent;
            ++empty.adjacentBombCount;}
    }

}
