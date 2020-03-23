/**
 * Main driver for the application. Either run the solver on a specific
 * configuration or to find the best configuration for a given algorithm
 */
public class Application {
    public static void main(String[] args) {
        String configuration = null, method = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-configuration")) {
                // Extract the desired configuration to run
                // Options include: ga_default_01, ..., ga_best, etc.
                configuration = args[++i];
            } else if (args[i].equals("-search_best_configuration")) {
                // Get the method to find the best configuration for
                // Options: ga, sa and pso
                method = args[++i];// HERE
            }
        }
        if (configuration != null) {
            //
        }
    }
}