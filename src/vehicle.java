
import processing.core.PVector;
import processing.core.*;


public class vehicle {
	PApplet parent;
	PVector location;
	PVector velocity;
	PVector acceleration;
	PVector target;
	PVector steer;
	float r = 6;
	float maxSpeed=3;
	float maxAcceleration=0.05f;
	int score=0;
	int colour=177;
	public vehicle(float x, float y, float t_x, float t_y) {
		location		= new PVector(x,y);
		velocity		= new PVector(0,0); 
		acceleration	= new PVector(0,0);
		target			= new PVector(t_x,t_y);
		steer			= new PVector(0,0);
	}
	void setParent(PApplet p) {
		parent=p;
	}
	void setVelocity(float x, float y) {
		velocity.x	=	x;
		velocity.y	=	y;
	}
	void setAccelration(float x, float y) {
		acceleration.x	=	x;
		acceleration.y	=	y;
	}
	void update() {
		//Update velocity from acceleration
		//Update location from velocity
		if (location.dist(target)<maxSpeed) {
			this.score=this.score+1;
			target.x = (float) (Math.random()*1200);
			target.y = (float) (Math.random()*720);
			//velocity.mult(0);
		} else {
			//velocity.y=(float) 0.9;
			seek();
			
		}
		
		velocity.add(acceleration);
		velocity.limit(maxSpeed);
		location.add(velocity);
		acceleration.mult(0); //As the acceleration force has been applied we set the acceleration back to 0
		if (location.x > 1200) {
			location.x	=	1;
			
		}
		if (location.x<0){
			location.x=1200;
		}
		if (location.y > 720) {
			//location.x	=	location.x + 20;
			location.y	=	1;
		}
		if (location.y<0) {
			location.y=720;
		}
		
		//System.out.println(location);
	}
	void applyForce(PVector force) {
		acceleration.add(force);
	}
	void seek() {
		
		
		PVector desired = PVector.sub(target,location); //Creates a vector between current location and target location
		desired.normalize(); //Sets the vector length to 1
		desired.mult(maxSpeed); //Multiplies the vector to be maxSpeed as length
		
		PVector steer	= PVector.sub(desired, velocity); 
		steer.limit(maxAcceleration);
		applyForce(steer);
		
		
	}
	  void display() {
		// Draw a triangle rotated in the direction of velocity
		float theta = (float) (velocity.heading2D() + Math.PI/2);
		parent.fill(this.colour);
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.pushMatrix();
		parent.translate(location.x,location.y);
		parent.rotate(theta);
		parent.beginShape();
		parent.vertex(0, -r*2);
		parent.vertex(-r, r*2);
		parent.vertex(r, r*2);
		parent.endShape();
		parent.popMatrix();
		parent.ellipse(target.x, target.y, 3, 3);

	  }
	
	
	
	
}