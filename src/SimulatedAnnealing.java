import java.util.ArrayList;

/**
 * Class used for running a simulated annealing solution to the knapsack
 * problem, given the specified parameters: initial temperature and cooling rate
 * etc.
 */
public class SimulatedAnnealing {
    // Parameters
    private String configuration;
    private int initialTemperature;
    private double coolingRate;

    public SimulatedAnnealing(String configuration, int initialTemperature, double coolingRate) {
        this.configuration = configuration;
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
    }

    /**
     * Returns the highest value of the knapsack achieved using SA with the given
     * parameters
     */
    public int run() {
        // Initialize the temperature
        double temperature = initialTemperature;

        // Start with an initial random solution
        int[] knapsack = generateRandom();

        int[] bestKnapsack = knapsack;
        System.out.println("Initial solution: " + getValue(knapsack));

        for (int i = 1; i <= ProblemConfiguration.instance.maximumNumberOfIterations; i++) {
            int[] neighbor = generateNeighbor(knapsack);
            int currentValue = getValue(knapsack);
            int neighborValue = getValue(neighbor);

            if (acceptanceProbability(currentValue, neighborValue,
                    temperature) > ProblemConfiguration.instance.randomGenerator.nextDouble()) {
                knapsack = neighbor;
            }

            if (getValue(knapsack) > getValue(bestKnapsack)) {
                bestKnapsack = knapsack;
                System.out.println(String.format("Temperature %.2f: %d", temperature, getValue(knapsack)));
            }
            temperature *= (1 - coolingRate);
        }

        return getValue(bestKnapsack);
    }

    private double acceptanceProbability(double currentValue, double neighborValue, double temperature) {
        if (neighborValue > currentValue) {
            return 1;
        }
        return Math.exp((neighborValue - currentValue) / temperature);
    }

    /** Gets the weight of the knapsack */
    private int getWeight(int[] knapsack) {
        int sum = 0;
        for (int i = 0; i < knapsack.length; i++) {
            // knapsack[i] is either 1 or 0 depending on whether the item is in the knapsack
            // or
            // not - hence, we only add its weight if knapsack[i] == 1
            sum += knapsack[i] * ProblemConfiguration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    private boolean isValid(int[] knapsack) {
        return getWeight(knapsack) <= ProblemConfiguration.instance.maximumCapacity;
    }

    /** Gets the value of the knapsack */
    private int getValue(int[] knapsack) {
        // Value of the knapsack must be 0 if it is overweight
        if (!isValid(knapsack)) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < knapsack.length; i++) {
            // knapsack[i] is either 1 or 0 depending on whether the item is in the knapsack
            // or
            // not - hence, we only add its value if knapsack[i] == 1
            sum += knapsack[i] * ProblemConfiguration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    /**
     * Generates a random knapsack by randomly adding items to the knapsack, and
     * stopping just before the knapsack becomes overweight
     */
    private int[] generateRandom() {
        int[] knapsack = new int[150];
        int total_weight = 0;
        // Keep adding items to the knapsack while the current weight is less than the
        // maximum capacity
        while (total_weight < ProblemConfiguration.instance.maximumCapacity) {
            int random_item = ProblemConfiguration.instance.randomGenerator.nextInt(150);
            if (knapsack[random_item] == 1) {
                continue; // i.e. if the item is already in the knapsack, pick a different one.
            }
            int weight = ProblemConfiguration.instance.knapsackItems.getWeight(random_item);
            if (total_weight + weight > ProblemConfiguration.instance.maximumCapacity) {
                // if adding this item will make the knapsack overweight, then end the loop and
                // use the current knapsack configuration as the random chromosome
                break;
            }
            knapsack[random_item] = 1;
            total_weight += weight;
        }
        return knapsack;
    }

    private int[] generateNeighbor(int[] knapsack) {
        int[] neighbor = new int[knapsack.length];
        System.arraycopy(knapsack, 0, neighbor, 0, knapsack.length);

        // Get the current chosen items
        ArrayList<Integer> currentUsed = new ArrayList<Integer>();
        for (int i = 0; i < knapsack.length; i++) {
            if (knapsack[i] == 1) {
                currentUsed.add(i);
            }
        }
        int numUsed = currentUsed.size();

        // Remove randomly one of the chosen items
        int itemToRemove = currentUsed.get(ProblemConfiguration.instance.randomGenerator.nextInt(numUsed));
        neighbor[itemToRemove] = 0;

        // Add one random item that keeps the knapsack valid
        do {
            int randomItem = ProblemConfiguration.instance.randomGenerator.nextInt(knapsack.length);
            if (neighbor[randomItem] == 1) {
                continue;
            }
            neighbor[randomItem] = 1;
            if (!isValid(neighbor)) {
                neighbor[randomItem] = 0; // put back the item
                continue;
            }
        } while (!isValid(neighbor));

        return neighbor;
    }

    public static void main(String[] args) {
        String configuration = "sa_default_01";
        int temp = 100000;
        double coolingRate = 0.1;
        SimulatedAnnealing sa = new SimulatedAnnealing(configuration, temp, coolingRate);
        sa.run();
    }
}