import java.util.*;
import java.io.*;

// Experiment #1 - top1 vs top1Traditional - comparison between first answer in our approach and first answer in 1 expert per skill approach (Cardinality and Cost)
public class Experiment1top1vsTraditional {
    private static final int NUM_OF_EXPERIMENTS = 100;
    private static final int MINIMAL_NUM_OF_SKILLS = 4;
    private static final int MAXIMAL_NUM_OF_SKILLS = 10;
    private static final int STEP_SIZE_NUM_OF_SKILLS = 2;
    private static final int NUM_OF_RESULTS = 1000;

    public static void main(String[] args){

        System.out.println(new File(".").getAbsoluteFile());

        String baseSkillsFileName = "testcase_";
        try {
            PrintWriter fileOurCar = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment1OurCardinality"))));
            PrintWriter fileOurCost = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment1OurCost"))));
//            PrintWriter fileTraditionalCar = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment1TraditionalCardinality")))); - NOT NEEDED - KNOWN IN ADVANCED
            PrintWriter fileTraditionalCost = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment1TraditionalCost"))));

            fileOurCar.println("numOfSkills,serialNumber,resultCardinality");
            fileOurCost.println("numOfSkills,serialNumber,resultCost");
//            fileTraditionalCar.println("numOfSkills,serialNumber,resultCardinality");
            fileTraditionalCost.println("numOfSkills,serialNumber,resultCost");


            for(int numOfSkills = MINIMAL_NUM_OF_SKILLS; numOfSkills<=MAXIMAL_NUM_OF_SKILLS; numOfSkills= numOfSkills+STEP_SIZE_NUM_OF_SKILLS){
                for(int i=1; i<=NUM_OF_EXPERIMENTS; i++) {
                    String skillsFileName = baseSkillsFileName + numOfSkills + "_skills_" + i;
                    Map<String, Skill> skillsMap = LoadWithJava.loadSkills("testcases/"+skillsFileName);

                    LoadWithJava loader = new LoadWithJava();
                    Map<String, Expert> expertsMap = loader.createRelevantExperts(skillsMap);

                    Set<Skill> requiredSkills = new HashSet<Skill>(skillsMap.values());
                    LinkedHashSet<Expert> relevantExperts = new LinkedHashSet<Expert>(expertsMap.values());

                    System.out.println("Start enumeration.");
                    TeamsEnumerationExp2top10vs1000 experiment2Enumerator = new TeamsEnumerationExp2top10vs1000(relevantExperts, requiredSkills);
                    Experiment2ValueHolder result = experiment2Enumerator.enumerate(NUM_OF_RESULTS, skillsFileName);
                    System.out.println("Done enumeration.");

                    String line = numOfSkills + "," + i + "," + result;
//                    file.println(line);
                    System.out.println(line);
//                    file.flush();
                }
            }

            fileOurCar.close();
            fileOurCost.close();
//            fileTraditionalCar.close();
            fileTraditionalCost.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


