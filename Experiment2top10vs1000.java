import java.io.*;
import java.util.*;

// Experiment #2 - top10 vs top1000 - how many results of the 990 results after the top 10, has cost smaller than the maximal cost among top10?
public class Experiment2top10vs1000 {

    private static final int NUM_OF_EXPERIMENTS = 100;
    private static final int MINIMAL_NUM_OF_SKILLS = 4;
    private static final int MAXIMAL_NUM_OF_SKILLS = 10;
    private static final int STEP_SIZE_NUM_OF_SKILLS = 2;
    private static final int NUM_OF_RESULTS = 1000;

    /* THIS IS JUST A COPY OF the static method loadSkills from LoadWithJava, so I put it in comment
    public static Map<String, Skill> loadSkills(String skillsFileName){
        System.out.println("Start loading Skills from file:" + skillsFileName);
        Map<String, Skill> skillsMap = new HashMap<String, Skill>();
        try (BufferedReader br = new BufferedReader(new FileReader(skillsFileName))) {
            String currentLine;
            while((currentLine=br.readLine()) != null){
                String skillName = currentLine;
                skillsMap.put(skillName, new Skill(skillName));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Done loading Skills.");
        return skillsMap;
    }
    */
/*
    public static Set<Skill> loadSkills(String filename){
        System.out.println("Start loading Skills.");
        Set<Skill> skillsSet = new HashSet<Skill>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String currentLine;
            while((currentLine=br.readLine()) != null){
                String skillName = currentLine.trim();
//                System.out.println(skillName);
                skillsSet.add(new Skill(skillName));
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Done loading Skills.");
        return skillsSet;
    }
*/
    public static void main(String[] args){

//        Map<String, Skill> skillsMap = loadSkills("C:\\Users\\zahi200\\Desktop\\testcases\\testcase_8_skills_24");
//        System.out.println("skills map:" + skillsMap.toString());
        System.out.println(new File(".").getAbsoluteFile());

        String baseSkillsFileName = "testcase_";
        try {
            PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment2top10vs1000ResultsContd"))));
            file.println("numOfSkills,serialNumber,numOfShouldBes,averagePositionofShouldBes");
            // update the variables numOfSkills and fileName
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

                    String line = numOfSkills + "," + i + "," + result.getNumOfShouldBeInTop10() + "," + result.getAveragePositionOfShouldBeInTop10();
                    file.println(line);
                    System.out.println(line);
                    file.flush();
                }
            }
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
