package eris.melonAethlie.enums;

public enum Multiplication {
	Bouture,
	Division,
	Greffe,
	Marcottage,
	Semis,
	Rejet,
	SemisSpontanne;
	
	public static Multiplication fromFrenchLabel(final String label) {
		switch (label) {
		case "bouture" :
			return Bouture;
		case "division" :
			return Division;
		case "greffe" :
			return Greffe;
		case "marcottage" :
			return Marcottage;
		case "semis" :
			return Semis;
		case "rejet" :
			return Rejet;
		case "semis spontané" :
			return SemisSpontanne;
		}
		return null;
	}

}
