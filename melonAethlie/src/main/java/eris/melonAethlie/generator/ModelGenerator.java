package eris.melonAethlie.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import eris.melonAethlie.Family;
import eris.melonAethlie.Plant;
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
import eris.melonAethlie.parser.MetaphysikPlantatorParser;

public class ModelGenerator {
	
	
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, OWLOntologyCreationException, OWLOntologyStorageException {
		List<Plant> plants = new MetaphysikPlantatorParser().read("./raw_mpp.xml");
		Set<Family> families = plants.stream().map(p -> readCleanFamilyName(p.getFamily())).collect(Collectors.toSet()).stream().map(f -> new Family(f)).collect(Collectors.toSet());
		Map<String, Plant> plantIndex = new HashMap<>();
		plants.forEach(p  -> plantIndex.put(p.getName(), p));
		Map<String, Family> familyIndex = new HashMap<>(); 
		families.forEach(f -> familyIndex.put(f.getName(), f));
		plants.forEach(p -> familyIndex.get(readCleanFamilyName(p.getFamily())).getPlants().add(p));
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		OWLOntology owlOntology = owlManager.createOntology(IRI.create("http://www.eris.org/melonAethlie"));
		OWLDataFactory owlFactory = owlManager.getOWLDataFactory();
		PrefixManager owlPrefixManager = new DefaultPrefixManager("http://www.eris.org/melonAethlie#"); 
		OWLDatatype owlInteger = owlFactory.getOWLDatatype(OWL2Datatype.XSD_INTEGER);
		OWLClass owlPlantClass = owlFactory.getOWLClass(":plant", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlPlantClass));
		
				
		OWLClass owlMedicalUses = owlFactory.getOWLClass(":" + "MedicinalUse", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlMedicalUses));
		
		OWLObjectProperty hasMedicinalUse = owlFactory.getOWLObjectProperty(":" + "hasMedicinalUse", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMedicinalUse));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasMedicinalUse, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasMedicinalUse, owlMedicalUses));
		
		OWLObjectProperty hasUse = owlFactory.getOWLObjectProperty(":" + "hasUse", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasUse));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasUse, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasUse, owlMedicalUses));
		
		
		//specific plant class generation
		for (Family family : families) {
			OWLClass owlFamilyClass = owlFactory.getOWLClass(":" +family.getName(), owlPrefixManager);
			owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlFamilyClass));
			owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlFamilyClass, owlPlantClass));
			for (Plant genus : family.getPlants()) {
				OWLClass owlGenusClass = owlFactory.getOWLClass(":" + genus.getName(), owlPrefixManager);
				owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlGenusClass));
				owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlGenusClass, owlFamilyClass));
				if (genus.getMedicalUses() !=  null && ! genus.getMedicalUses().isEmpty()) {
					List<OWLClass> usages = new ArrayList<>();
					for (String medicUse : genus.getMedicalUses()) {
						OWLClass owlmedicUse = owlFactory.getOWLClass(":" + medicUse, owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlmedicUse));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlmedicUse, owlMedicalUses));
						usages.add(owlmedicUse);
					}
					OWLObjectProperty owlMedicalUse = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasMedicinalUse", owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlMedicalUse));
					OWLClassExpression range = owlFactory.getOWLObjectUnionOf(usages);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(owlMedicalUse, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(owlMedicalUse, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(owlMedicalUse, hasMedicinalUse));
				}
				if (genus.getUses() !=  null && ! genus.getUses().isEmpty()) {
					List<OWLClass> usages = new ArrayList<>();
					for (String use : genus.getUses()) {
						OWLClass owlGenusUse = owlFactory.getOWLClass(":" + use, owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlGenusUse));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlGenusUse, owlMedicalUses));
						usages.add(owlGenusUse);
					}
					OWLObjectProperty owlUse = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasUse", owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlUse));
					OWLClassExpression range = owlFactory.getOWLObjectUnionOf(usages);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(owlUse, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(owlUse, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(owlUse, hasUse));
				}
			}
			
		}
		
		//enums class generation
		Class[] enums = new Class[] {Action.class, EdiblePart.class, GroundType.class, Months.class, MultiplicationType.class, NutrientsNeeded.class, PerennialType.class, PHNeeded.class, RootType.class, SunshineNeeded.class}; 
		for(Class e : enums) {
			OWLClass owlEnumClass =  owlFactory.getOWLClass(":" + e.getSimpleName(), owlPrefixManager);
			owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlEnumClass));
			List<OWLClass> enumeratedValues =  new ArrayList<>();
			for(Object value : e.getEnumConstants()) {
				OWLClass owlEnumeratedValue = owlFactory.getOWLClass(":" + value.toString(), owlPrefixManager);
				owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlEnumeratedValue));
				owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlEnumeratedValue, owlEnumClass));
				enumeratedValues.add(owlEnumeratedValue);
			}
			owlManager.addAxiom(owlOntology, owlFactory.getOWLDisjointClassesAxiom(enumeratedValues.toArray(new OWLClass[] {})));
		}
		
		
		// properties generation
		OWLObjectProperty hasNefariousNeighbor = owlFactory.getOWLObjectProperty(":" + "hasNefariousNeighbor", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasNefariousNeighbor));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasNefariousNeighbor, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasNefariousNeighbor, owlPlantClass));

		OWLObjectProperty hasBeneficialNeighbor = owlFactory.getOWLObjectProperty(":" + "hasBeneficialNeighbor", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasBeneficialNeighbor));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasBeneficialNeighbor, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasBeneficialNeighbor, owlPlantClass));

		OWLObjectProperty hasEdibleParts = owlFactory.getOWLObjectProperty(":" + "hasEdibleParts", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasEdibleParts));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasEdibleParts, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasEdibleParts, owlFactory.getOWLClass(":" +  EdiblePart.class.getSimpleName(), owlPrefixManager)));
		
		OWLDataProperty hasExpectedYield = owlFactory.getOWLDataProperty(":" + "hasExpectedYield", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasExpectedYield));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasExpectedYield, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasExpectedYield, owlInteger));
		
		OWLObjectProperty hasGroundTypeNeeded = owlFactory.getOWLObjectProperty(":" + "hasGround", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasGroundTypeNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasGroundTypeNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasGroundTypeNeeded, owlFactory.getOWLClass(":" +  GroundType.class.getSimpleName(), owlPrefixManager)));
		
		OWLDataProperty hasHeight = owlFactory.getOWLDataProperty(":" + "hasHeight", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasHeight));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasHeight, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasHeight, owlInteger));
		
		OWLDataProperty hasDepth = owlFactory.getOWLDataProperty(":" + "hasDepth", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasDepth));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasDepth, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasDepth, owlInteger));
		
		OWLDataProperty hasLineSpacing = owlFactory.getOWLDataProperty(":" + "hasLineSpacing", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasLineSpacing));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasLineSpacing, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasLineSpacing, owlInteger));
		
		OWLDataProperty hasMinimalTemperature = owlFactory.getOWLDataProperty(":" + "hasMinimalTemperature", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMinimalTemperature));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasMinimalTemperature, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasMinimalTemperature, owlInteger));
		
		OWLObjectProperty hasMultiplicationType = owlFactory.getOWLObjectProperty(":" + "hasMultiplicationType", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMultiplicationType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasMultiplicationType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasMultiplicationType, owlFactory.getOWLClass(":" +  MultiplicationType.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasNutrientsNeeded = owlFactory.getOWLObjectProperty(":" + "hasNutrientsNeeded", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasNutrientsNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasNutrientsNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasNutrientsNeeded, owlFactory.getOWLClass(":" +  NutrientsNeeded.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasPerennialType = owlFactory.getOWLObjectProperty(":" + "hasPerennialType", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasPerennialType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasPerennialType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasPerennialType, owlFactory.getOWLClass(":" +  PerennialType.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasPHNeeded = owlFactory.getOWLObjectProperty(":" + "hasPHNeeded", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasPHNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasPHNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasPHNeeded, owlFactory.getOWLClass(":" +  PHNeeded.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasRootType = owlFactory.getOWLObjectProperty(":" + "hasRootType", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasRootType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasRootType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasRootType, owlFactory.getOWLClass(":" +  RootType.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasSunshineNeeded = owlFactory.getOWLObjectProperty(":" + "hasSunshineNeeded", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasSunshineNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasSunshineNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasSunshineNeeded, owlFactory.getOWLClass(":" +  SunshineNeeded.class.getSimpleName(), owlPrefixManager)));

		OWLDataProperty hasRowSpacing = owlFactory.getOWLDataProperty(":" + "hasRowSpacing", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasRowSpacing));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasRowSpacing, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasRowSpacing, owlInteger));

		OWLDataProperty hasSeedConservationDuration = owlFactory.getOWLDataProperty(":" + "hasSeedConservationDuration", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasSeedConservationDuration));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasSeedConservationDuration, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasSeedConservationDuration, owlInteger));

		OWLDataProperty hasTemperatureToSprout = owlFactory.getOWLDataProperty(":" + "hasTemperatureToSprout", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasTemperatureToSprout));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasTemperatureToSprout, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasTemperatureToSprout, owlInteger));

		OWLDataProperty hasTimeToSprout = owlFactory.getOWLDataProperty(":" + "hasTimeToSprout", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasTimeToSprout));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasTimeToSprout, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasTimeToSprout, owlInteger));

		OWLDataProperty hasWidth = owlFactory.getOWLDataProperty(":" + "hasWidth", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasWidth));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasWidth, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasWidth, owlInteger));
		
		//specific properties generation
		for (Family family : families) {
			for (Plant genus : family.getPlants()) {
				OWLClass owlGenusClass = owlFactory.getOWLClass(":" + genus.getName(), owlPrefixManager);
				if (genus.getNefariousNeighbors() !=  null && !genus.getNefariousNeighbors().isEmpty()) {
					Set<Plant> nefariousNeighbors = genus.getNefariousNeighbors().stream().map(s -> plantIndex.get(s)).collect(Collectors.toSet());
					nefariousNeighbors.remove(null);
					if (!nefariousNeighbors.isEmpty()) {
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(nefariousNeighbors.stream().map(p -> owlFactory.getOWLClass(":" + p.getName(), owlPrefixManager)).collect(Collectors.toSet()));
						OWLObjectProperty plantHasNefariousNeighbor = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasNefariousNeighbor", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(plantHasNefariousNeighbor));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(plantHasNefariousNeighbor, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(plantHasNefariousNeighbor, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(plantHasNefariousNeighbor, hasNefariousNeighbor));
					}
					
				}
				
				if (genus.getDepth() != null) {
					OWLDataProperty genusHasDepth =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasDepth", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getDepth()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasDepth));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasDepth, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasDepth, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasDepth, hasDepth));
				}
				
				if (genus.getGround() != null) {
					OWLObjectProperty genusHasGroundType = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasGroundTypeNeeded");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getGround().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasGroundType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasGroundType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasGroundType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasGroundType, hasGroundTypeNeeded));
				}
				
				if (genus.getSunshineNeeded() != null) {
					OWLObjectProperty genusHasSunshineNeeded = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasSunshineNeeded");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getSunshineNeeded().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasSunshineNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasSunshineNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasSunshineNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasSunshineNeeded, hasSunshineNeeded));
				}
				
				if (genus.getNutrientsNeeded() != null) {
					OWLObjectProperty genusHasNutrientsNeeded = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasNutrientsNeeded");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getNutrientsNeeded().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasNutrientsNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasNutrientsNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasNutrientsNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasNutrientsNeeded, hasNutrientsNeeded));
				}
				
				if (genus.getPerennial() != null) {
					OWLObjectProperty genusHasPerennialType = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasPerennialType");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getPerennial().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasPerennialType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasPerennialType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasPerennialType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasPerennialType, hasPerennialType));
				}
				
				if (genus.getPhNeeded() != null) {
					OWLObjectProperty genusHasPHNeeded = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasPHNeeded");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getPhNeeded().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasPHNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasPHNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasPHNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasPHNeeded, hasPHNeeded));
				}
				
				if (genus.getRootType() != null) {
					OWLObjectProperty genusHasRootType = owlFactory.getOWLObjectProperty(":" + genus.getName() +"_hasRootType");
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getRootType().toString());
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasRootType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasRootType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasRootType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasRootType, hasRootType));
				}
				
				if(genus.getEdibleParts() != null) {
					genus.getEdibleParts().remove(null);
					if (!genus.getEdibleParts().isEmpty()) {
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(genus.getEdibleParts().stream().map(ep -> owlFactory.getOWLClass(":" + ep.toString(), owlPrefixManager)).collect(Collectors.toSet()));
						OWLObjectProperty genusHasEdiblePart = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasEdiblePart", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasEdiblePart));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasEdiblePart, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasEdiblePart, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasEdiblePart, hasEdibleParts));
					}
				}
				
				if(genus.getMultiplications() != null) {
					genus.getMultiplications().remove(null);
					if (!genus.getMultiplications().isEmpty()) {
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(genus.getMultiplications().stream().map(ep -> owlFactory.getOWLClass(":" + ep.toString(), owlPrefixManager)).collect(Collectors.toSet()));
						OWLObjectProperty genusHasEdiblePart = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasEdiblePart", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasEdiblePart));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasEdiblePart, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasEdiblePart, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasEdiblePart, hasMultiplicationType));
					}
				}

				if (genus.getExpectedYield() != null) {
					OWLDataProperty genusHasExpectedYield =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasExpectedYield", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getExpectedYield()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasExpectedYield));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasExpectedYield, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasExpectedYield, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasExpectedYield, hasExpectedYield));
				}
				
				if (genus.getHeight() != null) {
					OWLDataProperty genusHasHeight =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasHeight", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getHeight()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasHeight));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasHeight, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasHeight, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasHeight, hasHeight));
				}
				
				if (genus.getLineSpacing() != null) {
					OWLDataProperty genusHasLineSpacing =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasLineSpacing", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getLineSpacing()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasLineSpacing));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasLineSpacing, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasLineSpacing, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasLineSpacing, hasLineSpacing));
				}
				
				if (genus.getMinimalTemperature() != null) {
					OWLDataProperty genusHasMinimalTemperature =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasMinimalTemperature", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getMinimalTemperature()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasMinimalTemperature));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasMinimalTemperature, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasMinimalTemperature, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasMinimalTemperature, hasMinimalTemperature));
				}
				
				if (genus.getRowSpacing() != null) {
					OWLDataProperty genusHasRowSpacing =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasRowSpacing", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getRowSpacing()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasRowSpacing));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasRowSpacing, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasRowSpacing, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasRowSpacing, hasRowSpacing));
				}
				
				if (genus.getSeedConservationDuration() != null) {
					OWLDataProperty genusHasSeedConservationDuration =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasSeedConservationDuration", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getSeedConservationDuration()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasSeedConservationDuration));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasSeedConservationDuration, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasSeedConservationDuration, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasSeedConservationDuration, hasSeedConservationDuration));
				}
				
				if (genus.getTemperatureToSprout() != null) {
					OWLDataProperty genusHasTemperatureToSprout =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasTemperatureToSprout", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getTemperatureToSprout()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasTemperatureToSprout));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasTemperatureToSprout, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasTemperatureToSprout, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasTemperatureToSprout, hasTemperatureToSprout));
				}
				
				if (genus.getTimeToSprout() != null) {
					OWLDataProperty genusHasTimeToSprout =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasTimeToSprout", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getTimeToSprout()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasTimeToSprout));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasTimeToSprout, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasTimeToSprout, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasTimeToSprout, hasTimeToSprout));
				}
				
				if (genus.getWidth() != null) {
					OWLDataProperty genusHasWidth =  owlFactory.getOWLDataProperty(":" + genus.getName() +"_hasWidth", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getWidth()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasWidth));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasWidth, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasWidth, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasWidth, hasWidth));
				}
				
				if (genus.getBeneficialNeighbors() !=  null && !genus.getBeneficialNeighbors().isEmpty()) {
					Set<Plant> beneficialNeighbors = genus.getBeneficialNeighbors().stream().map(s -> plantIndex.get(s)).collect(Collectors.toSet());
					beneficialNeighbors.remove(null);
					if ( ! beneficialNeighbors.isEmpty()) {
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(beneficialNeighbors.stream().map(p -> owlFactory.getOWLClass(":" + p.getName(), owlPrefixManager)).collect(Collectors.toSet()));
						OWLObjectProperty plantHasBeneficialNeighbor = owlFactory.getOWLObjectProperty(":" + genus.getName() + "_hasBeneficialNeighbor", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(plantHasBeneficialNeighbor));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(plantHasBeneficialNeighbor, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(plantHasBeneficialNeighbor, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(plantHasBeneficialNeighbor, hasBeneficialNeighbor));
					}
				}
			}
		}
		
		owlManager.saveOntology(owlOntology, new FileOutputStream("eris.owl"));
		
	}

	public static String readCleanFamilyName(String familyName) {
		String clean = removeAccents(familyName);
		if (clean != null) clean = clean.toLowerCase();
		if (clean != null  && clean.charAt(clean.length() -1) == 's') clean = clean.substring(0, clean.length() - 1);
		return clean;
	}
	
	public static String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}
