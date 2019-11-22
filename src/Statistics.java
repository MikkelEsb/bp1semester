import java.util.ArrayList;

class Statistics {
    private ArrayList<Integer> movingAverageSpeed;
    private ArrayList<Integer> movingAverageDensity;
    private ArrayList<Integer> movingAverageFlow;
    private ArrayList<Float> vectorLengths;
    private int averageSpeed;
    private int averageFlow;
    private int averageDensity;
    private int t = 60; //how much data stored in array measured in time interval
    private int j = 0;
    private int N;

    //Constructs statistics as an array of floats that should be equal to the length of the vector of whatever vehicle
    Statistics(int carCount){
        N = carCount;
        vectorLengths        = new ArrayList<Float>(t*N);
        movingAverageSpeed   = new ArrayList<Integer>();
        movingAverageDensity = new ArrayList<Integer>();
        movingAverageFlow    = new ArrayList<Integer>();
    }

    //add your stats to the arraylist using the following command
    public void addStats(float speed) {
        if (vectorLengths.size() < t * N) {
            vectorLengths.add(speed);

        } else {
            vectorLengths.set(j,speed);
            j = j++%60;

            if(j == 59){
                movingAverageSpeed.add(averageSpeed());
                movingAverageDensity.add(averageDensity());
                movingAverageFlow.add(averageFlow());
            }
        }
    }

    //prints your average
    private int averageSpeed(){
        float x = 0;
        for(int i = 0; i == vectorLengths.size(); i++) {
            x = x + vectorLengths.get(i);                //sums the lengths of all vehicles and assigns them to memory at x.
        }
        averageSpeed = (int) (x / vectorLengths.size());      //calculates the average according to the size of your data, time interval should matter somewhere
        return(averageSpeed);
    }

    //prints average density for the iteration, is calculated from road start to road end, to intersection.
    private int averageDensity(){
        for(int i = 0; i < vectorLengths.size(); i++) {

        }
        return(averageDensity);
    }

    //prints average flow for the iteration
    private int averageFlow(){
        for(int i = 0; i < vectorLengths.size(); i++){
            averageFlow = averageDensity * averageSpeed;
        }
        return(averageFlow);
    }

    //return functions
    public ArrayList<Float> getStatistics(){return vectorLengths;}
    public ArrayList<Integer> getMovingAverageSpeed(){return movingAverageSpeed;}
    public ArrayList<Integer> getMovingAverageFlow(){return movingAverageFlow;}
    public ArrayList<Integer> getMovingAverageDensity(){return movingAverageDensity;}
}
