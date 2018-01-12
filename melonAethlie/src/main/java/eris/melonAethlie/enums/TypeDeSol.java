package eris.melonAethlie.enums;

public enum TypeDeSol {
	/*title="Sol argileux/lourd"
			title="Sol équilibré"
			title="Sol plante aquatique"
			title="Sol sableux/léger"
*/
	ArgileuxLourd,
	Equilibre,
	SableuxLeger,
	PlanteAquatique;
	
	public static TypeDeSol fromFrenchLabel(final String label) {
		switch (label) {
		case "argileux/lourd" :
			return ArgileuxLourd;
		case "équilibré" :
			return Equilibre;
		case "sableux/léger" :
			return SableuxLeger;
		case "plante aquatique" :
			return PlanteAquatique;
		}
		return null;
	}

}
