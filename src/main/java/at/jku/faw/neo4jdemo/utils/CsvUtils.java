package at.jku.faw.neo4jdemo.utils;

import java.util.Objects;

public class CsvUtils {

	public static Boolean extractBoolean(Object value) {
		switch (value) {
			case null -> {
				return null;
			}
			case Boolean b -> {
				return b;
			}
			case Number n -> {
				return n.intValue() != 0;
			}
			case String s -> {
				String t = s.trim().toLowerCase();
				if (Objects.equals(t, "true")) return true;
				if (Objects.equals(t, "false")) return false;
				try {
					return Integer.parseInt(t) != 0;
				} catch (NumberFormatException ignored) {
				}
			}
			default -> {
			}
		}
		return null;
	}
}
