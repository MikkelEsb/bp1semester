import java.util.ArrayList;

public class intersectionPath
{
    ArrayList subPath = new ArrayList<>(); // Should only be initilaized if we actually have multiple subpaths?
    public intersectionPath(float startX,float startY,float endX,float endY,float speed){
        //First we determine if the path is direct
        if (startX==endX || startY==endY) {
            subPath.add(new IntersectionStraightPath(startX,startY,endX,endY));
        }else{
            float deltaX=endX-startX;
            float deltaY=endY-startY;
            //This might be be a partly circular motion or only a circular motion
            if(Math.abs(deltaX)==Math.abs(deltaY)){
                //Since the distance between the two X points and the distance between the two Y points is equal we can make this into a circle.
                intersectionCircle currentCircle = new intersectionCircle(startX,startY,endX,endY);
                subPath.add(currentCircle);
                System.out.println("Angular velocity for cicle with speed 2: " + currentCircle.getAngularVelocity(2));

            }else{
                System.out.println("Partial circle?");
                //Ohh noes we have to think??
                //We find the coordinate x or y that has the smallest change.
                //The coordinate with the smallest change
                //TODO: Figure out logic for partial circules
            }
        }


    }
    public void getSubPathForCoords(float x,float y){
        //Not sure how to do this but to figure out what subpath we're on we should check the subpath that is a line and see if we're within the bounds?

    }

}
