import java.text.DecimalFormat;

/**
 * Defines the key components defined by the Knapsack problem - including the
 * maximum number of iterations for each algorithm (10000), the maximum capacity
 * of the knapsack (822), and this specific knapsack instance with all possible
 * items, and each item's associated weight and value.
 */
public enum ProblemConfiguration {
    instance;

    int maximumNumberOfIterations = 10000;
    int maximumCapacity = 822;
    KnapsackInstance knapsackItems = new KnapsackInstance(); // Stores the possible knapsack items

    MersenneTwister randomGenerator = new MersenneTwister(System.currentTimeMillis()); // For generating random numbers
    DecimalFormat decimalFormat = new DecimalFormat("00000"); // For outputting the number of generations
}