package eris.melonAethlie.enums;

public enum TypeDeRacine {
	Fasciculee,
	Pivotante,
	Tracante;
	
	public static TypeDeRacine fromFrenchLabel(final String label) {
		switch (label) {
		case "pivotante" :
			return Pivotante;
		case "fasciculée" :
			return Fasciculee;
		case "traçante" :
			return Tracante;
		}
		return null;
	}
}
