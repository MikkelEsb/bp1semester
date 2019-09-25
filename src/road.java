import java.util.ArrayList;
import processing.core.PVector;


public class road {
	//We define the road as points in an array
	ArrayList<PVector> points;
	//Each point is the entry/exit of the road.
	ArrayList<lane> lanes;
	ArrayList<connection> connections;
	
	
	public road() {
		points= new ArrayList<PVector>();
		lanes= new ArrayList<lane>();
		connections = new ArrayList<connection>();
		//On construction we create the arraylist which contains the points
		
	}
	void addPoint(float x,float y) {
		//First we create a vector at (x,y)
		PVector point = new PVector(x,y);
		//Then we add our vector to our array of vectors
		points.add(point);
	}
	connection getConnection(PVector connectingPlace) {
		 for (int i = 0; i < connections.size(); i++) {
			 if (connections.get(i).connectingPoint.dist(connectingPlace)<1) { //crudely using distance of 1 as the way to figure out if connecting
				 return connections.get(i);
			 }
		 }
		//Can't always return connection, must handle this edge case somehow.
		return null;
	}
	public ArrayList<PVector> getPoints() {
		return points;
	}
	public ArrayList<lane> getLanes() {
		return lanes;
	}
	public ArrayList<connection> getConnections() {
		return connections;
	}
	public void setPoints(ArrayList<PVector> points) {
		this.points = points;
	}
	public void setLanes(ArrayList<lane> lanes) {
		this.lanes = lanes;
	}
	public void setConnections(ArrayList<connection> connections) {
		this.connections = connections;
	}
	void setConnection(PVector connectingPlace,road connectingRoad) {
		connection newCon = new connection(connectingPlace);
		connections.add(newCon);
		//connections.get(0).addRoad(connectingRoad);//TODO BAD
		
	}
	void addLane(float x, float y) {
		lane newLane = new lane(x,y);
		lanes.add(newLane);
	}
	PVector getNPoint(int n) {
		return points.get(n);
	}
	PVector getStart() {
		return getNPoint(0);
	}
	PVector getEnd() {
		return getNPoint(points.size()-1);
	}
}

