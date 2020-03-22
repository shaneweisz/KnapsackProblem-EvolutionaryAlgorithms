import java.text.DecimalFormat;

public enum Configuration {
    instance;

    MersenneTwister randomGenerator = new MersenneTwister(System.currentTimeMillis());

    DecimalFormat decimalFormat = new DecimalFormat("00000"); // For outputting the number of generations

    int populationSize = 1000;
    int maximumNumberOfGenerations = 10000;
    int maximumCapacity = 822;

    double crossoverRatio = 0.7;
    double elitismRatio = 0.1;
    double mutationRatio = 0.5;
    String selectionMethod = "RWS";

    KnapsackInstance knapsackItems = new KnapsackInstance(); // Stores the possible knapsack items with their weights
                                                             // and values

}