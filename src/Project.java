import java.util.*;

public class Project implements Comparable<Project> {
    /**
     * Name
     * Duration
     * Score
     * Priority(int)
     * deadline
     * HashMap Roles
     * Set Skills
     * ArrLst Contributors
     * bool isReady
     */
    String name;
    int duration;
    int score;
    int priority;
    int priority2;
    // strictly before this date
    int deadline;
    // required roles for the project
    HashMap<String, Integer> roles;
    ArrayList<String> roleOrder;
    // Available skills to be mentored
    Set<String> skills;
    List<Contributor> contributors;
    boolean isReady;

    ArrayList<String> pastContributors = new ArrayList<>();


    // dung dung nhe :D
    int remainingDuration;

    public Project(String name, int duration, int score, int deadline, HashMap<String, Integer> roles, ArrayList<String> roleOrder) {
        this.name = name;
        this.duration = duration;
        this.score = score;
        this.deadline = deadline;
        this.roles = roles;
        this.skills = new HashSet<>();
        this.priority = 0;
        this.contributors = new ArrayList<>();
        this.isReady = false;
        this.roleOrder = roleOrder;
        updatePriority(0);
    }

    public void updatePriority(int date) {
        priority = getPriority(date);
    }

    public int getPriority(int date) {
        int penalty = deadline - date - duration;
        int potentialScore = Math.max(score + Math.min(0, penalty), 0);
        return potentialScore; // /total skills levels
        // priority /= duration
        //
    }

    public boolean tryAssignContributor(ArrayList<Contributor> contributors) {
        for (String role : roleOrder) {

            boolean assigned = false;
            for (int i = 0; i < contributors.size(); i++) {
                Contributor contributor = contributors.get(i);
                if (shouldAssignContributor(contributor, role)) {
                    assignContributor(contributors.remove(i));
                    assigned = true;
                    break;
                }
            }
            if (!assigned) {
                reset(contributors);
                return false;
            }
        }
        return true;
    }

    public void reset(ArrayList<Contributor> contributors) {
        for (Contributor contributor : this.contributors) {
            contributors.add(0, contributor);
        }
        this.contributors.clear();
        this.skills.clear();
    }

    // C++ Py Java
    // A: C++ (5)
    // B: C++ (1) .net

    public void updatePrio2(ArrayList<Contributor> contributors) {
        // simplified to no mentoring
        priority2 = 0;
        for (int i: roles.values()) priority2 += i * i;
    }

    // skill and level of the required skill of the project
    public boolean shouldAssignContributor(Contributor contributor, String skill) {
        int contributorSkill = contributor.skills.getOrDefault(skill, 0);
        // have the skill at the required level or higher
        if (contributorSkill >= roles.get(skill)) {
            contributor.skillInUse = skill;
            // contributor.isMentored = false;
            return true;
        }
        // have the skill at exactly one level below the required level
        // only if another contributor on the same project has this skill at the required level or higher
        if (roles.get(skill) - contributorSkill == 1) {
            for (Contributor peer: contributors) {
                if (peer.skills.getOrDefault(skill, 0) >= roles.get(skill)) {
                    contributor.skillInUse = skill;
                    // contributor.isMentored = true;
                    return true;
                }
            }
        }

        return false;
    }

    public void assignContributor(Contributor contributor) {
        contributors.add(contributor);
        skills.addAll(contributor.skills.keySet());
    }

    public void complete(ArrayList<Contributor> contributors) {
        // skillInUse
        for (Contributor contributor: this.contributors) {
            String skillInUse = contributor.skillInUse;
            if (roles.get(skillInUse) <= contributor.skills.get(skillInUse)) {
                contributor.updateSkill();
            }
        }

        for (Contributor contributor : this.contributors) pastContributors.add(contributor.name);

        reset(contributors);
    }

    @Override
    public int compareTo(Project p) {
        return Integer.compare(priority, p.priority);
    }
}
