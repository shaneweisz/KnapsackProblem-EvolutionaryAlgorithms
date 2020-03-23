public class GeneticAlgorithm {
    public static void main(String[] args) {
        double currentBestFitness = 0;

        int populationSize = 1000;
        String crossoverMethod = "1PX";
        double crossoverRatio = 0.7;
        double elitismRatio = 0.1;
        String mutationMethod = "BFM";
        double mutationRatio = 0.003;
        String selectionMethod = "RWS";

        Population population = new Population(populationSize, selectionMethod, crossoverMethod, crossoverRatio,
                mutationMethod, mutationRatio, elitismRatio);

        int generation = 1;
        Chromosome bestChromosome = population.getPopulation()[0];

        // System.out.println("Population " + i);
        // System.out.println("------------------------");
        // System.out.println(population);
        // System.out.println("------------------------");

        while ((++generation <= ProblemConfiguration.instance.maximumNumberOfGenerations)) {
            population.evolve();
            // System.out.println("Population " + i);
            // System.out.println("------------------------");
            // System.out.println(population);
            // System.out.println("------------------------");
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() > currentBestFitness) {
                currentBestFitness = bestChromosome.getFitness();
                System.out.println("Generation " + ProblemConfiguration.instance.decimalFormat.format(generation)
                        + ": Weight = " + bestChromosome.getTotalWeight() + ", Value = " + bestChromosome.getFitness()
                        + ", Knapsack = " + bestChromosome);
            }
        }

        System.out.println("Generation : " + ProblemConfiguration.instance.decimalFormat.format(generation) + " : "
                + bestChromosome.getTotalValue());
        System.out.println("numberOfCrossoverOperations : " + population.getNumberOfCrossoverOperations());
        System.out.println("numberOfMutationOperations  : " + population.getNumberOfMutationOperations());
    }
}