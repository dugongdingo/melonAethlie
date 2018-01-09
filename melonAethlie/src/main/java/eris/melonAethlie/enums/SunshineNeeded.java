package eris.melonAethlie.enums;

public enum SunshineNeeded {
	
	HIGH,
	MID,
	LOW;
	
	public static SunshineNeeded fromFrenchLabel(String label) {
		switch (label) {
		case "important" :
			return HIGH;
		case "moyen" :
			return MID;
		case "faible" :
			return LOW;
		}
		return null;
	}

}
