/*
 * CS121 A'09
 * HW5: a class for polynomials in one variable with real coefficients
 * 
 */

import java.util.Scanner;

public class Polynomial implements Dbl2Dbl {
	private double coef[];
	private int N;

	public Polynomial(double[] c) {
		// build from coefficients
		N = c.length;
		coef = new double[N];
		for (int i = 0; i < N; i++)
			coef[i] = c[i];

	}

	public Polynomial(double v) {
		// build from a constant
		N = 1;
		coef = new double[1];
		coef[0] = v;
	}

	public Polynomial(Scanner scanner) {
		if (!scanner.hasNextInt()) {
			System.out.println("Polynomial bad format: integer expected");
			System.exit(0);
		}

		N = scanner.nextInt();
		coef = new double[N];

		for (int i = 0; i < N; i++) {
			if (!scanner.hasNextDouble()) {
				System.out.println("i:" + i);
				System.out.println("Polynomial bad format: double expected");
				System.exit(0);
			}
			coef[i] = scanner.nextDouble();
		}
	}

	public void print() {
		if (N <= 0)
			System.out.printf(" %3.1f", 0.0);
		else if (N == 1)
			System.out.printf("%.4f", coef[0]);
		else {
			System.out.printf("(%.4f", coef[0]);
			for (int i = 1; i < N; i++) {
				if (coef[i] > 0.0)
					System.out.printf(" + %.4f X^%1d", coef[i], i);
				else if (coef[i] < 0.0)
					System.out.printf(" - %.4f X^%1d", -coef[i], i);
			}
			System.out.printf(") ");
		}
	}

	public static Polynomial scalarMult(double scalar, Polynomial poly) {
		for (int i = 0; i < poly.N; i++) {
			poly.coef[i] *= scalar;
		}
		return poly;
	}

	public static Polynomial polyMult(Polynomial poly1, Polynomial poly2) {
		int N = poly1.N + poly2.N;
		double[] coef = new double[N];
		for (int i = 0; i < poly1.N; i++) {
			for (int j = 0; j < poly2.N; j++) {
				coef[i + j] += poly1.coef[i] * poly2.coef[j];
			}
		}
		return new Polynomial(coef);
	}

	public double value(double x) {
		// evaluate with Horner's method
		double val = 0.0;
		for (int i = N - 1; i >= 0; i--)
			val = coef[i] + x * val;
		return val;
	}

	/* Test code */
	public static void main(String[] args) {
		Polynomial one = new Polynomial(1.0);
		Polynomial V = new Polynomial(new double[] { 1.0, 0.0, -3.0, 2.0 });
		System.out.print("The poly one is ");
		one.print();
		System.out.print("The poly V(X) is ");
		V.print();
		Polynomial V2 = polyMult(V, V);
		System.out.print("The poly V(X)^2 is ");
		V2.print();
		Polynomial threeV = scalarMult(3, V);
		System.out.print("The poly 3*V(X) is ");
		threeV.print();
	}

}
