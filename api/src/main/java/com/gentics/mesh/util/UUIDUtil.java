package com.gentics.mesh.util;

import java.util.UUID;
import java.util.regex.Pattern;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;

/**
 * Main source for UUIDs. The UUIDs are shorted in order to better utilize the database indices.
 */
public final class UUIDUtil {

	public static final RandomBasedGenerator UUID_GENERATOR = Generators.randomBasedGenerator();

	private static Pattern p = Pattern.compile("^[A-Fa-f0-9]+$");

	private UUIDUtil() {

	}

	/**
	 * Create a random UUID string which does not include dashes.
	 * 
	 * @return
	 */
	public static String randomUUID() {
		final UUID uuid = UUID_GENERATOR.generate();
		return (digits(uuid.getMostSignificantBits() >> 32, 8) + digits(uuid.getMostSignificantBits() >> 16, 4)
				+ digits(uuid.getMostSignificantBits(), 4) + digits(uuid.getLeastSignificantBits() >> 48, 4)
				+ digits(uuid.getLeastSignificantBits(), 12));
	}

	/**
	 * Returns val represented by the specified number of hex digits.
	 * 
	 * @param val
	 * @param digits
	 * @return
	 */
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	/**
	 * Check whether the given text is a uuid.
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isUUID(String text) {
		if (text == null || text.length() != 32) {
			return false;
		} else {
			return p.matcher(text).matches();
		}
	}
}
