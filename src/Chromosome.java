import java.util.ArrayList;

/**
 * Class for a chromosome that will form part of the population in the genetic
 * algorithm. Each chromosome encodes a given knapsack configuration - i.e.
 * which items are chosen for the knapsack and which are not.
 */
public class Chromosome implements Comparable<Chromosome> {
    private final int[] gene; // Stores the genes that make up the chromosome
    private final int fitness;

    public Chromosome(int[] gene) {
        this.gene = gene;
        fitness = calculateFitness(gene);
    }

    /**
     * Generates a random Chromosome for the initial population. This involves
     * randomly adding items to the knapsack, but stopping just before the knapsack
     * becomes overweight
     */
    protected static Chromosome generateRandom() {
        int[] gene = new int[150];
        int total_weight = 0;
        // Keep adding items to the knapsack while the current weight is less than the
        // maximum capacity
        while (total_weight < ProblemConfiguration.instance.maximumCapacity) {
            int random_item = ProblemConfiguration.instance.randomGenerator.nextInt(150);
            if (gene[random_item] == 1) {
                continue; // i.e. if the item is already in the knapsack, pick a different one.
            }
            int weight = ProblemConfiguration.instance.knapsackItems.getWeight(random_item);
            if (total_weight + weight > ProblemConfiguration.instance.maximumCapacity) {
                // if adding this item will make the knapsack overweight, then end the loop and
                // use the current knapsack configuration as the random chromosome
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
            // gene[i] is either 1 or 0 depending on whether the item is in the knapsack
            // or not - hence, we only add its weight if gene[i] == 1
            sum += gene[i] * ProblemConfiguration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /**
     * Static method that gets the total weight of the knapsack items encoded in a
     * given gene array
     */
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
     * Static method that gets the total value of the knapsack items encoded in a
     * given gene array
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

    /**
     * Returns true if the weight of the knapsack encoded by the given gene array is
     * less than or equal to the maximum capacity, else returns false
     */
    private static boolean isValid(int[] gene) {
        int weight = getTotalWeight(gene);
        // If the knapsack is above the maximum capacity, it is not valid
        if (weight > ProblemConfiguration.instance.maximumCapacity) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the fitness of a knapsack encoded by a particular gene array. The
     * fitness is 0 if the knapsack is overweight, otherwise it is the knapsack's
     * value
     */
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

    /**
     * Performs a crossover operation between two parent chromosomes
     * 
     * @param method Either "1PX" for 1-Point Crossover or "2PX" for 2-Point
     *               Crossover
     * @param c      A second parent Chromosome to perform the crossover with this
     *               Chromosome
     * @return A Chromosome array with the two children Chromosomes resulting from
     *         the crossover operation
     */
    public Chromosome[] doCrossover(String method, Chromosome c) {
        // 1-Point Crossover
        if (method.equals("1PX")) {
            // Pick a random index to be used as a crossover point
            int pivot = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int[] child1 = new int[this.gene.length];
            int[] child2 = new int[this.gene.length];

            // Encode the genes for child 1
            // The first part is from parent 1 and the second part is from parent 2
            System.arraycopy(this.gene, 0, child1, 0, pivot);
            System.arraycopy(c.gene, pivot, child1, pivot, child1.length - pivot);

            // Encode genes for child 2
            // The first part is from parent 2 and the second part is from parent 1
            System.arraycopy(c.gene, 0, child2, 0, pivot);
            System.arraycopy(this.gene, pivot, child2, pivot, child2.length - pivot);

            // Create children chromosomes from the crossed-over genes
            Chromosome[] children = new Chromosome[2];
            children[0] = new Chromosome(child1);
            children[1] = new Chromosome(child2);
            return children;
        }
        // 2-Point Crossover
        else if (method.equals("2PX")) {
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
            // The middle part is from parent 2, and the first and end part from parent 1
            System.arraycopy(this.gene, 0, child1, 0, pivot1);
            System.arraycopy(c.gene, pivot1, child1, pivot1, pivot2 - pivot1);
            System.arraycopy(this.gene, pivot2, child1, pivot2, child1.length - pivot2);

            // Encode genes for child 2
            // The middle part is from parent 1, and the first and end part from parent 2
            System.arraycopy(c.gene, 0, child2, 0, pivot1);
            System.arraycopy(this.gene, pivot1, child2, pivot1, pivot2 - pivot1);
            System.arraycopy(c.gene, pivot2, child2, pivot2, child2.length - pivot2);

            // Create children chromosomes from the crossed-over genes
            Chromosome[] children = new Chromosome[2];
            children[0] = new Chromosome(child1);
            children[1] = new Chromosome(child2);
            return children;
        }
        // Should not get here, since the method must either be "1PX" or "2PX"
        return null;
    }

    /**
     * Performs a mutation operation on this chromosome
     * 
     * @param method One of "BFM", "EXM", "IVM", "ISM", "DPM":
     * 
     *               "BFM" - Bit Flip Mutation
     * 
     *               "EXM" - Exchange/Swap/Interchanging Mutation
     * 
     *               "IVM" - Inversion Mutation
     * 
     *               "ISM" - Insertion Mutation
     * 
     *               "DPM" - Displacement Mutation
     * 
     *               "SM" - Scramble Mutation (as an additional feature)
     * 
     * @return A Chromosome resulting from the mutation
     */
    public Chromosome doMutation(String method) {
        // Chooses a random bit in a chromosome, and 'flips' it -
        // i.e. changes it from 0 to 1 or from 1 to 0
        if (method.equals("BFM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose a random item in the knapsack and 'flip' the bit
            int randomItem = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            newGene[randomItem] = newGene[randomItem] == 1 ? 0 : 1;

            return new Chromosome(newGene);
        }
        // Chooses two random items in the chromosome, and swaps them
        else if (method.equals("EXM")) {
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
        }
        // Chooses two distinct random indices in the chromosome, and
        // reverses the substring between them (inclusive of the endpoints)
        else if (method.equals("IVM")) {
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
        }
        // Choose two random items in the chromosome, and insert the second directly
        // after the first, moving the rest of the items back to accomodate
        else if (method.equals("ISM")) {
            // Copy the current genes to the new chromosome
            int[] newGene = new int[this.gene.length];
            System.arraycopy(this.gene, 0, newGene, 0, this.gene.length);

            // Choose two random indices in the knapsack
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // We will insert the item at index2 directly after the item at index1

            // If index1 is less than index2, since we are moving the item at index2 to the
            // left, we will need to shift the other items right to accomodate
            if (index1 < index2) {
                // Store the item directly after the first
                int temp = newGene[index1 + 1];
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
            }
            // If index2 is less than index1, since we are moving the item at index2 to the
            // right, we will need to shift the other items left to accomodate
            else if (index2 < index1) {
                // Store the item we are going to insert after index1
                int itemToInsert = newGene[index2];
                // Shift the other items back, including the item at index1
                for (int i = index2; i < index1; i++) {
                    newGene[i] = newGene[i + 1];
                }
                // Insert the item at index2 where the item at index1 was
                newGene[index1] = itemToInsert;
                // Note, the item at index2 now immediately follows the item at index1
            }

            // System.out.println("TEST: " + newGene[index2] + " inserted after " +
            // newGene[index1]);

            return new Chromosome(newGene);
        }
        // Select two random items in the chromosome, take the items between these two
        // as a group, and move the whole group to another random point in the
        // chromosome, displaced from the original
        else if (method.equals("DPM")) {
            // Create an array list with the elements from this gene array
            ArrayList<Integer> newGeneList = new ArrayList<Integer>();
            for (int x : this.gene) {
                newGeneList.add(x);
            }

            // Choose two random indices in the knapsack
            // index1 will be the leftmost index of the group,
            // index 2 will be the rightmost index
            int index1 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            int index2;
            do {
                index2 = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length);
            } while (index2 == index1); // Ensure the two chosen indices are not the same

            // Ensure index1 < index2
            if (index2 < index1) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            // The size of the group to be displaced
            int groupSize = index2 - index1 + 1;

            // Extract the group that we will displace
            int[] group = new int[groupSize];
            for (int i = 0; i <= index2 - index1; i++) {
                group[i] = this.gene[index1 + i];
            }

            // Remove the group that is to be displaced from the original list
            for (int i = 0; i < groupSize; i++) {
                // Each element in the group becomes index1 after removing the previous element
                newGeneList.remove(index1);
            }

            // Randomly choose the index in the resulting list where we must insert the
            // displaced group
            int insertIndex = ProblemConfiguration.instance.randomGenerator.nextInt(this.gene.length - groupSize + 1);

            // Insert the displaced group at the chosen index
            for (int i = groupSize - 1; i >= 0; i--) {
                // Insert the group elements one-by-one at index `insertIndex`
                // The order is backwards to ensure correct order in final list
                newGeneList.add(insertIndex, group[i]);
            }

            // Cast from the Array List to an Integer array to an int array
            Integer[] newIntegerGene = newGeneList.toArray(new Integer[this.gene.length]);
            int[] newGene = new int[this.gene.length];
            for (int i = 0; i < this.gene.length; i++) {
                newGene[i] = newIntegerGene[i].intValue();
            }

            // Build the resulting Chromosome
            return new Chromosome(newGene);
        }
        // Chooses two distinct random indices in the chromosome, and
        // randomly shuffles the substring between them (inclusive of the endpoints)
        else if (method.equals("SM")) {
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
        // Should not get here, since the method must be one of the above
        return null;
    }

    // This Chromosome < a second Chromosome iff this Chromosome's fitness is less
    // than the second Chromosome's fitness
    public int compareTo(Chromosome c) {
        if (this.fitness < c.getFitness()) {
            return -1;
        } else if (this.fitness > c.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Formats the chromosome as a string e.g. "[0101110...1110]" for display
     * purposes
     */
    public String toString() {
        String s = "[";
        int limit = Math.min(26, gene.length);
        for (int i = 0; i < limit; i++) {
            s += gene[i];
        }
        s += "...]";
        return s;
    }

    // Tests the various crossover and mutation operations
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