import processing.core.PApplet;
import processing.core.PVector;

import java.sql.Array;
import java.util.ArrayList;

public class core extends PApplet {
    //First we initialize our arrays
    //We'll have an array for each
    ArrayList<connection> allConnections= new ArrayList<connection>();
    ArrayList<road> allRoads = new ArrayList<road>();
    ArrayList<vehicle> allCars = new ArrayList<vehicle>();
    int numXRoads = 5;
    int numYRoads = 5;
    float distanceBetweenPoints=150f;

    public static void main(String[] args) {
        PApplet.main("core");
    }

    public void settings() {
        size(1200, 720);
    }

    public void setup() {
        surface.setResizable(true);
        int currentIndex=0;
        for (int x=0;x<numXRoads;x++){
            for (int y=0;y<numYRoads;y++){
                PVector currentPoint = new PVector(x*distanceBetweenPoints+20,y*distanceBetweenPoints+20);
                connection currentConnection = new connection(currentPoint);

                if (x>0 && x<numXRoads){
                    road tempRoad=new road(this);
                    tempRoad.addPVectorPoint(currentPoint);
                    currentConnection.addRoad(tempRoad);
                    tempRoad.addPoint(currentPoint.x-distanceBetweenPoints,currentPoint.y);
                    allRoads.add(tempRoad);

                }
                if (y>0 && y<numYRoads){
                    road tempRoad=new road(this);
                    tempRoad.addPVectorPoint(currentPoint);
                    currentConnection.addRoad(tempRoad);
                    tempRoad.addPoint(currentPoint.x,currentPoint.y-distanceBetweenPoints);
                    System.out.println("x: "+ x  + ", y: " + y + ". index:" + currentIndex);
                   // allConnections.get(currentIndex-numYRoads).addRoad(tempRoad);
                    allRoads.add(tempRoad);
                }

                allConnections.add(currentIndex,new connection(currentPoint));
                currentIndex++;
                System.out.println(currentPoint.toString());
            }
        }

        background(255);
        //smooth();

    }

    public void exit() {
        //System.out.println("One: [" + TestOne.score + "]. Two: [" + TestTwo.score + "]. Three [" + TestThree.score + "]");
    }

    public void draw() {
	surface.setResizable(true); //TODO switch all screen dependant variables to width, height respectively.
        background(255);
        strokeWeight(2);
        fill(127);
        //Test.setVelocity(1, 2);
        //path.display();
        for (int i=0;i<allRoads.size();i++){
            allRoads.get(i).display();
        }
        clock();

    }

    private void clock(){
        int m = millis();
        textSize(32);
        int s = m/1000;
        text("Seconds elasped " + s, 600, 30);

    }

}
