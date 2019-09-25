import processing.core.PApplet;
import processing.core.PVector;

public class core extends PApplet{
	vehicle TestOne = new vehicle(1,3, 150,75);
	vehicle TestTwo = new vehicle(150,30, 20,11);
	vehicle TestThree = new vehicle(40,200, 550,95);
	public static void main(String[] args) {
		PApplet.main("core");
		
		
	}
	public void settings() {
		size(1200, 720);
	}
	public void setup() {
		road RoadTest = new road();
		RoadTest.addLane(0,1);
		System.out.println(RoadTest.getLanes().get(0).toString());
		road secondRoad = new road();
		RoadTest.setConnection(new PVector(3,5), secondRoad);
		background(255);
		smooth();
		TestOne.setParent(this);
		TestOne.setVelocity(1, 1);
		TestTwo.setParent(this);
		TestTwo.maxSpeed=7;
		TestTwo.setVelocity(2, 1);
		TestThree.maxSpeed=5;
		TestThree.maxAcceleration=0.5f;
		TestThree.setParent(this);
		TestThree.setVelocity(5, 1);
		
	}
	public void exit() {
		System.out.println("One: [" + TestOne.score + "]. Two: [" + TestTwo.score + "]. Three [" + TestThree.score + "]");
	}
	 public void draw(){
		background(255);
		strokeWeight(2);
		fill(127);
		//Test.setVelocity(1, 2);
		TestOne.update();
		TestOne.display();
		TestTwo.update();
		TestTwo.display();
		TestThree.update();
		TestThree.display();
		
		//stroke(0);
		
		//rect(Test.location.x,Test.location.y,20,30);
    }
	 
}
