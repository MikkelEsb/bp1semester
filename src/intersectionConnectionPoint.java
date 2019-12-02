import java.util.ArrayList;

public class intersectionConnectionPoint {
    float x,y;
    lane thisLane;
    road thisRoad;
    intersectionConnectionPoint(float tx,float ty, lane ourLane,road ourRoad){
        x=tx;
        y=ty;
        thisLane=ourLane;
        thisRoad=ourRoad;
    }
}
