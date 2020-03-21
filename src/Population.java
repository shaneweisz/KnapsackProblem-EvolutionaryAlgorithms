import java.util.Arrays;
import java.util.Collections;

public class Population {
    private double elitismRatio;
    private double mutationRatio;
    private double crossoverRatio;
    private String selectionMethod;
    private Chromosome[] population;

    public Population(int size, double crossoverRatio, double elitismRatio, double mutationRatio,
            String selectionMethod) {
        this.crossoverRatio = crossoverRatio;
        this.elitismRatio = elitismRatio;
        this.mutationRatio = mutationRatio;
        this.selectionMethod = selectionMethod;
        population = new Chromosome[size];

        // Randomly generate the initial population
        for (int i = 0; i < size; i++) {
            population[i] = Chromosome.generateRandom();
        }

        // Sort the population in descending order i.e. fittest individuals at the
        // beginning of the array
        Arrays.sort(population, Collections.reverseOrder());
    }

    public void evolve() {
        Chromosome[] chromosomeArray = new Chromosome[population.length];
        int index = (int) Math.round(population.length * elitismRatio);
        // Keep the elite in the new chromosome array
        System.arraycopy(population, 0, chromosomeArray, 0, index);
        double[] rouletteWheel = createRouletteWheel();
        while (index < chromosomeArray.length) {
            Chromosome[] parents = selectParents(rouletteWheel);
            index++;
        }

        // Sort the new population in descending order
        Arrays.sort(population, Collections.reverseOrder());
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
            double probability = population[i].getFitness() / totalFitness;
            sumOfProbabilities += probability;
            rouletteWheel[i] = sumOfProbabilities;
        }
        return rouletteWheel;
    }

    /**
     * Picks a parent according to the probabilities in the specified roulette wheel
     */
    private Chromosome spinRouletteWheel(double[] rouletteWheel) {
        double selectionPoint = Configuration.instance.randomGenerator.nextFloat();
        for (int i = 0; i < rouletteWheel.length; i++) {
            if (selectionPoint < rouletteWheel[i]) {
                return population[i];
            }
        }
        // if we get here, the selection point must be one - so pick the last member of
        // the poulation
        return population[population.length - 1];
    }

    /**
     * Selects the two parent Chromosomes to be used for reproduction
     */
    private Chromosome[] selectParents(double[] rouletteWheel) {
        Chromosome[] parentArray = new Chromosome[2];
        // Choose two parents using Roulette Wheel Selection
        for (int i = 0; i < 2; i++) {
            Chromosome parent = spinRouletteWheel(rouletteWheel);
            parentArray[i] = parent;
        }
        return parentArray;
    }

}