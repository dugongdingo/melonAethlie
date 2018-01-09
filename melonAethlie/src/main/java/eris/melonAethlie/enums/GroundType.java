package eris.melonAethlie.enums;

public enum GroundType {
	/*title="Sol argileux/lourd"
			title="Sol équilibré"
			title="Sol plante aquatique"
			title="Sol sableux/léger"
*/
	HEAVY,
	MID,
	LIGHT,
	WATER;
	
	public static GroundType fromFrenchLabel(final String label) {
		switch (label) {
		case "argileux/lourd" :
			return HEAVY;
		case "équilibré" :
			return MID;
		case "sableux/léger" :
			return LIGHT;
		case "plante aquatique" :
			return WATER;
		}
		return null;
	}

}
