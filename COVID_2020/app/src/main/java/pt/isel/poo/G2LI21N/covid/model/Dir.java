package pt.isel.poo.G2LI21N.covid.model;

public enum Dir {

    LEFT(-1,0), UP(0,-1), RIGHT(+1,0), DOWN(0,+1);

    public final int dc;
    public final int dl;


    /**
     * Constructor.
     * @param dc Direction in x axe or in columns
     * @param dl Direction in y axe or in lines
     */
    Dir(int dc, int dl) { this.dc = dc; this.dl = dl; }


    /**
     * Reverses the current direction
     * @return Opposite direction
     */
    public Dir opposite() {
        return values()[ (ordinal()+2) % values().length ];
    }

    /*
    public static Dir fromVector(int dc, int dl) {
        for (Dir d : values())
            if (d.dc == dc && d.dl == dl) return d;
        return null;
    }
    */

}
