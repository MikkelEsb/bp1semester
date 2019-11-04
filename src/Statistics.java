import java.util.ArrayList;

public class Statistics {
    ArrayList<Float> vectorLengths;
    int average;
    int i = 1;
    float x;
    int t = 60; //how much data stored in array measured in time interval
    int N;      //define this as the number of vehicles in the system, this probably to remain constant
    ArrayList<Integer> movingAverage;

    //Constructs statistics as an array of floats that should be equal to the length of the vector of whatever vehicle
    public Statistics(){
        vectorLengths = new ArrayList<Float>(t*N);
        movingAverage = new ArrayList<>();
    }

    //add your stats to the arraylist using the following command
    public void addStats(float x) {
        if (vectorLengths.size() > t * N) {
            vectorLengths.add(x);

        } else {
            vectorLengths.set(i,x);
            i = i++%60;

            if(i == 59){
                movingAverage.add(average());
            }
        }
    }
    //prints your average
    public int average(){
        for(int i = 0; i < vectorLengths.size(); i++) {
            x = x + vectorLengths.get(i);                //sums all the lengths and assigns them to memory at x.
        }
        average = (int) (x / vectorLengths.size());      //calculates the average according to the size of your data, time interval should matter somewhere
        return(average);
    }

    //return functions
    public ArrayList<Float> getStatistics(){return vectorLengths;}
    public ArrayList<Integer> getMovingAverage(){return movingAverage;}
    public float currentAverage(){return average;}
}
