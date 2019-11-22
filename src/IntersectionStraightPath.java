public class IntersectionStraightPath {
    //This is a rather simple path defined by two coordinates.
    float x1,y1,x2,y2,totalLength;
    public IntersectionStraightPath(float xStart,float yStart,float xEnd,float yEnd){
        x1=xStart;
        y1=yStart;
        x2=xEnd;
        y2=yEnd;
        totalLength=(float) Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));

    }

}
