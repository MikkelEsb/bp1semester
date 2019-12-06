import processing.core.PVector;
import processing.core.*;

import java.util.ArrayList;

import static processing.core.PApplet.map;


public class vehicle {
    PApplet parent;
    PVector location, velocity, acceleration, myTarget, steer;
    road myRoad;
    lane myLane;
    road nextRoad;
    lane nextLane;
    PVector EntryPoint;
    intersectionPath myIntersectionPath;
    intersection currentIntersection;
    boolean needsRoad;
    float r = 4;
    float maxSpeed = 2;
    float maxAcceleration = 0.05f;
    int score = 0;
    int colour = 177;
    int testingVar=0;
    ArrayList<connection> allConnections= new ArrayList<connection>();
    ArrayList<intersection> allIntersections = new ArrayList<>();

    public vehicle(float x, float y, float t_x, float t_y) {
        location        = new PVector(x, y);
        velocity        = new PVector(0, 0);
        acceleration    = new PVector(0, 0);
        myTarget        = new PVector(t_x, t_y);
        steer           = new PVector(0, 0);
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
            //myTarget = allConnections.get((int) parent.random(0,allConnections.size())).connectingPoint.get();

            //myTarget.x = (float) (Math.random() * 1200);
            //myTarget.y = (float) (Math.random() * 720);
            //velocity.mult(0);
        } else {
            //velocity.y=(float) 0.9;
            //seek();

        }

