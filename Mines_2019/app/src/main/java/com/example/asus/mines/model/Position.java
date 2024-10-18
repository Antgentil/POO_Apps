package com.example.asus.mines.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Position {
    public final int line;
    public final int col;

    public Position(int l, int c) {
        line = l;
        col = c;
    }

    private static Position[][] all;

    static void init(int lines, int cols) {
        all = new Position[lines][cols];
    }

    public static Position of(int l, int c) {
        Position pos = all[l][c];
        if (pos==null)
            all[l][c] = pos = new Position(l, c);
        return pos;
    }

    public static int getLines() { return all.length; }
    public static int getCols() { return all[0].length;  }

    public void save(DataOutputStream dos) throws IOException {
        dos.writeByte(line);
        dos.writeByte(col);
    }

    public void load(DataInputStream dis) throws IOException {
        Position.of(dis.readByte(),dis.readByte());
    }

    public static Position random() {
        return of((int)(Math.random()*getLines()),(int)(Math.random()*getCols()));
    }

}
