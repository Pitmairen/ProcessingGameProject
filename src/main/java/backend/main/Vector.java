package backend.main;

/**
 * Describes a 3D vector. Can also be used for 2D vectors by setting the
 * z-component to 0. This class also calculates vectors, angles, and other
 * values to be used in the simulation.
 *
 * NB: Positive Y-axis is downwards, and radial angles have their positive
 * direction clockwise.
 *
 * @author Kristian Honningsvag.
 */
public class Vector {

    private double x;
    private double y;
    private double z;

    /**
     * Constructor. Creates a zero-vector.
     */
    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Constructor. Creates a vector with the provided values. When creating a
     * 2D vector, set Z-component to 0.
     *
     * @param x The vectors X-component.
     * @param y The vectors Y-component.
     * @param z The vectors Z-component.
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the provided X, Y and Z components to this vector, and returns the
     * resulting vector.
     *
     * @param x The X component to be added.
     * @param y The Y component to be added.
     * @param z The Z component to be added.
     * @return The resulting vector.
     */
    public Vector add(double x, double y, double z) {
        this.x = this.x + x;
        this.y = this.y + y;
        this.z = this.z + z;
        return this;
    }

    /**
     * Adds a vector to this vector, and returns the resulting vector.
     *
     * @param v2 The vector to be added.
     * @return The resulting vector.
     */
    public Vector add(Vector v2) {
        this.x = this.x + v2.getX();
        this.y = this.y + v2.getY();
        this.z = this.z + v2.getZ();
        return this;
    }

    /**
     * Adds to vectors together and returns the resulting vector.
     *
     * @param v1 First vector.
     * @param v2 Second vector.
     * @return The resulting vector.
     */
    static public Vector add(Vector v1, Vector v2) {
        Vector v3 = new Vector();
        v3.setX(v1.getX() + v2.getX());
        v3.setY(v1.getY() + v2.getY());
        v3.setZ(v1.getZ() + v2.getZ());
        return v3;
    }

    /**
     * Subtracts the provided X, Y and Z components from this vector and returns
     * the resulting vector.
     *
     * @param x The X component to be subtracted.
     * @param y The Y component to be subtracted.
     * @param z The Z component to be subtracted.
     * @return The resulting vector.
     */
    public Vector sub(double x, double y, double z) {
        this.x = this.x - x;
        this.y = this.y - y;
        this.z = this.z - z;
        return this;
    }

    /**
     * Subtracts a vector from this vector and returns the resulting vector.
     *
     * @param v2 The vector to be subtracted.
     * @return The resulting vector.
     */
    public Vector sub(Vector v2) {
        this.x = this.x - v2.getX();
        this.y = this.y - v2.getY();
        this.z = this.z - v2.getZ();
        return this;
    }

    /**
     * Subtracts a vector from another vector and returns the resulting vector.
     *
     * v3 = v1 - v2
     *
     * @param v1 First vector.
     * @param v2 Second vector.
     * @return The resulting vector.
     */
    static public Vector sub(Vector v1, Vector v2) {
        Vector v3 = new Vector();
        v3.setX(v1.getX() - v2.getX());
        v3.setY(v1.getY() - v2.getY());
        v3.setZ(v1.getZ() - v2.getZ());
        return v3;
    }

    /**
     * Multiplies this vector with a scalar and returns the resulting vector.
     *
     * @param n The scalar to multiply with the vector.
     * @return The resulting vector.
     */
    public Vector mult(double n) {
        this.x = this.x * n;
        this.y = this.y * n;
        this.z = this.z * n;
        return this;
    }

    /**
     * Divides this vector by a scalar and returns the resulting vector
     *
     * @param n The scalar to divide the vector by.
     * @return The resulting vector.
     */
    public Vector div(double n) {
        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
        return this;
    }

    /**
     * Calculates and returns the magnitude of the vector.
     *
     * mag = sqrt(x^2 + y^2 + z^2)
     *
     * @return The magnitude of the vector.
     */
    public double mag() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * Returns a copy of this vector.
     *
     * @return A copy of this vector.
     */
    public Vector copy() {
        return new Vector(this.x, this.y, this.z);
    }

    /**
     * Calculates and returns the euclidean distance between this point and any
     * other point in vector form.
     *
     * @param v2 The second point in vector form.
     * @return The distance between the two points.
     */
    public double dist(Vector v2) {
        double dx = this.x - v2.getX();
        double dy = this.y - v2.getY();
        double dz = this.z - v2.getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    }

    /**
     * Calculates and returns the euclidean distance between any two points in
     * vector form.
     *
     * @param v1 The first point in vector form.
     * @param v2 The second point in vector form.
     * @return The distance between the two points.
     */
    static public double dist(Vector v1, Vector v2) {
        double dx = v1.getX() - v2.getX();
        double dy = v1.getY() - v2.getY();
        double dz = v1.getZ() - v2.getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    }

    /**
     * Calculates and returns the dot product (scalar product) of this vector
     * and any other vector.
     *
     * @param v2 The second vector.
     * @return The dot product.
     */
    public double dot(Vector v2) {
        return this.x * v2.getX() + this.y * v2.getY() + this.z * v2.getZ();
    }

