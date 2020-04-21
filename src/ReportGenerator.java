import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * Class used for generating a report regarding important statistics regarding
 * the evaluation of an algorithm - namely solution iterations, runtime,
 * convergences and plateau statistics
 */
public class ReportGenerator {
    private final static int BEST_KNOWN_OPTIMUM = 997;
    private final static String REPORTS_PATH = "reports/";

    /**
     * Creates a report `report_[algorithm]_yyyymmdd.txt` based on inputted
     * statistics
     * 
     * @param configuration The name of the configuration e.g. "ga_default_01"
     * @param params        A string outlining the algorithm parameters e.g. "GA |
     *                      #10000 | RWS | 2PX (0.7) | EXM (0.003)"
     * @param bweights      An int [] with successive iterations' knapsack weights
     * @param bvalues       An int [] with successive iterations' knapsack values
     * @param knapsacks     A String [] with with successive iterations' knapsacks
     * @param runtime       The time in ms the algorithm took to run
     */
    public static String generateReport(String configuration, String params, int[] bweights, int[] bvalues,
            String[] knapsacks, long runtime, int numIterations) {
        String report = "";

        DateTimeFormatter dtfFull = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currDateTime = LocalDateTime.now();

        report += "Evaluation | " + dtfFull.format(currDateTime) + "\n";

        report += "Configuration:\t" + configuration + ".json\n";

        report += "\t\t" + params + "\n";

        report += addEqualsSigns();

        report += "#\tbweight\tbvalue\tsquality\tknapsack\n";

        report += addDashes();

        report += 1 + "\t\t" + bweights[0] + "\t" + bvalues[0] + " \t" + getSolutionQuality(bvalues[0]) + "\t\t"
                + knapsacks[0] + "\n";

        report += "...\n";

        report += numIterations + "\t" + bweights[numIterations - 1] + "\t" + bvalues[numIterations - 1] + " \t"
                + getSolutionQuality(bvalues[numIterations - 1]) + "\t\t" + knapsacks[numIterations - 1] + "\n";

        report += addDashes();

        report += "[Statistics]\n";

        report += "Runtime\t\t" + runtime + " ms\n\n";

        report += "Convergence\t#\t\tbweight\tbvalue\tsquality\n";

        // Print convergence statistics at 25%, 50%, 75% and 100% of iterations
        int intervalSize = numIterations / 4;
        for (int i = 1; i <= 4; i++) {
            int index = intervalSize * i - 1;
            report += "\t\t\t" + (index + 1) + " \t" + bweights[index] + "\t\t" + bvalues[index] + " \t"
                    + getSolutionQuality(bvalues[index]) + "\n";
        }

        report += "\n";

        report += addPlateauRow(bvalues);

        report += "\n";

        report += addEqualsSigns();

        return report;
    }

    /** Returns a row of equal signs */
    private static String addEqualsSigns() {
        // Add a row of ='s
        String row = "";
        for (int i = 0; i < 70; i++) {
            row += "=";
        }
        row += "\n";
        return row;
    }

    /** Returns a row of dashes */
    private static String addDashes() {
        // Add a row of -'s
        String row = "";
        for (int i = 0; i < 70; i++) {
            row += "-";
        }
        row += "\n";
        return row;
    }

    /**
     * Given an int solution value, returns its solution quality as a percentage
     * relative to the given previous best-known optimum of 997. e.g. 997 -> 100%,
     * 498 -> 49.99%, etc.
     */
    private static String getSolutionQuality(int value) {
        DecimalFormat df = new DecimalFormat("###.##");
        double quality = (double) value / BEST_KNOWN_OPTIMUM;
        return df.format(quality * 100) + "%";
    }

    /**
     * Returns a line of the report corresponding to the longest sequence without
     * improvement. e.g. Pleateau | Longest sequence without improvement: 443-472
     */
    private static String addPlateauRow(int[] bvalues) {
        int longestPlateauStart = 0;
        int longestPlateauEnd = 0;
        int longestPlateauLength = 0;

        int currentPlateauStart = 0;
        int currentPlateauEnd = 0;

        int currentValue = bvalues[0];

        for (int i = 0; i < bvalues.length; i++) {
            if (bvalues[i] == currentValue) {
                // if the value has not increased, increment the current plateau length by one
                currentPlateauEnd++;
            } else {
                // if the value has increased, end the current plateau and check if longer than
                // current longest plateau
                currentPlateauEnd--;
                int currentPlateauLength = currentPlateauEnd - currentPlateauStart;
                if (currentPlateauLength > longestPlateauLength) {
                    longestPlateauStart = currentPlateauStart;
                    longestPlateauEnd = currentPlateauEnd;
                    longestPlateauLength = currentPlateauLength;
                }
                // Restart current plateau
                currentPlateauStart = i;
                currentPlateauEnd = i;
                currentValue = bvalues[i];
            }
        }
        // Lastly, check if the unfinished current plateau is longer than any previous
        // one
        int currentPlateauLength = currentPlateauEnd - currentPlateauStart;
        if (currentPlateauLength > longestPlateauLength) {
            longestPlateauStart = currentPlateauStart;
            longestPlateauEnd = currentPlateauEnd;
            longestPlateauLength = currentPlateauLength;
        }

        // Add one to the values to start indexing at 1, not 0
        return String.format("Pleateau | Longest sequence without improvement: %d-%d", longestPlateauStart + 1,
                longestPlateauEnd + 1);
    }

    /**
     * Writes given text to a specified text file based on which configuration was
     * run e.g. ga_default_01
     */
    public static void writeToFile(String text, String configuration) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currDateTime = LocalDateTime.now();
        String fileName = "report_" + configuration + "_" + dtf.format(currDateTime) + ".txt";
        File file = new File(REPORTS_PATH + fileName);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** For testing purposes */
    public static void main(String[] args) {
        String configuration = "ga_default_01.json";
        String params = "GA | #10000 | RWS | 2PX (0.7) | EXM (0.003)";
        int[] bweights = { 769, 800, 802, 650, 702 };
        int[] bvalues = { 1000, 997, 1120, 443, 1120 };
        String[] knapsacks = { "110", "000", "111", "101", "011" };
        long runtime = 1230;
        String report = generateReport(configuration, params, bweights, bvalues, knapsacks, runtime, bweights.length);
        System.out.println(report);
        writeToFile(report, configuration);
    }
}