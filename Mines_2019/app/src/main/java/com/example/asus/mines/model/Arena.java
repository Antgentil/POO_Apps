package com.example.asus.mines.model;

import java.util.Iterator;

public class Arena implements Iterable<Cell> {
    Cell[][] map;

    Arena(int lines, int cols) {
        map = new Cell[lines][cols];
    }

    void set(Cell cell) {
        map[cell.position.line][cell.position.col] = cell;
  }
    public Cell get(Position pos) {
        return map[pos.line][pos.col];
    }
    public  Cell get(int l, int c){return map[l][c];}
    void reset(Position pos) { map[pos.line][pos.col]=null; }

    @Override
    public Iterator<Cell> iterator() {
        return new Iterator() {
            private Position cur = getFirst();
            @Override
            public boolean hasNext() { return cur != null; }
            @Override
            public Cell next() {
                Cell a = get(cur); cur=getNext(cur); return a;
            }
        };
    }

    private Position getNext(Position cur) {
        int line = cur.line;
        int col = cur.col+1;
        if (col>=map[0].length) { line++; col=0; }
        for (int l=line; l<map.length ; ++l)
            for(int c=(l==line)?col:0; c<map[0].length ; ++c) {
                Cell a = map[l][c];
                if (a!=null) return a.position;
            }
        return null;
    }

    private Position getFirst() {
        for (Cell[] cells : map)
            for (Cell c : cells)
                if (c!=null) return c.position;
        return null;
    }

     Position randomFreePosition() {
        Position pos;
        do pos = Position.random();
        while (get(pos)!=null);
        return pos;
    }

    void clear() {
        for (Cell cell : this)
            reset(cell.position);
    }
}
