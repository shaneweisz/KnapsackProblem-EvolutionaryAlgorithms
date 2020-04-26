/**
 * Class used for running a particle swarm optimization solution to the knapsack
 * problem, given the specified parameters such as number of particles, maximum
 * velocity etc.
 */
public class ParticleSwarmOptimization {

    // Parameters
    private String configuration;
    private int numParticles;
    private int minVelocity;
    private int maxVelocity;
    private double c1;
    private double c2;
    private double inertia;

    // Best values
    private Vector bestPosition;
    private int globalBestEvaluationValue;

    /**
     * Parametrized constructor for a ParticleSwarmOptimization instance with
     * parameters specified in the JSON files
     * 
     * @param configuration e.g "pso_default_01"
     * @param numParticles  e.g. 100
     * @param minVelocity   e.g. 4
     * @param maxVelocity   e.g. 4
     * @param c1            e.g. 0.5
     * @param c2            e.g. 0.5
     * @param inertia       e.g. 1.00
     */
    public ParticleSwarmOptimization(String configuration, int numParticles, int minVelocity, int maxVelocity,
            double c1, double c2, double inertia) {
        this.configuration = configuration;
        this.numParticles = numParticles;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;
    }

    /**
     * Returns the highest value of the knapsack achieved using PSO with the given
     * parameters
     */
    public int run() {
        // To be used for statistics for report
        long startTime = System.currentTimeMillis();
        int maxIterations = ProblemConfiguration.instance.maximumNumberOfIterations;
        int[] bweights = new int[maxIterations];
        int[] bvalues = new int[maxIterations];
        String[] knapsacks = new String[maxIterations];

        // The initial swarm of particles
        Particle[] particles = initialize();

        bweights[0] = Particle.getWeight(bestPosition);
        bvalues[0] = globalBestEvaluationValue;
        knapsacks[0] = bestPosition.toString();

        // int previousGlobalBestEvaluationValue = globalBestEvaluationValue;
        // System.out.println("Running...");
        // System.out.println("Global best evaluation (iteration " + 0 + "):\t" +
        // globalBestEvaluationValue);

        for (int i = 0; i < ProblemConfiguration.instance.maximumNumberOfIterations; i++) {
            // if (globalBestEvaluationValue > previousGlobalBestEvaluationValue) {
            // System.out.println("Global best evaluation (iteration " + (i + 1) + "):\t" +
            // globalBestEvaluationValue);
            // previousGlobalBestEvaluationValue = globalBestEvaluationValue;
            // }

            for (Particle particle : particles) {
                particle.updateIndividualBestValue();
                updateGlobalBest(particle);
            }
            // System.out.println("Iteration " + i + ":");
            for (Particle particle : particles) {
                updateVelocity(particle);
                particle.updatePosition();
                // System.out.println("Pos = " + particle.getPosition());
                // System.out.println("Vel = " + particle.getVelocity());
            }

            bweights[i] = Particle.getWeight(bestPosition);
            bvalues[i] = globalBestEvaluationValue;
            knapsacks[i] = bestPosition.toString();
        }

        // System.out.println();
        // System.out.println("Result:");
        // System.out.println("Global best evaulation " + globalBestEvaluationValue);
        // System.out.println("Best knapsack " + bestPosition);

        // To be used for statistics for report
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        String params = String.format(
                "PSO | #%d | %d particles | minV = %d | maxV = %d | c1 = %.1f | c2 = %.1f | inertia = %.2f",
                maxIterations, numParticles, minVelocity, maxVelocity, c1, c2, inertia);

        String report = ReportGenerator.generateReport(configuration, params, bweights, bvalues, knapsacks, runtime,
                maxIterations);
        ReportGenerator.writeToFile(report, configuration);

        System.out.println(configuration + ": " + globalBestEvaluationValue);

        return globalBestEvaluationValue;
    }

    /**
     * Initialize the swarm of particles by generating particles at random positions
     */
    private Particle[] initialize() {
        Particle[] particles = new Particle[numParticles];

        for (int i = 0; i < numParticles; i++) {
            Particle particle = new Particle();
            particles[i] = particle;
            updateGlobalBest(particle);
        }

        return particles;
    }

    /**
     * Updates the global best position if the particle's current position is better
     * than the current global best
     */
    private void updateGlobalBest(Particle particle) {
        if (particle.getIndividualBestValue() > globalBestEvaluationValue) {
            bestPosition = particle.getBestPosition();
            globalBestEvaluationValue = particle.getIndividualBestValue();
        }
    }

    /**
     * Updates a particle's velocity based on its old velocity, its current
     * position, the global best position and its individual best position
     */
    private void updateVelocity(Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector pBest = particle.getBestPosition();
        Vector gBest = bestPosition.clone();
        Vector position = particle.getPosition();

        Vector newVelocity = oldVelocity.clone();
        newVelocity.multiply(inertia);

        pBest.subtract(position);
        pBest.multiply(c1);
        // Multiply each dimension of the vector by a random double
        for (int i = 0; i < pBest.size(); i++) {
            double randomValue01 = ProblemConfiguration.instance.randomGenerator.nextDouble();
            pBest.setValue(i, pBest.getValue(i) * randomValue01);
        }
        newVelocity.add(pBest);

        gBest.subtract(position);
        gBest.multiply(c2);
        // Multiply each dimension of the vector by a random double
        for (int i = 0; i < gBest.size(); i++) {
            double randomValue02 = ProblemConfiguration.instance.randomGenerator.nextDouble();
            gBest.setValue(i, gBest.getValue(i) * randomValue02);
        }
        newVelocity.add(gBest);

        // Check the velocities are within the max and min bounds
        // If not, change them to be the bounds
        for (int i = 0; i < newVelocity.size(); i++) {
            if (newVelocity.getValue(i) > maxVelocity) {
                newVelocity.setValue(i, maxVelocity);
            }
            if (newVelocity.getValue(i) < -minVelocity) {
                newVelocity.setValue(i, -minVelocity);
            }
        }

        particle.setVelocity(newVelocity);
    }

    // For testing purposes
    public static void main(String[] args) {
        String configuration = "test";
        int numParticles = 100;
        int minVelocity = 4;
        int maxVelocity = 4;
        double c1 = 0.5;
        double c2 = 0.5;
        double inertia = 1.0;
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(configuration, numParticles, minVelocity,
                maxVelocity, c1, c2, inertia);
        pso.run();
    }

}