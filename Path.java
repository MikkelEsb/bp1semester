import processing.core.PApplet;
import processing.core.PVector;

public class Path {
    // A Path is line between two points (PVector objects)
    PApplet parent;
    PVector start;
    PVector end;
    // A path has a radius, i.e how far is it ok for the vehicle to wander off
    float radius;

    Path() {
        // Arbitrary radius of 20, indicates path size, also limits vehicles wander.
        radius = 20;
        start = new PVector(0, 0);
        end = new PVector(1200, 700);
    }

    void setParent(PApplet p) {
        parent = p;
    }

    // Draw the path
    void display() {

        parent.strokeWeight(radius * 2);
        parent.stroke(0, 100);
        parent.line(start.x, start.y, end.x, end.y);

        parent.strokeWeight(1);
        parent.stroke(0);
        parent.line(start.x, start.y, end.x, end.y);
    }
}
