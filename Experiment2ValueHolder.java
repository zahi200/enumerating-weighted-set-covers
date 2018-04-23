public class Experiment2ValueHolder {
    private int numOfShouldBeInTop10;
    private double averagePositionOfShouldBeInTop10;
    private int maximalPositionOfShouldBeInTop10;

    public Experiment2ValueHolder(int num, double avgPosition, int maximalPosition){
        this.numOfShouldBeInTop10 = num;
        this.averagePositionOfShouldBeInTop10 = avgPosition;
        this.maximalPositionOfShouldBeInTop10 = maximalPosition;
    }

    public double getAveragePositionOfShouldBeInTop10() {
        return averagePositionOfShouldBeInTop10;
    }

    public int getMaximalPositionOfShouldBeInTop10() {
        return maximalPositionOfShouldBeInTop10;
    }

    public int getNumOfShouldBeInTop10() {
        return numOfShouldBeInTop10;
    }
}
