import java.io.*;
public class Generate400TestCasesScript {

//    private final static String COMMAND = "psql -hdbcourse public -c \'\\copy (select * from skillsnow order by random() limit 4) To 'fileName' ' ";
    private final static String COMMAND_START = "psql -hdbcourse public -c \'\\copy (select * from skillsnow order by random() limit ";
    private final static String COMMAND_MIDDLE = ") To '";
    private final static String COMMAND_END = "' '";
    private final static int NUM_OF_EXPERIMENTS = 100;
//    private final static String COMMAND = "psql -hdbcourse public -c 'COPY authorword TO STDOUT;' > expertsSkills.txt";

    public static void main(String[] args){


//        System.out.println(COMMAND);
        System.out.println("bye-bye");

        try {
            PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(new File("scriptForGeneratingNewTestcases"))));

            // update the variables numOfSkills and fileName
            for(int numOfSkills = 4; numOfSkills<=10; numOfSkills= numOfSkills+2){
                String fileNameBase = "newTestcases/testcase_"+ numOfSkills +"_skills_";
                for(int i=1; i<=NUM_OF_EXPERIMENTS; i++) {
                    String fileName = fileNameBase + i;
                    String currentCommand = COMMAND_START + numOfSkills + COMMAND_MIDDLE + fileName + COMMAND_END;
                    file.println(currentCommand);
//                    System.out.println(currentCommand);
                }
            }

            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
