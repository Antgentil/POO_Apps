package com.example.asus.mines.model;

public interface GameListener {
    void cellRemoved(Position pos);
    void cellCreated(Cell cell);
    void cellChanged(Cell cell);
    void updateBombs(int MAX_BOMBS);
}
