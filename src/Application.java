import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * Main driver for the application. Either run the solver on a specific
 * configuration or to find the best configuration for a given algorithm
 */
public class Application {
    private final static String GA_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_ga_default/";
    private final static String SA_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_sa_default/";
    private final static String PSO_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/configurations/json_configuration_pso_default/";
    private final static int NUM_GA_CONFIGS = 28;
    private final static int NUM_SA_CONFIGS = 25;
    private final static int NUM_PSO_CONFIGS = 25;

    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("-configuration")) {
            // Extract the desired configuration to run
            // Options include: ga_default_01, ..., ga_best, etc.
            String configuration = args[1];
            runAlgorithm(configuration);

        } else if (args[0].equals("-search_best_configuration")) {
            // Get the method to find the best configuration for
            // Options: ga, sa and pso
            String method = args[1];
            DecimalFormat decimalFormat = new DecimalFormat("00");
            if (method.equalsIgnoreCase("ga")) {
                for (int i = 1; i <= NUM_GA_CONFIGS; i++) {
                    String configuration = "ga_default_" + decimalFormat.format(i);
                    runGA(configuration);
                }
            } else if (method.equalsIgnoreCase("sa")) {
                for (int i = 1; i <= NUM_SA_CONFIGS; i++) {
                    String configuration = "sa_default_" + decimalFormat.format(i);
                    runSA(configuration);
                }
            } else if (method.equalsIgnoreCase("pso")) {
                for (int i = 1; i <= NUM_PSO_CONFIGS; i++) {
                    String configuration = "pso_default_" + decimalFormat.format(i);
                    runPSO(configuration);
                }
            }
        }

    }

    private static void runAlgorithm(String configuration) {
        String algo = configuration.substring(0, configuration.indexOf("_"));
        if (algo.equalsIgnoreCase("ga")) {
            runGA(configuration);
        } else if (algo.equalsIgnoreCase("sa")) {
            runSA(configuration);
        } else if (algo.equalsIgnoreCase("pso")) {
            runPSO(configuration);
        }
    }

    private static void runGA(String configuration) {
        try {
            String fileName = GA_PATH + configuration + ".json";
            Scanner scFile = new Scanner(new File(fileName));
            scFile.useDelimiter(",");

            String selectionMethod = getJSONValueFromLine(scFile.next());
            scFile.next(); // Skip the configuration since we already know it
            double mutationRatio = Double.parseDouble(getJSONValueFromLine(scFile.next()));
            double crossoverRatio = Double.parseDouble(getJSONValueFromLine(scFile.next()));
            String crossoverMethod = getJSONValueFromLine(scFile.next());
            String mutationMethod = getJSONValueFromLine(scFile.next());
            GeneticAlgorithm.run(configuration, selectionMethod, crossoverMethod, crossoverRatio, mutationMethod,
                    mutationRatio);

            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    private static void runSA(String configuration) {
        try {
            String fileName = SA_PATH + configuration + ".json";
            Scanner scFile = new Scanner(new File(fileName));
            scFile.useDelimiter(",");

            int initialTemperature = Integer.parseInt(getJSONValueFromLine(scFile.next()));
            scFile.next(); // Skip the configuration since we already know it
            double coolingRate = Double.parseDouble(getJSONValueFromLine(scFile.next()));
            System.out.println(initialTemperature);
            System.out.println(coolingRate);
            // SimulatedAnnealing.run(configuration, initialTemperature, coolingRate);

            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    private static void runPSO(String configuration) {
        try {
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
        } catch (FileNotFoundException e) {
            System.out.println(e);
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