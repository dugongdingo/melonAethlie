package eris.melonAethlie.enums;

public enum BesoinEnSoleil {
	
	Important,
	Moyen,
	Faible;
	
	public static BesoinEnSoleil fromFrenchLabel(String label) {
		switch (label) {
		case "important" :
			return Important;
		case "moyen" :
			return Moyen;
		case "faible" :
			return Faible;
		}
		return null;
	}

}
