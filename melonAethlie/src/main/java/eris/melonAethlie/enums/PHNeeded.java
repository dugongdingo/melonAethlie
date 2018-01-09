package eris.melonAethlie.enums;

public enum PHNeeded {
	ACIDIC,
	BASIC,
	NEUTRAL;
	
	public static PHNeeded fromFrenchLabel(String label) {
		switch(label) {
		case "acide" :
			return ACIDIC;
		case "basique" :
			return BASIC;
		case "neutre" :
			return NEUTRAL;
		}
		return null;
	}
}
