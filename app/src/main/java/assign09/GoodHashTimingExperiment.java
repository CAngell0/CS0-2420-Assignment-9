package assign09;

import java.util.List;
import timing.TimingExperiment;

public class GoodHashTimingExperiment extends TimingExperiment {

    private HashTable<StudentGoodHash, Integer> table;
    private StudentGoodHash[] keyArr;

    public GoodHashTimingExperiment(List<Integer> problemSizes, int iterationCount) {
        super("Number of students (n)", problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        table = new HashTable<>();
        keyArr = new StudentGoodHash[problemSize];

        for (int i = 0; i < problemSize; i++) {
            int uid = 1000000 + i;
            String firstName = "Josh" + i%20;
            String lastName = "Last" + i;

            StudentGoodHash student = new StudentGoodHash(uid, firstName, lastName);

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
