import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

public class core extends PApplet {
    //First we initialize our arrays
    //We'll have an array for each
    ArrayList<connection> allConnections= new ArrayList<connection>();
    ArrayList<road> allRoads = new ArrayList<road>();
    ArrayList<vehicle> allCars = new ArrayList<vehicle>();
    int numVehicles = 5;
    int numXRoads = 5;
    int numYRoads = 5;
    int t, s = 0;
    float distanceBetweenPoints=150f;
    Statistics statistics = new Statistics(numVehicles);

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
                //System.out.println(currentPoint.toString());
                if (x>0 && x<=numXRoads){
                    road tempRoad=new road(this);
                    tempRoad.addPVectorPoint(currentPoint);
                    currentConnection.addRoad(tempRoad);
                    tempRoad.addPoint(currentPoint.x-distanceBetweenPoints,currentPoint.y);
                    tempRoad.generateLanes(1.5f,2);
                    allConnections.get(currentIndex-numYRoads).addRoad(tempRoad);
                    //System.out.println("Making connection between " + currentPoint.toString() + " and " + allConnections.get(currentIndex-numYRoads).connectingPoint.toString());
                    allRoads.add(tempRoad);

                }
                if (y>0 && y<=numYRoads){
                    road tempRoad=new road(this);
                    tempRoad.addPVectorPoint(currentPoint);
                    currentConnection.addRoad(tempRoad);
                    tempRoad.addPoint(currentPoint.x,currentPoint.y-distanceBetweenPoints);
                    tempRoad.generateLanes(1.5f,2);
                    //System.out.println("x: "+ x  + ", y: " + y + ". index:" + currentIndex);
                    allConnections.get(currentIndex-1).addRoad(tempRoad);
                    allRoads.add(tempRoad);
                    //System.out.println("Making connection between " + currentPoint.toString() + " and " + allConnections.get(currentIndex-1).connectingPoint.toString());
                }

                allConnections.add(currentIndex,currentConnection);
                currentIndex++;
                //System.out.println(currentPoint.toString() +"and value of last insert: " + allConnections.get(currentIndex-1).connectingPoint.toString());
            }
        }
        for (int i=0;i<numVehicles;i++){
            connection spawnConnection=allConnections.get((int) random(0,allConnections.size()));
            PVector targetLoc = allConnections.get((int)random(0,allConnections.size())).connectingPoint.get();
            PVector spawnLoc=spawnConnection.connectingPoint.get();
            road spawnRoad = spawnConnection.connectingRoads.get((int)random(0,spawnConnection.connectingRoads.size()));
            vehicle newVehicle = new vehicle(spawnLoc.x,spawnLoc.y,targetLoc.x,targetLoc.y);
            newVehicle.setRoad(spawnRoad,spawnRoad.lanes.get(0));
            newVehicle.setVelocity(spawnRoad.lanes.get(0).direction.x*0.2f,spawnRoad.lanes.get(0).direction.y*0.2f);
            //System.out.println("Spawnx: " + spawnRoad.lanes.get(0).direction.x +  "  Set velocity to" + newVehicle.velocity.toString());
            newVehicle.setAllConnections(allConnections);
            newVehicle.setParent(this);
            allCars.add(newVehicle);
        }

        background(255);
        //smooth();
       /* for (int r=0;r<allRoads.size();r++){
            for (int l=0;l<allRoads.get(r).lanes.size();l++){
                System.out.println(allRoads.get(r).lanes.get(l).direction.toString());
            }
        }*/
    }

    public void exit() {
        //System.out.println("One: [" + TestOne.score + "]. Two: [" + TestTwo.score + "]. Three [" + TestThree.score + "]");
    }

    public void draw() {
        background(255);
        strokeWeight(2);
        fill(127);
        //Test.setVelocity(1, 2);
        //path.display();
        for (int i=0;i<allRoads.size();i++){
            allRoads.get(i).display();
        }
        for (int i=0;i<allCars.size();i++){
            allCars.get(i).run();
        }
        clock();

    }

    private void clock(){
        fill(0);
        t = frameCount % 60;
        if(t == (int) 59){s+= 1;}
        textSize(32);
        text("Seconds elapsed " + s + "t: " + t + ". frameRate: " + frameRate, 30, 30 );
    }

}
