/* CS121 A'11
 * Sample use of Dbl2Dbl
 */

public class Sin implements Dbl2Dbl {
    public double value(double x) {
	return Math.sin(x);
    }

    public void print() {
	System.out.println("sin(x)");
    }
}