import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

public class Statistics {
    PApplet parent;
    ArrayList<Integer> vectorlength;
    int average;
    int i;

    public Statistics(){
        vectorlength = new ArrayList<Integer>(60);
    }
    void addStats(int x){
        vectorlength.add(x);
    }
    void average(ArrayList<Integer> vectorlength, int y){
        //for(int i, )
       average = vectorlength.get(y)/y; //TODO fix this with a good forloop
    }
    public int getAverage(){return average;}
    public ArrayList<Integer> getStatistics(){return vectorlength;}
}
