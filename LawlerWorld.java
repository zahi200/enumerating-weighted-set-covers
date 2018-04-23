import java.util.*;

public class LawlerWorld implements World, Comparable<LawlerWorld> {

    private LinkedHashSet<Expert> nextSolution;   // the next solution to print
    private LinkedHashSet<Expert> fixedExperts;   // the experts that are fixed in any sub world of this world
    private LinkedHashSet<Expert> addableExperts; // the experts that can be added to teams in sub worlds of this world
    double nextCost;

    public LawlerWorld(LinkedHashSet<Expert> nextSolution, LinkedHashSet<Expert> fixedExperts, LinkedHashSet<Expert> addableExperts){
        this.nextSolution =  new LinkedHashSet<Expert>();
        this.nextSolution.addAll(nextSolution);
        this.fixedExperts = new LinkedHashSet<Expert>();
        this.fixedExperts.addAll(fixedExperts);
        this.addableExperts = new LinkedHashSet<Expert>();
        this.addableExperts.addAll(addableExperts);
        this.nextCost = calculateCost();
    }

    public LinkedHashSet<Expert> getFixedExperts() {
        LinkedHashSet<Expert> fixedExpertsSet = new LinkedHashSet<Expert>();
        fixedExpertsSet.addAll(this.fixedExperts);
        return fixedExpertsSet;
    }

    public LinkedHashSet<Expert> getAddableExperts() {
        LinkedHashSet<Expert> addableExpertsSet = new LinkedHashSet<Expert>();
        addableExpertsSet.addAll(this.addableExperts);
        return addableExpertsSet;
    }

    // the cost of a solution is simply the sum of the weights of the sets in that solution
    private double calculateCost(){
        double cost = 0;
        for(Expert expert: nextSolution){
            cost += expert.getCost();
        }
        return cost;
    }

    @Override
    public double getNextCost() {
        return this.nextCost;
    }

    @Override
    public LinkedHashSet<Expert> getNextSolution() {
        LinkedHashSet<Expert> experts = new LinkedHashSet<Expert>();
        experts.addAll(nextSolution);
        return experts;
    }

    // S - all the skills to be covered (all all of them!)
    public Set<LawlerWorld> getDivisionToWorlds(Set<Skill> S){
        Set<LawlerWorld> newWorlds = new HashSet<LawlerWorld>();
        LinkedHashSet<Expert> nextTeam, T_tag, dividingExperts, E_f, E_o;
        Set<Skill> S_tag;
        LawlerWorld newWorld;

        E_f = new LinkedHashSet<Expert>();
        E_f.addAll(this.fixedExperts); // E_f is a copy of this.fixedExperts
        dividingExperts = new LinkedHashSet<Expert>(); // the experts that make the division
        dividingExperts.addAll(nextSolution); // nextSolution (in code) == T (in pseudo-code)
        dividingExperts.removeAll(E_f);
        E_o = new LinkedHashSet<Expert>();
        E_o.addAll(this.addableExperts); // E_o is a copy of this.addableExperts

//        if (addableExperts.isEmpty()){
//            return newWorlds; // which is empty
//        }

        for(Expert e_i: dividingExperts){
            E_o.remove(e_i);

            S_tag = new HashSet<Skill>();
            S_tag.addAll(S);
            S_tag.removeAll(getSetSkills(E_f));

            T_tag = ApproxMinCostTeam.calc(S_tag, E_o);

            if (T_tag != null){
                nextTeam = new LinkedHashSet<Expert>();
                nextTeam.addAll(T_tag);
                nextTeam.addAll(E_f);
                newWorld = new LawlerWorld(nextTeam, E_f, E_o);
                newWorlds.add(newWorld);
            }

            if (S_tag.isEmpty()){
                break;
            }

            E_f.add(e_i);
        }
        return newWorlds;
    }

    private Set<Skill> getSetSkills(Set<Expert> experts){
        Set<Skill> skills = new HashSet<Skill>();
        for(Expert expert:experts){
            skills.addAll(expert.getSkills());
        }
        return skills;
    }

    @Override
    //Returns: a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
    public int compareTo(LawlerWorld other) {
        return (int)Math.signum(this.getNextCost()-other.getNextCost());
    }

}
