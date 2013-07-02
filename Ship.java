/*
 * CS121 A'11
 * Adaptive integtration using the trapezoidal rule.
 *
 * Sample use of Dbl2Dbl: f(y) = 1100 * pi * sqrt( y ) * arctan ( y  )
 */

public class Ship implements Dbl2Dbl {
    double height;
    static double C = 1.0;

    // no constructor needed.
    public Ship(double height) {
        this.height = height;
    }

    public double value(double x) {
        return 1100*Math.PI * Math.sqrt(x) * Math.atan(x);
    }

    public void print() {
        System.out.printf("1100 * pi * sqrt( %f ) * arctan ( %f )", height, height);
    }

    // test code
    public static void main(String args[]) {
    }
}
