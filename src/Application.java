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
        return value.substring(1, value.lastIndexOf("\""));
    }
}