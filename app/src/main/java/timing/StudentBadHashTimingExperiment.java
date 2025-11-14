package timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assign09.HashTable;
import assign09.StudentBadHash;

/**
 * A timing experiment to test the speed fo the student object with bad hash functions
 * in the custom made HashTable data structure.
 * @author Carson Angell
 * @version 11/13/25
 */
public class StudentBadHashTimingExperiment extends TimingExperiment {
    private Random random = new Random();
    private ArrayList<StudentBadHash> students = new ArrayList<>();
    private ArrayList<Integer> grades = new ArrayList<>();
    private HashTable<StudentBadHash, Integer> studentGrades = new HashTable<>();

    public StudentBadHashTimingExperiment(String problemSizeName, List<Integer> problemSizes, int iterationCount) {
        super(problemSizeName, problemSizes, iterationCount);
    }

    //Runs the experiment
    public static void main(String[] args) {
        int warmup = 25;
        int iterationCount = 100;
        List<Integer> problemSizes = buildProblemSizes(100000, 10000, 40);

        TimingExperiment experiment = new StudentBadHashTimingExperiment("number_of_students_to_add", problemSizes, iterationCount);

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

        //Builds the first and last name strings
        firstName = firstNameBuilder.toString();
        lastName = lastNameBuilder.toString();

        return new StudentBadHash(uid, firstName, lastName);
    }

    /** {@inheritDoc} */
    @Override
    protected void setupExperiment(int problemSize) {
        //Clears the two array lists holding the students and grades
        students.clear();
        grades.clear();
        
        //Clears the HashTable and reinitializes it. This is to reset the capacity as well
        studentGrades.clear();
        studentGrades = new HashTable<StudentBadHash, Integer>();

        //Generates a problemSize number of random students and adds them to the array lists (along with a random grade)
        for (int i = 0; i < problemSize; i++){
            students.add(generateRandomStudent());
            grades.add(random.nextInt(100));
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void runComputation() {
        //Calls the HashTable's put() method for each student and their grades
        for (int index = 0; index < students.size(); index++){
            studentGrades.put(students.get(index), grades.get(index));
        }
    }
}
