import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.nio.file.Files;

/**
 * Main driver for the application. Either run the solver on a specific
 * configuration or to find the best configuration for a given algorithm
 */
public class Application {
    private final static String GA_PATH = "configurations/json_configuration_ga_default/";
    private final static String SA_PATH = "configurations/json_configuration_sa_default/";
    private final static String PSO_PATH = "configurations/json_configuration_pso_default/";
    private final static int NUM_GA_CONFIGS = 28;
    private final static int NUM_SA_CONFIGS = 25;
    private final static int NUM_PSO_CONFIGS = 25;

    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("-configuration")) {
            // Extract the desired configuration to run
            // Options include: ga_default_01, ..., ga_best, etc.
            String configuration = args[1].substring(0, args[1].indexOf(".json"));
            runAlgorithm(configuration);

        } else if (args[0].equals("-search_best_configuration")) {
            // Get the method to find the best configuration for
            // Options: ga, sa and pso
            String method = args[1];
            DecimalFormat decimalFormat = new DecimalFormat("00");
            int maxValue = 0;
            String bestConfiguration = "";
            if (method.equalsIgnoreCase("ga")) {
                for (int i = 1; i <= NUM_GA_CONFIGS; i++) {
                    String configuration = "ga_default_" + decimalFormat.format(i);
                    int value = runGA(configuration);
                    if (value > maxValue) {
                        maxValue = value;
                        bestConfiguration = configuration;
                    }
                }
                System.out.println("Best configuration is " + bestConfiguration);
                String srcFile = GA_PATH + bestConfiguration + ".json";
                String destFile = GA_PATH + "ga_best.json";
                // Delete the best file if it already exists, because we will replace it
                new File(destFile).delete();
                try {
                    Files.copy(new File(srcFile).toPath(), new File(destFile).toPath());
                } catch (IOException e) {
                    System.out.println(e);
                }
            } else if (method.equalsIgnoreCase("sa")) {
                for (int i = 1; i <= NUM_SA_CONFIGS; i++) {
                    String configuration = "sa_default_" + decimalFormat.format(i);
                    int value = runSA(configuration);
                    if (value > maxValue) {
                        maxValue = value;
                        bestConfiguration = configuration;
                    }
                }
                System.out.println("Best configuration is " + bestConfiguration);
                String srcFile = SA_PATH + bestConfiguration + ".json";
                String destFile = SA_PATH + "sa_best.json";
                // Delete the best file if it already exists, because we will replace it
                new File(destFile).delete();
                try {
                    Files.copy(new File(srcFile).toPath(), new File(destFile).toPath());
                } catch (IOException e) {
                    System.out.println(e);
                }
            } else if (method.equalsIgnoreCase("pso")) {
                for (int i = 1; i <= NUM_PSO_CONFIGS; i++) {
                    String configuration = "pso_default_" + decimalFormat.format(i);
                    int value = runPSO(configuration);
                    if (value > maxValue) {
                        maxValue = value;
                        bestConfiguration = configuration;
                    }
                }
                System.out.println("Best configuration is " + bestConfiguration);
                String srcFile = PSO_PATH + bestConfiguration + ".json";
                String destFile = PSO_PATH + "pso_best.json";
                // Delete the best file if it already exists, because we will replace it
                new File(destFile).delete();
                try {
                    Files.copy(new File(srcFile).toPath(), new File(destFile).toPath());
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
        }
    }

    private static int runAlgorithm(String configuration) {
        String algo = configuration.substring(0, configuration.indexOf("_"));
        if (algo.equalsIgnoreCase("ga")) {
            return runGA(configuration);
        } else if (algo.equalsIgnoreCase("sa")) {
            return runSA(configuration);
        } else if (algo.equalsIgnoreCase("pso")) {
            return runPSO(configuration);
        }
        return -1;
    }

    private static int runGA(String configuration) {
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

            scFile.close();

            GeneticAlgorithm ga = new GeneticAlgorithm(configuration, selectionMethod, crossoverMethod, crossoverRatio,
                    mutationMethod, mutationRatio);
            return ga.run();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return -1;
        }
    }

    private static int runSA(String configuration) {
        try {
            String fileName = SA_PATH + configuration + ".json";
            Scanner scFile = new Scanner(new File(fileName));
            scFile.useDelimiter(",");

            int initialTemperature = Integer.parseInt(getJSONValueFromLine(scFile.next()));
            scFile.next(); // Skip the configuration since we already know it
            double coolingRate = Double.parseDouble(getJSONValueFromLine(scFile.next()));

            scFile.close();

            SimulatedAnnealing sa = new SimulatedAnnealing(configuration, initialTemperature, coolingRate);
            return sa.run();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return -1;
        }
    }

    private static int runPSO(String configuration) {
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

            ParticleSwarmOptimization pso = new ParticleSwarmOptimization(configuration, numParticles, minVelocity,
                    maxVelocity, c1, c2, inertia);
            return pso.run();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return -1;
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