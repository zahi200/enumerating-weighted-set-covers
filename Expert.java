import java.util.*;

public class Expert {

    private Set<Skill> skills;
    private double cost;
    private String expertName;

    public Expert(double cost, Set<Skill> skills, String expertName){
        this.cost = cost;
        this.skills = new HashSet<Skill>();
        this.skills.addAll(skills);
        this.expertName = expertName;
    }

    public double getCost(){
        return this.cost;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public Set<Skill> getSkills(){
        Set<Skill> skillsSet = new HashSet<Skill>();
		skillsSet.addAll(this.skills);
		return skillsSet;
    }

    public void addSkill(Skill skill){
        this.skills.add(skill);
    }

    public String getExpertName() { return this.expertName; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Expert)){
            return false;
        }
        Expert otherExpert = (Expert)obj;
        return expertName.equals(otherExpert.getExpertName());
    }

    @Override
    public String toString(){
        return this.expertName;
    }
}
