package eris.melonAethlie.model;

import java.util.HashSet;
import java.util.Set;

public class Family {
	
	private String name;
	private Set<Plant> plants;
	
	public Family(String name) {
		this.name = name;
		plants = new HashSet<Plant>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Plant> getPlants() {
		return plants;
	}

	public void setPlants(Set<Plant> plants) {
		this.plants = plants;
	}

}
