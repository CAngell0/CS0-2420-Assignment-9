package assign09;

import java.util.List;
import timing.TimingExperiment;

public class HashTableContainsValueTimingExperiment extends TimingExperiment {

    private HashTable<String, Integer> table;
    private Integer doNotFindMe;
    private int repeat = 50;

    public HashTableContainsValueTimingExperiment(List<Integer> problemSizes, int iterationCount) {
        super("Number of entries (n)", problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        table = new HashTable<>();
        doNotFindMe = -1;

        for (int i = 0; i < problemSize; i++) {
            String key = "key-" + i;
            Integer value = i;
            table.put(key, value);
        }
    }

    @Override
    protected void runComputation() {
        boolean found = false;

        for (int i = 0; i < repeat; i++) {
            if (table.containsValue(doNotFindMe)) {
                found = true;
            }
        }
    }
}
