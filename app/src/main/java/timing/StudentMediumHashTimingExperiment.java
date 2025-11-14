package timing;

import java.util.List;
import java.util.Random;

import assign09.StudentMediumHash;

public class StudentMediumHashTimingExperiment extends TimingExperiment {
    private Random random = new Random();

    public StudentMediumHashTimingExperiment(String problemSizeName, List<Integer> problemSizes, int iterationCount) {
        super(problemSizeName, problemSizes, iterationCount);
    }

     /**
     * A helper method that creates a random student hash object
     * @return
     */
    private StudentMediumHash generateRandomStudent(){
        //Creates a random uid
        int uid = random.nextInt(99999999);
        String firstName = "";
        String lastName = "";

        //Creates a randomized first name and last name by creating random, 6 character long strings
        StringBuilder firstNameBuilder = new StringBuilder();
        StringBuilder lastNameBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) firstNameBuilder.append((char) random.nextInt(26) + 'a');
        for (int i = 0; i < 5; i++) lastNameBuilder.append((char) random.nextInt(26) + 'a');

        firstName = firstNameBuilder.toString();
        lastName = lastNameBuilder.toString();

        return new StudentMediumHash(uid, firstName, lastName);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setupExperiment'");
    }

    @Override
    protected void runComputation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'runComputation'");
    }
}
