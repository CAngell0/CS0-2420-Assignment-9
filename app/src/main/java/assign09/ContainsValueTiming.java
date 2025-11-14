package assign09;

import java.util.List;
import timing.TimingExperiment;

public class ContainsValueTiming {

    public static void main(String[] args) {
        List<Integer> sizes = TimingExperiment.buildProblemSizes(10000,10000,10);

        int iterations = 50;

        TimingExperiment hashTableExp = new HashTableContainsValueTimingExperiment(sizes, iterations);
        TimingExperiment treeMapExp = new TreeMapContainsValueTimingExperiment(sizes, iterations);

        hashTableExp.warmup(5);
        treeMapExp.warmup(5);

        hashTableExp.run();
        treeMapExp.run();

        hashTableExp.write("containsValue_hashTable.txt");
        treeMapExp.write("containsValue_treeMap.txt");
    }
}
