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
    private double globalBestEvaluationValue;

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
        return -1;
    }

    private Particle[] initialize() {
        Particle[] particles = new Particle[numParticles];

        for (int i = 0; i < numParticles; i++) {
            Particle particle = new Particle();
            particles[i] = particle;
            updateGlobalBest(particle);
        }

        return particles;
    }

    private void updateGlobalBest(Particle particle) {
        if (particle.getIndividualBestValue() > globalBestEvaluationValue) {
            bestPosition = particle.getBestPosition();
            globalBestEvaluationValue = particle.getIndividualBestValue();
        }
    }

    private void updateVelocity(Particle particle) {
        Vector oldVelocity = particle.getVelocity();
        Vector pBest = particle.getBestPosition();
        Vector gBest = bestPosition.clone();
        Vector position = particle.getPosition();

        double randomValue01 = ProblemConfiguration.instance.randomGenerator.nextDouble();
        double randomValue02 = ProblemConfiguration.instance.randomGenerator.nextDouble();

        Vector newVelocity = oldVelocity.clone();
        newVelocity.multiply(inertia);

        pBest.subtract(position);
        pBest.multiply(c1);
        pBest.multiply(randomValue01);
        newVelocity.add(pBest);

        gBest.subtract(position);
        gBest.multiply(c2);
        gBest.multiply(randomValue02);
        newVelocity.add(gBest);

        particle.setVelocity(newVelocity);
    }

}