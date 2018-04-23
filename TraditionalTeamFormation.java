import java.util.*;

public class TraditionalTeamFormation {

    private LinkedHashSet<Expert> experts; // all available experts
    private Set<Skill> skills;   // elements to cover by the sets

    public TraditionalTeamFormation(LinkedHashSet<Expert> experts, Set<Skill> skills) {
        this.experts = new LinkedHashSet<Expert>();
        this.experts.addAll(experts);
        this.skills = new HashSet<Skill>();
        this.skills.addAll(skills);
    }

    // print the formed team and returns its cost
    public double formTeam(){
        System.out.println("in formteam");

        List<Expert> team = new LinkedList<Expert>();
        double teamCost=0;

        for(Skill skill:skills){
            double minExpertCost = Double.POSITIVE_INFINITY;
            Expert bestExpert=null;
            for(Expert expert:this.experts){
                if(expert.getCost() < minExpertCost && expert.getSkills().contains(skill)){
                    minExpertCost = expert.getCost();
                    bestExpert = expert;
                }
            }
            if(bestExpert == null){
                System.out.println("No team satisfies S in Traditional team formation");
                return -1;
            } else {
                team.add(bestExpert);
            }
        }

        for(Expert expert:team){
            teamCost += expert.getCost();
        }
        System.out.println("the team is:" + team.toString() + " ");
        return teamCost;
    }

/*
    public static void main(String[] args){

        // SKILLS
        Skill s1 = new Skill("s1");
        Skill s2 = new Skill("s2");

        Set<Skill> requiredSkills = new HashSet<Skill>(Arrays.asList(s1,s2));

        // EXPERTS
        Set<Skill> e1Skills = new HashSet<Skill>(Arrays.asList(s1));
        Expert e1 = new Expert(5, e1Skills, "e1");

        Set<Skill> e2Skills = new HashSet<Skill>(Arrays.asList(s1,s2));
        Expert e2 = new Expert(2, e2Skills, "e2");

        LinkedHashSet<Expert> availableExpert = new LinkedHashSet<Expert>(Arrays.asList(e1,e2));


        System.out.println("Traditional Team Formation:");
        TraditionalTeamFormation teamformation = new TraditionalTeamFormation(availableExpert, requiredSkills);
        double cost = teamformation.formTeam();
        if (cost == -1){
            System.out.println("PROBLEMMMMM");
        }
        System.out.println("cost is:" + cost);
*/
       /*
        System.out.println("s1 hashcode=" + s1.hashCode());
        System.out.println("s2 hashcode=" + s2.hashCode());
        System.out.println("s1 hashcode=" + s1.hashCode());
        System.out.println("hashcode(\"1s\")="+"1s".hashCode());
        System.out.println("s1 equals s2:" + s1.equals(s2));
        System.out.println("s1 equals s1:" + s1.equals(s1));
        System.out.println("************************************");
        Set<Skill> stamSet = new HashSet<Skill>();
        stamSet.add(s1);
        System.out.println(1);
        System.out.println("stamSet contains s2?:" + stamSet.contains(s2));
        System.out.println("stamSet contains s1?:" + stamSet.contains(s1));

        System.out.println("stamSet contains s1?:" + stamSet.contains(s1));

        Skill s3 = new Skill("s1");
        System.out.println(2);
        System.out.println("stamSet contains s3?:" + stamSet.contains(s3));

        System.out.println(3);
        Skill s4 = s3;
        System.out.println("stamSet contains s4?:" + stamSet.contains(s4));

        System.out.println(4);
        Skill s5 = s1;
        System.out.println("stamSet contains s5?:" + stamSet.contains(s5));

        System.out.println(5);
        System.out.println(s2.toString());
        System.out.println(s1.toString());
        System.out.println(s3.toString());
        System.out.println(s4.toString());
        System.out.println(s5.toString());

        System.out.println(6);
        System.out.println("Is s1 contained in e1.getSkills()?");
        System.out.println(e1.getSkills().contains(s1));
        System.out.println("Is s2 contained in e1.getSkills()?");
        System.out.println(e1.getSkills().contains(s2));
        System.out.println("Is s3 contained in e1.getSkills()?");
        System.out.println(e1.getSkills().contains(s3));
        System.out.println("Is s4 contained in e1.getSkills()?");
        System.out.println(e1.getSkills().contains(s4));
        System.out.println("Is s5  contained in e1.getSkills()?");
        System.out.println(e1.getSkills().contains(s5));

    }*/
}
