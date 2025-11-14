package assign09;

import java.util.List;

import timing.TimingExperiment;

public class HashTiming {
    public static void main(String[] args) {
        List<Integer> sizes = TimingExperiment.buildProblemSizes(10000, 10000, 10);

        int iterations = 50;

        TimingExperiment bad = new BadHashTimingExperiment(sizes, iterations);
        TimingExperiment med = new MediumHashTimingExperiment(sizes, iterations);
        TimingExperiment good = new GoodHashTimingExperiment(sizes, iterations);

        bad.warmup(5);
        med.warmup(5);
        good.warmup(5);

        bad.run();
        med.run();
        good.run();

        bad.write("bad_hash_times.txt");
        med.write("medium_hash_times.txt");
        good.write("good_hash_times.txt");
    }

}
