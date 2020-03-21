public class Chromosome implements Comparable<Chromosome> {
    private final int[] gene;
    private final int fitness;

    public Chromosome(int[] gene) {
        this.gene = gene;
        fitness = calculateFitness(gene);
    }

    /** Generates a random Chromosome for the initial population */
    protected static Chromosome generateRandom() {
        int[] gene = new int[150];
        for (int i = 0; i < 150; i++) {
            gene[i] = Configuration.instance.randomGenerator.nextInt(2); // Generates either 0 or 1
        }
        return new Chromosome(gene);
    }

    /** Gets the total weight of the knapsack items encoded in a given gene array */
    private static int getTotalWeight(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /** Gets the total value of the knapsack items encoded in a given gene array */
    private static int getTotalValue(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its value if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    /** Calculates the fitness of a particular gene */
    private static int calculateFitness(int[] gene) {
        int weight = getTotalWeight(gene);
        // If the knapsack is above the maximum capacity, assign a zero fitness
        if (weight > Configuration.instance.maximumCapacity) {
            return 0;
        }
        // Else the fitness is the value of the gene
        return getTotalValue(gene);
    }

    public int[] getGene() {
        return gene;
    }

    public int getFitness() {
        return fitness;
    }

    /** Returns the chromosome as a string e.g. "0101110...1110" */
    public String toString() {
        String s = "";
        for (int i = 0; i < gene.length; i++) {
            s += gene[i];
        }
        return s;
    }

    public int compareTo(Chromosome c) {
        if (this.fitness < c.getFitness()) {
            return -1;
        } else if (this.fitness > c.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        // for (int i = 0; i < 10; i++) {
        // System.out.println(generateRandom());
        // }
    }

}