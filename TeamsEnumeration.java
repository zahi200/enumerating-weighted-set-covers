import java.util.*;

public class TeamsEnumeration {

    private LinkedHashSet<Expert> experts; // all available experts
    private Set<Skill> skills;   // elements to cover by the sets

    public TeamsEnumeration(LinkedHashSet<Expert> experts, Set<Skill> skills) {
        this.experts = new LinkedHashSet<Expert>();
        this.experts.addAll(experts);
        this.skills = new HashSet<Skill>();
        this.skills.addAll(skills);
    }

    public void enumerate(int numOfResults){
        LinkedHashSet<Expert> E = new LinkedHashSet<Expert>();
        E.addAll(experts);
        Set<Skill> S = new HashSet<Skill>();
        S.addAll(skills);

        PriorityQueue<LawlerWorld> Q1 = new PriorityQueue<LawlerWorld>();
        PriorityQueue<SimpleWorld> Q2 = new PriorityQueue<SimpleWorld>();

        double cost1, cost2;
        int countPrintedResult=0;

        System.out.println("Start enumerating (without printing):");

        LinkedHashSet<Expert> T = ApproxMinCostTeam.calc(S, E);
        if(T!=null){
            Q1.add(new LawlerWorld(T, new LinkedHashSet<Expert>(), E));
        }
        else{
            System.out.println("No team satisfies S");
        }

        while((!Q1.isEmpty() || !Q2.isEmpty()) && countPrintedResult<numOfResults){
            cost1 = getNextCost(Q1);
            cost2 = getNextCost(Q2);

            if(cost1 <= cost2){ // this condition includes the case that Q2 is empty
                LawlerWorld lawlerWorld = Q1.poll();
                LinkedHashSet<Expert> nextTeam = lawlerWorld.getNextSolution();
//                printTeam(nextTeam, cost1);

                LinkedHashSet<Expert> E_o = lawlerWorld.getAddableExperts();
                E_o.removeAll(nextTeam);
                if(!E_o.isEmpty()){
                    Expert e = getMinExpert(E_o);
                    E_o.remove(e);
                    Q2.add(new SimpleWorld(nextTeam, e, E_o));
                }

                Set<LawlerWorld> newLawlerWorlds = lawlerWorld.getDivisionToWorlds(S);
                Q1.addAll(newLawlerWorlds);
            }

            else {
                SimpleWorld simpleWorld = Q2.poll();
                LinkedHashSet<Expert> nextTeam = simpleWorld.getNextSolution();
//                printTeam(nextTeam, cost2);
                Set<SimpleWorld> newSimpleWorlds = simpleWorld.getDivisionToWorlds();
                Q2.addAll(newSimpleWorlds);
            }
            countPrintedResult++;
        }
        System.out.println("Done enumerating!");
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

    private void printTeam(LinkedHashSet<Expert> team, double cost) {
        System.out.println(team.toString() + " cost = " + cost);
    }

    private double getNextCost(PriorityQueue<? extends World> q) {
        if (q.isEmpty()){
            return Double.POSITIVE_INFINITY;
        }
        World world = q.peek();
        return world.getNextCost();
    }

    private LinkedHashSet<Expert> subtractSets(LinkedHashSet<Expert> main, LinkedHashSet<Expert> part) {
        LinkedHashSet<Expert> result = new LinkedHashSet<Expert>();
        result.addAll(main);
        result.removeAll(part);
        return result;
    }


/*
    public static void main (String[] args){

        // SKILLS
        Skill s1 = new Skill("s1");
        Skill s2 = new Skill("s2");
        Skill s3 = new Skill("s3");
        Skill s4 = new Skill("s4");
        Skill s5 = new Skill("s5");
        Skill s6 = new Skill("s6");
        Skill s7 = new Skill("s7");

        Set<Skill> requiredSkills = new HashSet<Skill>(Arrays.asList(s1,s2,s3));

        // EXPERTS
        Set<Skill> e1Skills = new HashSet<Skill>(Arrays.asList(s1));
        Expert e1 = new Expert(1, e1Skills, "e1");

        Set<Skill> e2Skills = new HashSet<Skill>(Arrays.asList(s1,s2));
        Expert e2 = new Expert(4, e2Skills, "e2");

        Set<Skill> e3Skills = new HashSet<Skill>(Arrays.asList(s1,s2,s3));
        Expert e3 = new Expert(9, e3Skills, "e3");

        Set<Skill> e4Skills = new HashSet<Skill>(Arrays.asList(s2,s3));
        Expert e4 = new Expert(8, e4Skills, "e4");

        LinkedHashSet<Expert> availableExpert = new LinkedHashSet<Expert>(Arrays.asList(e1,e2,e3,e4));


        TeamsEnumeration enumerator = new TeamsEnumeration(availableExpert, requiredSkills);
        enumerator.enumerate(100);

    }
*/
}