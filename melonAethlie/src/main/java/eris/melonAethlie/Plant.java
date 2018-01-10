package eris.melonAethlie;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import eris.melonAethlie.enums.Action;
import eris.melonAethlie.enums.EdiblePart;
import eris.melonAethlie.enums.GroundType;
import eris.melonAethlie.enums.Months;
import eris.melonAethlie.enums.MultiplicationType;
import eris.melonAethlie.enums.NutrientsNeeded;
import eris.melonAethlie.enums.PHNeeded;
import eris.melonAethlie.enums.PerennialType;
import eris.melonAethlie.enums.RootType;
import eris.melonAethlie.enums.SunshineNeeded;

public class Plant {

	public EnumMap<Months, List<Action>> getCalendar() {
		return calendar;
	}
	public void setCalendar(EnumMap<Months, List<Action>> calendar) {
		this.calendar = calendar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public Set<EdiblePart> getEdibleParts() {
		return edibleParts;
	}
	public void setEdibleParts(Set<EdiblePart> edibleParts) {
		this.edibleParts = edibleParts;
	}
	public Set<MultiplicationType> getMultiplications() {
		return multiplications;
	}
	public void setMultiplications(Set<MultiplicationType> multiplications) {
		this.multiplications = multiplications;
	}
	public GroundType getGround() {
		return ground;
	}
	public void setGround(GroundType ground) {
		this.ground = ground;
	}
	public PerennialType getPerennial() {
		return perennial;
	}
	public void setPerennial(PerennialType perennial) {
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
	public SunshineNeeded getSunshineNeeded() {
		return sunshineNeeded;
	}
	public void setSunshineNeeded(SunshineNeeded sunshineNeeded) {
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
	public NutrientsNeeded getNutrientsNeeded() {
		return nutrientsNeeded;
	}
	public void setNutrientsNeeded(NutrientsNeeded nutrientsNeeded) {
		this.nutrientsNeeded = nutrientsNeeded;
	}
	public PHNeeded getPhNeeded() {
		return phNeeded;
	}
	public void setPhNeeded(PHNeeded phNeeded) {
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
	public RootType getRootType() {
		return rootType;
	}
	public void setRootType(RootType rootType) {
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
	private EnumMap<Months, List<Action>> calendar;
	private Set<EdiblePart> edibleParts;
	private Set<MultiplicationType> multiplications;
	private GroundType ground;
	private PerennialType perennial;
	private Integer width;
	private Integer height;
	private SunshineNeeded sunshineNeeded;
	private String comments;
	private Set<String> nefariousNeighbors;
	private Set<String> beneficialNeighbors;
	private Integer lineSpacing;
	private Integer rowSpacing;
	private NutrientsNeeded nutrientsNeeded;
	private PHNeeded phNeeded;
	private Integer timeToSprout;
	private Integer temperatureToSprout;
	private Integer expectedYield;
	private Set<String> uses;
	private Set<String> medicalUses;
	private Integer minimalTemperature;
	private RootType rootType;
	private Integer depth;
	private Integer seedConservationDuration;
	
}
