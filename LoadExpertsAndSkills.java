import java.io.*;
import java.util.*;

public class LoadExpertsAndSkills {

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

    public static Set<Expert> loadExperts(String expertsCostsFileName, String expertsSkillFileName, Set<Skill> skills){
        Set<Expert> expertsSet = new HashSet<Expert>();
        // first - read the file of experts with costs and load it
        System.out.println("Start loading experts");
        try (BufferedReader br = new BufferedReader(new FileReader(expertsCostsFileName))) {
            String currentLine;
            while((currentLine=br.readLine()) != null){
                String[] splittedLine = currentLine.split("\\|");
                String expertName = splittedLine[0].trim();
                int expertCost = Integer.parseInt(splittedLine[1].trim());
//                System.out.println("expert:"+expertName+", cost:"+expertCost+".");
                expertsSet.add(new Expert(expertCost,new HashSet<Skill>(), expertName));
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Done loading experts-costs");
        // second - read the file of experts with skills and load it
        System.out.println("Start loading experts-skills");
        try (BufferedReader br = new BufferedReader(new FileReader(expertsSkillFileName))) {
            String currentLine;
            while((currentLine=br.readLine()) != null){
                String[] splittedLine = currentLine.split("\\|");
                String expertName = splittedLine[0].trim();
                Expert expert = null;

                for(Iterator<Expert> it = expertsSet.iterator(); it.hasNext(); ){
                    Expert currentExpert = it.next();
                    if (currentExpert.getExpertName().equals(expertName) && expert == null){
                        expert = currentExpert;
                        break;
                    }
//                    else if(currentExpert.getExpertName().equals(expertName) && expert != null){
//                        System.out.println("FIND TWO EXPERTS WITH SAME NAME:" + expertName);
//                    }

                }

                String skillName = splittedLine[1].trim();
                Skill skillToSearch = new Skill(skillName);
                Skill correspondingSkill = null;

                for(Iterator<Skill> it = skills.iterator(); it.hasNext(); ){
                    Skill currentSkill = it.next();
                    if (currentSkill.equals(skillToSearch) && correspondingSkill == null){
                        correspondingSkill = currentSkill;
                        break;
                    }
//                    else if(currentSkill.equals(skillToSearch) && correspondingSkill != null){
//                        System.out.println("FIND TWO SKILLS WITH SAME NAME:" + skillName);
//                    }
                }

                if(expert != null && correspondingSkill != null){
                    expert.addSkill(correspondingSkill);
                }
                else{
                    System.out.println("WEIRD ERROR IN LINE:" + currentLine);
                }

//                expertsSet.add(new Expert(expertCost,new HashSet<Skill>(), expertName));
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Done loading experts");
        return expertsSet;
    }

    public static void main1(String[] args) throws FileNotFoundException {
        /*
        System.out.println("Start loading.");
        System.out.println(new File(".").getAbsoluteFile());
        Set<Skill> ss = loadSkills("src/stamSkills.txt"); // this is how to create
        Set<Expert> es = loadExperts("src/stamExpertsCost.txt", "src/stamExpertsSkills.txt", ss);

//        System.out.println("num of skills=" + ss.size());
        System.out.println();
        for(Expert expert: es){
            System.out.println("Expert:" + expert.getExpertName() + ", Cost:" + expert.getCost());
            System.out.print("Skills: ");
            for(Skill skill: expert.getSkills()){
                System.out.print(skill.getSkillName() + " ");
            }
            System.out.println(".");
        }
        System.out.println("Done loading.");
        */

        System.out.println("Start loading.");

        Set<Skill> ss = loadSkills("C:/Users/zahi200/Desktop/preprocessingDBLP/results of the postgress part/skillsFile.txt"); // this is how to create
        Set<Expert> es = loadExperts("C:/Users/zahi200/Desktop/preprocessingDBLP/results of the postgress part/expertsWithCostsDelimiter.txt", "C:/Users/zahi200/Desktop/preprocessingDBLP/results of the postgress part/expertsSkillsDelimiter.txt", ss);

        System.out.println("Done loading.");
        System.out.println("num of skills:" + ss.size());
        System.out.println("num of experts:" + es.size());
    }
}
