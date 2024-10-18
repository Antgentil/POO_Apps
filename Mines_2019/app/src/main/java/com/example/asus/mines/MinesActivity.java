package com.example.asus.mines;

import android.app.Activity;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.asus.mines.model.Arena;
import com.example.asus.mines.model.BombCell;
import com.example.asus.mines.model.Cell;
import com.example.asus.mines.model.EmptyCell;
import com.example.asus.mines.model.Game;
import com.example.asus.mines.model.GameListener;
import com.example.asus.mines.model.Position;
import com.example.asus.mines.tile.OnBeatListener;
import com.example.asus.mines.tile.OnTileTouchListener;
import com.example.asus.mines.tile.TilePanel;
import com.example.asus.mines.view.BombTile;
import com.example.asus.mines.view.CellTile;
import com.example.asus.mines.view.EmptyTile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MinesActivity extends Activity implements OnTileTouchListener, OnBeatListener {
    private static final int LINES = 12, COLS = 12;

    private TilePanel view;
    private TextView numBombView,GameMsg;
    private Button save,load,newGame;
    private Switch sw;
    private Game model;
    private int levelNumber=1;
    private Updater observer;
    private static final String TAG = "MyActivity";
    private Updater updater = new Updater();
    private boolean  flagOn=false;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_mines);
        //CellTile.context = this;
        CellTile.setContext(this);
        view            = findViewById(R.id.panel);
        newGame         = findViewById(R.id.newGame);
        save            = findViewById(R.id.save);
        load            = findViewById(R.id.load);
        GameMsg         = findViewById(R.id.GameMsg);
        numBombView     = findViewById(R.id.numBombs);

        view.setListener(this);
        view.setHeartbeatListener(500 ,this);
        model = new Game(LINES,COLS);

        newGame.setOnClickListener( v-> onClickNewGame() );
        save.setOnClickListener(v -> onClickSave());
        load.setOnClickListener(v -> onClickLoad());
        run();
    }

    private void onClickLoad() {

    }
    private void onClickSave() {
    }

    public void onClickNewGame() {
        //Log.i(TAG, "New Game!");
        model.bombClicked = false;
        refresh();
        run();
    }

    private void run(){
        newGame.setEnabled(true);
        save.setEnabled(true);
        load.setEnabled(true);
        model.startLevel(levelNumber);
        play();
    }
    public void play(){
        refresh();
        model.setObserver(updater);
        numBombView.setText(Integer.toString(model.getBombs()));
        GameMsg.setVisibility(View.INVISIBLE);
        newGame.setVisibility(View.INVISIBLE);
        //model.showBoard();
    }

    public void refresh(){

    view.setSize(LINES,COLS);
    for(int l=0;l<12;l++)
        for(int c=0;c<12;c++){
           Cell cell=model.arena.get(l,c);
           if(cell instanceof BombCell)
               view.setTile(l,c,new BombTile(cell));
           else
               view.setTile(l,c,new EmptyTile(cell));
        }
        numBombView.setText(Integer.toString(model.getBombs()));

    }

    private class Updater implements GameListener {
        @Override
        public void cellRemoved(Position pos){
            view.getTile(pos.line,pos.col);
        }
        @Override
        public void cellCreated(Cell cell){
            Position pos=cell.getPosition();
               if(cell instanceof BombCell)
                  view.setTile(pos.line,pos.col,new BombTile(cell));
               if(cell instanceof EmptyCell)
                   view.setTile(pos.line,pos.col,new EmptyTile(cell));
        }
        @Override
        public void cellChanged(Cell cell){
            cellCreated(cell);
        }
        @Override
        public void updateBombs(int MAX_BOMBS){
            numBombView.setText(Integer.toString(model.getBombs()));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        ByteArrayOutputStream baos;
        try (DataOutputStream dos =
                     new DataOutputStream(baos=
                             new ByteArrayOutputStream())) {
            state.putInt("LEVEL",levelNumber);
            model.save(dos);
        } catch (IOException e) {
            e.printStackTrace(); return;
        }
        state.putByteArray("MODEL",baos.toByteArray());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle state) {
        super.onRestoreInstanceState(state);
        try(DataInputStream dis =
                    new DataInputStream(
                            new ByteArrayInputStream(state.getByteArray("MODEL")))) {
            model.load(dis);
            levelNumber = state.getInt("LEVEL");
            //endConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   private int X=-1 , Y ;
   private int a=0;

   @Override
   public boolean onClick(int xTile, int yTile){
        a++;
        if(a==2){
            Log.v(TAG, "X : "+X+" Y : "+Y+"   *******************************");
        }
        //if(X != -1 ) { return true ;}
        X=xTile;
        Y=yTile;
        if(!model.isFinish()){
            if(flagOn)
                model.addFlag(Position.of(X,Y));
            else
                model.processClick(Position.of(X,Y));
        }
        else
            model.showBoard();
        refresh();
        return true;
   }
    @Override
   public  boolean onDrag(int xFrom, int yFrom, int xTo, int yTo){
        return true;
   }

    @Override
    public void onDragEnd(int x, int y){
    }

    @Override
    public void onDragCancel(){
    }


    @Override
    public void onBeat(long beat, long time) {
        if(model.isFinish()){
            GameMsg.setText(model.bombClicked
                    ?getString(R.string.loseMsg):getString(R.string.winMsg));

            GameMsg.setVisibility(View.VISIBLE);
            newGame.setVisibility(View.VISIBLE);

        }
    }


    public void Switch(View view){
        flagOn=!flagOn;
        play();
    }


}
