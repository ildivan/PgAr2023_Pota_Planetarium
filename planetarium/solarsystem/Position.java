package planetarium.solarsystem;

/**
 * Represents a position in the universe.
 * The position is stored using x and y coordinates.
 */
public class Position {
    private double x;
    private double y;

    /**
     * The constructor for the Position class
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter method for the x coordinate.
     * @return The x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Setter method for the x coordinate
     * @param x The new x coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Getter method for the y coordinate.
     * @return The y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Setter method for the y coordinate
     * @param y The new x coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Shorthand for increasing the x coordinate by some value.
     * @param amount The amount to add to the current x coordinate.
     */
    public void increaseX(double amount) { x += amount; }

    /**
     * Shorthand for increasing the y coordinate by some value.
     * @param amount The amount to add to the current y coordinate.
     */
    public void increaseY(double amount) { y += amount; }

    /**
     * Shorthand for adding two positions.
     * Similar to vector sum.
     *
     * @param amount The position to be added.
     */
    public void increase(Position amount) {
        x += amount.getX();
        y += amount.getY();
    }

    /**
     * Shorthand for multiplying the coordinates by the same scalar.
     * @param amount The scalar to multiply to the coordinates.
     * @return Self reference.
     */
    public Position multiplyBy(double amount) {
        x *= amount;
        y *= amount;
        return this;
    }

    /**
     * Shorthand for multiplying the coordinates by the same scalar.
     * @param amountX The scalar to multiply to the x coordinate.
     * @param amountY The scalar to multiply to the y coordinate.
     * @return Self reference.
     */
    public Position multiplyBy(double amountX, double amountY) {
        x *= amountX;
        y *= amountY;
        return this;
    }

    /**
     * To string method for the Position class
     * Example: ( 12.000 , -7.445 )
     * @return A string representing the position.
     */
    @Override
    public String toString() {
        return String.format("( %.3f , %.3f )", x, y);
    }
}