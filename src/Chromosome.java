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
        int total_weight = 0;
        while (total_weight < Configuration.instance.maximumCapacity) {
            int random_item = Configuration.instance.randomGenerator.nextInt(150);
            if (gene[random_item] == 1) {
                continue; // i.e. if the item is already in the knapsack, pick another one.
            }
            int weight = Configuration.instance.knapsackItems.getWeight(random_item);
            if (total_weight + weight > Configuration.instance.maximumCapacity) {
                break;
            }
            gene[random_item] = 1;
            total_weight += weight;
        }
        return new Chromosome(gene);
    }

    /** Gets the total weight of the knapsack items in the current Chromsome */
    public int getTotalWeight() {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /** Gets the total weight of the knapsack items encoded in a given gene array */
    public static int getTotalWeight(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /**
     * Gets the total value of the knapsack items encoded in the current Chromosome
     */
    public int getTotalValue() {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its value if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    /**
     * Gets the total value of the knapsack items encoded in a given gene array
     */
    public static int getTotalValue(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its value if gene[i] == 1
            sum += gene[i] * Configuration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    private static boolean isValid(int[] gene) {
        int weight = getTotalWeight(gene);
        // If the knapsack is above the maximum capacity, it is not valid
        if (weight > Configuration.instance.maximumCapacity) {
            return false;
        }
        return true;
    }

    /** Calculates the fitness of a particular gene */
    private static int calculateFitness(int[] gene) {
        if (!isValid(gene)) {
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

    public Chromosome[] doCrossover(Chromosome c) {
        int pivot = Configuration.instance.randomGenerator.nextInt(this.gene.length);
        int[] child1 = new int[this.gene.length];
        int[] child2 = new int[this.gene.length];
        System.arraycopy(this.gene, 0, child1, 0, pivot);
        System.arraycopy(c.gene, pivot, child1, pivot, child1.length - pivot);
        System.arraycopy(c.gene, 0, child2, 0, pivot);
        System.arraycopy(this.gene, pivot, child2, pivot, child2.length - pivot);
        Chromosome[] children = new Chromosome[2];
        children[0] = new Chromosome(child1);
        children[1] = new Chromosome(child2);
        return children;
    }

    public Chromosome doMutation(String method) {
        if (method.equals("BFM")) {
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);
            int randomItem = Configuration.instance.randomGenerator.nextInt(this.gene.length);
            newGene[randomItem] = newGene[randomItem] == 1 ? 0 : 1;
            return new Chromosome(newGene);
        }
        return null;
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

    /** Returns the chromosome as a string e.g. "0101110...1110" */
    public String toString() {
        String s = "[";
        for (int i = 0; i < 26; i++) {
            s += gene[i];
        }
        s += "...]";
        return s;
    }

    public static void main(String[] args) {
        System.out.println("Test Chromosome");
        int[] gene = { 1, 0, 0, 1 };
        Chromosome c = new Chromosome(gene);
        System.out.println("Before mutation: " + c);
        System.out.println("After mutation: " + c.doMutation("BFM"));
        System.out.println();
        int[] parent1 = { 1, 1, 1, 1 };
        int[] parent2 = { 0, 0, 0, 0 };
        Chromosome c1 = new Chromosome(parent1);
        Chromosome c2 = new Chromosome(parent2);
        System.out.println("Before crossover: " + c1 + " " + c2);
        Chromosome[] children = c1.doCrossover(c2);
        System.out.println("After crossover: " + children[0] + " " + children[1]);
    }
}