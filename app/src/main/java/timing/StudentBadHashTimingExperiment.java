package timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assign09.HashTable;
import assign09.StudentBadHash;

public class StudentBadHashTimingExperiment extends TimingExperiment {
    private Random random = new Random();
    private ArrayList<StudentBadHash> students = new ArrayList<>();
    private ArrayList<Integer> grades = new ArrayList<>();
    private HashTable<StudentBadHash, Integer> studentGrades = new HashTable<>();

    public StudentBadHashTimingExperiment(String problemSizeName, List<Integer> problemSizes, int iterationCount) {
        super(problemSizeName, problemSizes, iterationCount);
    }

    public static void main(String[] args) {
        int warmup = 25;
        int iterationCount = 100;
        List<Integer> problemSizes = buildProblemSizes(100000, 10000, 40);

        TimingExperiment experiment = new StudentBadHashTimingExperiment("studentAmount", problemSizes, iterationCount);

        experiment.warmup(warmup);
        experiment.run();
        experiment.print();
        experiment.writeToCSV("timing.csv");
    }

    /**
     * A helper method that creates a random student hash object
     * @return
     */
    private StudentBadHash generateRandomStudent(){
        //Creates a random uid
        int uid = random.nextInt(99999999);
        String firstName = "";
        String lastName = "";

        //Creates a randomized first name and last name by creating random, 6 character long strings
        StringBuilder firstNameBuilder = new StringBuilder();
        StringBuilder lastNameBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) firstNameBuilder.append((char) (random.nextInt(26) + 'a'));
        for (int i = 0; i < 5; i++) lastNameBuilder.append((char) (random.nextInt(26) + 'a'));

        firstName = firstNameBuilder.toString();
        lastName = lastNameBuilder.toString();

        return new StudentBadHash(uid, firstName, lastName);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        students.clear();
        grades.clear();
        
        studentGrades.clear();
        studentGrades = new HashTable<StudentBadHash, Integer>();
        for (int i = 0; i < problemSize; i++){
            students.add(generateRandomStudent());
            grades.add(random.nextInt(100));
        }
    }

    @Override
    protected void runComputation() {
        for (int index = 0; index < students.size(); index++){
            studentGrades.put(students.get(index), grades.get(index));
        }
    }
}
