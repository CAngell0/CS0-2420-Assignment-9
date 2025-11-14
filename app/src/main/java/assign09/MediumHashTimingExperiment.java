package assign09;

import java.util.List;
import timing.TimingExperiment;

public class MediumHashTimingExperiment extends TimingExperiment {

    private HashTable<StudentMediumHash, Integer> table;
    private StudentMediumHash[] keyArr;

    public MediumHashTimingExperiment(List<Integer> problemSizes, int iterationCount) {
        super("Number of students (n)", problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        table = new HashTable<>();
        keyArr = new StudentMediumHash[problemSize];

        for (int i = 0; i < problemSize; i++) {
            int uid = 1000000 + i;
            String firstName = "Josh" + i%20;
            String lastName = "Last" + i;

            StudentMediumHash student = new StudentMediumHash(uid, firstName, lastName);

            table.put(student, i);
            keyArr[i] = student;
        }
    }

    @Override
    protected void runComputation() {
        Integer sum = 0;
        for (int i = 0; i < keyArr.length; i++) {
            Integer val = table.get(keyArr[i]);
            sum += val;
        }
    }
}
