package timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assign09.HashTable;

/**
 * A timing experiment to test the speed of the containsValue() method
 * in the custom made HashTable data structure.
 * @author Carson Angell
 * @version 11/13/25
 */
public class CustomContainsValueTimingExperiment extends TimingExperiment {
    private Random random = new Random();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<Integer> values = new ArrayList<>();
    private HashTable<String, Integer> table = new HashTable<>();

    public CustomContainsValueTimingExperiment(String problemSizeName, List<Integer> problemSizes, int iterationCount) {
        super(problemSizeName, problemSizes, iterationCount);
    }

    //Runs the experiment
    public static void main(String[] args) {
        int warmup = 25;
        int iterationCount = 50;
        List<Integer> problemSizes = buildProblemSizes(1000, 1000, 20);

        TimingExperiment experiment = new CustomContainsValueTimingExperiment("number_of_values_to_check", problemSizes, iterationCount);
        experiment.warmup(warmup);
        experiment.run();
        experiment.print();
        experiment.writeToCSV("timing.csv");
    }

    /**
     * A helper method that generates a random key to be used in the data structure.
     * It simply concattenates 20 random alphabetical characters.
     * @return
     */
    private String generateRandomKey(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++)
            builder.append((char) (random.nextInt(26) + 'a'));

        return builder.toString();
    }

    /**
     * A helper method that generates a random value to be used in the data structure.
     * It creates a random number between 0 and 100,000 exclusive.
     * @return
     */
    private int generateRandomValue(){
        return random.nextInt(100_000);
    }

    /** {@inheritDoc} */
    @Override
    protected void setupExperiment(int problemSize) {
        //Clears the table data structure and the lists holding the data
        keys.clear();
        values.clear();
        table.clear();

        //Generates random key-value pairs and adds them tom the keys and values lists. Also adds to the table
        for (int i = 0; i < problemSize; i++){
            String key = generateRandomKey();
            int value = generateRandomValue();

            keys.add(key);
            values.add(value);
            table.put(key, value);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void runComputation() {
        //Calls the table's containsValue() method for each unique value in the table.
        for (Integer value : values){
            table.containsValue(value);
        }
    }
}
