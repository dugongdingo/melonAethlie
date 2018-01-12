package eris.melonAethlie;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import eris.melonAethlie.enums.Action;
import eris.melonAethlie.enums.PartieComestible;
import eris.melonAethlie.enums.TypeDeSol;
import eris.melonAethlie.enums.Mois;
import eris.melonAethlie.enums.Multiplication;
import eris.melonAethlie.enums.BesoinsEnNutriments;
import eris.melonAethlie.enums.BesoinsEnPH;
import eris.melonAethlie.enums.CaractereVivace;
import eris.melonAethlie.enums.TypeDeRacine;
import eris.melonAethlie.enums.BesoinEnSoleil;
import eris.melonAethlie.generator.ModelGenerator;

public class Plant {
	
	public String getPrefLabel() {
		return null;
	}
	
	public String getAltLabel() {
		return null;
	}

	public EnumMap<Action, List<Mois>> getCalendar() {
		return calendar;
	}
	public void setCalendar(EnumMap<Action, List<Mois>> calendar) {
		this.calendar = calendar;
	}
	public String getName() {
		return ModelGenerator.capitalize(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFamily() {
		return family == null ? "Autre": family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public Set<PartieComestible> getEdibleParts() {
		return edibleParts;
	}
	public void setEdibleParts(Set<PartieComestible> edibleParts) {
		this.edibleParts = edibleParts;
	}
	public Set<Multiplication> getMultiplications() {
		return multiplications;
	}
	public void setMultiplications(Set<Multiplication> multiplications) {
		this.multiplications = multiplications;
	}
	public TypeDeSol getGround() {
		return ground;
	}
	public void setGround(TypeDeSol ground) {
		this.ground = ground;
	}
	public CaractereVivace getPerennial() {
		return perennial;
	}
	public void setPerennial(CaractereVivace perennial) {
		this.perennial = perennial;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public BesoinEnSoleil getSunshineNeeded() {
		return sunshineNeeded;
	}
	public void setSunshineNeeded(BesoinEnSoleil sunshineNeeded) {
		this.sunshineNeeded = sunshineNeeded;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Set<String> getNefariousNeighbors() {
		return nefariousNeighbors;
	}
	public void setNefariousNeighbors(Set<String> nefariousNeighbors) {
		this.nefariousNeighbors = nefariousNeighbors;
	}
	public Set<String> getBeneficialNeighbors() {
		return beneficialNeighbors;
	}
	public void setBeneficialNeighbors(Set<String> beneficialNeighbors) {
		this.beneficialNeighbors = beneficialNeighbors;
	}
	public Integer getLineSpacing() {
		return lineSpacing;
	}
	public void setLineSpacing(Integer lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	public Integer getRowSpacing() {
		return rowSpacing;
	}
	public void setRowSpacing(Integer rowSpacing) {
		this.rowSpacing = rowSpacing;
	}
	public BesoinsEnNutriments getNutrientsNeeded() {
		return nutrientsNeeded;
	}
	public void setNutrientsNeeded(BesoinsEnNutriments nutrientsNeeded) {
		this.nutrientsNeeded = nutrientsNeeded;
	}
	public BesoinsEnPH getPhNeeded() {
		return phNeeded;
	}
	public void setPhNeeded(BesoinsEnPH phNeeded) {
		this.phNeeded = phNeeded;
	}
	public Integer getTimeToSprout() {
		return timeToSprout;
	}
	public void setTimeToSprout(Integer timeToSprout) {
		this.timeToSprout = timeToSprout;
	}
	public Integer getTemperatureToSprout() {
		return temperatureToSprout;
	}
	public void setTemperatureToSprout(Integer temperatureToSprout) {
		this.temperatureToSprout = temperatureToSprout;
	}
	public Integer getExpectedYield() {
		return expectedYield;
	}
	public void setExpectedYield(Integer expectedYield) {
		this.expectedYield = expectedYield;
	}
	public Set<String> getUses() {
		return uses;
	}
	public void setUses(Set<String> uses) {
		this.uses = uses;
	}
	public Set<String> getMedicalUses() {
		return medicalUses;
	}
	public void setMedicalUses(Set<String> medicalUses) {
		this.medicalUses = medicalUses;
	}
	public Integer getMinimalTemperature() {
		return minimalTemperature;
	}
	public void setMinimalTemperature(Integer minimalTemperature) {
		this.minimalTemperature = minimalTemperature;
	}
	public TypeDeRacine getRootType() {
		return rootType;
	}
	public void setRootType(TypeDeRacine rootType) {
		this.rootType = rootType;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Integer getSeedConservationDuration() {
		return seedConservationDuration;
	}
	public void setSeedConservationDuration(Integer seedConservationDuration) {
		this.seedConservationDuration = seedConservationDuration;
	}

	@Override
	public String toString() {
		return "Plant [name=" + name + ", family=" + family + ", calendar=" + calendar + ", edibleParts=" + edibleParts
				+ ", multiplications=" + multiplications + ", ground=" + ground + ", perennial=" + perennial
				+ ", width=" + width + ", height=" + height + ", sunshineNeeded=" + sunshineNeeded + ", comments="
				+ comments + ", nefariousNeighbors=" + nefariousNeighbors + ", beneficialNeighbors="
				+ beneficialNeighbors + ", lineSpacing=" + lineSpacing + ", rowSpacing=" + rowSpacing
				+ ", nutrientsNeeded=" + nutrientsNeeded + ", phNeeded=" + phNeeded + ", timeToSprout=" + timeToSprout
				+ ", temperatureToSprout=" + temperatureToSprout + ", expectedYield=" + expectedYield + ", uses=" + uses
				+ ", medicalUses=" + medicalUses + ", minimalTemperature=" + minimalTemperature + ", rootType="
				+ rootType + ", depth=" + depth + ", seedConservationDuration=" + seedConservationDuration + "]";
	}

	private String name;
	private String family;
	private EnumMap<Action, List<Mois>> calendar;
	private Set<PartieComestible> edibleParts;
	private Set<Multiplication> multiplications;
	private TypeDeSol ground;
	private CaractereVivace perennial;
	private Integer width;
	private Integer height;
	private BesoinEnSoleil sunshineNeeded;
	private String comments;
	private Set<String> nefariousNeighbors;
	private Set<String> beneficialNeighbors;
	private Integer lineSpacing;
	private Integer rowSpacing;
	private BesoinsEnNutriments nutrientsNeeded;
	private BesoinsEnPH phNeeded;
	private Integer timeToSprout;
	private Integer temperatureToSprout;
	private Integer expectedYield;
	private Set<String> uses;
	private Set<String> medicalUses;
	private Integer minimalTemperature;
	private TypeDeRacine rootType;
	private Integer depth;
	private Integer seedConservationDuration;
	
}
