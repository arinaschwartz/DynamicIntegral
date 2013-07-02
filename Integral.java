/*
 * CS121 A'11
 * adaptive integtration using the trapezoidal rule
 *
 * Arin Schwartz
 * 
 */

public class Integral {

    private static final int MINLEVEL = 1;
    private static final int MAXLEVEL = 20;
    //Added level parameter
    private static int level = 0;

    /**
     * Integrate f over the interval [left, right] using adaptive integration.
     * 
     * double left: left end-point of the interval 
     * double right: right end-point of the interval 
     * Dbl2Dbl f: the function to integrate 
     * double tol: the acceptable amount of error
     */
    public static double atrap(double left, double right, Dbl2Dbl f, double tol) {
    	double midpoint = (left + right)/2;
    	if(checkDiff(left, right, f, tol) == true){
    		if(level < MINLEVEL){
    			level++;
        		return atrap(left, midpoint, f, tol) + atrap(midpoint, right, f, tol);
    		}
    		return SimpsonsRule(left, right, f);
    	}
    	
    	else if(level >= MAXLEVEL){
    		return SimpsonsRule(left, right, f);
    	}
    	
    	else{
    		level++;
    		return atrap(left, right/2, f, tol) + atrap(right/2, right, f, tol);
    	}
    }
    
    //Returns the trapezoidal area of ONE interval
    public static double oneArea(double left, double right, Dbl2Dbl f){
        double area = ((f.value(right) + f.value(left))/2) * (right - left);
        return area;
    }
    
    
    
    //Checks the difference between an interval and the sum of area of the two halves of that interval
    public static boolean checkDiff(double left, double right, Dbl2Dbl f, double tol){
    	double area = oneArea(left, right, f);
    	
    	double midpoint = (left + right)/2;
    	double firstHalfArea = oneArea(left, midpoint, f);
    	double secondHalfArea = oneArea(midpoint, right, f);
    	
    	if(Math.abs(area) - (Math.abs(firstHalfArea) + Math.abs(secondHalfArea)) < tol){
    		return true;
    	}
    	return false;
    }
    
    //Applies Simpson's Ruleto one interval
    public static double SimpsonsRule(double left, double right, Dbl2Dbl f){
    	double midpoint = (left + right)/2;
    	double SimpsonsRule = ((right - left)/6) * (f.value(left) + (4*f.value(midpoint)) + f.value(right));
    	return SimpsonsRule;
    }
    
    


    /* runTest: test integral code
     *   Dbl2Dbl f: function to be integrated (as an object)
     *   double a,b: range to integrate over
     *   double answer: expected answer
     *   double tol: error tolerance
     */
    private static void runTest(Dbl2Dbl f, double a, double b, double answer, double tol) {
        try {
            double guess = Integral.atrap(a, b, f, tol);
            boolean bool = ((guess < answer + tol) && (guess > answer - tol));
            System.out.print("f: ");
            f.print();
            System.out.println();
            System.out.println("range: [" + a + ".. " + b + "]");
            System.out.println("tolerance: " + tol);
            System.out.println("expected answer: " + answer);
            System.out.println("your answer: " + guess);
            
            if (bool == true) {
                System.out.println("Passed Test");
            } else {
                System.out.println("Failed Test");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Your program generated an exception:");
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        Dbl2Dbl sqrtX = ParseExpression.expression("sqrt(x)");
        double tol = 0.01;
        double answer = 4 * Math.sqrt(2) / 3.0 - 2 / 3.0;
        
        //My tests begin here
        oneArea(1, 2, sqrtX);
        checkDiff(1, 2, sqrtX, tol);
        SimpsonsRule(1, 2, sqrtX);
        //Eclipse and other IEDs are the greatest freaking invention ever created.
        //Tests of helper functions passed independently after some math bugs were found.
        
        runTest(sqrtX, 1, 2, answer, tol);
        tol = 0.0001;
        runTest(sqrtX, 1, 2, answer, tol);

        //Both tolerance levels passed.

    }
}
