/**
 * Class that encodes a single particle that participates in the swarm in PSO
 */
public class Particle {
    public Vector position;
    public Vector velocity;
    public Vector bestPosition;
    public double individualBestValue;

    public Particle() {
        position = new Vector();
        velocity = new Vector();
        bestPosition = position;
        setRandomPosition();
        bestPosition = position.clone();
        individualBestValue = evaluateCurrentPosition();
    }

    public int evaluateCurrentPosition() {
        if (!isValid(position)) {
            return 0;
        }
        return getValue(position);
    }

    public void updateIndividualBestValue() {
        int value = evaluateCurrentPosition();
        if (value > individualBestValue) {
            bestPosition = position.clone();
            individualBestValue = value;
        }
    }

    public Vector getPosition() {
        return position.clone();
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity.clone();
    }

    public Vector getBestPosition() {
        return bestPosition.clone();
    }

    public double getIndividualBestValue() {
        return individualBestValue;
    }

    public void updatePosition() {

        this.position.add(velocity);
    }

    /** Gets the weight of the knapsack encoded by a position vector */
    private int getWeight(Vector position) {
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
    private boolean isValid(Vector position) {
        return getWeight(position) <= ProblemConfiguration.instance.maximumCapacity;
    }

    /** Gets the value of the knapsack */
    private int getValue(Vector position) {
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
     * Generates a random knapsack by randomly adding items to the knapsack, and
     * stopping just before the knapsack becomes overweight
     */
    private void setRandomPosition() {
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