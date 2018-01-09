package onto.generator;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import onto.htmlreader.enums.Action;
import onto.htmlreader.enums.EdiblePart;
import onto.htmlreader.enums.GroundType;
import onto.htmlreader.enums.Months;
import onto.htmlreader.enums.MultiplicationType;
import onto.htmlreader.enums.NutrientsNeeded;
import onto.htmlreader.enums.PHNeeded;
import onto.htmlreader.enums.PerennialType;
import onto.htmlreader.enums.RootType;
import onto.htmlreader.enums.SunshineNeeded;

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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
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
	public int getLineSpacing() {
		return lineSpacing;
	}
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	public int getRowSpacing() {
		return rowSpacing;
	}
	public void setRowSpacing(int rowSpacing) {
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
	public int getTimeToSprout() {
		return timeToSprout;
	}
	public void setTimeToSprout(int timeToSprout) {
		this.timeToSprout = timeToSprout;
	}
	public int getTemperatureToSprout() {
		return temperatureToSprout;
	}
	public void setTemperatureToSprout(int temperatureToSprout) {
		this.temperatureToSprout = temperatureToSprout;
	}
	public int getExpectedYield() {
		return expectedYield;
	}
	public void setExpectedYield(int expectedYield) {
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
	public int getMinimalTemperature() {
		return minimalTemperature;
	}
	public void setMinimalTemperature(int minimalTemperature) {
		this.minimalTemperature = minimalTemperature;
	}
	public RootType getRootType() {
		return rootType;
	}
	public void setRootType(RootType rootType) {
		this.rootType = rootType;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getSeedConservationDuration() {
		return seedConservationDuration;
	}
	public void setSeedConservationDuration(int seedConservationDuration) {
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
	private int width;
	private int height;
	private SunshineNeeded sunshineNeeded;
	private String comments;
	private Set<String> nefariousNeighbors;
	private Set<String> beneficialNeighbors;
	private int lineSpacing;
	private int rowSpacing;
	private NutrientsNeeded nutrientsNeeded;
	private PHNeeded phNeeded;
	private int timeToSprout;
	private int temperatureToSprout;
	private int expectedYield;
	private Set<String> uses;
	private Set<String> medicalUses;
	private int minimalTemperature;
	private RootType rootType;
	private int depth;
	private int seedConservationDuration;
	
}