    /**
     * Calculates and returns the dot product (scalar product) of any two
     * vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The dot product.
     */
    static public double dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    /**
     * Calculates and returns the cross product (vector product) of this vector
     * and any other vector.
     *
     * @param v2 The second vector.
     * @return The cross product.
     */
    public Vector cross(Vector v2) {
        double crossX = this.getY() * v2.getZ() - this.getZ() * v2.getY();
        double crossY = this.getZ() * v2.getX() - this.getX() * v2.getZ();
        double crossZ = this.getX() * v2.getY() - this.getY() * v2.getX();
        return new Vector(crossX, crossY, crossZ);
    }

    /**
     * Calculates and returns the cross product (vector product) of any two
     * vectors.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The cross product.
     */
    static public Vector cross(Vector v1, Vector v2) {
        double crossX = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        double crossY = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
        double crossZ = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        return new Vector(crossX, crossY, crossZ);
    }

    /**
     * Transform this vector to an unit vector by making it length 1.
     *
     * @return The resulting vector.
     */
    public Vector normalize() {
        double m = this.mag();
        if (m != 0 && m != 1) {
            this.div(m);
        }
        return this;
    }

    /**
     * Set the magnitude of this vector to the provided value. The vectors
     * direction is left unchanged.
     *
     * @param magnitude The new magnitude of the vector.
     * @return The resulting vector.
     */
    public Vector setMag(double magnitude) {
        this.normalize();
        this.mult(magnitude);
        return this;
    }

    /**
     * Calculates and returns the angle of rotation in the 2D plane for this
     * vector.
     *
     * @return Angle of rotation in radians.
     */
    public double getAngle2D() {

        double theta = Math.atan(y / x);
        double angle = 0;

        // If vector lies in quadrant 1
        if (x > 0 && y > 0) {
            angle = theta;
        }
        // If vector lies in quadrant 2
        if (x < 0 && y > 0) {
            angle = theta + Math.PI;
        }
        // If vector lies in quadrant 3
        if (x < 0 && y < 0) {
            angle = theta + Math.PI;
        }
        // If vector lies in quadrant 4
        if (x > 0 && y < 0) {
            angle = theta + 2 * Math.PI;
        }

        // If vector is straight to the right.
        if (x > 0 && y == 0) {
            angle = 0;
        }
        // If vector is straight down.
        if (x == 0 && y > 0) {
            angle = Math.PI / 2;
        }
        // If vector is straight to the left.
        if (x < 0 && y == 0) {
            angle = Math.PI;
        }
        // If vector is straight up.
        if (x == 0 && y < 0) {
            angle = (2 * Math.PI) - (Math.PI / 2);
        }
        // If vector is zero.
        if (x == 0 && y == 0) {
            angle = 0;
        }
        return angle;
    }

    /**
     * Calculates and returns the angle (in radians) between this and any other
     * vector.
     *
     * @param v2 The second vector.
     * @return The angle between the vectors in radians.
     */
    public double angleBetween(Vector v2) {

        // Return zero if one of the vectors are zero.
        if ((this.x == 0 && this.y == 0 && this.z == 0) || (v2.getX() == 0 && v2.getY() == 0 && v2.getZ() == 0)) {
            return 0.0f;
        }

        double dot = this.dot(v2);
        double v1mag = this.mag();
        double v2mag = v2.mag();
        double amt = dot / (v1mag * v2mag);  // This should be a number between -1 and 1.

        if (amt <= -1) {
            return Math.PI;
        } else if (amt >= 1) {
            return 0;
        }
        return Math.acos(amt);
    }

    /**
     * Rotates this vector in the XY plane and returns the resulting vector.
     * Initial magnitude is left unchanged.
     *
     * @param angle The angle of rotation.
     * @return The resulting vector.
     */
    public Vector rotate(double angle) {
        double oldX = this.x;
        this.x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
        this.y = oldX * Math.sin(angle) + y * Math.cos(angle);
        return this;
    }

    /**
     * Calculates linear interpolation between two points in vector form and
     * returns the resulting point in vector form.
     *
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @param pos The amount of interpolation. Value from 0 to 1. 0.2 is closer
     * to first vector, 0.5 is in the middle, and 0.8 is closer to the second
     * vector.
     * @return The point of interpolation in vector form.
     */
    static public Vector linInterp(Vector v1, Vector v2, double pos) {
        Vector v3 = new Vector();
        v3.setX(v1.getX() + (v2.getX() - v1.getX()) * pos);
        v3.setY(v1.getY() + (v2.getY() - v1.getY()) * pos);
        v3.setZ(v1.getZ() + (v2.getZ() - v1.getZ()) * pos);
        return v3;
    }

    /**
     * Returns the vectors X-component.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the vectors Y-component.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the vectors Z-component.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the vectors X-component.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the vectors Y-component.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the vectors Z-component.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Sets the vectors X, Y and Z components to the provided values and returns
     * the resulting vector. When creating a 2D vector, set the Z-component to
     * 0.
     *
     * @param x The vectors new X component.
     * @param y The vectors new Y component.
     * @param z The vectors new Z component.
     * @return The resulting vector.
     */
    public Vector set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Sets the vectors X, Y and Z components to the same values as the provided
     * vector and returns the resulting vector.
     *
     * @param v2 The vector which values are to be copied.
     * @return The resulting vector.
     */
    public Vector set(Vector v2) {
        this.x = v2.getX();
        this.y = v2.getY();
        this.z = v2.getZ();
        return this;
    }

}
