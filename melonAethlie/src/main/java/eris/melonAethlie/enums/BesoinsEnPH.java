package eris.melonAethlie.enums;

public enum BesoinsEnPH {
	Acide,
	Basique,
	Neutre;
	
	public static BesoinsEnPH fromFrenchLabel(String label) {
		switch(label) {
		case "acide" :
			return Acide;
		case "basique" :
			return Basique;
		case "neutre" :
			return Neutre;
		}
		return null;
	}
}
