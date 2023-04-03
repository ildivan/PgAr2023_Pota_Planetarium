package planetarium.solarsystem;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void increaseX(double amount) {setX(getX() + amount);}
    public void increaseY(double amount) {setY(getY() + amount);}

    public Position increase(Position amount) {
        increaseX(amount.getX());
        increaseY(amount.getY());
        return this;
    }

    public Position multiplyBy(double amount){
        setX(getX()*amount);
        setY(getY()*amount);
        return this;
    }

    @Override
    public String toString() {
        return "( " + getX() + " , " + getY() + " )";
    }
}