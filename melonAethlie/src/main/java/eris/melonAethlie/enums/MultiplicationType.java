package eris.melonAethlie.enums;

public enum MultiplicationType {
	CUTTING,
	DIVISION,
	GRAFTING,
	LAYERING,
	SOWING,
	SHOOT,
	SPONTANEOUS_GROWTH;
	
	public static MultiplicationType fromFrenchLabel(final String label) {
		switch (label) {
		case "bouture" :
			return CUTTING;
		case "division" :
			return DIVISION;
		case "greffe" :
			return GRAFTING;
		case "marcottage" :
			return LAYERING;
		case "semis" :
			return SOWING;
		case "rejet" :
			return SHOOT;
		case "semis spontané" :
			return SPONTANEOUS_GROWTH;
		}
		return null;
	}

}
