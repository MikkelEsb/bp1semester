import java.util.ArrayList;

import processing.core.PVector;

public class connection {
    //Connection is a class that contains a point (x,y) and every road connected to that point

    PVector connectingPoint;
    ArrayList<road> connectingRoads = new ArrayList<road>();

    connection(PVector cPoint) {
        connectingPoint = cPoint;

    }

    void addRoad(road newRoad) {
        this.connectingRoads.add(newRoad);
    }
}
