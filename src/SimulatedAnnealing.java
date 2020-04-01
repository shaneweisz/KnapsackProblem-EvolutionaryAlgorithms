/**
 * Class used for running a simulated annealing solution to the knapsack
 * problem, given the specified parameters of initial temperature and cooling
 * rate.
 */
public class SimulatedAnnealing {
    private String configuration;
    private int initialTemperature;
    private double coolingRate;

    private final double MIN_TEMP = 1;

    /**
     * Parametrized constructor for a Simulated Annealing instance with parameters
     * specified in the JSON files
     * 
     * @param configuration      e.g. "sa_default_01"
     * @param initialTemperature e.g. 10000
     * @param coolingRate        e.g 0.5
     */
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
        int[] knapsack = generateRandomKnapsack();
        System.out.println("Initial solution: " + getValue(knapsack));

        // To store the best knapsack found so far
        int[] bestKnapsack = knapsack;

        int i = 1; // To keep track of the number of iterations

        // Stop the loop if the max number of generations has been hit
        // or the temperature is below the specified minimum
        while (i <= ProblemConfiguration.instance.maximumNumberOfIterations && temperature > MIN_TEMP) {
            // Generate a random neighbor solution by flipping a random bit
            int[] neighbor = generateNeighbor(knapsack);

            // Decide whether to accept or reject the neighbor based on the respective
            // energies, and the probability acceptance funtion
            int currentEnergy = getEnergy(knapsack);
            int neighborEnergy = getEnergy(neighbor);
            if (ProblemConfiguration.instance.randomGenerator.nextDouble() < acceptanceProbability(currentEnergy,
                    neighborEnergy, temperature)) {
                knapsack = neighbor;
            }

            // Update the current best solution if necessary
            if (getValue(knapsack) > getValue(bestKnapsack)) {
                bestKnapsack = knapsack;
                System.out.println(String.format("Temperature %.2f: W=%d V=%d", temperature, getWeight(knapsack),
                        getValue(knapsack)));
            }

            // Have 750 iterations at each temperature
            if (i % 750 == 0) {
                temperature *= (1 - coolingRate);
            }

            i += 1;
        }

        return getValue(bestKnapsack);
    }

    /**
     * Uses the Boltzmann distribution to determine the probability of accepting a
     * new solution
     */
    private double acceptanceProbability(double currentValue, double neighborValue, double temperature) {
        if (neighborValue > currentValue) {
            return 1;
        }
        if (neighborValue == 0) { // Don't accept an overweight solution
            return 0;
        }
        return Math.exp((neighborValue - currentValue) / temperature);
    }

    /**
     * Returns the 'energy' of the knapsack for the algorithm - 0 if it is
     * overweight, else return the value of the knapsack
     */
    private int getEnergy(int[] knapsack) {
        // Energy of the knapsack must be 0 if it is overweight
        if (!isValid(knapsack)) {
            return 0;
        }
        return getValue(knapsack);

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

    /**
     * Returns true if the weight of the knapsack is less than or equal to the
     * maximum capacity, else returns false
     */
    private boolean isValid(int[] knapsack) {
        return getWeight(knapsack) <= ProblemConfiguration.instance.maximumCapacity;
    }

    /** Gets the value of the knapsack */
    private int getValue(int[] knapsack) {
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
    private int[] generateRandomKnapsack() {
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

    /** Generates a random neighbour knapsack based on flipping a random bit */
    private int[] generateNeighbor(int[] knapsack) {
        int[] neighbor = new int[knapsack.length];
        int randomItem = ProblemConfiguration.instance.randomGenerator.nextInt(150);
        System.arraycopy(knapsack, 0, neighbor, 0, knapsack.length);
        neighbor[randomItem] = neighbor[randomItem] == 1 ? 0 : 1;
        return neighbor;
    }

    /** Used for testing purposes */
    public static void main(String[] args) {
        String configuration = "sa_default_01";
        int temp = 10000;
        double coolingRate = 0.5;
        SimulatedAnnealing sa = new SimulatedAnnealing(configuration, temp, coolingRate);
        sa.run();
    }
}