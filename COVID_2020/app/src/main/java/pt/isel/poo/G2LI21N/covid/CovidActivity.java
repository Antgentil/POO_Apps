package pt.isel.poo.G2LI21N.covid;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import pt.isel.poo.G2LI21N.covid.model.Dir;
import pt.isel.poo.G2LI21N.covid.model.Game;
import pt.isel.poo.G2LI21N.covid.model.Level;
import pt.isel.poo.G2LI21N.covid.model.Loader;

import pt.isel.poo.G2LI21N.covid.model.actors.Player;
import pt.isel.poo.G2LI21N.covid.model.cells.Cell;

import pt.isel.poo.G2LI21N.covid.view.FieldView;
import pt.isel.poo.G2LI21N.covid.view.MessageView;
import pt.isel.poo.G2LI21N.covid.view.tiles.CellTile;
import pt.isel.poo.G2LI21N.tile.OnTileTouchListener;
import pt.isel.poo.G2LI21N.tile.TilePanel;

public class CovidActivity extends Activity {

    public static final String COVID = "COVID.txt";
    public static final String APP_NAME = "COVID";
    private final int STEP_TIME = 300;

    private int levelNumber = 1;

    private Level.Observer observer;
    private Game game;
    private Level level;
    private TilePanel tilePanel;
    private FieldView levelView;
    private FieldView virusView;

    private MessageView winMessage;
    private MessageView gameOverMessage;
    private MessageView finalMessage;

    private Button restart, saveGame, loadGame, cheatWin;
    private ImageButton left, right;

