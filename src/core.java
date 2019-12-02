import processing.core.PApplet;
import processing.core.PVector;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class core extends PApplet {
    //First we initialize our arrays
    //We'll have an array for each
    ArrayList<connection> allConnections= new ArrayList<connection>();
    ArrayList<intersection> allIntersections = new ArrayList<>();
    ArrayList<road> allRoads = new ArrayList<road>();
    ArrayList<vehicle> allCars = new ArrayList<vehicle>();
    int t,s;
    int lastInt=0;
    int numVehicles = 1;
    int numXRoads = 3;
    int numYRoads = 3;
    float intersectionWidth=75;
    float distanceBetweenPoints=250f;
    Statistics statistics = new Statistics(numVehicles);



    public static void main(String[] args) {
        PApplet.main("core");
    }

    public void settings() {
        size(1200, 720);
    }

    public void setup() {
        //intersectionPath testIntersectionPath = new intersectionPath(20,0,0,20,2);
        rectMode(CORNERS);
        surface.setResizable(true);


        /*
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
        */

        int currentIndex=0;
        for (int y=0;y<numYRoads;y++){
            //We iterate through each y coordinate and then go through all the X coordinates for that Y coordinate.
            for (int x=0;x<numXRoads;x++){
                //We create an intersection for each x and y coordinate.
                //The coordinates for a given intersection is defined by two points: (x*150,y*150,x*150+30,y*150+30)
                float x1,y1,x2,y2;
                x1=x*distanceBetweenPoints+intersectionWidth;
                y1=y*distanceBetweenPoints+intersectionWidth;
                x2=x1+intersectionWidth;
                y2=y1+intersectionWidth;
                intersection newIntersection= new intersection(x1,y1,x2,y2,this);
                //We have made a new intersection which has no roads connected to it, let's make roads which connect to already made intersections if we have any.
                if (x>0 && x<=numXRoads){
                    //We only make the roads when we already have an intesection to connect them between.
                    road tempRoad=new road(this);
                    //Our road will be along the X axis.
                    //allIntersections.get(currentIndex-1).x2
                    tempRoad.addPoint(allIntersections.get(currentIndex-1).x2,y1+intersectionWidth/2);
                    tempRoad.addPoint(x1,y1+intersectionWidth/2);
                    //Now that we have generated the road we want some of those lovely lanes on it.
                    tempRoad.generateLanes(1.5f,2);
                    //Lanes have been generated, let's add the road to both of the intersections so they are aware of this connection.
                    allIntersections.get(currentIndex-1).addRoad(tempRoad);
                    newIntersection.addRoad(tempRoad);
                    allRoads.add(tempRoad);
                }
                if (y>0 && y<=numYRoads){
                    //We only make the roads when we already have an intesection to connect them between.
                    road tempRoad=new road(this);
                    //Our road will be along the Y axis.
                    //allIntersections.get(currentIndex-1).x2
                    tempRoad.addPoint(x1+intersectionWidth/2,allIntersections.get(currentIndex-numYRoads).y2);
                    tempRoad.addPoint(x1+intersectionWidth/2,y1);
                    //Now that we have generated the road we want some of those lovely lanes on it.
                    tempRoad.generateLanes(1.5f,2);
                    //Lanes have been generated, let's add the road to both of the intersections so they are aware of this connection.
                    allIntersections.get(currentIndex-numYRoads).addRoad(tempRoad);
                    newIntersection.addRoad(tempRoad);
                    allRoads.add(tempRoad);
                }
                //System.out.println("Made intersection at:[" + x1 +"," +y1 + "] ["+ x2 +"," +y2 + "]");
                allIntersections.add(currentIndex,newIntersection);
                currentIndex++;
            }
        }
        for (int i=0;i<allIntersections.size();i++){
            allIntersections.get(i).updateConnections();
        }

        for (int i=0;i<numVehicles;i++){
            intersection spawnIntersection=allIntersections.get((int) random(0,allIntersections.size()));
            //road spawnRoad = spawnIntersection.connectedRoads.get((int) random(0,spawnIntersection.connectedRoads.size()));
            road spawnRoad = spawnIntersection.entryPoints.get(1).thisRoad;
            int entryPoint=0;
            PVector spawnLoc = new PVector(spawnIntersection.entryPoints.get(entryPoint).x +spawnIntersection.entryPoints.get(entryPoint).thisLane.direction.x*60 ,spawnIntersection.entryPoints.get(entryPoint).y +spawnIntersection.entryPoints.get(entryPoint).thisLane.direction.y*60);
            //PVector spawnLoc = spawnRoad.getEnd();
            vehicle newVehicle = new vehicle(spawnLoc.x,spawnLoc.y,400,400);
            newVehicle.setRoad(spawnRoad,spawnIntersection.entryPoints.get(entryPoint).thisLane);
            newVehicle.setVelocity(spawnIntersection.entryPoints.get(entryPoint).thisLane.direction.x*0.2f,spawnIntersection.entryPoints.get(entryPoint).thisLane.direction.y*0.2f);
            //System.out.println("Spawnx: " + spawnRoad.lanes.get(0).direction.x +  "  Set velocity to" + newVehicle.velocity.toString());
            //newVehicle.setAllConnections(allConnections);
            newVehicle.setParent(this);
            newVehicle.setIntersection(spawnIntersection);
            newVehicle.setAllIntersections(allIntersections);
            allCars.add(newVehicle);

        }

        background(0,200,50);
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
        //lastInt=(lastInt+1)%allRoads.size();
        //allRoads.get(lastInt).display();
        for (int i=0;i<allIntersections.size();i++){
           // allIntersections.get(i).display();
        }
        /*for (int i=0;i<allRoads.size();i++){
            allRoads.get(i).display();
        }*/
        for (int i=0;i<allCars.size();i++){
            //allCars.get(i).run();
            //allCars.get(i).followIntersectionPath(allIntersections.get(5).intersectionConnections.get(1).get(0));
            fill(200,100,10);
            allCars.get(i).currentIntersection.display();
            allCars.get(i).run();

        }
        for (int i=0;i<allConnections.size();i++){
           PVector conPoint= allConnections.get(i).connectingPoint.get();
           //ellipse(conPoint.x,conPoint.y,5,5);
           for (int j=0;j<allConnections.get(i).connectingRoads.size();j++){
               allConnections.get(i).connectingRoads.get(j).display();

           }
        }

        //clock();

    }
    public void keyPressed(){
        frameRate(60);
    }
    public void mousePressed(){


        for (int i=0;i<allCars.size();i++){
            allCars.get(i).myTarget.x=mouseX;
            allCars.get(i).myTarget.y=mouseY;

        }
        System.out.println("Mouse pressed");
        if(mouseButton==RIGHT){
            allCars.get(0).location.x=mouseX;
            allCars.get(0).location.y=mouseY;
            allCars.get(0).getIntersectionPaths(allCars.get(0).currentIntersection);

        }
    }
    private void clock(){
        fill(0);
        t = frameCount % 60;
        if(t == (int) 59){s+= 1;}
        textSize(32);
        text("Seconds elapsed " + s + "t: " + t + ". frameRate: " + frameRate, 30, 30 );
    }

}
