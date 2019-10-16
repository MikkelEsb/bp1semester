import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;


public class road {
    //We define the road as points in an array
    PApplet parent;
    ArrayList<PVector> points;
    //Each point is the entry/exit of the road.
    ArrayList<lane> lanes;
    LinkedList<connection> connections;


    public road() {
        points = new ArrayList<PVector>();
        lanes = new ArrayList<lane>();
        connections = new LinkedList<connection>();
        //On construction we create the arraylist which contains the points

    }

    void addPoint(float x, float y) {
        //First we create a vector at (x,y)
        PVector point = new PVector(x, y);
        //Then we add our vector to our array of vectors
        points.add(point);
    }

    connection getConnection(PVector connectingPlace) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).connectingPoint.dist(connectingPlace) < 1) { //crudely using distance of 1 as the way to figure out if connecting
                return connections.get(i);
            }
        }
        //Can't always return connection, must handle this edge case somehow.
        return null;
    }

    public ArrayList<PVector> getPoints() {
        return points;
    }

    public ArrayList<lane> getLanes() {
        return lanes;
    }

    public LinkedList<connection> getConnections() {
        return connections;
    }

    public void setPoints(ArrayList<PVector> points) {
        this.points = points;
    }

    public void setLanes(ArrayList<lane> lanes) {
        this.lanes = lanes;
    }

    public void setConnections(LinkedList<connection> connections) {
        this.connections = connections;
    }

    void setConnection(PVector connectingPlace, road connectingRoad) {
        connection newCon = new connection(connectingPlace);
        connections.add(newCon);
        //connections.get(0).addRoad(connectingRoad);//TODO BAD

    }

    void addLane(float x, float y) {
        //Adds the velocity vector for the lane
        lane newLane = new lane(x, y);
        lanes.add(newLane);
    }

    PVector getNPoint(int n) {
        return points.get(n);
    }

    PVector getStart() {
        return getNPoint(0);
    }

    PVector getEnd() {
        return getNPoint(points.size() - 1);
    }
    void setParent(PApplet p) {
        parent = p;
    }
    void display() {

        parent.strokeWeight(5);
        parent.stroke(0, 100);
        parent.line(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);

    }
}

