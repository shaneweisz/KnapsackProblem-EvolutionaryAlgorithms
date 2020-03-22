public class Application {
    public static void main(String[] args) {
        double currentBestFitness = 0;

        Population population = new Population(Configuration.instance.populationSize,
                Configuration.instance.crossoverRatio, Configuration.instance.elitismRatio,
                Configuration.instance.mutationRatio, Configuration.instance.selectionMethod);

        int i = 1;
        Chromosome bestChromosome = population.getPopulation()[0];
        // System.out.println("Population " + i);
        // System.out.println("------------------------");
        // System.out.println(population);
        // System.out.println("------------------------");

        while ((++i <= Configuration.instance.maximumNumberOfGenerations)) {
            population.evolve();
            // System.out.println("Population " + i);
            // System.out.println("------------------------");
            // System.out.println(population);
            // System.out.println("------------------------");
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() > currentBestFitness) {
                currentBestFitness = bestChromosome.getFitness();
                System.out.println("Generation " + Configuration.instance.decimalFormat.format(i) + ": Weight = "
                        + bestChromosome.getTotalWeight() + ", Value = " + bestChromosome.getFitness() + ", Knapsack = "
                        + bestChromosome);
            }
        }

        System.out.println("Generation : " + Configuration.instance.decimalFormat.format(i) + " : "
                + bestChromosome.getTotalValue());
        System.out.println("numberOfCrossoverOperations : " + population.getNumberOfCrossoverOperations());
        System.out.println("numberOfMutationOperations  : " + population.getNumberOfMutationOperations());

        int[] bestGene = bestChromosome.getGene();
        String weights = "[";
        String values = "[";
        for (int j = 0; j < bestGene.length; j++) {
            if (bestGene[j] == 1) {
                System.out.println((j + 1) + "|" + Configuration.instance.knapsackItems.getWeight(j) + "|"
                        + Configuration.instance.knapsackItems.getValue(j));
                weights += Configuration.instance.knapsackItems.getWeight(j) + ",";
                values += Configuration.instance.knapsackItems.getValue(j) + ",";
            }
        }
        System.out.println(weights);
        System.out.println(values);
    }
}