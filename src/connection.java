import java.util.ArrayList;

import processing.core.PVector;

public class connection {
    PVector connectingPoint;
    ArrayList<road> connectingRoads;

    connection(PVector cPoint) {
        connectingPoint = cPoint;

    }

    void addRoad(road newRoad) {
        connectingRoads.add(newRoad);
    }
}
