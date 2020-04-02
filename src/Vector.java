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

    /** Sets the value at a specified index of the vector to a new value */
    public void setValue(int index, double val) {
        vector[index] = val;
    }

    /** Gets the value at a specified index of the vector */
    public double getValue(int index) {
        return vector[index];
    }

    /** Gets the number of dimensions of the vector */
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

    /** Returns a cloned copy of the vector */
    public Vector clone() {
        double[] newVector = new double[vector.length];
        System.arraycopy(vector, 0, newVector, 0, vector.length);
        return new Vector(newVector);
    }

    /**
     * Formats the vector as a string e.g. "[0101110...1110]" for display purposes
     */
    public String toString() {
        String s = "[";
        int limit = 26;
        for (int i = 0; i < limit; i++) {
            // If the elements are whole numbers, display them as 0 and 1
            // rather than 0.0 and 1.0
            if (vector[i] == (int) vector[i]) {
                s += (int) vector[i];

            } else {
                s += vector[i];
            }
        }
        s += "...]";
        return s;
    }

}