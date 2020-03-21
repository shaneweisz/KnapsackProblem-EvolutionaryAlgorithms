public class Application {
    public static void main(String[] args) {
        Population population = new Population(Configuration.instance.populationSize,
                Configuration.instance.crossoverRatio, Configuration.instance.elitismRatio,
                Configuration.instance.mutationRatio, Configuration.instance.selectionMethod);

    }
}