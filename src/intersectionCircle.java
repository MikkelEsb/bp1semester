public class intersectionCircle {
    float radius,angle,xStart,yStart,xEnd,yEnd,totalLength;
    public intersectionCircle(float x1,float y1,float x2,float y2){
        //To define a section of a circle we either need the start and end point of the circle
        //Or we would need to define the start radians and end radians and the radius.
        //Our circle will be defined by coordinates and then we calculate the radius and the angle
        float deltaX=x2-x1;
        float deltaY=y2-y1;
        radius=Math.abs(deltaX);
        angle=(float)Math.atan2(deltaY,deltaX);
        xStart=x1;
        xEnd=x2;
        yStart=y1;
        yEnd=y2;
        totalLength=angle*radius;
    }
    public float getAngularVelocity(float speed){
        //calculates the angular velocity needed for a certain speed.
        return speed/radius;
    }
    public void setAngularVelocityFromSpeed(float speed){
        float angularVelocity = getAngularVelocity(speed);

    }
    public float getLength(){
        //Length of whole circle Is 2Pi*r but we only have part of the circle so angle/2Pi: (2*Math.PI*radius * angle/(2*Math.PI));
        //Therefor our length is angle*radius
        return totalLength;
    }
    public float getRemainingLength(float x,float y){
        //We get the current angle our point is at and then multiply it by the radius to see the length we have gone and then we
        float angleOfCircle=(float)Math.atan2(x-xStart,y-yStart);
        float remainingLength=totalLength- angleOfCircle*radius;
        return remainingLength;
    }
}