package eris.melonAethlie.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import eris.melonAethlie.Family;
import eris.melonAethlie.Plant;
import eris.melonAethlie.parser.MetaphysikPlantatorParser;

public class ModelGenerator {
	
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, OWLOntologyCreationException, OWLOntologyStorageException {
		List<Plant> plants = new MetaphysikPlantatorParser().read("/home/timothee/Bureau/re_re_out.xml");
		Set<Family> families = plants.stream().map(p -> p.getFamily()).collect(Collectors.toSet()).stream().map(f -> new Family(f)).collect(Collectors.toSet());
		Map<String, Family> familyIndex = new HashMap<>(); 
		families.forEach(f -> familyIndex.put(f.getName(), f));
		plants.forEach(p -> familyIndex.get(p.getFamily()).getPlants().add(p));
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		owlManager.addIRIMapper(new AutoIRIMapper(new File("eris"), true));
		OWLOntology owlOntology = owlManager.createOntology(IRI.create("http://www.eris.org/melonAethlie"));
		OWLDataFactory owlFactory = owlManager.getOWLDataFactory();
		PrefixManager owlPrefixManager = new DefaultPrefixManager("http://www.eris.org/melonAethlie#"); 
		OWLClass owlPlantClass = owlFactory.getOWLClass(":Plant", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlPlantClass));
		for (Family family : families) {
			OWLClass owlFamilyClass = owlFactory.getOWLClass(":" +family.getName(), owlPrefixManager);
			owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlFamilyClass));
			owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlPlantClass, owlFamilyClass));
			for (Plant genus : family.getPlants()) {
				OWLClass owlGenusClass = owlFactory.getOWLClass(":" + genus.getName(), owlPrefixManager);
				owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlGenusClass));
				owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlFamilyClass, owlGenusClass));
			}
		}
		owlManager.saveOntology(owlOntology, new FileOutputStream("eris"));
		
	}

}
