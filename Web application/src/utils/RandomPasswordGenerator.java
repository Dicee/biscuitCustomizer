package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomPasswordGenerator {
	private static final int	UPPER_CASE_INDEX	= 26;
	private static final int	NUMBERS_INDEX		= 52;

	private static final int	N_CHARS				= 62;
	private static final int	N_LETTERS			= 26;
	private static final int	N_NUMBERS			= 10;
	
	private static final char[] validCharacters = new char[N_CHARS];
	
	static {
		int k = 0;
		for (char i='a' ; i<='z' ; i++) validCharacters[k++] = i;
		for (char i='A' ; i<='Z' ; i++)	validCharacters[k++] = i;
		for (char i='0' ; i<='9' ; i++)	validCharacters[k++] = i;
		Arrays.toString(validCharacters);			
	}
	/**
	 * 
	 * @param length
	 * @param nminUpperCase
	 * @param nminNumber
	 * @throws IllegalArgumentException if length < nminUpperCase + nminNumber
	 * @return
	 */
	public static String randomPassword(int length, int nminUpperCase, int nminNumber) {
		if (length < nminUpperCase + nminNumber)
			throw new IllegalArgumentException();
		
		char[]        password = new char[length];
		Random        rd       = new Random(System.nanoTime());
		List<Integer> indexes  = new ArrayList<>();		
		for (int i=0 ; i<length ; i++) indexes.add(i);
		
		for (int i=0 ; i<nminUpperCase ; i++) {
			int j = rd.nextInt(indexes.size());
			password[indexes.get(j)] = validCharacters[UPPER_CASE_INDEX + rd.nextInt(N_LETTERS)];
			indexes.remove(j);
		}
			
		for (int i=0 ; i<nminNumber ; i++) {
			int j = rd.nextInt(indexes.size());
			password[indexes.get(j)] = validCharacters[NUMBERS_INDEX + rd.nextInt(N_NUMBERS)];
			indexes.remove(j);
		}	
		
		while (!indexes.isEmpty()) {
			int j = rd.nextInt(indexes.size());
			password[indexes.get(j)] = validCharacters[rd.nextInt(N_CHARS)];
			indexes.remove(j);
		}
		return new String(password);
	}
}
