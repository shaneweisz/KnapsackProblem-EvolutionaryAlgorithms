public class Chromosome {
    private final int[] gene;
    private final int fitness;

    public Chromosome(int[] gene) {
        this.gene = gene;
        fitness = calculateFitness(gene);
    }

    protected static Chromosome generateRandom() {
        int[] gene = new int[150];
        for (int i = 0; i < 150; i++) {
            gene[i] = Configuration.instance.randomGenerator.nextInt(2); // Generates either 0 or 1
        }
        return new Chromosome(gene);
    }

    private static int getTotalWeight(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    private static int getTotalValue(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its value if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    private static int calculateFitness(int[] gene) {
        int weight = getTotalWeight(gene);
        if (weight > Configuration.instance.maximumCapacity) {
            return 0;
        }
        return getTotalValue(gene);
    }

    public int[] getGene() {
        return gene;
    }

    public int getFitness() {
        return fitness;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < gene.length; i++) {
            s += gene[i];
        }
        return s;
    }

    public static void main(String[] args) {
        // for (int i = 0; i < 10; i++) {
        // System.out.println(generateRandom());
        // }
    }

}