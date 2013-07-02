/*
 * CS121 A'11 
 *
 *   Simple class for representing mathematical functions of one variable.
 *   supports usual unary and binary operations, plus sqrt, interval, and basic
 *   trigonometric operators.
 */


public class FofX implements Dbl2Dbl {
    private String op = "";

    // left operand or if unary operator, only operand
    private Dbl2Dbl left = null;
    private Dbl2Dbl right = null;

    // used for intervals
    private double lb = Double.MIN_VALUE;
    private double ub = Double.MAX_VALUE;

    // used for constants
    private double c;

    // constructor for binary operations
    public FofX(String op, Dbl2Dbl left, Dbl2Dbl right) {
        if (!(op.equals("add") || op.equals("subtract") ||
              op.equals("mult") || op.equals("div") ||
              op.equals("pow"))) {
            System.out.println(op + ": unsupported operator");
            System.exit(0);
        }

        if ((left == null) || (right == null)) {
            System.out.println(op + ": requires two non-null arguments");
            System.exit(0);     
        }


        this.op = op;
        this.left = left;
        this.right = right;
    }

    // constructor for math functions
    public FofX(String op, Dbl2Dbl left) {
        this.op = op;
        if (op.equals("x")) {
            if (left != null) {
                System.out.println(op + ": requires a null argument");
            }
        } else if (op.equals("sqrt") || 
                   op.equals("sin") || 
                   op.equals("cos") || 
                   op.equals("tan") || 
                   op.equals("asin") || 
                   op.equals("acos") ||                    
                   op.equals("atan")) {
            if (left == null) {
                System.out.println(op + ": requires a non-null argument");
                System.exit(0);     
            }
        } else {
            System.out.println("unsupported operator: " + op);
            System.exit(0);
        }
                
        this.left = left;
    }

    public FofX(double c) {
        this.op = "constant";
        this.c = c;
    }

    // constructor for intervals
    public FofX(double lb, double ub, Dbl2Dbl fn) {
        // tfd commented out print 23oct08
        //StdOut.println("constructor for intervals being called");
        if (fn == null) {
            System.out.println(op + ": requires a non-null argument");
            System.exit(0);     
        }

        this.op = "interval";
        this.left = fn;
        this.lb = lb;
        // tfd commented out print 23oct08
        //StdOut.println("new lower bound: " + this.lb);
        this.ub = ub;
        // tfd commented out print 23oct08
        //StdOut.println("new upper bound: " + this.ub);
    }


    // evalute F (the "function")  on x
    public double value(double x) {
        if (op.equals("x"))
            return x;
        else if (op.equals("constant"))
            return c;
        else if (op.equals("interval")) {
            // clamp -- arguments outside the interval
            // should return zero by definition.
            // tfd: commented out info about out of range 23oct08
            // StdOut.println("x: " + x + "\tub: " + ub);
            if ((x < lb) || (x > ub))
                return 0.0;
            else return left.value(x);
        } else if (op.equals("add")) {
            return left.value(x) + right.value(x);
        } else if (op.equals("subtract")) {
            return left.value(x) - right.value(x);
        } else if (op.equals("mult")) {
            return left.value(x) * right.value(x);
        } else if (op.equals("div")) {
            return left.value(x) / right.value(x);
        } else if (op.equals("pow")) {
            return Math.pow(left.value(x), right.value(x));
        } else if (op.equals("sqrt")) {
            return Math.sqrt(left.value(x));
        } else if (op.equals("sin")) {
            return Math.sin(left.value(x));
        } else if (op.equals("cos")) {
            return Math.cos(left.value(x));
        } else if (op.equals("tan")) {
            return Math.tan(left.value(x));
        } else if (op.equals("asin")) {
            return Math.asin(left.value(x));
        } else if (op.equals("acos")) {
            return Math.acos(left.value(x));
        } else if (op.equals("atan")) {
            return Math.atan(left.value(x));
        } else {
            // should not get here.
            System.out.println();
            System.out.println(op);
            System.out.println(op + ":unsupported operation:");
            System.exit(0);
        }

        // should never get here.
        return -1;

    }

    public void print() {
        if (op.equals("x"))
            System.out.print("x");
        else if (op.equals("constant"))
            System.out.print(c);
        else if (op.equals("interval")) {
            System.out.print("interval(" + lb + ", " + ub + ", ");
            left.print();
            System.out.print(")");
        } else if (op.equals("add")) {
            System.out.print("(");
            left.print();
            System.out.print(" + ");
            right.print();
            System.out.print(")");      
        } else if (op.equals("subtract")) {
            System.out.print("(");
            left.print();
            System.out.print(" - ");
            right.print();
            System.out.print(")");      
        } else if (op.equals("mult")) {
            left.print();
            System.out.print(" * ");
            right.print();
        } else if (op.equals("pow")) {
            System.out.print("pow(");
            left.print();
            System.out.print(",");               
            right.print();
            System.out.print(")");
        } else if (op.equals("sqrt") || 
                   op.equals("sin") || 
                   op.equals("cos") || 
                   op.equals("tan") || 
                   op.equals("asin") || 
                   op.equals("acos") ||                    
                   op.equals("atan")) {
            if (left != null) {
                System.out.print(op + "(");
                left.print();
                System.out.print(")");
            } else {
                System.out.print(op + "(x)");
            }
        } else {
            // should not get here.
            System.out.println("unsupported operation:" + op + ":");
            System.exit(0);
        }
    }
}
