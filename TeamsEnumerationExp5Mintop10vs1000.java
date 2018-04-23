import java.util.*;

public class TeamsEnumerationExp5Mintop10vs1000 {

    public static final int NUM_TOP = 10;

    private LinkedHashSet<Expert> experts; // all available experts
    private Set<Skill> skills;   // elements to cover by the sets

    public TeamsEnumerationExp5Mintop10vs1000(LinkedHashSet<Expert> experts, Set<Skill> skills) {
        this.experts = new LinkedHashSet<Expert>();
        this.experts.addAll(experts);
        this.skills = new HashSet<Skill>();
        this.skills.addAll(skills);
    }

    // returns the index of the maximal element among the top 1000 that should be inside top 10
    public Experiment5MinValueHolder enumerateMinimally(int numOfResults, String experimentName) {
        HashSet<TeamOfExperts> printedTeams = new HashSet<TeamOfExperts>(); // for heuristic 3 - container of all printed teams
        int countDumpedResults = 0;
        int countPrintedResults = 0;
        int maximalPosition = 0;
        double cost;
        Experiment5MinValueHolder result;
        PriorityQueue<Double> top10CostsQueue = new PriorityQueue<>(NUM_TOP, Collections.reverseOrder());

        LinkedHashSet<Expert> E = new LinkedHashSet<Expert>();
        E.addAll(experts);
        Set<Skill> S = new HashSet<Skill>();
        S.addAll(skills);

        PriorityQueue<LawlerWorld> Q1 = new PriorityQueue<LawlerWorld>();
        System.out.println("Start minimally enumerating Ex5:");

        LinkedHashSet<Expert> T = ApproxMinCostTeam.calc(S, E);
        if (T != null) {
            Q1.add(new LawlerWorld(T, new LinkedHashSet<Expert>(), E));
        } else {
            System.out.println("No team satisfies S");
        }

        while ((!Q1.isEmpty()) && countPrintedResults < numOfResults) {
            cost = getNextCost(Q1);
            LawlerWorld lawlerWorld = Q1.poll();
            LinkedHashSet<Expert> nextTeam = lawlerWorld.getNextSolution();

            // minimize the experts in nextTeam that are not fixed (heuristic 2)
            LinkedHashSet<Expert> fixedExperts = lawlerWorld.getFixedExperts();
            LinkedHashSet<Expert> nonFixedExperts = new LinkedHashSet<Expert>();
            nonFixedExperts.addAll(nextTeam);
            nonFixedExperts.removeAll(fixedExperts);
            Set<Skill> remainingSkills = new HashSet<Skill>();
            remainingSkills.addAll(this.skills);
            remainingSkills.removeAll(getSetSkills(fixedExperts));
            LinkedHashSet<Expert> minimizedPart = minimizeTeam(nonFixedExperts, remainingSkills);
            LinkedHashSet<Expert> newTeam = new LinkedHashSet<Expert>();
            newTeam.addAll(fixedExperts);
            newTeam.addAll(minimizedPart);

            // minimize newTeam if needed and check if it was already printed (heuristic 3)
            LinkedHashSet<Expert> minimizedTeam = minimizeTeam(newTeam, this.skills);
            // check if minimizedTeam was already printed (in some HashSet of previously printed teams) - if not, print it

            TeamOfExperts team = new TeamOfExperts(minimizedTeam);
            if(!printedTeams.contains(team)){
                printedTeams.add(team);
//                printTeam(team);
                countPrintedResults++;

                if (countPrintedResults <= NUM_TOP) {
                    top10CostsQueue.add(cost);
                }
                else{
                    if(cost < top10CostsQueue.peek()){
                        // this answer should be in top ten
                        maximalPosition = countPrintedResults;
                        top10CostsQueue.poll();
                        top10CostsQueue.add(cost);
                    }
                }
            }
            else{
                countDumpedResults++;
            }

            Set<LawlerWorld> newLawlerWorlds = lawlerWorld.getDivisionToWorlds(S);
            Q1.addAll(newLawlerWorlds);
        }

        if (countPrintedResults < numOfResults){
            System.out.println("only " + countPrintedResults + " results were found out of " + numOfResults + " requested, in experiment:" + experimentName + ".");
        }
        System.out.println("Done minimally enumerating Exp5!");
        result = new Experiment5MinValueHolder(maximalPosition, countDumpedResults, countPrintedResults);
        return result;
    }

