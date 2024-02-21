package inf101.sem2.game;

import inf101.grid.Location;

public class BlobWarsLocations {
    private Location from;
    private Location to;

    /**
     * Lager et objekt som tar inn to Locations, med hensikten å enten flytte/kopiere brikken fra from til to
     * @param from
     * @param to
     */
    public BlobWarsLocations(Location from, Location to){
        this.from = from;
        this.to = to;
    }

    public Location getFromLocation(){
        return this.from;
    }

    public Location getToLocation(){
        return this.to;
    }

}
