package onto.htmlreader.enums;

public enum EdiblePart {	
	LEAF,
	FLOWER,
	ROOT,
	STEM_OR_SAP,
	FRUIT;
	
	public static EdiblePart fromFrenchLabel(final String label) {
		switch(label) {
		case "feuille" :
			return LEAF;
		case "fleur" :
			return FLOWER;
		case "racine" :
			return ROOT;
		case "tige/sève" :
			return STEM_OR_SAP;
		case "fruit" :
			return FRUIT;
		}
		return null;
	}
		

}