    private Expert getMinExpert(Set<Expert> E_o) {
        double minExpertCost = Double.POSITIVE_INFINITY;
        Expert minExpert = null;
        for (Expert expert : E_o) {
            if (expert.getCost() < minExpertCost) {
                minExpert = expert;
                minExpertCost = expert.getCost();
            }
        }
        return minExpert;
    }

    private void printTeam(TeamOfExperts team) {
        System.out.println(team.toString() + " cost = " + team.getTeamCost());
    }

//    private void printTeam(LinkedHashSet<Expert> team, double cost) {
//        System.out.println(team.toString() + " cost = " + cost);
//    }

    private double getNextCost(PriorityQueue<? extends World> q) {
        if (q.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        World world = q.peek();
        return world.getNextCost();
    }

    private Set<Skill> getSetSkills(Set<Expert> experts){
        Set<Skill> skills = new HashSet<Skill>();
        for(Expert expert:experts){
            skills.addAll(expert.getSkills());
        }
        return skills;
    }


    private LinkedHashSet<Expert> minimizeTeam(LinkedHashSet<Expert> experts, Set<Skill> skills){
        Set<Expert> remainingExperts = new HashSet<Expert>();
        remainingExperts.addAll(experts);

        LinkedHashSet<Expert> minimizedTeam = new LinkedHashSet<Expert>();
        List<Expert> expertsCopy = new LinkedList<Expert>();
        expertsCopy.addAll(experts);

        Collections.reverse(expertsCopy);

        for(Expert expert:expertsCopy){
            Set<Skill> expertSkills = expert.getSkills();
            expertSkills.retainAll(skills);

            remainingExperts.remove(expert);
            Set<Skill> teamSkills = getSetSkills(minimizedTeam);
            teamSkills.addAll(getSetSkills(remainingExperts));

            if(!teamSkills.containsAll(expertSkills)){
                minimizedTeam.add(expert);
            }
        }

        // reversing minimizedTeam into a result - since the order is important
        List<Expert> list = new LinkedList<Expert>(minimizedTeam);
        Collections.reverse(list);
        minimizedTeam = new LinkedHashSet<Expert>(list);

//        List<Expert> e = Collections.reverse(new LinkedList<Expert>(minimizedTeam));
        return minimizedTeam;

//        LinkedHashSet<Expert> result = new LinkedHashSet<Expert>();
//        LinkedList<Expert> list = new LinkedList<Expert>(minimizedTeam);
//        Iterator<Expert> iterator = list.descendingIterator();
//        while(iterator.hasNext()) {
//            Expert expert = iterator.next();
//            result.add(expert);
//        }
//        return result;
    }


/*
    public static void main (String[] args){

        // SKILLS
        Skill s1 = new Skill("s1");
        Skill s2 = new Skill("s2");
        Skill s3 = new Skill("s3");
        Skill s4 = new Skill("s4");
//        Skill s5 = new Skill("s5");
//        Skill s6 = new Skill("s6");
//        Skill s7 = new Skill("s7");

        Set<Skill> requiredSkills = new HashSet<Skill>(Arrays.asList(s1,s2,s3,s4));

        // EXPERTS
        Set<Skill> e1Skills = new HashSet<Skill>(Arrays.asList(s1,s2));
        Expert e1 = new Expert(1, e1Skills, "e1");

        Set<Skill> e2Skills = new HashSet<Skill>(Arrays.asList(s1,s3));
        Expert e2 = new Expert(2, e2Skills, "e2");

        Set<Skill> e3Skills = new HashSet<Skill>(Arrays.asList(s1,s4));
        Expert e3 = new Expert(3, e3Skills, "e3");

        Set<Skill> e4Skills = new HashSet<Skill>(Arrays.asList(s2,s3));
        Expert e4 = new Expert(4, e4Skills, "e4");

        Set<Skill> e5Skills = new HashSet<Skill>(Arrays.asList(s2,s4));
        Expert e5 = new Expert(5, e5Skills, "e5");

        Set<Skill> e6Skills = new HashSet<Skill>(Arrays.asList(s3,s4));
        Expert e6 = new Expert(6, e6Skills, "e6");

//        Set<Skill> e7Skills = new HashSet<Skill>(Arrays.asList(s7,s1));
//        Expert e7 = new Expert(7, e7Skills, "e7");

        LinkedHashSet<Expert> availableExpert = new LinkedHashSet<Expert>(Arrays.asList(e1,e2,e3,e4,e5,e6));

        MinimalTeamsEnumeration minimalEnumerator = new MinimalTeamsEnumeration(availableExpert, requiredSkills);
        minimalEnumerator.enumerateMinimally(100, "testForCorrectness");
    }
*/

}