    private LinearLayout statsLayout, gameLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_covid);

        // game model
        observer = new LevelObserver();
        game = new Game(getResources().openRawResource(R.raw.covid_levels));

        // game view
        tilePanel = findViewById(R.id.tile_panel);
        //tilePanel.setListener(new TouchListener());

        // views for the current player stats
        levelView = findViewById(R.id.level_count);
        virusView = findViewById(R.id.virus_count);

        // views needed for messages
        winMessage      = findViewById(R.id.win_message);
        gameOverMessage = findViewById(R.id.game_over_message);
        finalMessage    = findViewById(R.id.final_message);
        winMessage.setOnClickListener(new LevelWinListener());
        gameOverMessage.setOnClickListener(new LevelLoseListener());
        finalMessage.setOnClickListener(v -> finish());

        statsLayout = findViewById(R.id.stats_layout);
        gameLayout = findViewById(R.id.game_layout);

        //Buttons
        restart = findViewById(R.id.restart);
        saveGame = findViewById(R.id.saveGame);
        loadGame = findViewById(R.id.loadGame);
        cheatWin = findViewById(R.id.winLevel);
        //ImageButtons
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        restart.setOnClickListener(v -> restartLevel());
        saveGame.setOnClickListener(v -> saveGame());
        loadGame.setOnClickListener(v -> loadGame());
        cheatWin.setOnClickListener(v -> winLevel());
        left.setOnClickListener(v -> buttonPressed(Dir.LEFT));
        right.setOnClickListener(v -> buttonPressed(Dir.RIGHT));

        setTimer();
        if (savedState == null) loadNextLevel();
    }

    /**
     * Sets the timer that constantly calls
     * The method gravity() from level and
     * Updates the number of viruses
     */
    private void setTimer(){
        tilePanel.setHeartbeatListener(STEP_TIME, (n,t)-> { level.gravity(); updateValues();});
    }


    /**
     * Moves the Player according to
     * Which button was pressed
     * And updates the number of viruses
     * @param dir direction pressed
     */
    private void buttonPressed (Dir dir) {
        setTimer();
        level.moveActor(level.playerNurse, dir);
        updateValues();
    }

    /**
     * Takes action when "Save" button is pressed.
     * Calls save() method from the class Game to
     * Write in the DataOutputStream the information that
     * Needs to be saved.
     */
    private void saveGame() {
        try(DataOutputStream data =  new DataOutputStream(openFileOutput(COVID, MODE_PRIVATE))){
            levelNumber = level.getNumber();
            game.saveState(data);
            Toast.makeText(this, getString(R.string.save), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes action when "Load" button is pressed.
     * Reads the needed information from the DataInputStream
     * By calling load() method of the Game class.
     * Updates the level and sets the listener to the level
     */

    private void loadGame() {
        try(DataInputStream data = new DataInputStream(openFileInput(COVID))){
            levelNumber = getLevelNumber();
            level = game.load(data, levelNumber);
            level.setObserver(observer);
            loadLevel();
            Toast.makeText(this, getString(R.string.load), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save current level
        outState.putInt("level_number", level.getNumber());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        game.saveState(bos);
        outState.putByteArray("level", bos.toByteArray());

        // save messages status
        outState.putBoolean("message_win", winMessage.isActive());
        outState.putBoolean("message_gameOver", gameOverMessage.isActive());
        outState.putBoolean("message_final", finalMessage.isActive());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        try {

            int levelNumber = savedState.getInt("level_number", 1);
            ByteArrayInputStream is = new ByteArrayInputStream(savedState.getByteArray("level"));
            level = game.load(is, levelNumber);
            level.setObserver(observer);

            // load message or level if message is not available
            if (savedState.getBoolean("message_win"))
                {showMessage(winMessage); loadLevel();}
            else if (savedState.getBoolean("message_gameOver"))
                showMessage(gameOverMessage);
            else if (savedState.getBoolean("message_final"))
                showMessage(finalMessage);
            else
                loadLevel();
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the correct level number
     * Stored in file 'COVID.txt'
     */
    private int getLevelNumber(){
        int value = 0;
        String line;
        Scanner levelData;
        try {
            DataInputStream data = new DataInputStream(openFileInput(COVID));
            levelData = new Scanner(data);
            line = levelData.nextLine();
            value = line.charAt(1) - '0';
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Loads the next level, if available
     */
    private boolean loadNextLevel() {
        try {
            Level temp = game.loadNextLevel();
            if (temp == null) {
                showMessage(finalMessage); return false;
            }
            level = temp;
            level.setObserver(observer);
            loadLevel();
            return true;
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Loads the selected level
     */
    private void loadLevel() {
        if (level == null) return;

        int lw = level.getWidth(), lh = level.getHeight();
        tilePanel.setSize(lw, lh);
        for (int y = 0; y < lh; ++y) {
            for (int x = 0; x < lw; ++x) {
                Cell cell = level.getCell(y, x);
                tilePanel.setTile(x, y, CellTile.tileOf(this, cell));
            }
        }
        updateValues();
    }

    /**
     * Restarts the current level
     */
    private void restartLevel() {
        enableButtons(true);
        game.restart();
        loadLevel();
        setTimer();
        Toast.makeText(this, getString(R.string.restart), Toast.LENGTH_SHORT).show();
        //tilePanel.removeHeartbeatListener();
    }


    /**
     * Automatic win
     */
    public void winLevel() {level.levelWon(); }

    /**
     * Update the game values
     */
    private void updateValues() {
        levelView.setValue(level.getNumber());
        virusView.setValue(level.getRemainingVirus());
    }

    /**
     * Shows a message, hiding the game and stats panels
     * @param msg Message to show
     */
    private void showMessage(MessageView msg) {
        statsLayout.setVisibility(View.GONE);
        gameLayout.setVisibility(View.GONE);
        msg.setVisibility(View.VISIBLE);
    }

    /**
     * Hides a message, showing the game and stats panels again
     * @param msg Message to hide
     */
    private void hideMessage(MessageView msg) {
        statsLayout.setVisibility(View.VISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        msg.setVisibility(View.GONE);
    }


    /**
     * Disables or enables the buttons in
     * Case level is lost or won
     * @param ans boolean: true-enable/false-disable
     */
    private void enableButtons (boolean ans) {
        restart.setEnabled(ans);
        saveGame.setEnabled(ans);
        loadGame.setEnabled(ans);
        cheatWin.setEnabled(ans);
        left.setEnabled(ans);
        right.setEnabled(ans);
    }

    /**
     * Shows the game over message
     * And kills the timer so it doesn't
     * Repeat itself
     */
    private void loadingRestart(){
        showMessage(gameOverMessage);
        tilePanel.removeHeartbeatListener();
    }


    /**
     * Handles level-related events
     */
    private class LevelObserver implements Level.Observer {

        @Override
        public void onCellUpdated(Cell cell) { tilePanel.invalidate(cell.column, cell.line); }

        @Override
        public void onPlayerDead(Player player, Cell cell) {
            enableButtons(false);
            onCellUpdated(cell);
            tilePanel.setHeartbeatListener(1000, (n,t)-> loadingRestart());
        }

        @Override
        public void onLevelWin() {
            if (loadNextLevel()) showMessage(winMessage);
            //tilePanel.removeHeartbeatListener();
        }

    }


    /**
     * Handles when a player wins the level
     * And clicks the button to proceed
     */
    private class LevelWinListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { hideMessage(winMessage); }
    }


    /**
     * Handles when a player loses a level
     * And clicks the button to restart
     */
    private class LevelLoseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { hideMessage(gameOverMessage); restartLevel(); }

    }






         /*
    private boolean movePlayer (int xFrom, int yFrom, int xTo, int yTo) {
        Dir dir = calculateDirection(xFrom, yFrom, xTo, yTo);
        if (dir != null) {
            if (level.moveMan(dir)) {
                updateValues();
                return true;
            }
        }
        return false;
    }
    */


    /*
    private static Dir calculateDirection(int xFrom, int yFrom, int xTo, int yTo) throws IllegalArgumentException {
        int difX = xTo - xFrom, difY = yTo - yFrom;
        return Dir.fromVector(difX, difY);
    }
*/

    /*
    private class TouchListener implements OnTileTouchListener {
        @Override
        public boolean onClick(int xTile, int yTile) {
            Cell cell = level.getCell(yTile, xTile);
            Cell from = level.getPlayerCell();
            if (movePlayer(from.column, from.line, cell.column, cell.line)) {
                tilePanel.invalidate(xTile, yTile);
                return true;
            }

            return false;
        }

        @Override
        public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
            movePlayer(xFrom, yFrom, xTo, yTo);
            return false;
        }

        @Override
        public void onDragEnd(int x, int y) { }

        @Override
        public void onDragCancel() { }

    }
*/

}
