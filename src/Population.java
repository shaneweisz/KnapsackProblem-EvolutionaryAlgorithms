import java.util.Arrays;
import java.util.Collections;

/**
 * Class for a population of chromosomes that evolves as part of the genetic
 * algorithm.
 */
public class Population {
    private double elitismRatio;
    private String selectionMethod;
    private String crossoverMethod;
    private double crossoverRatio;
    private String mutationMethod;
    private double mutationRatio;

    private Chromosome[] population;
    private int numberOfCrossoverOperations = 0;
    private int numberOfMutationOperations = 0;

    public Population(int size, String selectionMethod, String crossoverMethod, double crossoverRatio,
            String mutationMethod, double mutationRatio, double elitismRatio) {
        this.selectionMethod = selectionMethod;
        this.crossoverMethod = crossoverMethod;
        this.crossoverRatio = crossoverRatio;
        this.mutationMethod = mutationMethod;
        this.mutationRatio = mutationRatio;
        this.elitismRatio = elitismRatio;

        // Randomly generate the initial population
        population = new Chromosome[size];
        for (int i = 0; i < size; i++) {
            population[i] = Chromosome.generateRandom();
        }

        // Sort the population in descending order i.e. fittest individuals at the
        // beginning of the array
        Arrays.sort(population, Collections.reverseOrder());
    }

    public Chromosome[] getPopulation() {
        Chromosome[] chromosomeArray = new Chromosome[population.length];
        System.arraycopy(population, 0, chromosomeArray, 0, population.length);
        return chromosomeArray;
    }

    public int getNumberOfCrossoverOperations() {
        return numberOfCrossoverOperations;
    }

    public int getNumberOfMutationOperations() {
        return numberOfMutationOperations;
    }

    /** Evolves the population to the next generation */
    public void evolve() {
        Chromosome[] chromosomeArray = new Chromosome[population.length];

        // Keep the elite in the new chromosome array for the next generation
        int index = (int) Math.round(population.length * elitismRatio);
        System.arraycopy(population, 0, chromosomeArray, 0, index);

        double[] rouletteWheel = null;
        // Compute the roulette wheel for the population, in advance,
        // if the method is RWS
        if (this.selectionMethod.equals("RWS")) {
            rouletteWheel = createRouletteWheel();
        }

        // PRINT ROULETTE WHEEL
        // for (int i = 0; i < rouletteWheel.length; i++) {
        // System.out.print(rouletteWheel[i] + " ");
        // }

        while (index < chromosomeArray.length) {
            if (ProblemConfiguration.instance.randomGenerator.nextFloat() <= crossoverRatio) {
                Chromosome[] parents = new Chromosome[2];
                if (this.selectionMethod.equals("RWS")) {
                    parents = selectParentsRWS(rouletteWheel);
                } else if (this.selectionMethod.equals("TS")) {
                    parents = selectParentsTS();
                }
                Chromosome[] children = parents[0].doCrossover(this.crossoverMethod, parents[1]);
                numberOfCrossoverOperations++;

                if (ProblemConfiguration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                    chromosomeArray[index] = children[0].doMutation(this.mutationMethod);
                    numberOfMutationOperations++;
                } else {
                    chromosomeArray[index] = children[0];
                }
                index++;
                if (index < chromosomeArray.length) {
                    if (ProblemConfiguration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                        chromosomeArray[index] = children[1].doMutation("BFM");
                        numberOfMutationOperations++;
                    } else {
                        chromosomeArray[index] = children[1];
                    }
                }
            } else if (ProblemConfiguration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                chromosomeArray[index] = population[index].doMutation(this.mutationMethod);
                numberOfMutationOperations++;
            } else {
                chromosomeArray[index] = population[index];
            }
            index++;
        }

        // Sort the new population in descending order
        Arrays.sort(chromosomeArray, Collections.reverseOrder());
        population = chromosomeArray;
    }

    /**
     * Returns the sum of the fitness values of all chromosomes in the population
     */
    private int calculateTotalFitness() {
        int sum = 0;
        for (int i = 0; i < population.length; i++) {
            sum += population[i].getFitness();
        }
        return sum;
    }

    /**
     * Returns the cut off points as a double [] corresponding to the roulette wheel
     * 
     * e.g. [0.3, 0.32, 0.39, ..., 0.95, 1.0] means that individual 0 has a 0.3
     * chance of being selected, individual 1 has a 0.02 chance etc.
     */
    private double[] createRouletteWheel() {
        int totalFitness = calculateTotalFitness();
        double[] rouletteWheel = new double[population.length];
        double sumOfProbabilities = 0;
        for (int i = 0; i < population.length; i++) {
            double probability = (double) population[i].getFitness() / totalFitness;
            sumOfProbabilities += probability;
            rouletteWheel[i] = sumOfProbabilities;
        }
        return rouletteWheel;
    }

    /**
     * Picks a parent according to the probabilities in the specified roulette wheel
     * Note: the roulette wheel sections are disjoint
     */
    private Chromosome spinRouletteWheel(double[] rouletteWheel) {
        double selectionPoint = ProblemConfiguration.instance.randomGenerator.nextFloat();
        for (int i = 0; i < rouletteWheel.length; i++) {
            if (selectionPoint < rouletteWheel[i]) {
                return population[i];
            }
        }
        // if we get here, the selection point must be one - so pick the last member of
        // the population
        return population[population.length - 1];
    }

    /**
     * Selects the two parent Chromosomes to be used for reproduction using Roulette
     * Wheel Selection
     */
    private Chromosome[] selectParentsRWS(double[] rouletteWheel) {
        Chromosome[] parentArray = new Chromosome[2];
        // Choose two parents using Roulette Wheel Selection
        for (int i = 0; i < 2; i++) {
            Chromosome parent = spinRouletteWheel(rouletteWheel);
            parentArray[i] = parent;
        }
        return parentArray;
    }

    /**
     * Selects the two parent Chromosomes to be used for reproduction using
     * Tournament Selection
     */
    private Chromosome[] selectParentsTS() {
        Chromosome[] parentArray = new Chromosome[2];

        // Run two tournaments each to pick one of the parents
        for (int i = 0; i < 2; i++) {
            // Select the first contender for the tournament
            int best = ProblemConfiguration.instance.randomGenerator.nextInt(population.length);
            for (int j = 0; j < 3; j++) {
                // Test three contenders against the current best contender
                int contender = ProblemConfiguration.instance.randomGenerator.nextInt(population.length);
                if (population[contender].getFitness() > population[best].getFitness()) {
                    best = contender;
                }
            }
            // Add the winner of the tournament as one of the parent
            parentArray[i] = population[best];
        }

        return parentArray;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < population.length; i++) {
            s += String.format("Weight = %d, Value = %d", population[i].getTotalWeight(), population[i].getTotalValue())
                    + "\n";
        }
        return s;
    }
}