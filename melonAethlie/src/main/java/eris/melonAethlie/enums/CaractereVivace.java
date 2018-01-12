package eris.melonAethlie.enums;

public enum CaractereVivace {
		Vivace,
		Annuelle;
	
	public static CaractereVivace fromFrenchLabel(String label) {
		switch(label) {
		case "vivace" :
			return Vivace;
		case "annuelle" :
			return Annuelle;
		}
		return null;
	}
}
