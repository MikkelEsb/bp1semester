import processing.core.PVector;
import processing.core.*;

import static processing.core.PApplet.map;


public class vehicle {
    PApplet parent;
    PVector location;
    PVector velocity;
    PVector acceleration;
    PVector myTarget;
    PVector steer;
    road myRoad;
    lane myLane;
    float r = 4;
    float maxSpeed = 3;
    float maxAcceleration = 0.05f;
    int score = 0;
    int colour = 177;

    public vehicle(float x, float y, float t_x, float t_y) {
        location = new PVector(x, y);
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);
        myTarget = new PVector(t_x, t_y);
        steer = new PVector(0, 0);
    }

    void setParent(PApplet p) {
        parent = p;
    }

    void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    void setAcceleration(float x, float y) {
        acceleration.x = x;
        acceleration.y = y;
    }

    void update() {
        //Update velocity from acceleration
        //Update location from velocity
        if (location.dist(myTarget) < maxSpeed) {
            this.score = this.score + 1;
            myTarget.x = (float) (Math.random() * 1200);
            myTarget.y = (float) (Math.random() * 720);
            //velocity.mult(0);
        } else {
            //velocity.y=(float) 0.9;
            //seek();

        }

        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);
        acceleration.mult(0); //As the acceleration force has been applied we set the acceleration back to 0
        if (location.x > 1200) {
            location.x = 1;

        }
        if (location.x < 0) {
            location.x = 1200;
        }
        if (location.y > 720) {
            //location.x	=	location.x + 20;
            location.y = 1;
        }
        if (location.y < 0) {
            location.y = 720;
        }

        //System.out.println(location);
    }

    // A function to get the normal point from a point (p) to a line segment (a-b)
    // This function could be optimized to make fewer new Vector objects
    PVector getNormalPoint(PVector p, PVector a, PVector b) {
        // Vector from a to p
        PVector ap = PVector.sub(p, a);
        // Vector from a to b
        PVector ab = PVector.sub(b, a);
        ab.normalize(); // Normalize the line
        // Project vector "diff" onto line by using the dot product
        ab.mult(ap.dot(ab));
        PVector normalPoint = PVector.add(a, ab);
        return normalPoint;
    }
    float VelocityToDinstance(float v1, float v2){
        return (v1*v1+v2*v2)/(2*maxAcceleration);
    }
    lane getBestLaneToTarget(road tempRoad){
        float shortestDist=100000;
        int laneNum=-1;
        for (int i=0; i<tempRoad.lanes.size();i++){
            PVector dirWithLoc = tempRoad.lanes.get(i).direction.get();
            dirWithLoc.add(location);
            float tDist = dirWithLoc.dist(myTarget);
            if (tDist<shortestDist){
                shortestDist=tDist;
                laneNum=i;
            }
        }
        if (laneNum==-1){
            System.out.println("Shiit man -1 laneNum");
        }
        return tempRoad.lanes.get(laneNum);
    }
    road getNextRoad(connection ourConnections,PVector currentPoint){
        float shortestDist=100000; //Big distance, should be easy to beat ;)
        int bestRoad=-1; //Initially set value to something that's not possible to later check if we got a valid value.
        int nCheck;
        //We loop through all roads to our connection
        for (int i = 0; i < ourConnections.connectingRoads.size(); i++){
            //Then we check if our first point in the road is equals to the point we're checking from.
            if (ourConnections.connectingRoads.get(i).getNPoint(0).equals(currentPoint)){
                //If the point we're at is the same as the point we're looking at then we're interested in the opposite point (assuming we only have 2 points).
                nCheck=1;

            }
            else{
                nCheck=0;
            }
            //We check the distance between our desired point to our current target.
            float dis=ourConnections.connectingRoads.get(i).getNPoint(nCheck).dist(myTarget);
            if (dis<shortestDist){
                //If our distance is shorter than the current best then we conclude that this is the best road for now.
                shortestDist=dis;
                bestRoad=i;
            }
        }
        if (bestRoad!=-1){


        }else{
            System.out.println("We couldn't find a road woopsies dis is baaad");

        }
        return ourConnections.connectingRoads.get(bestRoad);
    }

    void followRoad(road CurRoad,lane desiredLane){
        if (CurRoad==null){
            return;
        }
        PVector a = CurRoad.getNPoint(0);
        PVector b = CurRoad.getNPoint(1);
        PVector predictLoc = velocity.get();
        predictLoc.normalize();
        predictLoc.mult(50);
        predictLoc.add(location);
        //System.out.println(predictLoc); //debugging
        float adist=a.dist(location);
        float bdist=b.dist(location);
       /* if(adist<=(velocity.mag()+20)){
            connection newCon           = CurRoad.getConnection(a);
            if (newCon!=null) {
                road nextRoad = getNextRoad(newCon, a);
                this.setRoad(nextRoad, getBestLaneToTarget(nextRoad));
                System.out.println("new road set a");
            }
        }*/
        if(bdist<=(velocity.mag()+20)) {
            connection newCon = CurRoad.getConnection(b);
            if (newCon!=null) { //Sometimes we don't actually have any connections so we have to handle this edge case.
                road nextRoad = getNextRoad(newCon, b);
                getBestLaneToTarget(nextRoad);
                this.setRoad(nextRoad, getBestLaneToTarget(nextRoad));
                System.out.println("new road set b");
            }
        }
        PVector normalPoint = getNormalPoint(predictLoc,a,b);


        float distance = PVector.dist(predictLoc, normalPoint);
        //System.out.println(distance);
        if (distance > 10) { //TODO change 5 to a number that makes sense in regard to where the specific lane is on the road.
            PVector t=desiredLane.direction.get();
            PVector yolo=PVector.add(normalPoint,t.mult(10));
            //System.out.println("We seekin bro");
            seek(yolo);

        }else{
            acceleration.set(desiredLane.direction);
            acceleration.setMag(maxAcceleration);
        }


    }
    void follow(Path p) {

        // Predict location 50 (arbitrary choice) frames ahead
        PVector predict = velocity.get();
        predict.normalize();
        predict.mult(50);
        PVector predictLoc = PVector.add(location, predict);

        // Look at the line segment
        PVector a = p.start;
        PVector b = p.end;

        // Get the normal point to that line
        PVector normalPoint = getNormalPoint(predictLoc, a, b);

        // Find target point a little further ahead of normal
        PVector dir = PVector.sub(b, a);
        dir.normalize();
        dir.mult(10);  // This could be based on velocity instead of just an arbitrary 10 pixels
        PVector targetn = PVector.add(normalPoint, dir);

        // How far away are we from the path?
        float distance = PVector.dist(predictLoc, normalPoint);
        // Only if the distance is greater than the path's radius do we bother to steer
        if (distance > p.radius) {
            seek(targetn);
        }else{
            acceleration.set(velocity);
            acceleration.setMag(maxAcceleration);
        }

    }

    void applyForce(PVector force) {
        acceleration.add(force);
    }

    void seek(PVector target) {


        PVector desired = PVector.sub(target, location); //Creates a vector between current location and target location
        desired.normalize(); //Sets the vector length to 1
        desired.mult(maxSpeed); //Multiplies the vector to be maxSpeed as length

        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxAcceleration);
        applyForce(steer);
        arrive(target);
    }

    void arrive(PVector target){
        PVector desired = PVector.sub(target,location);  //see seek
        float d = desired.mag();                         //
        desired.normalize();                             //
        if (d<200) {
            float m = map(d,0,200,0,maxSpeed); //edit range here
            desired.mult(m);
        } else {
            desired.mult(maxSpeed);
        }
        PVector steer = PVector.sub(desired, velocity); //see seek
        steer.limit(maxAcceleration);                   //
        applyForce(steer);                              //
    }
    void setRoad(road newRoad,lane newLane){
        myRoad=newRoad;
        myLane=newLane;
    }


    void run() {
        followRoad(myRoad,myLane);
        update();
        display();
    }

    void display() {
        // Draw a triangle rotated in the direction of velocity
        float theta = (float) (velocity.heading2D() + Math.PI / 2);
        parent.fill(this.colour);
        parent.stroke(0);
        parent.strokeWeight(1);
        parent.pushMatrix();
        parent.translate(location.x, location.y);
        parent.rotate(theta);
        parent.beginShape();
        parent.vertex(0, -r * 2);
        parent.vertex(-r, r * 2);
        parent.vertex(r, r * 2);
        parent.endShape();
        parent.popMatrix();
        parent.ellipse(myTarget.x,myTarget.y,5,5);
        //parent.ellipse(target.x, target.y, 3, 3);

    }


}