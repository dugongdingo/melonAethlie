package onto.htmlreader.enums;

public enum RootType {
	FASCICLE,
	ROTATING,
	CREEPING;
	
	public static RootType fromFrenchLabel(final String label) {
		switch (label) {
		case "pivotante" :
			return ROTATING;
		case "fasciculée" :
			return FASCICLE;
		case "traçante" :
			return CREEPING;
		}
		return null;
	}
}
