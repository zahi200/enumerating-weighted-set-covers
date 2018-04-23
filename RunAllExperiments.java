import java.util.*;
import java.io.*;

// Runs all the experiment together
// Experiment #1 - top1 vs top1Traditional - comparison between first answer in our approach and first answer in 1 expert per skill approach (Cardinality and Cost)
public class RunAllExperiments {
    private static final int NUM_OF_EXPERIMENTS = 100;
    private static final int MINIMAL_NUM_OF_SKILLS = 4;
    private static final int MAXIMAL_NUM_OF_SKILLS = 10;
    private static final int STEP_SIZE_NUM_OF_SKILLS = 2;
    private static final int NUM_OF_RESULTS = 1000;

    public static void main(String[] args){

        System.out.println(new File(".").getAbsoluteFile()); // getting current complete path
        final String baseSkillsFileName = "testcase_";
//        boolean wasTimeout = false;

        try {
            // preparing the files for the results of the experiments
            PrintWriter experiment3fileOur = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment3NewOurTime"))));
            PrintWriter experiment3fileMinimals = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment3NewMinimalsTime"))));
            experiment3fileOur.println("numOfSkills,testcaseSerialNumber,ourEnumerationTime[seconds],neto[seconds],tara[seconds]");
            experiment3fileMinimals.println("numOfSkills,testcaseSerialNumber,minimalsEnumerationTime[seconds],neto[seconds],tara[seconds],countDumpedResults,countPrintedResult");

            PrintWriter experiment5fileOur = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment5OurMaxPos"))));
            PrintWriter experiment5fileMin = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment5MinimalsMaxPos"))));
            experiment5fileOur.println("numOfSkills,testcaseSerialNumber,maximalPosition");
            experiment5fileMin.println("numOfSkills,testcaseSerialNumber,maximalPosition,countDumpedResults,countPrintedResult");

            PrintWriter experiment6fileOur = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment6OurIntervals"))));
            PrintWriter experiment6fileMin = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment6MinimalsIntervals"))));
            experiment6fileOur.println("numOfSkills,testcaseSerialNumber,100st,200-100,300-200,400-300,500-400,600-500,700-600,800-700,900-800,1000-900");
            experiment6fileMin.println("numOfSkills,testcaseSerialNumber,100st,200-100,300-200,400-300,500-400,600-500,700-600,800-700,900-800,1000-900");

            PrintWriter experiment7fileOur = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment7Our6skillsTopK"))));
            PrintWriter experiment7fileMin = new PrintWriter(new BufferedWriter(new FileWriter(new File("experiment7Min6skillsTopK"))));
            experiment7fileOur.println("k,testcaseSerialNumber,runTime[seconds]");
            experiment7fileMin.println("k,testcaseSerialNumber,runTime[seconds]");

            PrintWriter errorsFile = new PrintWriter(new BufferedWriter(new FileWriter(new File("errorsFile"))));


            // running the experiments
            for(int numOfSkills = MINIMAL_NUM_OF_SKILLS; numOfSkills<=MAXIMAL_NUM_OF_SKILLS; numOfSkills= numOfSkills+STEP_SIZE_NUM_OF_SKILLS){
                for(int i=1; i<=NUM_OF_EXPERIMENTS; i++) {
//                    wasTimeout = false;
//                    if (numOfSkills ==6 && i==19){
//                        System.out.println("6_19_TIMEOUT WORKED!");
//                    }
//
//                    if(numOfSkills!=6 || i!=18){
//                        continue;
//                    }

//                    if (numOfSkills <8 || (numOfSkills == 8 && i<54)){ // for skipping some part in case of crush
//                        continue;
//                    }

                    // loading the skill for this testcase
                    String skillsFileName = baseSkillsFileName + numOfSkills + "_skills_" + i;
                    System.out.println(skillsFileName);
                    Map<String, Skill> skillsMap = LoadWithJava.loadSkills("newTestcases/"+skillsFileName);
                    Set<Skill> requiredSkills = new HashSet<Skill>(skillsMap.values());
                    // loading the relevant experts for this testcase
                    LoadWithJava loader = new LoadWithJava();
                    Map<String, Expert> expertsMap = loader.createRelevantExperts(skillsMap);
                    LinkedHashSet<Expert> relevantExperts = new LinkedHashSet<Expert>(expertsMap.values());


                    System.out.println("Start experiment 3.");
                    Long startExp3Our = System.currentTimeMillis();
                    TeamsEnumeration experiment3Enumerator = new TeamsEnumeration(relevantExperts, requiredSkills);
                    Long startExp3Enumeration = System.currentTimeMillis();
                    experiment3Enumerator.enumerate(NUM_OF_RESULTS);
                    Long endExp3Our = System.currentTimeMillis();
                    double totalTimeExp3Our = (double)(endExp3Our - startExp3Our)/(double)1000;
                    double netoTimeExp3Our = (double)(endExp3Our - startExp3Enumeration)/(double)1000;;
                    double objectsCreationTimeExp3Our = (double)(startExp3Enumeration - startExp3Our)/(double)1000;
                    String lineExp3Our = numOfSkills + "," + i + "," + totalTimeExp3Our + "," + netoTimeExp3Our + "," + objectsCreationTimeExp3Our;
                    experiment3fileOur.println(lineExp3Our);
                    System.out.println(lineExp3Our);
                    experiment3fileOur.flush();

                    Long startExp3Minimal = System.currentTimeMillis();
                    MinimalTeamsEnumeration minimalEnumerator = new MinimalTeamsEnumeration(relevantExperts, requiredSkills);
                    Long startExp3MinimalEnumeration = System.currentTimeMillis();
                    Experiment3MinValueHolder resultEx3Minimal = minimalEnumerator.enumerateMinimally(NUM_OF_RESULTS, new String(numOfSkills + "_" + i));
                    if(resultEx3Minimal.getCountPrintedResult() < 0){
                        // TIMEOUT!
                        errorsFile.println("TIMEOUT in experiment 3 Minimals in testcase: " + skillsFileName);
                        System.out.println("TIMEOUT in experiment 3 Minimals in testcase: " + skillsFileName);
                        continue;
                    }

                    Long endExp3Minimal = System.currentTimeMillis();
                    double totalTimeExp3Minimal = (double)(endExp3Minimal - startExp3Minimal)/(double)1000;
                    double netoTimeExp3Minimal = (double)(endExp3Minimal - startExp3MinimalEnumeration)/(double)1000;;
                    double objectsCreationTimeExp3Minimal = (double)(startExp3MinimalEnumeration - startExp3Minimal)/(double)1000;
                    String lineEx3Minimal = numOfSkills + "," + i + "," + totalTimeExp3Minimal + "," + netoTimeExp3Minimal + "," + objectsCreationTimeExp3Minimal + "," + resultEx3Minimal.getCountDumpedResults() + "," + resultEx3Minimal.getCountPrintedResult();
                    experiment3fileMinimals.println(lineEx3Minimal);
                    System.out.println(lineEx3Minimal);
                    experiment3fileMinimals.flush();
                    System.out.println("Done experiment 3.");


                    System.out.println("Start experiment 5.");
                    TeamsEnumerationExp5top10vs1000 exp5Enumerator = new TeamsEnumerationExp5top10vs1000(relevantExperts, requiredSkills);
                    int maxPosition = exp5Enumerator.enumerate(NUM_OF_RESULTS, skillsFileName);
                    String exp5Line = numOfSkills + "," + i + "," + maxPosition;
                    experiment5fileOur.println(exp5Line);
                    System.out.println(exp5Line);
                    experiment5fileOur.flush();

                    TeamsEnumerationExp5Mintop10vs1000 ex5MinEnumerator = new TeamsEnumerationExp5Mintop10vs1000(relevantExperts, requiredSkills);
                    Experiment5MinValueHolder resultExp5Min = ex5MinEnumerator.enumerateMinimally(NUM_OF_RESULTS, skillsFileName);
                    String exp5MinLine = numOfSkills + "," + i + "," + resultExp5Min.getMaximalPosition() + "," + resultExp5Min.getCountDumpedResults() + "," + resultExp5Min.getCountPrintedResult();
                    experiment5fileMin.println(exp5MinLine);
                    System.out.println(exp5MinLine);
                    experiment5fileMin.flush();
                    if (resultExp5Min.getCountPrintedResult() < NUM_OF_RESULTS){
                        errorsFile.println("ERROR in experiment5Min in testcase:" + numOfSkills + "_skills_" + i);
                        errorsFile.flush();
                    }
                    System.out.println("Done experiment 5.");


                    System.out.println("Start experiment 6.");
                    TeamsEnumerationExp6 exp6Enumerator = new TeamsEnumerationExp6(relevantExperts, requiredSkills);
                    double[] timeResults = exp6Enumerator.enumerate(NUM_OF_RESULTS);
                    String ex6Line = numOfSkills + "," + i + "," + timeResults[0] + "," + timeResults[1] + "," + timeResults[2] + "," +
                            timeResults[3] + "," + timeResults[4] + "," + timeResults[5] + "," + timeResults[6] + "," +
                            + timeResults[7] + "," + timeResults[8] + "," + timeResults[9];
                    experiment6fileOur.println(ex6Line);
                    System.out.println(ex6Line);
                    experiment6fileOur.flush();

                    TeamsEnumerationExp6Min exp6MinEnumerator = new TeamsEnumerationExp6Min(relevantExperts, requiredSkills);
                    double[] timeResultsMin = exp6MinEnumerator.enumerateMinimally(NUM_OF_RESULTS, skillsFileName);
                    String ex6MinLine = numOfSkills + "," + i + "," + timeResultsMin[0] + "," + timeResultsMin[1] + "," + timeResultsMin[2] + "," +
                            timeResultsMin[3] + "," + timeResultsMin[4] + "," + timeResultsMin[5] + "," + timeResultsMin[6] + "," +
                            + timeResultsMin[7] + "," + timeResultsMin[8] + "," + timeResultsMin[9];
                    experiment6fileMin.println(ex6MinLine);
                    System.out.println(ex6MinLine);
                    experiment6fileMin.flush();
                    System.out.println("Done experiment 6.");



                    System.out.println("Start experiment 7.");
                    if(numOfSkills==6){
                        for(int k=500; k<=2500; k=k+500){
                            Long startExp7Our = System.currentTimeMillis();
                            TeamsEnumeration exp7Enumerator = new TeamsEnumeration(relevantExperts, requiredSkills);
                            exp7Enumerator.enumerate(k);
                            Long endExp7Our = System.currentTimeMillis();
                            double totalTimeExp7Our = (double)(endExp7Our - startExp7Our)/(double)1000;
                            String lineEx7Our = k + "," + i + "," + totalTimeExp7Our;
                            experiment7fileOur.println(lineEx7Our);
                            System.out.println(lineEx7Our);
                            experiment7fileOur.flush();

                            Long startExp7Min = System.currentTimeMillis();
                            MinimalTeamsEnumeration exp7MinEnumerator = new MinimalTeamsEnumeration(relevantExperts, requiredSkills);
                            Experiment3MinValueHolder result = exp7MinEnumerator.enumerateMinimally(k, skillsFileName);
                            if(result.getCountPrintedResult() < 0){
                                // TIMEOUT!
                                errorsFile.println("TIMEOUT in experiment 7 Minimals in testcase: " + skillsFileName);
                                System.out.println("TIMEOUT in experiment 7 Minimals in testcase: " + skillsFileName);
                                k=3000;
                                continue;
                            }
                            Long endExp7Min = System.currentTimeMillis();
                            double totalTimeExp7Min = (double)(endExp7Min - startExp7Min)/(double)1000;
                            String lineEx7Min = k + "," + i + "," + totalTimeExp7Min;
                            experiment7fileMin.println(lineEx7Min);
                            System.out.println(lineEx7Min);
                            experiment7fileMin.flush();
                        }
                    }
                    System.out.println("Done experiment 7.");


                    errorsFile.flush();
                }
            }

            // flushing and closing the files
            experiment3fileOur.flush();
            experiment3fileMinimals.flush();
            experiment5fileOur.flush();
            experiment5fileMin.flush();
            experiment6fileOur.flush();
            experiment6fileMin.flush();
            experiment7fileOur.flush();
            experiment7fileMin.flush();
            errorsFile.flush();

            experiment3fileOur.close();
            experiment3fileMinimals.close();
            experiment5fileOur.close();
            experiment5fileMin.close();
            experiment6fileOur.close();
            experiment6fileMin.close();
            experiment7fileOur.close();
            experiment7fileMin.close();
            errorsFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


