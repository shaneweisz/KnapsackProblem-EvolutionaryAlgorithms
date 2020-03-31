/**
 * Class used for running a genetic algorithm solution to the knapsack problem,
 * given the specified parameters such as selection method, crossover method
 * etc.
 */
public class GeneticAlgorithm {
    private final static int POPULATION_SIZE = 300;
    private final static double ELITISM_RATIO = 0.1;

    private String configuration;
    private String selectionMethod;
    private String crossoverMethod;
    private double crossoverRatio;
    private String mutationMethod;
    private double mutationRatio;

    public GeneticAlgorithm(String configuration, String selectionMethod, String crossoverMethod, double crossoverRatio,
            String mutationMethod, double mutationRatio) {
        this.configuration = configuration;
        this.selectionMethod = selectionMethod;
        this.crossoverMethod = crossoverMethod;
        this.crossoverRatio = crossoverRatio;
        this.mutationMethod = mutationMethod;
        this.mutationRatio = mutationRatio;
    }

    public int run() {

        // Create initial population
        Population population = new Population(POPULATION_SIZE, selectionMethod, crossoverMethod, crossoverRatio,
                mutationMethod, mutationRatio, ELITISM_RATIO);
        Chromosome bestChromosome = population.getPopulation()[0];
        // double currentBestFitness = bestChromosome.getFitness();
        int generation = 1;

        // To be used for statistics for report
        int numGenerations = ProblemConfiguration.instance.maximumNumberOfIterations;
        String params = String.format("GA | #%d | %s | %s (%.1f) | %s (%.3f)", numGenerations, selectionMethod,
                crossoverMethod, crossoverRatio, mutationMethod, mutationRatio);
        int[] bweights = new int[numGenerations];
        int[] bvalues = new int[numGenerations];
        String[] knapsacks = new String[numGenerations];
        long startTime = System.currentTimeMillis();

        bweights[0] = bestChromosome.getTotalWeight();
        bvalues[0] = bestChromosome.getTotalValue();
        knapsacks[0] = bestChromosome.toString();

        // Iterate through generations
        while ((++generation) <= ProblemConfiguration.instance.maximumNumberOfIterations) {
            population.evolve();
            bestChromosome = population.getPopulation()[0];

            bweights[generation - 1] = bestChromosome.getTotalWeight();
            bvalues[generation - 1] = bestChromosome.getTotalValue();
            knapsacks[generation - 1] = bestChromosome.toString();

            // if (bestChromosome.getFitness() > currentBestFitness) {
            // currentBestFitness = bestChromosome.getFitness();
            // }
        }
        generation--; // We stopped before evolving the next generation

        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;

        // Final best solution
        int maxValue = bestChromosome.getTotalValue();

        System.out.println(configuration + ": " + maxValue);

        // Create report
        String report = ReportGenerator.generateReport(configuration, params, bweights, bvalues, knapsacks, runtime);
        ReportGenerator.writeToFile(report, configuration);

        return maxValue;
    }
}