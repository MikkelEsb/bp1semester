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
    public float width=40;
    boolean drawAlongX=false;
    LinkedList<connection> connections;


    public road(PApplet p) {
        parent=p;
        points = new ArrayList<PVector>();
        lanes = new ArrayList<lane>();
        connections = new LinkedList<connection>();
        //On construction we create the arraylist which contains the points

    }
    void addPVectorPoint(PVector point){
        points.add(point);
    }
    void addPoint(float x, float y) {
        //First we create a vector at (x,y)
        PVector point = new PVector(x, y);
        //Then we add our vector to our array of vectors
        points.add(point);
        if(points.size()==2){
            //We figure out which axis to draw our boundary lines for.
            float deltaX=Math.abs(points.get(1).x-points.get(0).x);
            if (deltaX>0){
                //our change is along the X axis
                drawAlongX=true;
            }else{
                drawAlongX=false;
            }
        }
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

        boolean hasSameConnection=false;
        int connectionNum=-1;
        for (int i=0; i<connections.size() && !hasSameConnection;i++){
            if(connections.get(i).connectingPoint.equals(connectingPlace)){
                hasSameConnection=true;
                connectionNum=i;
            }
        }
        if (hasSameConnection) {
            connections.get(connectionNum).addRoad(connectingRoad);
        }else{
            connection newCon = new connection(connectingPlace);
            connections.add(newCon);
            connections.getLast().addRoad(connectingRoad);
        }


        //connections.get(0).addRoad(connectingRoad);//TODO BAD

    }
    float getLength(){
        //Get's the length of the road by looking at each vector point and taking the distance between them.
        return this.getStart().dist(this.getEnd());
    }
    float getTraverseTime(int laneIndex){
        //Gets the time it takes to traverse the entire length of the road considering the lane you're in.
        return 1f;
    }
    void generateLanes(float speed,int desiredLanes){

        if (desiredLanes%2!=0){
            System.out.println("Grr use numbers divisible by 2");
        }
        PVector a = this.getStart().get();
        PVector b = this.getEnd().get();
        //System.out.println("A: " + a.toString() + ". B: " + b.toString());
        PVector ab = a.get().sub(b);
       // System.out.println("AB: " +ab.toString());
        ab.setMag(speed);
        //System.out.println("A: " + a.toString() + ". B: " + b.toString());
        PVector ba = b.get().sub(a);
        //System.out.println("BA: " +ba.toString());
        ba.setMag(speed);
        for (int i=0;i<desiredLanes;i++ ){
            if(i%2==0){
                addLane(ab.x,ab.y);


            }else{
                addLane(ba.x,ba.y);
            }

        }

    }

    void addLane(float x, float y) {
        //Adds the velocity vector for the lane
        //System.out.println("adding lane with vector:" + x + ", " + y);
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
        //System.out.println("Trying to draw points:" + points.get(0).toString() + " and " + points.get(1).toString());
        parent.strokeWeight(2);
        parent.stroke(0, 100);
        float xDelta=0;
        float yDelta=0;
        if (drawAlongX){
            yDelta=width/2;
        }else{
            xDelta=width/2;
        }
        //System.out.println("deltaX: " + xDelta + ", deltay: " +yDelta);
        parent.line(points.get(0).x+xDelta, points.get(0).y+yDelta, points.get(1).x+xDelta, points.get(1).y+yDelta);
        parent.line(points.get(0).x-xDelta, points.get(0).y-yDelta, points.get(1).x-xDelta, points.get(1).y-yDelta);
        parent.strokeWeight(1);
        float laneWidth=width/lanes.size();
        float laneWx=0;
        float laneWy=0;
        if (drawAlongX){
            laneWy=laneWidth;
        }else{
            laneWx=laneWidth;
        }
        for (int i=1;i<lanes.size();i++){
            //For a given road that has two lanes we have a dividing line between each lane.
            //So if we start our road from x-xDelta y-yDelta and then add our lanewidth for each lane.
            parent.line(points.get(0).x-xDelta+laneWx*i,points.get(0).y-yDelta+laneWy*i,points.get(1).x-xDelta+laneWx*i,points.get(1).y-yDelta+laneWy*i);

        }
        //parent.line(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);

    }
}

