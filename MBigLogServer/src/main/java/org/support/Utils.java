package org.support;

import java.util.regex.Pattern;

/**
 * @author wangzhanwei
 */
public class Utils {
	public static Pattern logPattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) \\[\\w+\\]\\[\\w+\\]");
	private static char[] trans = {'*', '.', '?', '+', '$', '^', '[', ']', '(', ')', '{', '}', '|', '\\', '/'};

	public static int bytesToInt(byte[] bytes, int offset) {
		int num = 0;
		for (int i = offset; i < offset + 4; ++i) {
			num <<= 8;
			num |= (bytes[i] & 0xff);
		}

		return num;
	}

	public static byte[] intToBytes1(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	public static int bytesToInt1(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF)
				| ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	public static String transString(String s) {
		StringBuilder stringBuilder = new StringBuilder(s);
		for (int i = 0; i < stringBuilder.length(); i++) {
			if (inTrans(stringBuilder.charAt(i))) {
				stringBuilder.insert(i, "\\");
				i++;
			}
		}
		return stringBuilder.toString();
	}

	private static boolean inTrans(char c) {
		for (int i = 0; i < trans.length; i++) {
			if (c == trans[i]) {
				return true;
			}
		}
		return false;
	}
}
