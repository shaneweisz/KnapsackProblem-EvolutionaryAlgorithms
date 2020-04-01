/**
 * Class that encodes a vector with n dimensions. By default, the vector will
 * have 150 dimensions as per this knapsack problem.
 */
public class Vector {
    private double[] vector;

    public Vector(double[] vector) {
        this.vector = vector;
    }

    public double[] multiply(int scalar) {
        double[] newVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            newVector[i] = vector[i] * scalar;
        }
        return newVector;
    }

    public double[] add(double[] vector2) {
        double[] newVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            newVector[i] = vector[i] + vector2[i];
        }
        return newVector;
    }
}