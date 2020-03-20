public enum Configuration {
    instance;

    MersenneTwister randomGenerator = new MersenneTwister(System.currentTimeMillis());

    int populationSize = 2500;
    int maximumNumberOfGenerations = 10000;

    double crossoverRatio = 0.7;
    double elitismRatio = 0.1;
    double mutationRatio = 0.00005;
}