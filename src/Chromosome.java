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
        while (total_weight < ProblemConfiguration.instance.maximumCapacity) {
            int random_item = ProblemConfiguration.instance.randomGenerator.nextInt(150);
            if (gene[random_item] == 1) {
                continue; // i.e. if the item is already in the knapsack, pick another one.
            }
            int weight = ProblemConfiguration.instance.knapsackItems.getWeight(random_item);
            if (total_weight + weight > ProblemConfiguration.instance.maximumCapacity) {
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
            sum += gene[i] * ProblemConfiguration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /** Gets the total weight of the knapsack items encoded in a given gene array */
    public static int getTotalWeight(int[] gene) {
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack or
            // not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * ProblemConfiguration.instance.knapsackItems.getWeight(i);
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
            sum += gene[i] * ProblemConfiguration.instance.knapsackItems.getValue(i);
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
            sum += gene[i] * ProblemConfiguration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    private static boolean isValid(int[] gene) {
        int weight = getTotalWeight(gene);
        // If the knapsack is above the maximum capacity, it is not valid
        if (weight > ProblemConfiguration.instance.maximumCapacity) {
            return false;
        }
        return true;
    }

    /** Calculates the fitness of a particular gene */
    private static int calculateFitness(int[] gene) {
        if (!isValid(gene)) { // Assign a zero fitness if the knapsack is overweight
            return 0;
        } // Otherwise, the fitness is the value of the knapsack
        return getTotalValue(gene);
    }

    public int[] getGene() {
        return gene;
    }

    public int getFitness() {
        return fitness;
    }

    public Chromosome[] doCrossover(String method, Chromosome c) {
        if (method.equals("1PX")) {
            // Pick a random index to be used as a crossover point
            int pivot = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int[] child1 = new int[this.gene.length];
            int[] child2 = new int[this.gene.length];

            // Encode genes for child 1
            System.arraycopy(this.gene, 0, child1, 0, pivot);
            System.arraycopy(c.gene, pivot, child1, pivot, child1.length - pivot);

            // Encode genes for child 2
            System.arraycopy(c.gene, 0, child2, 0, pivot);
            System.arraycopy(this.gene, pivot, child2, pivot, child2.length - pivot);

            // Create children chromosomes from the genes
            Chromosome[] children = new Chromosome[2];
            children[0] = new Chromosome(child1);
            children[1] = new Chromosome(child2);
            return children;
        } else if (method.equals("2PX")) {
            // Pick two random indices to be used as crossover points
            int pivot1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int pivot2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);

            // Makes sure that pivot1 < pivot2
            if (pivot2 < pivot1) {
                int temp = pivot1;
                pivot1 = pivot2;
                pivot2 = temp;
            }

            int[] child1 = new int[this.gene.length];
            int[] child2 = new int[this.gene.length];

            // Encode genes for child 1
            System.arraycopy(this.gene, 0, child1, 0, pivot1);
            System.arraycopy(c.gene, pivot1, child1, pivot1, pivot2 - pivot1);
            System.arraycopy(this.gene, pivot2, child1, pivot2, child1.length - pivot2);

            // Encode genes for child 2
            System.arraycopy(c.gene, 0, child2, 0, pivot1);
            System.arraycopy(this.gene, pivot1, child2, pivot1, pivot2 - pivot1);
            System.arraycopy(c.gene, pivot2, child2, pivot2, child2.length - pivot2);

            // Create children chromosomes from the genes
            Chromosome[] children = new Chromosome[2];
            children[0] = new Chromosome(child1);
            children[1] = new Chromosome(child2);
            return children;
        }
        return null;
    }

    public Chromosome doMutation(String method) {
        if (method.equals("BFM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose a random item in the knapsack and 'flip' the bit
            int randomItem = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            newGene[randomItem] = newGene[randomItem] == 1 ? 0 : 1;

            return new Chromosome(newGene);
        } else if (method.equals("EXM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose two distinct random indices in the knapsack
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // Swap the bits of the two random indices
            int temp = newGene[index1];
            newGene[index1] = newGene[index2];
            newGene[index2] = temp;

            return new Chromosome(newGene);
        } else if (method.equals("IVM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose two random indices in the knapsack
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // Make sure index1 < index2
            if (index2 < index1) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }
            // Reverse the substring between index1 and index2, inclusive
            for (int i = 0; i <= index2 - index1; i++) {
                newGene[index1 + i] = this.gene[index2 - i];
            }

            return new Chromosome(newGene);
        } else if (method.equals("ISM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose two random indices in the knapsack
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // Make sure index1 < index2
            int temp;
            if (index2 < index1) {
                temp = index1;
                index1 = index2;
                index2 = temp;
            }
            // Store the item directly after the first
            temp = newGene[index1 + 1];
            // Insert the second item directly after the first
            newGene[index1 + 1] = newGene[index2];

            // Move the rest of the items back by one
            int curr = temp, next;
            for (int i = 1; i < index2 - index1; i++) {
                // Store the next item which we are about to replace
                next = newGene[index1 + 1 + i];
                // Replace the next item with the current item i.e. moving the current item back
                // by one
                newGene[index1 + 1 + i] = curr;
                // Set the current item to be the stored next value that is to be moved back
                curr = next;
            }
            return new Chromosome(newGene);

        } else if (method.equals("DPM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose two random indices in the knapsack
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // Make sure index1 < index2
            int temp;
            if (index2 < index1) {
                temp = index1;
                index1 = index2;
                index2 = temp;
            }

            // We will 'scramble' the genes between index1 and index2, inclusive
            for (int i = index1; i <= index2; i++) {
                // Pick a random index within the chosen portion to swap the current item with
                int indexToSwap = ProblemConfiguration.instance.randomGenerator.nextInt(index1, index2);
                // Swap the chosen two items
                temp = newGene[i];
                newGene[i] = newGene[indexToSwap];
                newGene[indexToSwap] = temp;
            }

            return new Chromosome(newGene);

        }
        return null; // Chosen method must be one of the above
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
        int limit = Math.min(26, gene.length);
        for (int i = 0; i < limit; i++) {
            s += gene[i];
        }
        s += "...]";
        return s;
    }

    public static void main(String[] args) {
        System.out.println("Test Chromosome Methods: ");
        System.out.println();

        int[] gene = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Chromosome c = new Chromosome(gene);
        System.out.println("Before BFM mutation: " + c);
        System.out.println("After BFM mutation: " + c.doMutation("BFM"));
        System.out.println();

        int[] gene2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Chromosome cc = new Chromosome(gene2);
        System.out.println("Before EXM mutation: " + cc);
        System.out.println("After EXM mutation: " + cc.doMutation("EXM"));
        System.out.println();

        System.out.println("Before IVM mutation: " + cc);
        System.out.println("After IVM mutation: " + cc.doMutation("IVM"));
        System.out.println();

        System.out.println("Before ISM mutation: " + cc);
        System.out.println("After ISM mutation: " + cc.doMutation("ISM"));
        System.out.println();

        System.out.println("Before DPM mutation: " + cc);
        System.out.println("After DPM mutation: " + cc.doMutation("DPM"));
        System.out.println();

        int[] parent1 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        int[] parent2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Chromosome c1 = new Chromosome(parent1);
        Chromosome c2 = new Chromosome(parent2);
        System.out.println("Before crossover: " + c1 + " " + c2);
        Chromosome[] children = c1.doCrossover("1PX", c2);
        System.out.println("After crossover: " + children[0] + " " + children[1]);
        System.out.println();

        System.out.println("Before 2PX crossover: " + c1 + " " + c2);
        children = c1.doCrossover("2PX", c2);
        System.out.println("After 2PX crossover: " + children[0] + " " + children[1]);
    }
}