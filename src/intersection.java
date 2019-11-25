import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class intersection {
    float x1,x2,y1,y2;
    PApplet parent;
    ArrayList<intersectionPath> intersectionConnections = new ArrayList();
    ArrayList<road> connectedRoads = new ArrayList<>();
    ArrayList<PVector> testPoints = new ArrayList<>();
    ArrayList<intersectionConnectionPoint> entryPoints = new ArrayList();
    ArrayList<intersectionConnectionPoint> exitPoints = new ArrayList();

    public intersection(float x,float y,float x0,float y0,PApplet p){
        //An intersection is a rectangle defined by two opposite corners
        x1=x;
        x2=x0;
        y1=y;
        y2=y0;
        parent=p;

    }
    public void makeRoadForIntersections(intersection otherIntersection){
        //We have another intersection that we want a connection with.
    }
    public void addRoad(road newRoad){
        //
        connectedRoads.add(newRoad);
    }
    public void updateConnections(){
        //Iterates through all the roads connected to the point and then makes circular paths between the turns
        //If a lane has a direction that goes towards the intersection classify them as entry otherwise classify them as exit.
        //If two lanes share the same direction then connect them directly with a straight path between the two points
        //Do not connect lanes from one road to the same road.

        for (int i=0;i<connectedRoads.size();i++){
            //Look through every single road connected
            float roadWidth=connectedRoads.get(i).width;
            float laneWidth=roadWidth/(connectedRoads.get(i).lanes.size());
            float xCoord = 0,yCoord=0;
            float deltaX,deltaY;
            float checkingX,checkingY;
            float oppositeX,oppositeY;
            if (connectedRoads.get(i).drawAlongX){
                //Our coordinates will only change for the yCoordinate for each lane.
                if (connectedRoads.get(i).points.get(0).x==x1 || connectedRoads.get(i).points.get(0).x==x2){
                    xCoord=connectedRoads.get(i).points.get(0).x;
                    yCoord=connectedRoads.get(i).points.get(0).y;


                }
                if (connectedRoads.get(i).points.get(1).x==x1 || connectedRoads.get(i).points.get(1).x==x2){
                    xCoord=connectedRoads.get(i).points.get(1).x;
                    yCoord=connectedRoads.get(i).points.get(1).y;
                }
                if (xCoord==x1){
                    oppositeX=x2;
                }else{
                    oppositeX=x1;
                }
                oppositeY=yCoord;
                deltaX=0;
                yCoord=yCoord-roadWidth/2;
                deltaY=laneWidth;
            }else{
                if (connectedRoads.get(i).points.get(0).y==y1 || connectedRoads.get(i).points.get(0).y==y2){
                    xCoord=connectedRoads.get(i).points.get(0).x;
                    yCoord=connectedRoads.get(i).points.get(0).y;

                }
                if (connectedRoads.get(i).points.get(1).y==y1 || connectedRoads.get(i).points.get(1).y==y2){
                    xCoord=connectedRoads.get(i).points.get(1).x;
                    yCoord=connectedRoads.get(i).points.get(1).y;
                }
                if (yCoord==y1){
                    oppositeY=y2;
                }else{
                    oppositeY=y1;
                }
                oppositeX=xCoord;
                deltaY=0;
                xCoord=xCoord-roadWidth/2;
                deltaX=laneWidth;
            }
            for (int j=0;j<connectedRoads.get(i).lanes.size();j++){
                //First we need the coordinates of this specific lane.
                float laneMidX,laneMidY;
                if(connectedRoads.get(i).drawAlongX){
                    laneMidX=xCoord;
                    laneMidY=yCoord+laneWidth/2+laneWidth*(j);

                }else{
                    laneMidX=xCoord+laneWidth/2+laneWidth*(j);
                    laneMidY=yCoord;

                }
                testPoints.add(new PVector(laneMidX,laneMidY));
                PVector laneDir = connectedRoads.get(i).lanes.get(j).direction;
                float distX=oppositeX-laneMidX+laneDir.x,distY=oppositeY-laneMidY+laneDir.y;
                if (Math.sqrt(distX*distX+distY*distY)<(Math.abs(x1-x2))){
                    //If the distance to the opposite point is getting smaller than the length of our intersection we are entering it
                    entryPoints.add(new intersectionConnectionPoint(laneMidX,laneMidY,connectedRoads.get(i).lanes.get(j)));
                }else{
                    exitPoints.add(new intersectionConnectionPoint(laneMidX,laneMidY,connectedRoads.get(i).lanes.get(j)));
                }
                //now we we need to figure out if this point is an exit or and entry
                //We have the point at which we connect and we know the direction of our lane by knowing if the road is drawn vertically or horizontally we can
                //Since we know the starting coordinate in one axis we can see if we get closer to the oppisite by adding the direction vector from the lane.


                //For each lane we need to see if we exit or enter the intersection with it.


            }

        }
        //We should have all our lanes as entry or exit points at this time, now we just gotta connect all of them
        for (int i=0; i<entryPoints.size();i++){
            for (int j=0;j<exitPoints.size();j++){
                intersectionConnections.add(i,new intersectionPath(entryPoints.get(i).x,entryPoints.get(i).y,exitPoints.get(j).x,exitPoints.get(j).y,2f));
            }
        }



    }

    public void display(){
        parent.fill(150);
        parent.rect(x1,y1,x2,y2);
        parent.textSize(30);
        parent.text(connectedRoads.size(),x2,y2);
        for (int i=0;i<connectedRoads.size();i++){
            connectedRoads.get(i).display();
        }
        for (int i = 0; i<entryPoints.size();i++){
            parent.fill(0,255,0);
            parent.ellipse(entryPoints.get(i).x,entryPoints.get(i).y,5,5);
        }
        for (int i=0;i<exitPoints.size();i++){
            parent.fill(255,0,0);
            parent.ellipse(exitPoints.get(i).x,exitPoints.get(i).y,5,5);
        }
    }

}
