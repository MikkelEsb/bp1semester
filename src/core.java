import processing.core.PApplet;
import processing.core.PVector;


public class core extends PApplet {
    vehicle TestOne = new vehicle(1, 3, 150, 75);
    vehicle TestTwo = new vehicle(150, 30, 1000, 500);
    vehicle TestThree = new vehicle(40, 200, 550, 95);
    road testRoad = new road();
    road secondRoad = new road();
    road thirdRoad = new road();
    Path path;

    public static void main(String[] args) {
        PApplet.main("core");


    }

    public void settings() {
        size(1200, 720);
    }

    public void setup() {
		/*road RoadTest = new road();
		RoadTest.addLane(0,1);
		System.out.println(RoadTest.getLanes().get(0).toString());
		road secondRoad = new road();
		RoadTest.setConnection(new PVector(3,5), secondRoad);
		*/
        background(255);
        smooth();
        path = new Path();
        path.setParent(this);
        TestOne.setParent(this);
        TestOne.setVelocity(1, 1);
        TestOne.follow(path);

        testRoad.addPoint(1,1);
        testRoad.addPoint(500,200);
        testRoad.addLane(1,1/2);
        testRoad.setParent(this);

        secondRoad.addPoint(500,200);
        secondRoad.addPoint(10,700);
        secondRoad.generateLanes(3,2);
        secondRoad.setParent(this);

        thirdRoad.addPoint(500,200);
        thirdRoad.addPoint(1000,700);
        thirdRoad.generateLanes(3,2);
        thirdRoad.setParent(this);

        testRoad.setConnection(new PVector(500,200),secondRoad);
        secondRoad.setConnection(new PVector(500,200),testRoad);

        testRoad.setConnection(new PVector(500,200),thirdRoad);


        TestTwo.setParent(this);
        TestTwo.setVelocity(1,1);
        TestTwo.colour=0;
        TestTwo.setRoad(testRoad,testRoad.lanes.get(0));
        //TestTwo.followRoad(testRoad,testRoad.lanes.get(0));
		/*TestTwo.setParent(this);
		TestTwo.maxSpeed=7;
		TestTwo.setVelocity(2, 1);
		TestThree.maxSpeed=5;
		TestThree.maxAcceleration=0.5f;
		TestThree.setParent(this);
		TestThree.setVelocity(5, 1);
		*/
    }

    public void exit() {
        System.out.println("One: [" + TestOne.score + "]. Two: [" + TestTwo.score + "]. Three [" + TestThree.score + "]");
    }

    public void draw() {
	surface.setResizable(true); //TODO switch all screen dependant variables to width, height respectively.
        background(255);
        strokeWeight(2);
        fill(127);
        //Test.setVelocity(1, 2);
        path.display();
        TestOne.follow(path);
        TestOne.run();
        testRoad.display();
        secondRoad.display();
        thirdRoad.display();
        TestTwo.run();
        clock();
        /*
		TestThree.update();
		TestThree.display();
		*/
        //stroke(0);

        //rect(Test.location.x,Test.location.y,20,30);

    }

    private void clock(){
        int m = millis();
        textSize(32);
        int s = m/1000;
        text("Seconds elasped " + s, 10, 30);

    }

}
