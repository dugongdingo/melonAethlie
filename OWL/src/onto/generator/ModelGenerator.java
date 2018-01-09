package onto.generator;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import onto.htmlreader.MetaphysikPlantatorParser;

public class ModelGenerator {
	
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
		List<Plant> plants = new MetaphysikPlantatorParser().read("/home/timothee/Bureau/re_re_out.xml");
		Set<Family> families = plants.stream().map(p -> p.getFamily()).collect(Collectors.toSet()).stream().map(f -> new Family(f)).collect(Collectors.toSet());
		Map<String, Family> familyIndex = new HashMap<>(); 
		families.forEach(f -> familyIndex.put(f.getName(), f));
		plants.forEach(p -> familyIndex.get(p.getFamily()).getPlants().add(p));
		
	}

}
