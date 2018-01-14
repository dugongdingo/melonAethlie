package eris.melonAethlie.enums;

public enum PartieComestible {	
	Feuille,
	Fleur,
	Racine,
	TigeOuSeve,
	Fruit;
	
	public static PartieComestible fromFrenchLabel(final String label) {
		switch(label) {
		case "feuille" :
			return Feuille;
		case "fleur" :
			return Fleur;
		case "racine" :
			return Racine;
		case "tige/sève" :
			return TigeOuSeve;
		case "fruit" :
			return Fruit;
		}
		return null;
	}
		

}
