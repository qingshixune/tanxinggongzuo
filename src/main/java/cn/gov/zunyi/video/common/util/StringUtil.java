package cn.gov.zunyi.video.common.util;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class StringUtil {

	public static int getCommonStr(final CharSequence[] charSequences, String keywords) {
		for (int i = 0; i < charSequences.length; i++) {
			CharSequence sequence = charSequences[i];
			HashSet<Character> h1 = new LinkedHashSet<Character>(), h2 = new LinkedHashSet<Character>();
			for (int j = 0; j < sequence.length(); j++) {
				h1.add(sequence.charAt(j));
			}
			for (int k = 0; k < keywords.length(); k++) {
				h2.add(keywords.charAt(k));
			}
			h1.retainAll(h2);
			Character[] res = h1.toArray(new Character[0]);
			String s = "";
			for (Character character : res) {
				s += character.toString();
			}
			if (sequence.equals(s)) {
				return i;
			}
		}
		return 0;
	}

}
