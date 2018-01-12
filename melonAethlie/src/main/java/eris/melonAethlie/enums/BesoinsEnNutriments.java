package eris.melonAethlie.enums;

public enum BesoinsEnNutriments {
	
	Eleves,
	Moyens,
	Faibles;
	
	public static BesoinsEnNutriments fromFrenchLabel(final String label) {
		switch (label) {
		case "élevés":
			return Eleves;
		case "moyens" :
			return Moyens;
		case "faibles" :
			return Faibles;
		}
		return null;
	}

}
