import java.util.HashMap;

public class Contributor {
    /**
     * name
     * HashMap Skills
     * bool isMentored
     */

    String name;
    HashMap<String, Integer> skills;
    String skillInUse;
//    boolean isMentored;

    public Contributor(String name, HashMap<String, Integer> skills) {
        this.name = name;
        this.skills = skills;
//        this.isMentored = false;
    }

    public void updateSkill() {
        int afterUpdate = skills.getOrDefault(skillInUse, 0) + 1;
        skills.put(skillInUse, afterUpdate) ;
    }
}
