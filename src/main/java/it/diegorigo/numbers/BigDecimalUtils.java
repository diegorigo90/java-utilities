package it.diegorigo.numbers;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalUtils {

	private static BigDecimal epsilon = new BigDecimal(1e-32);

	/**
	 * Calcola la radice n-esima del numero "a"
	 * 
	 * @param a : radicando
	 * @param n : indice della radice
	 * @return : radice n-esima di "a"
	 */
	public static BigDecimal sqrt(BigDecimal a, int n) {
		if (a.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("The number cannot be negative");
		}
		BigDecimal prev = a;
		BigDecimal next = new BigDecimal(a.toString());
		do {
			prev = new BigDecimal(next.toString());
			next = prev.subtract(prev.pow(n).subtract(a).divide(prev.pow(n - 1).multiply(new BigDecimal(n)),
					MathContext.DECIMAL128));
		} while (next.subtract(prev).abs().subtract(epsilon).compareTo(BigDecimal.ZERO) > 0);
		return next;
	}

	public static BigDecimal sqrt(BigDecimal a) {
		return sqrt(a, 2);
	}
}
