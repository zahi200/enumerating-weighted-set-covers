import java.util.*;

public class SimpleWorld implements World, Comparable<SimpleWorld> {

    private LinkedHashSet<Expert> previousTeam;   // this is a team that was already printed, and we are willing to extend
    private Expert nextExpert;          // an expert not in previousTeam to be added to it - the union of them is nextSolution
    private LinkedHashSet<Expert> addableExperts; // potential experts for next sub-worlds
    private double nextCost;

    public SimpleWorld(LinkedHashSet<Expert> previousTeam, Expert nextExpert, LinkedHashSet<Expert> addableExperts){
        this.previousTeam = new LinkedHashSet<Expert>();
        this.previousTeam.addAll(previousTeam);
        this.nextExpert = nextExpert;
        this.addableExperts = new LinkedHashSet<Expert>();
        this.addableExperts.addAll(addableExperts);
        this.nextCost = calculateCost();
    }

    // the cost of a solution is simply the sum of the costs of the experts in that solution
    private double calculateCost(){
        double cost = this.nextExpert.getCost();
        for(Expert expert: previousTeam){
            cost += expert.getCost();
        }
        return cost;
    }

    public LinkedHashSet<Expert> getAddableExperts() {
        LinkedHashSet<Expert> addableExpertsSet = new LinkedHashSet<Expert>();
        addableExpertsSet.addAll(this.addableExperts);
        return addableExpertsSet;
    }

    @Override
    public double getNextCost(){
        return this.nextCost;
    }

    @Override
    public LinkedHashSet<Expert> getNextSolution() {
        LinkedHashSet<Expert> experts = new LinkedHashSet<Expert>();
        experts.addAll(this.previousTeam);
        experts.add(this.nextExpert);
        return experts;
    }

    public Set<SimpleWorld> getDivisionToWorlds() {
        Set<SimpleWorld> newWorlds = new HashSet<SimpleWorld>();
        SimpleWorld newWorld1, newWorld2;

        LinkedHashSet<Expert> T = new LinkedHashSet<Expert>();
        T.addAll(this.previousTeam);
        Expert e = this.nextExpert;
        LinkedHashSet<Expert> E_o  = new LinkedHashSet<Expert>();
        E_o.addAll(this.addableExperts);

        if(E_o.isEmpty()){
            return newWorlds; // which is empty
        }

        Expert e_tag = getMinExpert(E_o);
        E_o.remove(e_tag);

        newWorld1 = new SimpleWorld(T, e_tag, E_o);
        T.add(e);
        newWorld2 = new SimpleWorld(T, e_tag, E_o);

        newWorlds.add(newWorld1);
        newWorlds.add(newWorld2);
        return newWorlds;
    }

    private Expert getMinExpert(Set<Expert> E_o) {
        double minExpertCost = Double.POSITIVE_INFINITY;
        Expert minExpert = null;
        for(Expert expert:E_o){
            if(expert.getCost() < minExpertCost){
                minExpert = expert;
                minExpertCost = expert.getCost();
            }
        }
        return minExpert;
    }

    @Override
    //Returns: a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
    public int compareTo(SimpleWorld other) {
        return (int)Math.signum(this.getNextCost()-other.getNextCost());
    }

}