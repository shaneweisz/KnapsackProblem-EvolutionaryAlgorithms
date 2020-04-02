/**
 * Class that encodes a particle that participates in a swarm in PSO
 */
public class Particle {
    public Vector position;
    public Vector velocity;
    public Vector bestPosition;
    public int individualBestValue;

    /** Create a new particle with a randomly initialized position */
    public Particle() {
        position = new Vector();
        velocity = new Vector();
        bestPosition = position;
        // Randomly initialize the position of a new particle
        setRandomPosition();
        bestPosition = position.clone();
        individualBestValue = evaluateCurrentPosition();
    }

    /** Returns the fitness of the knapsack at the current position */
    public int evaluateCurrentPosition() {
        if (!isValid(position)) {
            return 0;
        }
        return getValue(position);
    }

    /**
     * Updates the particle's individual best value - should be invoked once the
     * particle's position has changed
     */
    public void updateIndividualBestValue() {
        int value = evaluateCurrentPosition();
        if (value > individualBestValue) {
            // System.out.println("New individual best");
            bestPosition = position.clone();
            individualBestValue = value;
        }
    }

    /** Returns a cloned copy of the particle's position vector */
    public Vector getPosition() {
        return position.clone();
    }

    /** Returns a cloned copy of the particle's velocity vector */
    public Vector getVelocity() {
        return velocity.clone();
    }

    /** Sets the particle's velocity vector to a new vector */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity.clone();
    }

    /**
     * Returns the particle's a cloned copy of the particle's best position vector
     */
    public Vector getBestPosition() {
        return bestPosition.clone();
    }

    /** Reurns the best fitness value the particle has encountered so far */
    public int getIndividualBestValue() {
        return individualBestValue;
    }

    /**
     * Updates the particle's position to its new position after its velocity has
     * changed.
     * 
     * Uses the sigmoid function to map each dimension's velocity value to a value
     * between 0 and 1 that represents the probability of the corresponding item
     * being in the knapsack or not - and then updates the position vector in
     * accordance with this probability
     */
    public void updatePosition() {
        // Loop through each dimension of the position vector
        for (int i = 0; i < position.size(); i++) {
            double rand = ProblemConfiguration.instance.randomGenerator.nextDouble();
            // Update the position dimension's value according to the probability
            // given by the sigmoid function applied to the velocity
            if (rand < (1.0 / (1 + Math.exp(-velocity.getValue(i))))) {
                this.position.setValue(i, 1);
            } else {
                this.position.setValue(i, 0);
            }
        }
    }

    /** Gets the weight of the knapsack encoded by a position vector */
    public static int getWeight(Vector position) {
        int sum = 0;
        for (int i = 0; i < position.size(); i++) {
            // knapsack[i] is either 1 or 0 depending on whether the item is in the knapsack
            // or
            // not - hence, we only add its weight if knapsack[i] == 1
            sum += (int) position.getValue(i) * ProblemConfiguration.instance.knapsackItems.getWeight(i);
        }
        return sum;
    }

    /**
     * Returns true if the weight of the knapsack is less than or equal to the
     * maximum capacity, else returns false
     */
    public static boolean isValid(Vector position) {
        return getWeight(position) <= ProblemConfiguration.instance.maximumCapacity;
    }

    /** Gets the value of the knapsack */
    public static int getValue(Vector position) {
        int sum = 0;
        for (int i = 0; i < position.size(); i++) {
            // knapsack[i] is either 1 or 0 depending on whether the item is in the knapsack
            // or
            // not - hence, we only add its value if knapsack[i] == 1
            sum += (int) position.getValue(i) * ProblemConfiguration.instance.knapsackItems.getValue(i);
        }
        return sum;
    }

    /**
     * Generates a random position vector (a knapsack) by randomly adding items to
     * the knapsack, and stopping just before the knapsack becomes overweight
     */
    public void setRandomPosition() {
        int total_weight = 0;
        // Keep adding items to the knapsack while the current weight is less than the
        // maximum capacity
        while (total_weight < ProblemConfiguration.instance.maximumCapacity) {
            int random_item = ProblemConfiguration.instance.randomGenerator.nextInt(150);
            if (position.getValue(random_item) == 1.0) {
                continue; // i.e. if the item is already in the knapsack, pick a different one.
            }
            int weight = ProblemConfiguration.instance.knapsackItems.getWeight(random_item);
            if (total_weight + weight > ProblemConfiguration.instance.maximumCapacity) {
                // if adding this item will make the knapsack overweight, then end the loop and
                // use the current knapsack configuration as the random chromosome
                break;
            }
            position.setValue(random_item, 1);
            total_weight += weight;
        }
    }
}