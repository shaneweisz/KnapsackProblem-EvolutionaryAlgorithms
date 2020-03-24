public class GeneticAlgorithm {
    private final static int POPULATION_SIZE = 250;
    private final static double ELITISM_RATIO = 0.1;

    public static void run(String configuration, String selectionMethod, String crossoverMethod, double crossoverRatio,
            String mutationMethod, double mutationRatio) {

        System.out.println("Configuration: " + configuration);

        // Create initial population
        Population population = new Population(POPULATION_SIZE, selectionMethod, crossoverMethod, crossoverRatio,
                mutationMethod, mutationRatio, ELITISM_RATIO);
        Chromosome bestChromosome = population.getPopulation()[0];
        double currentBestFitness = bestChromosome.getFitness();
        int generation = 1;

        // Iterate through generations
        while ((++generation) <= ProblemConfiguration.instance.maximumNumberOfIterations) {
            population.evolve();
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() > currentBestFitness) {
                currentBestFitness = bestChromosome.getFitness();
                // System.out.println("Generation " +
                // ProblemConfiguration.instance.decimalFormat.format(generation)
                // + ": Weight = " + bestChromosome.getTotalWeight() + ", Value = " +
                // bestChromosome.getFitness()
                // + ", Knapsack = " + bestChromosome);
            }
        }
        generation--; // We stopped before evolving the next generation

        // Output final best solution
        System.out.println("Generation " + ProblemConfiguration.instance.decimalFormat.format(generation) + " : "
                + bestChromosome.getTotalValue());
        // System.out.println("numberOfCrossoverOperations : " +
        // population.getNumberOfCrossoverOperations());
        // System.out.println("numberOfMutationOperations : " +
        // population.getNumberOfMutationOperations());
    }
}