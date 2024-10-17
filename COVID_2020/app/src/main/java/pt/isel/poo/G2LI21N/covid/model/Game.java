package pt.isel.poo.G2LI21N.covid.model;

import java.io.*;
import java.util.Scanner;

public class Game {

    private final InputStream input;
    private int levelNumber = 0;
    private Level currLevel = null;

    // --- Methods to be use by Controller ---

    /**
     * Game constructor
     * @param levelsFile file with levels
     */
    public Game(InputStream levelsFile) {
        input = levelsFile.markSupported() ? levelsFile : new BufferedInputStream(levelsFile);
        input.mark(40*1024);
    }

    /**
     * Loads next level if possible
     * @return current level or null
     * @throws Loader.LevelFormatException
     */
    public Level loadNextLevel() throws Loader.LevelFormatException {
        Level temp = new Loader(createScanner()).load(levelNumber + 1);
        if (temp != null) {
            currLevel = temp;
            ++levelNumber;
        }
        return temp;
    }

    /**
     * Reloads current level
     */
    public void restart() {
        new Loader(createScanner()).reload(currLevel);
    }

    /**
     * Loads level
     * @param is Destination of the load operation
     * @param levelNumber Level number
     * @return The current level
     * @throws Loader.LevelFormatException
     */
    public Level load(InputStream is, int levelNumber) throws Loader.LevelFormatException {
        this.levelNumber = levelNumber;
        currLevel = new Loader(new Scanner(is)).load(levelNumber);
        return currLevel;
    }

    /**
     * Writes in bytes the current level number
     * And calls saveState() method from the class Level to
     * Write the actors and cells
     * @param os Destination of the save operation
     */
    public void saveState(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        int lw = currLevel.getWidth(), lh = currLevel.getHeight();
        pw.println("#" + levelNumber + " " + lh + " x " + lw);
        currLevel.saveState(pw);
        pw.close();
    }

    /**
     * Creates scanner
     */
    private Scanner createScanner() {
        try {
            input.reset();
            return new Scanner(input);
        } catch (IOException e) {
            throw new RuntimeException("IOException",e);
        }
    }
}
