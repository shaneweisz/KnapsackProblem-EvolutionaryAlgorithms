import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Stores all the possible knapsack items with weights and values in a single
 * object in memory.
 */
public class KnapsackInstance {
    private final static String DATA_PATH = "/Users/shaneweisz/Documents/UCT/Honours/Evolutionary Computing/Assignment/data/knapsack_instance.csv";
    private final static int[] weights = new int[150];
    private final static int[] values = new int[150];

    public KnapsackInstance() {
        try {
            Scanner scFile = new Scanner(new File(DATA_PATH));
            scFile.nextLine(); // skip the header line
            for (int i = 0; i < 150; i++) // loop through the 150 lines in the csv file
            {
                String line = scFile.nextLine();
                Scanner scLine = new Scanner(line);
                scLine.useDelimiter(";");
                scLine.nextInt(); // skip the index
                weights[i] = scLine.nextInt();
                values[i] = scLine.nextInt();
                scLine.close();
            }
            scFile.close();
        } catch (FileNotFoundException fe) {
            System.out.println(fe);
        }
    }

    /** Returns the weight of the knapsack item at index i */
    public int getWeight(int i) {
        return weights[i];
    }

    /** Returns the value of the knapsack item at index i */
    public int getValue(int i) {
        return values[i];
    }
}