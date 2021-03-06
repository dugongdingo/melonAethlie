package eris.melonAethlie.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import eris.melonAethlie.enums.BesoinEnSoleil;
import eris.melonAethlie.enums.BesoinsEnNutriments;
import eris.melonAethlie.enums.BesoinsEnPH;
import eris.melonAethlie.enums.CaractereVivace;
import eris.melonAethlie.enums.Multiplication;
import eris.melonAethlie.enums.PartieComestible;
import eris.melonAethlie.enums.TypeDeRacine;
import eris.melonAethlie.enums.TypeDeSol;
import eris.melonAethlie.model.Plant;

public enum DivType {
	
	FAM("Famille ", (p, t) -> p.setFamily(getValue("Famille ", t))),
	NAM("Nom usuel ", (p, t) -> p.setName(getValue("Nom usuel ", t))),
	HEI("Hauteur ", (p, t) -> p.setHeight(getNumericValue("Hauteur ", t))),
	WID("Largeur ", (p, t) -> p.setWidth(getNumericValue("Largeur ", t))),
	PER("Caractère vivace ", (p, t) -> p.setPerennial(CaractereVivace.fromFrenchLabel(getValue("Caractère vivace ", t)))),
	EDB("Partie comestible ", (p, t) -> p.setEdibleParts(getListedValues("Partie comestible ", t).stream().map(e -> PartieComestible.fromFrenchLabel(e)).collect(Collectors.toSet()))),
	MUL("Multiplication par ", (p, t) -> p.setMultiplications(getListedValues("Multiplication par", t).stream().map(m -> Multiplication.fromFrenchLabel(m)).collect(Collectors.toSet()))),
	SUN("Ensoleillement ", (p, t) -> p.setSunshineNeeded(BesoinEnSoleil.fromFrenchLabel(getValue("Ensoleillement ", t)))),
	BNE("Compagnons néfastes ", (p, t) -> p.setNefariousNeighbors(getListedValues("Compagnons néfastes ", t))),
	GNE("Bons compagnons ", (p, t) -> p.setBeneficialNeighbors(getListedValues("Bons compagnons ", t))),
	NUT("Besoins en nutriments ", (p, t) -> p.setNutrientsNeeded(BesoinsEnNutriments.fromFrenchLabel(getValue("Besoins en nutriments ", t)))),
	GRD("Sol ", (p, t) -> p.setGround(TypeDeSol.fromFrenchLabel(getValue("Sol ", t)))),
	CNS("Durée de conservation des semences ", (p, t) -> p.setSeedConservationDuration(getNumericValue("Durée de conservation des semences ", t))),
	LIN("Espacement dans la ligne ", (p, t) -> p.setLineSpacing(getNumericValue("Espacement dans la ligne ", t))),
	ROW("Espacement dans le rang ", (p, t) -> p.setRowSpacing(getNumericValue("Espacement dans le rang ", t))),
	TIM("Nombre de jours de la graine à la plantule ", (p, t) -> p.setTimeToSprout(getNumericValue("Nombre de jours de la graine à la plantule ", t))),
	YLD("Nombre de kg/m² lors de la récolte ", (p, t) -> p.setExpectedYield(getNumericValue("Nombre de kg/m² lors de la récolte ", t))),
	USE("Utilisations de la plante ", (p, t) -> p.setUses(getListedValues("Utilisations de la plante ", t))),
	MED("Usage médical ", (p, t) -> p.setMedicalUses(getListedValues("Usage médical ", t))),
	TMP("Température mini ", (p, t) -> p.setMinimalTemperature(getNumericValue("Température mini ", t))),
	PH_("pH ", (p, t) -> p.setPhNeeded(BesoinsEnPH.fromFrenchLabel(getValue("pH ", t)))),
	SPR("Température de levée des graines ", (p, t) -> p.setTemperatureToSprout(getNumericValue("Température de levée des graines ", t))),
	ROO("Racine ", (p, t) -> p.setRootType(TypeDeRacine.fromFrenchLabel(getValue("Racine ", t)))),
	DEP("Profondeur ", (p, t) -> p.setDepth(getNumericValue("Profondeur ", t))),
	COM("Commentaire ", (p, t) -> p.setComments(getValue("Commentaire ", t)));
	
	
	private final String titleStart;
	
	private final BiConsumer<Plant, String> setterCaller;
	
	
	private final static Map<String, DivType> RMAP;
	
	static {
		RMAP = new HashMap<String, DivType>();
		for (DivType dt : DivType.values()) {
			RMAP.put(dt.getTitleStart(), dt);
		}
	}
	
	public static DivType fromTitleStart(String titleStart) {
		return RMAP.get(titleStart);
	}
	
	public static void feedFromTitle(Plant plant, String title) {
		DivType.fromTitle(title).setterCaller.accept(plant, title);
	}
	
	public static DivType fromTitle(String title) {
		for (String titleStart : getTitleStarts()) {
			if (title.startsWith(titleStart)) {
				return fromTitleStart(titleStart);
			}
		}
		return null;
	}
	
	public static Set<String> getTitleStarts() {
		return RMAP.keySet();
	}
	
	private DivType(String titleStart, BiConsumer<Plant, String> setterCaller) {
		this.setterCaller = setterCaller;
		this.titleStart = titleStart;
	}

	public String getTitleStart() {
		return titleStart;
	}
	
	
	
	private static String getValue(String titleStart, String title) {
		return title.replaceFirst(titleStart, "").trim();
	}
	
	private static Set<String> getListedValues(String titleStart, String title) {
		return new HashSet<String>(Arrays.asList(getValue(titleStart, title).split(",")));
	}
	
	private static Integer getNumericValue(String titleStart, String title) {
		String[] vals = getValue(titleStart, title).split(" ");
		vals = vals.length == 0 ? null : vals;
		return vals.length == 0 ? null : Integer.parseInt(vals[0]);
	}

}
