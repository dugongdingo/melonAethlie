package onto.htmlreader.enums;

public enum NutrientsNeeded {
	
	HIGH,
	MID,
	LOW;
	
	public static NutrientsNeeded fromFrenchLabel(final String label) {
		switch (label) {
		case "élevés":
			return HIGH;
		case "moyens" :
			return MID;
		case "faibles" :
			return LOW;
		}
		return null;
	}

}
