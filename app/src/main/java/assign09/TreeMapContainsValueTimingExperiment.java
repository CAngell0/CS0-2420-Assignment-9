package assign09;

import java.util.List;
import java.util.TreeMap;
import timing.TimingExperiment;

public class TreeMapContainsValueTimingExperiment extends TimingExperiment {

    private TreeMap<String, Integer> treeMap;
    private Integer doNotFindMe;
    private int repeat = 50;

    public TreeMapContainsValueTimingExperiment(List<Integer> problemSizes, int iterationCount) {
        super("Number of entries (n)", problemSizes, iterationCount);
    }

    @Override
    protected void setupExperiment(int problemSize) {
        treeMap = new TreeMap<>();
        doNotFindMe = -1;

        for (int i = 0; i < problemSize; i++) {
            String key = "key-" + i;
            Integer value = i;
            treeMap.put(key, value);
        }
    }

    @Override
    protected void runComputation() {
        boolean found = false;

        for (int i = 0; i < repeat; i++) {
            if (treeMap.containsValue(doNotFindMe)) {
                found = true;
            }
        }
    }
}
