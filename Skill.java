public class Skill {

    private final String skillName;

    public Skill (String skillName){
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    @Override
    public int hashCode() {
//        System.out.println("In Skill Hashcode with skill:" + this.skillName + " ");
        return this.skillName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Skill)){
            return false;
        }
        Skill otherSkill = (Skill)obj;
//        System.out.println("In Skill equals with skill:" + this.skillName + " and " + otherSkill.getSkillName());
        return skillName.equals(otherSkill.getSkillName());
    }
}
