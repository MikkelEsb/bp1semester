import java.util.ArrayList;

public class Statistics {
    ArrayList<Float> vectorlength;
    int average;
    int i;
    float x;

    public Statistics(){vectorlength = new ArrayList<Float>(60);
    }
    //add your stats to the arraylist using the following command
    void addStats(float x){
        vectorlength.add(x);
    }
    //prints your average
    int average(){
        for(int i = 0; i < vectorlength.size(); i++){
            x = x + vectorlength.get(i);                //sums all the lengths and assigns them to memory at x.
        }
        average = (int) (x / vectorlength.size());      //calculates the average according to the size of your data, time interval should matter somewhere
        return(average);
    }
    //sanity control
    public ArrayList<Float> getStatistics(){return vectorlength;}
}
