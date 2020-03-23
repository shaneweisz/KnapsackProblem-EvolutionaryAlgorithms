import java.text.DecimalFormat;

public enum ProblemConfiguration {
    instance;

    MersenneTwister randomGenerator = new MersenneTwister(System.currentTimeMillis());

    DecimalFormat decimalFormat = new DecimalFormat("00000"); // For outputting the number of generations

    int maximumNumberOfGenerations = 10000;
    int maximumCapacity = 822;

    KnapsackInstance knapsackItems = new KnapsackInstance(); // Stores the possible knapsack items with their weights
                                                             // and values

}