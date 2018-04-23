public class Experiment3MinValueHolder {
    private int countDumpedResults;
    private int countPrintedResult;

    public Experiment3MinValueHolder(int countDumpedResults, int countPrintedResult){
        this.countDumpedResults = countDumpedResults;
        this.countPrintedResult = countPrintedResult;
    }

    public int getCountDumpedResults() {
        return countDumpedResults;
    }

    public int getCountPrintedResult() {
        return countPrintedResult;
    }
}
