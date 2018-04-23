import java.util.*;

public class ApproxMinCostTeam {
    public static LinkedHashSet<Expert> calc(Set<Skill> uncoveredSkills, LinkedHashSet<Expert> availableExperts) {
        Set<Skill> S = new HashSet<Skill>();
        S.addAll(uncoveredSkills);
        LinkedHashSet<Expert> E = new LinkedHashSet<Expert>();
        E.addAll(availableExperts);
        LinkedHashSet<Expert> T = new LinkedHashSet<Expert>();

        Set<Skill> coverableSkills = new HashSet<Skill>();
        for (Expert expert : E) {
            coverableSkills.addAll(expert.getSkills());
        }
        if (!coverableSkills.containsAll(uncoveredSkills)) {
            return null;
        }

//        Set<Expert> unrelevantExperts = new HashSet<Expert>();
//        for(Expert expert:E){
//            if (==0){
//                unrelevantExperts.add(expert);
//            }
//        }
//        E.removeAll(unrelevantExperts);

        Set<Skill> expertSkills = null;
        Expert bestExpert = null;
        double lowestAverageCost;
        double expertAverageCost;
        while (!S.isEmpty()) {
            lowestAverageCost = Double.POSITIVE_INFINITY;
            for (Expert expert : E) {
                expertSkills = expert.getSkills();
                expertSkills.retainAll(S);
                if(expertSkills.size()>0) {
                    expertAverageCost = expert.getCost() / expertSkills.size();
                    if (expertAverageCost < lowestAverageCost) {
                        bestExpert = expert;
                        lowestAverageCost = expertAverageCost;
                    }
                }
            }
            T.add(bestExpert);
            E.remove(bestExpert);
            S.removeAll(bestExpert.getSkills());
        }
        return T;
    }

/*
    public static void main (String[] args){
        Skill s1 = new Skill();
        Skill s2 = new Skill();
        Skill s3 = new Skill();

        Skill[] e1SkillsArray = {s1};
        Set<Skill> e1Skills = new HashSet<Skill>(Arrays.asList(e1SkillsArray));
        Expert e1 = new Expert(1, e1Skills, "e1");

        Skill[] e2SkillsArray = {s2};
        Set<Skill> e2Skills = new HashSet<Skill>(Arrays.asList(e2SkillsArray));
        Expert e2 = new Expert(4, e2Skills, "e2");

        Skill[] e3SkillsArray = {s1,s2};
        Set<Skill> e3Skills = new HashSet<Skill>(Arrays.asList(e3SkillsArray));
        Expert e3 = new Expert(4, e3Skills, "e3");

        Skill[] e4SkillsArray = {s1,s2};
        Set<Skill> e4Skills = new HashSet<Skill>(Arrays.asList(e4SkillsArray));
        Expert e4 = new Expert(5, e4Skills, "e4");

        Skill[] e5SkillsArray = {s3};
        Set<Skill> e5Skills = new HashSet<Skill>(Arrays.asList(e5SkillsArray));
        Expert e5 = new Expert(5, e5Skills, "e5");

        Skill[] skillsArray = {s1,s2};
        Set<Skill> skills = new HashSet<Skill>(Arrays.asList(skillsArray));

        Set<Expert> experts = new HashSet<Expert>();
        experts.add(e1);
        experts.add(e2);
        experts.add(e3);
        experts.add(e4);
        experts.add(e5);

        Set<Expert> result = calc(skills, experts);
        if(result == null){
            System.out.println("null");
        }
        else if(!result.isEmpty()) {
            System.out.println(result.toString());
            System.out.println("bye");
        }
        else{
            System.out.println("empty");
        }

        /*
        System.out.println("hiush");
        Skill s1 = new Skill();
        Skill s2 = new Skill();
        Skill s3 = new Skill();

        Skill[] e1SkillsArray = {s1};
        Set<Skill> e1Skills = new HashSet<Skill>(Arrays.asList(e1SkillsArray));
        Expert e1 = new Expert(1, e1Skills, "e1");

        Skill[] e2SkillsArray = {s2};
        Set<Skill> e2Skills = new HashSet<Skill>(Arrays.asList(e2SkillsArray));
        Expert e2 = new Expert(4, e2Skills, "e2");

        Skill[] e3SkillsArray = {s1,s2};
        Set<Skill> e3Skills = new HashSet<Skill>(Arrays.asList(e3SkillsArray));
        Expert e3 = new Expert(4, e3Skills, "e3");

        Set<Expert> experts1 = new HashSet<Expert>();
        experts1.add(e1);
        experts1.add(e2);
        experts1.add(e3);

        System.out.println(experts1);
        System.out.println(experts1==experts1);

        Set<Expert> experts2 = new HashSet<Expert>(experts1);

        System.out.println(experts2);
        System.out.println(experts1==experts2);

        experts2.remove(e2);
        System.out.println("After remove e2:");
        System.out.println(experts1);
        System.out.println(experts2);

        Set<Expert> experts3 = new HashSet<Expert>();
        experts3.addAll(experts1);

        System.out.println("experts3");
        System.out.println(experts3);
        System.out.println(experts1==experts3);

        experts3.remove(e3);
        System.out.println("after removing e3");
        System.out.println(experts1);
        System.out.println(experts3);

        Set<Expert> experts4 = new HashSet<Expert>();
        experts4 = experts1;

        System.out.println("experts4");
        System.out.println(experts4);
        System.out.println(experts1==experts4);

        experts4.remove(e1);
        System.out.println(experts1);
        */

        /*
        Set<Expert> ss = new HashSet<Expert>();
        Set<Expert> ss2 = new HashSet<Expert>();
        ss2.add(e1);
        ss2.addAll(ss);
        System.out.println(ss);
        System.out.println(ss2);
        */

        /*
        Set<Skill> stamSet = new HashSet<Skill>();
        if(stamSet.containsAll(e3Skills)){
            System.out.println("yay");
        }
        else{
            System.out.println("uff");
        }
        */
//    }

}
