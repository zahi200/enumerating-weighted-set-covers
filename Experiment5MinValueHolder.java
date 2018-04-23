public class Experiment5MinValueHolder {
    private int countDumpedResults;
    private int maximalPosition;
    private int countPrintedResult;

    public Experiment5MinValueHolder(int maximalPosition, int countDumpedResults, int countPrintedResult){
        this.maximalPosition = maximalPosition;
        this.countDumpedResults = countDumpedResults;
        this.countPrintedResult = countPrintedResult;
    }

    public int getCountDumpedResults() {
        return countDumpedResults;
    }

    public int getMaximalPosition() {
        return maximalPosition;
    }

    public int getCountPrintedResult() {
        return countPrintedResult;
    }
}
