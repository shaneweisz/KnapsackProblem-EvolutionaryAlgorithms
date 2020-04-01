/**
 * Class that encodes a vector with n dimensions. By default, the vector will
 * have 150 dimensions as per this knapsack problem.
 */
public class Vector {
    private double[] vector;

    public Vector() {
        vector = new double[150];
    }

    public Vector(double[] vector) {
        this.vector = vector;
    }

    public void setValue(int index, double val) {
        vector[index] = val;
    }

    public double getValue(int index) {
        return vector[index];
    }

    public int size() {
        return vector.length;
    }

    /** Multiplies the vector by a scalar multiple */
    public void multiply(double scalar) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalar;
        }
    }

    /** Adds two vectors together */
    public void add(Vector vector2) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] += vector2.vector[i];
        }
    }

    /** Subtracts a vector from another */
    public void subtract(Vector vector2) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] -= vector2.vector[i];
        }
    }

    /** Returns a clone of the vector */
    public Vector clone() {
        double[] newVector = new double[vector.length];
        System.arraycopy(vector, 0, newVector, 0, vector.length);
        return new Vector(newVector);
    }

}