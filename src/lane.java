import processing.core.PVector;

//Lanes are part of the road so there can be multiple lanes per road
//Each lane will be restricted to a direction
/*
 * A lane is the subsection of our road object
 * Geometrically the lanes are running parralel to each other
 * Either we will have to divide the width of the entire road for each lane
 *  and divide themevenly
 * Or we will define the width of each lane and extend the road to fit them all
 *
 *
 */
public class lane {

    PVector direction;

    //This direction could be used as "speed-limit"
    //Or we could simply normalize the vector
    lane(float x, float y) {
        direction = new PVector(x, y);
    }

    @Override
    public String toString() {
        return "lane [direction=" + direction + "]";
    }

    void setDirection(float x, float y) {
        direction.set(x, y);
    }

    void setSpeedLimit(float v) {
        direction.setMag(v);
    }

    float getSpeedLimit() {
        return direction.mag();
    }

    void reverseDirection() {
        direction.set(direction.x * -1, direction.y * -1);
    }
}
