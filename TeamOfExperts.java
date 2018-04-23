// This class wrap a set of experts to be printed in the enumeration in MinimalTeamsEnumeration.
// We need it to override equals method, to check in the container of all previously printed teams if it was already printed before.

import java.util.*;

public class TeamOfExperts {

    private LinkedHashSet<Expert> team;

//    public TeamOfExperts(){
//        team = new LinkedHashSet<Expert>();
//    }

    public TeamOfExperts(LinkedHashSet<Expert> experts){
        team = new LinkedHashSet<Expert>();
        team.addAll(experts);
    }

    public double getTeamCost() {
        double cost = 0;
        for(Expert expert:team){
            cost += expert.getCost();
        }
        return cost;
    }

//    public LinkedHashSet<Expert> getTeam() {
//        LinkedHashSet<Expert> teamOfExpert = new LinkedHashSet<Expert>();
//        teamOfExpert.addAll(this.team);
//        return teamOfExpert;
//    }

    @Override
    public String toString() {
        return team.toString();
    }

    @Override
    public int hashCode() {
        int hashcode = 0;
        for(Expert expert:team){
            String expertName = expert.getExpertName();
            hashcode += expertName.hashCode();
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
//        System.out.println("equals of Teams of experts");
        if(!(obj instanceof TeamOfExperts)){
            return false;
        }
        TeamOfExperts otherTeam = (TeamOfExperts)obj;
//        System.out.println("Teams compared are:" + team + " and:" + otherTeam.team + ".");
        if((!this.team.containsAll(otherTeam.team)) || (!otherTeam.team.containsAll(this.team))){
            return false;
        }
        return true;
    }
}
