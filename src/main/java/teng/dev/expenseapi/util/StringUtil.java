package teng.dev.expenseapi.util;

public class StringUtil
{
	public static String capitalizeEachWord(String inputString) {
		if (inputString == null || inputString.isEmpty()) {
			return inputString;
		}

		String[] words = inputString.split("\\s+"); // Split by spaces
		StringBuilder capitalizedString = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				capitalizedString.append(Character.toUpperCase(word.charAt(0)))
						.append(word.substring(1))
						.append(" ");
			}
		}

		return capitalizedString.toString().trim();
	}
}
