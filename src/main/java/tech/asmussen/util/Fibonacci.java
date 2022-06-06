package tech.asmussen.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * A class for calculating, formatting and caching Fibonacci numbers.
 */
public class Fibonacci {
	
	/**
	 * The default size of the Fibonacci cache.
	 */
	private static final int DEFAULT_CACHE_SIZE = 100_000;
	
	/**
	 * The default format for numbers.
	 */
	private static final String DEFAULT_DECIMAL_FORMAT = "###,###,###";
	
	/**
	 * The size of the fibonacci cache.
	 */
	public final int CACHE_SIZE;
	
	/**
	 * The array of Fibonacci numbers.
	 */
	private final BigInteger[] CACHE;
	
	/**
	 * Constructs a new Fibonacci object with the cache size of {@link #DEFAULT_CACHE_SIZE}.
	 *
	 * @see #Fibonacci(int)
	 */
	public Fibonacci() {
		
		this(DEFAULT_CACHE_SIZE);
	}
	
	/**
	 * This constructor will create a cache of the Fibonacci numbers up to the specified size.
	 *
	 * @param cacheSize The size of the cache.
	 */
	public Fibonacci(int cacheSize) {
		
		this.CACHE_SIZE = cacheSize;
		
		CACHE = new BigInteger[cacheSize];
		
		CACHE[0] = BigInteger.ZERO;
		CACHE[1] = BigInteger.ONE;
	}
	
	/**
	 * Get the nth Fibonacci number
	 *
	 * @param n The Fibonacci number you want.
	 * @return The nth Fibonacci number.
	 */
	public BigInteger get(int n) {
		
		if (n < 0 || n >= CACHE_SIZE)
			
			throw new IllegalArgumentException("n must be between 0 and " + (CACHE_SIZE - 1) + "!");
		
		if (CACHE[n] == null)
			
			CACHE[n] = get(n - 1).add(get(n - 2));
		
		return CACHE[n];
	}
	
	/**
	 * Get the nth Fibonacci number as a string.
	 *
	 * @param n      The Fibonacci number you want.
	 * @param format The decimal format to use.
	 * @return The nth Fibonacci number as a string.
	 */
	public String getFormatted(int n, String format) {
		
		return new DecimalFormat(format).format(get(n));
	}
	
	/**
	 * Get the nth Fibonacci number as a string.
	 *
	 * @param n The Fibonacci number you want.
	 * @return The nth Fibonacci number as a string formatted as {@link #DEFAULT_DECIMAL_FORMAT}.
	 * @see #getFormatted(int, String)
	 */
	public String getFormatted(int n) {
		
		return getFormatted(n, DEFAULT_DECIMAL_FORMAT);
	}
	
	/**
	 * Print all the numbers to a given argument.
	 *
	 * @param out    The output stream to use.
	 * @param n      The number to print to.
	 * @param format The decimal format to use.
	 * @throws IOException If an error occurs using the given output stream.
	 * @see #getFormatted(int, String)
	 */
	public void printTo(OutputStream out, int n, String format) throws IOException {
		
		for (int i = 0; i < n; i++)
			
			out.write((i + ": " + getFormatted(i, format) + "\n").getBytes());
	}
	
	/**
	 * Print all the numbers to a given argument.
	 *
	 * @param out The output stream to use.
	 * @param n   The number to print to.
	 * @throws IOException If an error occurs using the given output stream.
	 * @see #printTo(OutputStream, int, String)
	 */
	public void printTo(OutputStream out, int n) throws IOException {
		
		for (int i = 0; i < n; i++)
			
			out.write((i + ": " + getFormatted(i) + "\n").getBytes());
	}
	
	/**
	 * Print all the numbers to a given argument.
	 *
	 * @param n      The number to print to.
	 * @param format The decimal format to use.
	 * @throws IOException If an error occurs using the given output stream.
	 * @see #printTo(OutputStream, int, String)
	 * @see #printTo(OutputStream, int)
	 * @see #getFormatted(int, String)
	 */
	public void printTo(int n, String format) throws IOException {
		
		for (int i = 0; i < n; i++)
			
			printTo(System.out, i, format);
	}
	
	/**
	 * Print all the numbers to a given argument.
	 *
	 * @param n The number to print to.
	 * @throws IOException If an error occurs using the given output stream.
	 * @see #printTo(OutputStream, int, String)
	 * @see #printTo(int, String)
	 * @see #getFormatted(int, String)
	 */
	public void printTo(int n) throws IOException {
		
		for (int i = 0; i < n; i++)
			
			printTo(System.out, i);
	}
	
	/**
	 * Clear the cache.
	 */
	public void clearCache() {
		
		for (int i = 0; i < CACHE_SIZE; i++)
			
			CACHE[i] = null;
	}
}
