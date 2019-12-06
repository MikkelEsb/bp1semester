public class intersectionCircle {
    float radius,angle,angleStart,angleEnd,xStart,yStart,xEnd,yEnd,totalLength,angularVelocity,centerX,centerY;
    public intersectionCircle(float x1,float y1,float x2,float y2, intersectionConnectionPoint exit,float turnAngle){
        //To define a section of a circle we either need the start and end point of the circle
        //Or we would need to define the start radians and end radians and the radius.
        //Our circle will be defined by coordinates and then we calculate the radius and the angle

        if (exit.thisLane.direction.x!=0){
            //Change in X
            centerX=x2;
            centerY=y1;
        }else{
            centerX=x1;
            centerY=y2;
        }
        float deltaX=x2-x1;
        float deltaY=y2-y1;
        radius=Math.abs(deltaX);
        //angle= (float) Math.atan2(y2-centerY,x2-centerX);
        angleEnd = (float) ((Math.atan2(y2-centerY,x2-centerX)));
        if(angleEnd<0){ angleEnd+=(Math.PI*2);}

        angleStart = (float) (Math.atan2(y1-centerY,x1-centerX));
        if(angleStart<0){ angleStart+=(Math.PI*2);}
        System.out.println("AngleEnd :[" + angleEnd + "] dy2:[ " + (y2-centerY) + "] dx2" + (x2-centerX) +  " ] angleStart: [" + angleStart + "] dy1: [" + (y1-centerY) + "] dx1: [" + (x1-centerX) +"]" );
        angle= angleEnd-angleStart;
        if ((angle)<-Math.PI){
           angle+=Math.PI*2;
        }
        
        if ((angle)>Math.PI){
            angle-=Math.PI*2;
        }
        /*if (angle>0){
            angle= (float) (Math.PI/2);
        }else{
            angle= (float) (-1*Math.PI/2);
        }*/
        xStart=x1;
        xEnd=x2;
        yStart=y1;
        yEnd=y2;
        totalLength=Math.abs(angle*radius);
        System.out.println("Circle constructed from Angle " + angle + "p1:[ " + x1 + ", " +y1 + "]  p2: [ " + x2 + ", " +y2 +"]. DeltaX:[" + deltaX +"] Y: ["+deltaY +"] c:" + centerX + ", " +centerY + " turnangle: " + turnAngle);
    }
    public float getAngularVelocityFromSpeed(float speed){
        //calculates the angular velocity needed for a certain speed.

        return speed/(radius*angle);
    }
    public float getRadius(){
        return radius;
    }
    public void setAngularVelocityFromSpeed(float speed){
        angularVelocity = getAngularVelocityFromSpeed(speed);

    }
    public float getAngularVelocity(){
        return angularVelocity;
    }
    public float getLength(){
        //Length of whole circle Is 2Pi*r but we only have part of the circle so angle/2Pi: (2*Math.PI*radius * angle/(2*Math.PI));
        //Therefor our length is angle*radius
        return totalLength;
    }
    public float getRemainingLengthFromAngle(float angle){
        return Math.abs(totalLength-angle*radius);
    }
    public float getNextAngle(float x, float y){
        float angleOfCircle=(float) Math.atan2(y-centerY,x-centerX) + angularVelocity;
        return angleOfCircle;
    }
    public float getRemainingLength(float x,float y){
        //We get the current angle our point is at and then multiply it by the radius to see the length we have gone and then we
        float angleLeft= (float) ( Math.atan2(y-centerY,x-centerX));
        if (angleLeft<0){angleLeft+=Math.PI*2;}
        float remainingLength= (Math.abs(angleEnd-angleLeft))*radius;
        System.out.println("Angle End: " + angleEnd + ", AngleLeft: " + angleLeft + "AngleStart: " + angleStart + "dY: " + (y-centerY) + ", dX: " + (x-centerX));
        System.out.println("Remaining length: " + remainingLength + " angle: " + (angleEnd-angleLeft));
        return remainingLength;
    }

}