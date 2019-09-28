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
    float r = 6;
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

    void follow(Path p) {
        // Predict location 50 (arbitrary choice) frames ahead
        PVector predict = velocity.get();
        predict.normalize();
        predict.mult(110); //How far ahead location is on the path, should be farther than the arrival modifier
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
        if (d<100) {
            float m = map(d,0,100,0,maxSpeed); //edit range here
            desired.mult(m);
            //System.out.println("Slowing speed");
        } else {
            desired.mult(maxSpeed);
        }
        PVector steer = PVector.sub(desired, velocity); //see seek
        steer.limit(maxAcceleration);                   //
        applyForce(steer);                              //
    }

    void run() {
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
        //parent.ellipse(target.x, target.y, 3, 3);
    }
}