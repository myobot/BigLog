package org.support;

import java.util.regex.Pattern;

/**
 * @author wangzhanwei
 */
public class Utils {
	public static Pattern logPattern;
//	= Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) \\[\\w+\\]\\[\\w+\\]");
	public static Pattern logProtocolPattern;
//	= Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) \\[\\w+\\]\\[\\w+\\] ((SC|CS)\\w+)");
	public static Pattern LogErrorPattern;
//	= Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) \\(\\w+.java:\\d+\\) (\\[ERROR\\])(\\[\\w+\\])");
	public static Pattern LogExceptionPattern = Pattern.compile("(\\w+Exception)");
	private static char[] trans = {'*', '.', '?', '+', '$', '^', '[', ']', '(', ')', '{', '}', '|', '\\', '/'};

	public static int bytesToInt(byte[] bytes, int offset) {
		int num = 0;
		try {
			for (int i = offset; i < offset + 4; ++i) {
				num <<= 8;
				num |= (bytes[i] & 0xff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;

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
