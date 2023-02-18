package it.diegorigo.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BigDecimalUtils {

	private static final BigDecimal epsilon = new BigDecimal("1e-32");

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
		BigDecimal prev;
		BigDecimal next = new BigDecimal(a.toString());
		do {
			prev = new BigDecimal(next.toString());
			next = prev.subtract(prev.pow(n).subtract(a).divide(prev.pow(n - 1).multiply(new BigDecimal(n)),
					MathContext.DECIMAL128));
		} while (next.subtract(prev).abs().subtract(epsilon).compareTo(BigDecimal.ZERO) > 0);
		return next;
	}

	public static <T> List<BigDecimal> toBigDecimal(List<T> values) {

		return values.stream().map(item -> {
			BigDecimal val;
			if (item instanceof Double) {
				val = new BigDecimal((Double) item);
			} else if (item instanceof String) {
				val = new BigDecimal((String) item);
			} else if (item instanceof BigInteger) {
				val = new BigDecimal((BigInteger) item);
			} else if (item instanceof Integer) {
				val = new BigDecimal((Integer) item);
			} else {
				throw new IllegalArgumentException(
						"It is not possible to convert a " + item.getClass()
																 .getName() + " to a BigDecimal");
			}
			return val;
		}).collect(Collectors.toList());
	}
	public static <T> List<BigDecimal> asBigDecimalList(T... values) {

		return toBigDecimal(Arrays.asList(values));
	}

	public static BigDecimal sqrt(BigDecimal a) {
		return sqrt(a, 2);
	}
}
