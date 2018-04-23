public class Experiment1ValueHolder {

    private double top1TeamCost;
    private int top1TeamCardinality;

    public Experiment1ValueHolder(double top1TeamCost, int top1TeamCardinality){
        this.top1TeamCost = top1TeamCost;
        this.top1TeamCardinality = top1TeamCardinality;
    }

    public double getTop1TeamCost() {
        return top1TeamCost;
    }

    public int getTop1TeamCardinality() {
        return top1TeamCardinality;
    }
}
