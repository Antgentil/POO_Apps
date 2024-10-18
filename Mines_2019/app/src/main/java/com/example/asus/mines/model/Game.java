package com.example.asus.mines.model;
import com.example.asus.mines.MinesActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;


public class Game implements Iterable<Cell> {
    private int maxBombs =25;
    private int notBombs;
    BombCell[] bombList = new BombCell[maxBombs];
    public Arena arena;
    public boolean bombClicked = false;
    private GameListener observer;

    /**
     * Constructor for the game
     *
     * @param lines Line limit
     * @param cols  Col limit
     */
    public Game(int lines, int cols) {
        Position.init(lines, cols);
        arena = new Arena(lines, cols);
        notBombs = lines * cols - maxBombs;
    }

    /**
     * Initializes the game
     *
     * @param levelNumber Level number is always 1
     */
    public void startLevel(int levelNumber) {
        arena.clear();
        BombCell bomb;
        for (int i = 0; i < maxBombs * levelNumber; i++) {
            bomb = new BombCell(arena.randomFreePosition(), this);
            bombList[i] = bomb;
        }
        getStarted();
    }

    /**
     * Initialises all cells that aren't bombs/mines as EmptyCells,
     * and sets them up.
     */
    private void getStarted() {
        for (int i = 0; i < getLines(); i++)
            for (int j = 0; j < getCols(); j++)
                if (arena.get(i, j) == null)
                    arena.map[i][j] = new EmptyCell(i, j, this, 0);

        EmptyCell.setAdjacentCells(this);
        firstPlay();
    }

    /**
     * Used to randomly click on a cell without bomb and without
     * bombs on any of its adjacent cells
     */
    private void firstPlay() {
        int c = Position.random().col;
        int l = Position.random().line;

        Cell firstMove = arena.get(l, c);
        if (!firstMove.canExplode()) {
            EmptyCell first = (EmptyCell) firstMove;
            if (first.getCount() == 0)
                first.canBeClicked(firstMove, this);
            else firstPlay();
        } else firstPlay();
    }

    /**
     * Gets lines limit
     *
     * @return Lines limit
     */
    public int getLines() {
        return Position.getLines();
    }

    /**
     * Gets col limit
     *
     * @return Cols limit
     */
    public int getCols() {
        return Position.getCols();
    }

    /**
     * Gets number of bombs left to be found by the player.
     *
     * @return Number of bombs
     */
    public int getBombs() {
        return maxBombs;
    }

    @Override
    public Iterator<Cell> iterator() {
        return arena.iterator();
    }

    /**
     * Verifies if all cells that aren't bombs have been
     * clicked by the player or if a bomb as exploded, thus ending the game.
     *
     * @return True/False is game is/isn't over
     */
    public boolean isFinish() {
        return (notBombs == 0 || bombClicked);
    }

    private LinkedList<GameListener> listeners = new LinkedList<>();

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    void createCell(Cell cell) {
        arena.set(cell);
        for (GameListener listener : listeners)
            listener.cellCreated(cell);
    }

    void changedCell(Cell cell) {
        for (GameListener listener : listeners)
            listener.cellChanged(cell);
    }

    void bombUpdate(int MAX_BOMBS) {
        for (GameListener listener : listeners)
            listener.updateBombs(MAX_BOMBS);
    }

    public void addFlag(Position pos) {
        int l = pos.line;
        int c = pos.col;
        Cell cell = arena.get(l,c);
        cell.canBeFlagged(cell,this);
    }

    /**
     * Updates the remaining bombs left to find for the game
     *
     * @param value Increment/Decrement bombs found/flags used
     */
    void incrementFlagCount(int value) {
        maxBombs += value;
    }


    /**
     * Used to know whether the player won or lost
     *
     * @return True/False if the player wins/loses
     */
    public boolean victory() {
        return notBombs == 0;
    }

    /**
     * Used each time a cell without bomb is clicked/cleared,
     * decrementing the variable 'notBombs', thus creating a
     * count down until the game is won.
     */
    public void processClick(Position pos) {
        Cell cell = arena.get(pos.line, pos.col);
        cell.canBeClicked(cell,this);
        //System.out.println("NOT BOMBS = " + notBombs);
    }
    void countDownToVictory() {
        --notBombs;
    }

    public void showBoard() {
        for (int i = 0; i < getLines(); i++)
            for (int j = 0; j < getCols(); j++) {
                Cell cell = arena.get(i, j);
                if (cell.canExplode()) cell.reverseCellClick();
                if (isFinish()) cell.setClick();
                changedCell(cell);
            }
    }


    public void setObserver(GameListener listener) {
        observer = listener ;
    }


    public void save(DataOutputStream dos) throws IOException {
        for(Cell a : arena) {
            dos.writeUTF(a.getClass().getSimpleName());
            a.position.save(dos);
        }
        dos.writeUTF("END");
    }

    public void load(DataInputStream dis) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for(;;) {
            String name = dis.readUTF();
            if (name.equals("END")) break;
            Class cls = Class.forName("com.example.asus.mines.model."+name);
            Cell a = (Cell) cls.newInstance();
            a.game = this;
            a.position.load(dis);
            createCell(a);
            //if (a instanceof Person)
               // hero = (Person) a;
        }
    }
}




