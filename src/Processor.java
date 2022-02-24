import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Processor {

    ArrayList<Project> projects = new ArrayList<>();
    ArrayList<Contributor> contributors = new ArrayList<>();
    ArrayList<Project> onGoingProjects = new ArrayList<>();
    ArrayList<Project> completedProjects = new ArrayList<>();
    public static String[] inputFiles = new String[]{
            "a_an_example", "b_better_start_small", "c_collaboration",
            "d_dense_schedule", "e_exceptional_skills", "f_find_great_mentors"
    };

    int score = 0;

    public Processor(String path) {
        try {
            readInput(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int idx = 1;
        Processor processor = new Processor(Processor.inputFiles[idx]);
        processor.optimizeSortGreedy();
        System.out.println();
        System.out.println(processor.score);
        processor.writeSubmission(Processor.inputFiles[idx]);
    }

    public void optimizeSortGreedy() {
        projects.sort(Collections.reverseOrder());
        optimize();
    }

    public void optimize() {
        int date = 0;
        boolean assigned = true;
        while ((projects.size() != 0 || onGoingProjects.size() != 0)) {
            System.out.println(date);
            assigned = false;
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).tryAssignContributor(contributors)) {

                    onGoingProjects.add(projects.remove(i));
                    i--;
                    assigned = true;
                }
            }
            if (!assigned && onGoingProjects.size() == 0) return;
            for (int i = 0; i < onGoingProjects.size(); i++) {
                Project project = onGoingProjects.get(i);
                System.out.println(project.duration);
//                if (project.getPriority(date) <= 0) {
//                    onGoingProjects.remove(i);
//                    i--;
//                    continue;
//                }
                if (project.duration > 0) project.duration--;
                if (project.duration <= 0) {
                    project.complete(contributors);
                    score += project.getPriority(date);
                    completedProjects.add(onGoingProjects.remove(i));
                    i--;
                }
            }
            date++;
        }
    }


    public void optimize2() {
        for (Project p: projects) p.updatePrio2(contributors);
        projects.sort(Comparator.comparingInt(a -> a.priority2));
        projects.sort(Collections.reverseOrder());
        optimize();
    }

    public void readInput(String path) throws IOException {
        readInput(new BufferedReader(new FileReader("input/" + path + ".in.txt")));
    }

    public void readInput(BufferedReader input) throws IOException {
        String[] split = input.readLine().split(" ");
        int C = Integer.parseInt(split[0]);
        int P = Integer.parseInt(split[1]);

        for (int i = 0; i < C; i++) {
            split = input.readLine().split(" ");
            String name = split[0];
            int N = Integer.parseInt(split[1]);
            HashMap<String, Integer> skills = new HashMap<>();
            for (int j = 0; j < N; j++) {
                split = input.readLine().split(" ");
                skills.put(split[0], Integer.parseInt(split[1]));
            }
            contributors.add(new Contributor(name, skills));
        }

        for(int i = 0; i < P; i++) {
            split = input.readLine().split(" ");
            String name = split[0];
            int D = Integer.parseInt(split[1]);
            int S = Integer.parseInt(split[2]);
            int B = Integer.parseInt(split[3]);
            int R = Integer.parseInt(split[4]);
            HashMap<String, Integer> roles = new HashMap<>();

            for (int j = 0; j < R; j++) {
                split = input.readLine().split(" ");
                roles.put(split[0], Integer.parseInt(split[1]));
            }

            projects.add(new Project(name, D, S, B, roles));
        }
    }

    public void writeSubmission(String output) {
        try {
            FileWriter writer = new FileWriter("output/" + output + ".out.txt");
int temp = completedProjects.size();
            writer.write(String.valueOf(temp));
            writer.write("\n");
            for (int i = 0; i < completedProjects.size(); i++) {
                Project currentProject = completedProjects.get(i);
                writer.write(currentProject.name + "\n");
                for (int j = 0; j < currentProject.pastContributors.size(); j++) {
                    String currentContributor = currentProject.pastContributors.get(j);
                    writer.write(currentContributor);
                    if (j != currentProject.pastContributors.size() - 1) {
                        writer.write(" ");
                    } else
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

