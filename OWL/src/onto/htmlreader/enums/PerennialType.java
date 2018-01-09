package onto.htmlreader.enums;

public enum PerennialType {
		PERENNIAL,
		ANNUAL;
	
	public static PerennialType fromFrenchLabel(String label) {
		switch(label) {
		case "vivace" :
			return PERENNIAL;
		case "annuelle" :
			return ANNUAL;
		}
		return null;
	}
}