        /*velocity.add(acceleration);
        velocity.limit(maxSpeed);
        */
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
            //dirWithLoc.add(location);
            float tDist = dirWithLoc.dist(myTarget);
            System.out.println("distance lane to target" + tDist + " for vector: " + tempRoad.lanes.get(i).direction.get());
            if (tDist<shortestDist){
                shortestDist=tDist;
                laneNum=i;
            }
        }
        if (laneNum==-1){
            System.out.println("Shiit man -1 laneNum");
        }
        System.out.println("We think lane with dist " + shortestDist + ", and vector: " + tempRoad.lanes.get(laneNum)+ ". is the best for reaching goal");
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
            System.out.println("Distance to point: " + dis);
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

    public connection getConnectionFromPoint(PVector point){
        for (int i=0;i<allConnections.size();i++){
            if (allConnections.get(i).connectingPoint.dist(point)<1){
                return allConnections.get(i);
            }
        }
        System.out.println("Couldn't find le conectione");
        return null;
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
        //parent.fill(255);
        //parent.ellipse(a.x,a.y,20,20);
        //parent.ellipse(b.x,b.y,20,20);
        float adist=a.dist(location);
        float bdist=b.dist(location);

        if (!needsRoad){
            //If we don't need a road then we check if we might need one soon
            if (!(adist<=(velocity.mag()+20) || bdist<=(velocity.mag()+20))){
                //If the distance to either points on our current road is larger than the speed+20 (arbitrarialy) then we need to find a new road soon

                needsRoad=true;
                //System.out.println("NeedRoad=true");
            }else{
                needsRoad=false;
                //System.out.println("NeedRoad=false");
            }

        }else { //If we do need a road we look for one and then set that we don't need one anymore
            if (adist <= (velocity.mag() + 20) && EntryPoint.dist(location)<10) {
                //connection newCon = getConnectionFromPoint(a);
                intersectionPath newCon = getIntersectionPaths(currentIntersection);

                if (newCon != null ) {
                    //road nextRoad = getNextRoad(newCon, a);
                    myIntersectionPath=newCon;
                    //findNextIntersection();
                    //this.setRoad(nextRoad, getBestLaneToTarget(nextRoad));
                    System.out.println("new road set a");
                    //location.set(a);
                    needsRoad=false;
                    myRoad=null;
                    myLane=null;
                }else{
                    myRoad=null;
                    myLane=null;
                    System.out.println("Nullie1");
                    //this.velocity.setMag(0);
                    //System.out.println("Setting speed to 0 as no connection was found");
                }
            }else{
                //System.out.println("adist: " + adist);
            }
            if (bdist <= (velocity.mag() + 20) && EntryPoint.dist(location)<10) {
                //System.out.println("Adist: " + adist +", Bdist: " + bdist);
                //connection newCon = getConnectionFromPoint(b);
                intersectionPath newCon = getIntersectionPaths(currentIntersection);

                if (newCon != null ) {
                    //road nextRoad = getNextRoad(newCon, a);
                    myIntersectionPath=newCon;
                    //findNextIntersection();
                    //this.setRoad(nextRoad, getBestLaneToTarget(nextRoad));
                    System.out.println("new road set a");
                    //location.set(a);
                    needsRoad=false;
                    myRoad=null;
                    myLane=null;

                }else{
                    myRoad=null;
                    myLane=null;
                    System.out.println("nullie");
                    //this.velocity.setMag(0);
                    //System.out.println("Couldn't find connection!");
                }
            }else{
                //System.out.println("Bdist:" +bdist  );
            }

        }
        /*PVector normalPoint = getNormalPoint(predictLoc, a, b);
        float distance = PVector.dist(predictLoc, normalPoint);
        //System.out.println(distance);
        if (distance > 10) { //TODO change 5 to a number that makes sense in regard to where the specific lane is on the road.
            PVector t=desiredLane.direction.get();
            PVector yolo=PVector.add(normalPoint,t.mult(10));
            //System.out.println("We seekin bro");
            seek(yolo);

        }else{*/
            velocity.set(desiredLane.direction);
        //System.out.println(desiredLane.direction.toString());
            //acceleration.setMag(maxAcceleration);



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
    int getBestEntryFromIntersection(intersection thatIntersection){
        float shortestDist=100000;
        int laneNum=-1;
        for (int i=0; i<thatIntersection.entryPoints.size();i++){
            //PVector dirWithLoc = thatIntersection.entryPoints.get(i).thisLane.direction.get();
            //dirWithLoc.add(location);
            float tDist = location.dist(new PVector(thatIntersection.entryPoints.get(i).x,thatIntersection.entryPoints.get(i).y));
            //System.out.println("distance lane to target" + tDist + " for vector: " );
            if (tDist<shortestDist){
                shortestDist=tDist;
                laneNum=i;
            }
        }
        if (laneNum==-1){
            System.out.println("Shiit man -1 laneNum");
        }
        System.out.println("We think lane with dist " + shortestDist + ", and vector: " + thatIntersection.entryPoints.get(laneNum).thisLane + ". is the best for reaching the intersection");
        return laneNum;
    }
    int getBestExitFromIntersection(intersection thatIntersection){
        float shortestDist=100000;
        int laneNum=-1;
        for (int i=0; i<thatIntersection.exitPoints.size();i++){
            PVector dirWithLoc = thatIntersection.exitPoints.get(i).thisLane.direction.get();
            if (! thatIntersection.exitPoints.get(i).thisRoad.equals(myRoad)){
            dirWithLoc.setMag(maxSpeed*2);
            dirWithLoc.add(new PVector(thatIntersection.exitPoints.get(i).x,thatIntersection.exitPoints.get(i).y));
            //dirWithLoc.add(location);
            float tDist = dirWithLoc.dist(myTarget);
           // System.out.println("distance lane to target" + tDist + " for vector: " );
            if (tDist<shortestDist ){
                shortestDist=tDist;
                laneNum=i;
            }
            }
        }
        if (laneNum==-1){
            System.out.println("Shiit man -1 laneNum");
        }
        //System.out.println("We think lane with dist " + shortestDist + ", and vector: " + thatIntersection.exitPoints.get(laneNum).thisLane + ". is the best for reaching goal");
        //parent.line(thatIntersection.exitPoints.get(laneNum).x,thatIntersection.exitPoints.get(laneNum).y,myTarget.x +thatIntersection.exitPoints.get(laneNum).thisLane.direction.get().x*10 ,myTarget.y + thatIntersection.exitPoints.get(laneNum).thisLane.direction.get().y*10);
        return laneNum;
    }
    public intersectionPath getIntersectionPaths(intersection thatIntersection){
        //We find out which exit we need then we get a path.
        //Logically our entrypoint must be the closest to our location or we would be driving on another lane.
        int Entry=getBestEntryFromIntersection(thatIntersection);
        int Exit= getBestExitFromIntersection(thatIntersection);
        myLane = thatIntersection.entryPoints.get(Entry).thisLane;
        myRoad = thatIntersection.entryPoints.get(Entry).thisRoad;
        System.out.println("Setting mylane to: " + myLane.direction.toString());
        nextLane = thatIntersection.exitPoints.get(Exit).thisLane;
        nextRoad = thatIntersection.exitPoints.get(Exit).thisRoad;
        EntryPoint= new PVector(thatIntersection.entryPoints.get(Entry).x,thatIntersection.entryPoints.get(Entry).y);

        return thatIntersection.intersectionConnections.get(Entry).get(Exit);

    }
    public void moveAlongRoad(){
        //We move along our currentRoad if we have one
        if (myRoad!=null){
            //First we check if we have a road
            //Then we check the distance to each point and see if we're getting close to an intersection.
            PVector a = myRoad.getNPoint(0);
            PVector b = myRoad.getNPoint(1);


        }
    }
    void useNextRoad(){
        System.out.println("Called useNextRoad");
        myRoad=nextRoad;
        nextRoad=null;
        myLane=nextLane;
        nextLane=null;
    }
    public void followIntersectionPath(intersectionPath currentPath){
        //This logic is for while we are in range of an intersection.

        //On the currentPath we're on we might be driving on a subpath
        /*
            Pseudo-logic:
            See if our speed exceeds the distance remaining of our sub-path (straight or circular)
                Do the position change calculation on our first sub-path for the remaining distance
                Then do the remaining position change calculation for our next subpath.
            Else continue along subpath.
         */
        if (currentPath.subPath.size()==1){
            //We only have one subpath in the intersection.
            System.out.println("We have a single subpath, let's followie?");
            intersectionCircle circleman= (intersectionCircle) currentPath.CircularPath;
            if (circleman!=null) {

                float length = circleman.getRemainingLength(location.x, location.y);
                circleman.setAngularVelocityFromSpeed(maxSpeed);
                System.out.println("currentPath: " + length + "this turn has angle: " + circleman.angle + " and angVelo:" + circleman.getAngularVelocity());
                velocity.setMag(maxSpeed);


                if (length > velocity.mag()) {
                    float nextAngle = circleman.getNextAngle(location.x,location.y);
                    float angVelo= circleman.getAngularVelocity();
                    //We have more road than velocity so we just drive along the circle

                    location.x = circleman.centerX + (float) (circleman.radius*Math.cos(nextAngle));
                    location.y = circleman.centerY + (float) (circleman.radius*Math.sin(nextAngle));
                    parent.line(circleman.xStart,circleman.yStart,circleman.xEnd,circleman.yEnd);
                    parent.circle(circleman.centerX,circleman.centerY,circleman.radius/2);
                    velocity.x =   (float) (-1*angVelo*circleman.radius*Math.sin(nextAngle));
                    velocity.y =   (float) (angVelo*circleman.radius * Math.cos(nextAngle));
                    velocity.setMag(0.01f);
                    //System.out.println("angVelo : " + angVelo + ", rad: " + circleman.radius + " sinAng: " +Math.sin(angVelo));
                    float angleInDegree= (float) (nextAngle*360/(Math.PI*2));
                    parent.text(angleInDegree,50,50);
                    parent.text("dy: " + Math.floor(location.y-circleman.centerY) + " dx: " + Math.floor(location.x-circleman.centerX),50,70);
                    parent.text( "Rem length " + length,250,50);
                    System.out.println("Next angle: " + nextAngle);

                } else {
                    //Our velocity is larger so we only move velocity-length
                    System.out.println("Going lidl bit crazy");
                    float angVelo = circleman.getAngularVelocityFromSpeed(velocity.mag() - length);
                    location.x = location.x + (float) (-1*angVelo*circleman.radius*Math.sin(angVelo));
                    location.y = location.y + (float) (angVelo*circleman.radius * Math.cos(angVelo));
                    //We have to move along the road also now
                    velocity.x = (float) (-1*angVelo*circleman.radius*Math.sin(circleman.angle));
                    velocity.y = (float) (angVelo*circleman.radius * Math.cos(circleman.angle));
                    findNextIntersection();
                    //useNextRoad();
                    myIntersectionPath=null;


                }
                System.out.println(location.toString());
            }else{
                IntersectionStraightPath WeGoStraightNow = (IntersectionStraightPath) currentPath.subPath.get(0);
                if (WeGoStraightNow!=null){
                    //location.x = WeGoStraightNow.x1;
                    //location.y = WeGoStraightNow.y1;
                    velocity =  new PVector(WeGoStraightNow.x2-WeGoStraightNow.x1,WeGoStraightNow.y2-WeGoStraightNow.y1);
                    velocity.setMag(maxSpeed);
                    System.out.println("We teleport a bit");
                    if(nextRoad!=null){
                        findNextIntersection();
                        //myIntersectionPath=getIntersectionPaths(currentIntersection);
                        //useNextRoad();
                        myIntersectionPath=null;


                    }
                    //myIntersectionPath=null;
                }


            }






        }
        if (currentPath.subPath.size()>1){
            //We have more than one subpath for our currentpath
            System.out.println("Nooes");


        }
    }
    void findNextIntersection(){
        //We find the intersection that has our nextRoad in it but isn't our current intersection
        for (int i=0;i<allIntersections.size();i++){
            if( allIntersections.get(i).connectedRoads.contains(nextRoad) && !allIntersections.get(i).equals(currentIntersection)){
                System.out.println("Found next intersection prev was:");
                setIntersection(allIntersections.get(i));
                return;
            }
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
        if (myIntersectionPath!=null){
            //we at an intersection so we follow that path.
            followIntersectionPath(myIntersectionPath);
            System.out.println("Trying to follow intersectionpath?" );


        }else{
            if (myIntersectionPath!=null){
                //System.out.println(myIntersectionPath.subPath.get(0).getClass());
            }
            if (myRoad!=null && myLane!=null) {
                //We're on a road so we follow that road.
                followRoad(myRoad, myLane);
            }else {
                if (nextRoad != null && nextLane != null) {
                    //If we have a next road we should follow it.
                    useNextRoad();
                }
            }


        }
        /*if (myRoad!=null && myLane!=null) {
            followRoad(myRoad, myLane);
        }*/

        update();

        display();
    }
    void setIntersection(intersection newIntersection){
        currentIntersection=newIntersection;
        myIntersectionPath= getIntersectionPaths(currentIntersection);
        //findNextIntersection();

    }



    void display() {
        // Draw a triangle rotated in the direction of velocity
        float theta = (float) (velocity.heading2D() + Math.PI / 2);
        /*
        if (myIntersectionPath!=null && myIntersectionPath.CircularPath!=null){
            intersectionCircle theCircularPath = myIntersectionPath.CircularPath;
            parent.line(theCircularPath.xStart,theCircularPath.yStart,theCircularPath.xEnd,theCircularPath.yEnd);
        }
        if (nextRoad!=null){
            //parent.frameRate(5);
            parent.fill(30,255,0);
            parent.circle(nextRoad.getStart().x,nextRoad.getStart().y,30);
            parent.circle(nextRoad.getEnd().x,nextRoad.getEnd().y,30);
        }
        parent.fill(0,200,200);
        parent.circle(EntryPoint.x,EntryPoint.y,20);
        if (currentIntersection!=null){
            int bestExit= getBestExitFromIntersection(currentIntersection);
            parent.circle(currentIntersection.exitPoints.get(bestExit).x,currentIntersection.exitPoints.get(bestExit).y,35);
        }*/
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
        parent.fill(255);
        parent.ellipse(myTarget.x,myTarget.y,10,10);
        //parent.ellipse(target.x, target.y, 3, 3);


    }


    public void setAllConnections(ArrayList<connection> newConnections) {
        allConnections=newConnections;
    }
    public void setAllIntersections(ArrayList<intersection> newIntersections){
        allIntersections=newIntersections;

    }
}