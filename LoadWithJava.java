import java.sql.*;
import java.util.*;
import java.io.*;

public class LoadWithJava {
    private static final String CONNECTION_URL = "jdbc:postgresql://dbcourse/public?user=zahi200";
//    private static String basicQuery = "SELECT author, word FROM authorwordnodups WHERE "; // + "word = 'skill1' or word='skill2' or word='skill3'
    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

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


    public Map<String, Expert> createRelevantExperts(Map<String, Skill> skillsMap){
        System.out.println("Start loading relevant experts");
        Map<String, Expert> expertsMap = new HashMap<String, Expert>();
        // establishing connection
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CONNECTION_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("EXCEPTION: Driver wasn't found");
            close();
            System.exit(-1);
        } catch (SQLException e) {
            System.out.println("EXCEPTION: establishing connection failed");
            close();
            System.exit(-1);
        }

        // creating the relevant experts and fill inside the map their names and skills
        for (String skillName:skillsMap.keySet()){
            try{
//                System.out.println("skill name is:" + skillName);
                Skill currentSkill = skillsMap.get(skillName);
//                String queryForAuthors = "SELECT author FROM authorwordnodups WHERE word = ' " + skillName + "';";
                String queryForAuthors = "SELECT author FROM AuthorWordNow WHERE word = ?;";
//                System.out.println("query is:"+queryForAuthors);
                preparedStatement = connection.prepareStatement(queryForAuthors);
                preparedStatement.setString(1,skillName);
//                preparedStatement.setString(1, " " +skillName); //TODO - if from args - add " " !!!!
                resultSet = preparedStatement.executeQuery();
//                System.out.println("Executed query");
                while(resultSet.next()){
//                    System.out.println("looping over resultSet");
                    String expertName = resultSet.getString("author");
//                    System.out.println("expert is:" + expertName);
                    Expert expert = null;
                    if (expertsMap.containsKey(expertName)){
                        expert = expertsMap.get(expertName);
                    } else {
                        expert = new Expert(-1, new HashSet<Skill>(), expertName);
                        expertsMap.put(expertName, expert);
                    }
                    expert.addSkill(currentSkill);
                }
            }
            catch (SQLException e){
                System.out.println("EXCEPTION: querying authorwordnodups table failed");
                close();
                System.exit(-1);
            } finally {
                closeResources();
            }
        }

        // fill in the cost of each relevant expert
        for (String expertName:expertsMap.keySet()){
            try{
//                System.out.println("author is:" + expertName);
                String queryForCost = "SELECT numOfPublication FROM authorNumOfPublication WHERE author = ?;";
//                String queryForCost = "SELECT numOfPublication FROM authorNumOfPublication WHERE author = '" + expertName + "';";
//                System.out.println("query is:"+queryForCost);
                preparedStatement = connection.prepareStatement(queryForCost);
                preparedStatement.setString(1,expertName);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    Expert expert = expertsMap.get(expertName);
                    double cost = resultSet.getDouble(1);
//                    System.out.println("cost is:" + cost);
                    expert.setCost(cost);
                } else {
                    System.out.println("No cost for author:" + expertName);
                    close();
                    System.exit(-1);
                }

            } catch (SQLException e){
                System.out.println("EXCEPTION: querying authorNumOfPublication table failed");
                System.out.println("the failing author is:" + expertName);
                close();
                System.exit(-1);
            } finally {
                closeResources();
            }
        }

        close();
        System.out.println("Done loading relevant experts");
        return expertsMap;
    }


    public void close(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {System.out.println("EXCEPTION: closing result set exception");}
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {System.out.println("EXCEPTION: closing prepared statement exception");}
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {System.out.println("EXCEPTION: closing connection exception");}
        }
    }

    private void closeResources (){
        // closing resultsets and preparedStatements but not connection
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {System.out.println("EXCEPTION(resources): closing result set exception");}
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {System.out.println("EXCEPTION(resources): closing prepared statement exception");}
        }
    }

    // format: skillsCommaSeperated k-NumOfResultsToPrint
    public static void main2(String[] args){
        // creating the map skills from args[0]
        String skillsString = args[0];
        String[] splittedSkills = skillsString.split("\\,");
        Map<String, Skill> skillsMap = new HashMap<String, Skill>();
        for(String skillString:splittedSkills){
            skillsMap.put(skillString, new Skill(skillString));
        }
        System.out.println("Number of skills="+skillsMap.size());

        // loading the data from postgres
        LoadWithJava loader = new LoadWithJava();
        Map<String, Expert> expertsMap = loader.createRelevantExperts(skillsMap);

        System.out.println("size of map is:" + expertsMap.size());

        /*
        // printing the experts in the map - for debugging
        for(String expertName:expertsMap.keySet()){
            Expert expert = expertsMap.get(expertName);
            System.out.print("Name=" + expert.getExpertName() +",cost=" + expert.getCost()+", skills=");
            for(Skill skill:expert.getSkills()){
                System.out.print(skill.getSkillName() + ",");
            }
            System.out.println();
        }
        */

        Set<Skill> requiredSkills = new HashSet<Skill>(skillsMap.values());
        LinkedHashSet<Expert> relevantExperts = new LinkedHashSet<Expert>(expertsMap.values());


        int numOfResults = Integer.parseInt(args[1]);
        // enumeration
        // TODO - add timer here
        System.out.println("Start enumeration.");
        TeamsEnumeration enumerator = new TeamsEnumeration(relevantExperts, requiredSkills);
        enumerator.enumerate(numOfResults);
        System.out.println("Done enumeration.");
        // stop timer

        // minimals enumeration
        System.out.println("Start:");
        MinimalTeamsEnumeration minimalEnumerator = new MinimalTeamsEnumeration(relevantExperts, requiredSkills);
        minimalEnumerator.enumerateMinimally(numOfResults, "stam");
        System.out.println("End");

        System.out.println("Done - bye bye.");
    }

}
