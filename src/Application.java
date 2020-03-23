import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main driver for the application. Either run the solver on a specific
 * configuration or to find the best configuration for a given algorithm
 */
public class Application {
    private final static String GA_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_ga_default/";
    private final static String SA_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_sa_default/";
    private final static String PSO_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_pso_default/";

    public static void main(String[] args) {
        if (args[0].equals("-configuration")) {
            // Extract the desired configuration to run
            // Options include: ga_default_01, ..., ga_best, etc.
            String configuration = args[1];
            String algo = configuration.substring(0, configuration.indexOf("_"));
            try {
                if (algo.equals("ga")) { // Then it is a Genetic Algorithm configuration
                    String fileName = GA_PATH + configuration + ".json";
                    Scanner scFile = new Scanner(new File(fileName));
                    scFile.useDelimiter(",");

                    String selectionMethod = getJSONValueFromLine(scFile.next());
                    scFile.next(); // Skip the configuration since we already know it
                    double mutationRatio = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    double crossoverRatio = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    String crossoverMethod = getJSONValueFromLine(scFile.next());
                    String mutationMethod = getJSONValueFromLine(scFile.next());
                    GeneticAlgorithm.run(configuration, selectionMethod, crossoverMethod, crossoverRatio,
                            mutationMethod, mutationRatio);

                    scFile.close();
                } else if (algo.equals("sa")) {
                    String fileName = SA_PATH + configuration + ".json";
                    Scanner scFile = new Scanner(new File(fileName));
                    scFile.useDelimiter(",");

                    int initialTemperature = Integer.parseInt(getJSONValueFromLine(scFile.next()));
                    scFile.next(); // Skip the configuration since we already know it
                    double coolingRate = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    System.out.println(initialTemperature);
                    System.out.println(coolingRate);
                    // SimulatedAnnealing.run(configuration, initialTemperature, coolingRate);
                } else if (algo.equals("pso")) {
                    String fileName = PSO_PATH + configuration + ".json";
                    Scanner scFile = new Scanner(new File(fileName));
                    scFile.useDelimiter(",");

                    int minVelocity = Integer.parseInt(getJSONValueFromLine(scFile.next()));
                    int maxVelocity = Integer.parseInt(getJSONValueFromLine(scFile.next()));
                    double inertia = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    scFile.next(); // Skip the configuration since we already know it
                    int numParticles = Integer.parseInt(getJSONValueFromLine(scFile.next()));
                    double c1 = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    double c2 = Double.parseDouble(getJSONValueFromLine(scFile.next()));
                    System.out.println(minVelocity);
                    System.out.println(maxVelocity);
                    System.out.println(inertia);
                    System.out.println(numParticles);
                    System.out.println(c1);
                    System.out.println(c2);
                    // ParticleSwarmOptimization.run(configuration, numParticles, minVelocity,
                    // maxVelocity, c1, c2, inertia);
                } else {
                    // The algorithm must be one of the above 3 options so will not get here
                }

            } catch (FileNotFoundException e) {
                System.out.println(e);
            }

        } else if (args[0].equals("-search_best_configuration")) {
            // Get the method to find the best configuration for
            // Options: ga, sa and pso
            String method = args[1];
            System.out.println(method);
        }

    }

    /** Takes in a line in the format "[key]":"[value]", and returns [value] */
    private static String getJSONValueFromLine(String line) {
        Scanner scLine = new Scanner(line);
        scLine.useDelimiter(":");
        scLine.next(); // Skip the key
        String value = scLine.next();
        scLine.close();
        return value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\""));
    }
}