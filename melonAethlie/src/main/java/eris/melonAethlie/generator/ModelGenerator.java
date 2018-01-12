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

import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import eris.melonAethlie.Family;
import eris.melonAethlie.Plant;
import eris.melonAethlie.enums.Action;
import eris.melonAethlie.enums.BesoinEnSoleil;
import eris.melonAethlie.enums.BesoinsEnNutriments;
import eris.melonAethlie.enums.BesoinsEnPH;
import eris.melonAethlie.enums.CaractereVivace;
import eris.melonAethlie.enums.Mois;
import eris.melonAethlie.enums.Multiplication;
import eris.melonAethlie.enums.PartieComestible;
import eris.melonAethlie.enums.TypeDeRacine;
import eris.melonAethlie.enums.TypeDeSol;
import eris.melonAethlie.parser.MetaphysikPlantatorParser;

public class ModelGenerator {
	
	public static String capitalize(String toCaps) {
		if (toCaps == null) return null;
		toCaps = toCaps.toLowerCase();
		String acc = "";
		for (String word : toCaps.split(" ")) {
			word = word.trim();
			if (word.length() > 1) word = word.substring(0, 1).toUpperCase() + word.substring(1);
			else if (word.length() == 1) word = word.toUpperCase();
			acc += word;
		}
		return acc.trim().equals("") ? null : acc.trim();
	}
	
	public static String lowerCaseFirst(String toLower) {
		if (toLower == null) return null;
		return toLower.substring(0,1).toLowerCase() + toLower.substring(1); 
	}
	
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, OWLOntologyCreationException, OWLOntologyStorageException {
		List<Plant> plants = new MetaphysikPlantatorParser().read("./raw_mpp.xml");
		Set<Family> families = plants.stream().map(p -> readCleanFamilyName(p.getFamily())).collect(Collectors.toSet()).stream().map(f -> new Family(f)).collect(Collectors.toSet());
		Map<String, Plant> plantIndex = new HashMap<>();
		plants.forEach(p  -> plantIndex.put(p.getName(), p));
		plantIndex.remove(null);
		Map<String, Family> familyIndex = new HashMap<>(); 
		families.forEach(f -> familyIndex.put(f.getName(), f));
		plants.forEach(p -> familyIndex.get(readCleanFamilyName(p.getFamily())).getPlants().add(p));
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		OWLOntology owlOntology = owlManager.createOntology(IRI.create("http://www.eris.org/melonAethlie"));
		OWLDataFactory owlFactory = owlManager.getOWLDataFactory();
		PrefixManager owlPrefixManager = new DefaultPrefixManager("http://www.eris.org/melonAethlie#"); 
		OWLDatatype owlInteger = owlFactory.getOWLDatatype(OWL2Datatype.XSD_INTEGER);
		OWLClass owlPlantClass = owlFactory.getOWLClass(":Plante", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlPlantClass));
				
		OWLClass owlMedicalUses = owlFactory.getOWLClass(":" + "UsageMedicinal", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlMedicalUses));
		
		OWLClass owlUses = owlFactory.getOWLClass(":" + "Utilite", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlMedicalUses));
		
		OWLObjectProperty hasMedicinalUse = owlFactory.getOWLObjectProperty(":" + "aUsageMedicinal", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMedicinalUse));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasMedicinalUse, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasMedicinalUse, owlMedicalUses));
		
		OWLObjectProperty hasUse = owlFactory.getOWLObjectProperty(":" + "aUtilite", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasUse));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasUse, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasUse, owlMedicalUses));
		
		
		//specific plant class generation
		for (Family family : families) {
			OWLClass owlFamilyClass = owlFactory.getOWLClass(":" +family.getName(), owlPrefixManager);
			owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlFamilyClass));
			owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlFamilyClass, owlPlantClass));
			plants : for (Plant genus : family.getPlants()) {
				if (genus.getName() == null) continue plants;
				OWLClass owlGenusClass = owlFactory.getOWLClass(":" + genus.getName(), owlPrefixManager);
				owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlGenusClass));
				owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlGenusClass, owlFamilyClass));
				if (genus.getPrefLabel() != null) {
					owlManager.addAxiom(owlOntology, owlFactory.getOWLAnnotationAssertionAxiom(
							owlFactory.getOWLAnnotationProperty(SKOS.PREF_LABEL.stringValue()),
							owlGenusClass.getIRI(),
							owlFactory.getOWLLiteral(genus.getPrefLabel())));
				}
				owlManager.addAxiom(owlOntology, owlFactory.getOWLAnnotationAssertionAxiom(
						owlFactory.getOWLAnnotationProperty(RDFS.COMMENT.stringValue()),
						owlGenusClass.getIRI(),
						owlFactory.getOWLLiteral(genus.getDefaultAnnotation())));

				if (genus.getAltLabel() != null) {
					owlManager.addAxiom(owlOntology, owlFactory.getOWLAnnotationAssertionAxiom(
							owlFactory.getOWLAnnotationProperty(SKOS.ALT_LABEL.stringValue()),
							owlGenusClass.getIRI(),
							owlFactory.getOWLLiteral(genus.getAltLabel())));
				}

				if (genus.getMedicalUses() !=  null && ! genus.getMedicalUses().isEmpty()) {
					genus.getMedicalUses().remove(null);
					if (!genus.getMedicalUses().isEmpty()) {
						List<OWLClass> usages = new ArrayList<>();
						for (String medicUse : genus.getMedicalUses()) {
							OWLClass owlmedicUse = owlFactory.getOWLClass(":" + capitalize(medicUse), owlPrefixManager);
							owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlmedicUse));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlmedicUse, owlMedicalUses));
							usages.add(owlmedicUse);
						}
						OWLObjectProperty owlMedicalUse = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "AUsageMedicinal", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlMedicalUse));
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(usages);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(owlMedicalUse, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(owlMedicalUse, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(owlMedicalUse, hasMedicinalUse));
					}
				}
				if (genus.getUses() !=  null && ! genus.getUses().isEmpty()) {
					genus.getUses().remove(null);
					if (!genus.getUses().isEmpty()) {
						List<OWLClass> usages = new ArrayList<>();
						for (String use : genus.getUses()) {
							OWLClass owlGenusUse = owlFactory.getOWLClass(":" + capitalize(use), owlPrefixManager);
							owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlGenusUse));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLSubClassOfAxiom(owlGenusUse, owlUses));
							usages.add(owlGenusUse);
						}
						OWLObjectProperty owlUse = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "AUtilite", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(owlUse));
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(usages);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(owlUse, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(owlUse, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(owlUse, hasUse));
					}
				}
			}
		}
		
		//enums class generation
		Class[] enums = new Class[] {PartieComestible.class, TypeDeSol.class, Mois.class, Multiplication.class, BesoinsEnNutriments.class, CaractereVivace.class, BesoinsEnPH.class, TypeDeRacine.class, BesoinEnSoleil.class}; 
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
		
		OWLClass owlMonthClass =  owlFactory.getOWLClass(":" + Mois.class.getSimpleName(), owlPrefixManager);

		// properties generation
		OWLObjectProperty isSownIn = owlFactory.getOWLObjectProperty(":estSemeEn", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(isSownIn));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(isSownIn, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(isSownIn, owlMonthClass));

		OWLObjectProperty isHarvestedIn = owlFactory.getOWLObjectProperty(":estRecolteEn", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(isHarvestedIn));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(isHarvestedIn, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(isHarvestedIn, owlMonthClass));
		
		OWLObjectProperty hasNefariousNeighbor = owlFactory.getOWLObjectProperty(":" + "aPourMauvaisVoisin", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasNefariousNeighbor));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasNefariousNeighbor, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasNefariousNeighbor, owlPlantClass));

		OWLObjectProperty hasBeneficialNeighbor = owlFactory.getOWLObjectProperty(":" + "aPourBonVoisin", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasBeneficialNeighbor));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasBeneficialNeighbor, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasBeneficialNeighbor, owlPlantClass));

		OWLObjectProperty hasEdibleParts = owlFactory.getOWLObjectProperty(":" + "aPourPartieComestible", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasEdibleParts));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasEdibleParts, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasEdibleParts, owlFactory.getOWLClass(":" +  PartieComestible.class.getSimpleName(), owlPrefixManager)));
		
		OWLDataProperty hasExpectedYield = owlFactory.getOWLDataProperty(":" + "aPourRendementAttendu", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasExpectedYield));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasExpectedYield, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasExpectedYield, owlInteger));
		
		OWLObjectProperty hasGroundTypeNeeded = owlFactory.getOWLObjectProperty(":" + "aBesoinCommeTypeDeSol", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasGroundTypeNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasGroundTypeNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasGroundTypeNeeded, owlFactory.getOWLClass(":" +  TypeDeSol.class.getSimpleName(), owlPrefixManager)));
		
		OWLDataProperty hasHeight = owlFactory.getOWLDataProperty(":" + "aPourHauteur", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasHeight));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasHeight, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasHeight, owlInteger));
		
		OWLDataProperty hasDepth = owlFactory.getOWLDataProperty(":" + "aPourProfondeur", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasDepth));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasDepth, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasDepth, owlInteger));
		
		OWLDataProperty hasLineSpacing = owlFactory.getOWLDataProperty(":" + "aPourEspacementDansLaLigne", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasLineSpacing));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasLineSpacing, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasLineSpacing, owlInteger));
		
		OWLDataProperty hasMinimalTemperature = owlFactory.getOWLDataProperty(":" + "aPourTemperatureMinimale", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMinimalTemperature));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasMinimalTemperature, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasMinimalTemperature, owlInteger));
		
		OWLObjectProperty hasMultiplicationType = owlFactory.getOWLObjectProperty(":" + "seMultipliePar", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasMultiplicationType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasMultiplicationType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasMultiplicationType, owlFactory.getOWLClass(":" +  Multiplication.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasNutrientsNeeded = owlFactory.getOWLObjectProperty(":" + "aPourBesoinEnNutriments", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasNutrientsNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasNutrientsNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasNutrientsNeeded, owlFactory.getOWLClass(":" +  BesoinsEnNutriments.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasPerennialType = owlFactory.getOWLObjectProperty(":" + "aPourCaractereVivace", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasPerennialType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasPerennialType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasPerennialType, owlFactory.getOWLClass(":" +  CaractereVivace.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasPHNeeded = owlFactory.getOWLObjectProperty(":" + "aBesoinCommePH", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasPHNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasPHNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasPHNeeded, owlFactory.getOWLClass(":" +  BesoinsEnPH.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasRootType = owlFactory.getOWLObjectProperty(":" + "aPourTypeDeRacine", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasRootType));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasRootType, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasRootType, owlFactory.getOWLClass(":" +  TypeDeRacine.class.getSimpleName(), owlPrefixManager)));
		
		OWLObjectProperty hasSunshineNeeded = owlFactory.getOWLObjectProperty(":" + "aPourBesoinEnEnsoleillement", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasSunshineNeeded));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(hasSunshineNeeded, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(hasSunshineNeeded, owlFactory.getOWLClass(":" +  BesoinEnSoleil.class.getSimpleName(), owlPrefixManager)));

		OWLDataProperty hasRowSpacing = owlFactory.getOWLDataProperty(":" + "aPourEspacementDansLeRang", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasRowSpacing));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasRowSpacing, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasRowSpacing, owlInteger));

		OWLDataProperty hasSeedConservationDuration = owlFactory.getOWLDataProperty(":" + "aPourDureeDeConservationEnTantQueGraine", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasSeedConservationDuration));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasSeedConservationDuration, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasSeedConservationDuration, owlInteger));

		OWLDataProperty hasTemperatureToSprout = owlFactory.getOWLDataProperty(":" + "aPourTemperatureDeLeveeDesGraines", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasTemperatureToSprout));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasTemperatureToSprout, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasTemperatureToSprout, owlInteger));

		OWLDataProperty hasTimeToSprout = owlFactory.getOWLDataProperty(":" + "aPourTempsDeLeverDesGraines", owlPrefixManager);
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(hasTimeToSprout));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(hasTimeToSprout, owlPlantClass));
		owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(hasTimeToSprout, owlInteger));

		OWLDataProperty hasWidth = owlFactory.getOWLDataProperty(":" + "aPourLargeur", owlPrefixManager);
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
						OWLObjectProperty plantHasNefariousNeighbor = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "APourMauvaisVoisin", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(plantHasNefariousNeighbor));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(plantHasNefariousNeighbor, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(plantHasNefariousNeighbor, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(plantHasNefariousNeighbor, hasNefariousNeighbor));
					}
					
				}
				
				if (genus.getCalendar() != null)
					for (Action action : genus.getCalendar().keySet())
						if (action == Action.HARVEST && genus.getCalendar().get(action) != null && !genus.getCalendar().get(action).isEmpty()) {
							OWLObjectProperty genusIsHarvestedIn = owlFactory.getOWLObjectProperty(":"+ genus.getName() + "EstRecolteEn", owlPrefixManager);
							OWLClassExpression range = owlFactory.getOWLObjectUnionOf(genus.getCalendar().get(action).stream().map(m -> owlFactory.getOWLClass(":" + m.toString(), owlPrefixManager)).collect(Collectors.toSet()));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusIsHarvestedIn));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusIsHarvestedIn, owlGenusClass));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusIsHarvestedIn, range));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusIsHarvestedIn, isHarvestedIn));
						}
						else if (action == Action.SOW && genus.getCalendar().get(action) != null  &&!genus.getCalendar().get(action).isEmpty()) {
							OWLObjectProperty genusIsSownIn = owlFactory.getOWLObjectProperty(":"+ genus.getName() + "EstSemeEn", owlPrefixManager);
							OWLClassExpression range = owlFactory.getOWLObjectUnionOf(genus.getCalendar().get(action).stream().map(m -> owlFactory.getOWLClass(":" + m.toString(), owlPrefixManager)).collect(Collectors.toSet()));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusIsSownIn));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusIsSownIn, owlGenusClass));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusIsSownIn, range));
							owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusIsSownIn, isSownIn));
						}
				
				if (genus.getDepth() != null) {
					OWLDataProperty genusHasDepth =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourProfondeur", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getDepth()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasDepth));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasDepth, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasDepth, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasDepth, hasDepth));
				}
				
				if (genus.getGround() != null) {
					OWLObjectProperty genusHasGroundType = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"ACommeBesoinEnTypeDeSol", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getGround().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasGroundType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasGroundType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasGroundType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasGroundType, hasGroundTypeNeeded));
				}
				
				if (genus.getSunshineNeeded() != null) {
					OWLObjectProperty genusHasSunshineNeeded = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"APourBesoinEnEnsoleillement", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getSunshineNeeded().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasSunshineNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasSunshineNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasSunshineNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasSunshineNeeded, hasSunshineNeeded));
				}
				
				if (genus.getNutrientsNeeded() != null) {
					OWLObjectProperty genusHasNutrientsNeeded = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"APourBesoinEnNutriments", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getNutrientsNeeded().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasNutrientsNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasNutrientsNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasNutrientsNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasNutrientsNeeded, hasNutrientsNeeded));
				}
				
				if (genus.getPerennial() != null) {
					OWLObjectProperty genusHasPerennialType = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"APourCaractereVivace", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getPerennial().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasPerennialType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasPerennialType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasPerennialType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasPerennialType, hasPerennialType));
				}
				
				if (genus.getPhNeeded() != null) {
					OWLObjectProperty genusHasPHNeeded = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"ABesoinCommePH", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":"+genus.getPhNeeded().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasPHNeeded));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasPHNeeded, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasPHNeeded, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasPHNeeded, hasPHNeeded));
				}
				
				if (genus.getRootType() != null) {
					OWLObjectProperty genusHasRootType = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) +"APourTypeDeRacine", owlPrefixManager);
					OWLClass range =  owlFactory.getOWLClass(":" + genus.getRootType().toString(), owlPrefixManager);
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasRootType));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasRootType, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasRootType, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasRootType, hasRootType));
				}
				
				if(genus.getEdibleParts() != null) {
					genus.getEdibleParts().remove(null);
					if (!genus.getEdibleParts().isEmpty()) {
						OWLClassExpression range = owlFactory.getOWLObjectUnionOf(genus.getEdibleParts().stream().map(ep -> owlFactory.getOWLClass(":" + ep.toString(), owlPrefixManager)).collect(Collectors.toSet()));
						OWLObjectProperty genusHasEdiblePart = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "APourPartieComestible", owlPrefixManager);
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
						OWLObjectProperty genusHasEdiblePart = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "SeMultipliePar", owlPrefixManager);
						owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasEdiblePart));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyDomainAxiom(genusHasEdiblePart, owlGenusClass));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLObjectPropertyRangeAxiom(genusHasEdiblePart, range));
						owlManager.addAxiom(owlOntology, owlFactory.getOWLSubObjectPropertyOfAxiom(genusHasEdiblePart, hasMultiplicationType));
					}
				}

				if (genus.getExpectedYield() != null) {
					OWLDataProperty genusHasExpectedYield =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourRendementAttendu", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getExpectedYield()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasExpectedYield));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasExpectedYield, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasExpectedYield, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasExpectedYield, hasExpectedYield));
				}
				
				if (genus.getHeight() != null) {
					OWLDataProperty genusHasHeight =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourHauteur", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getHeight()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasHeight));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasHeight, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasHeight, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasHeight, hasHeight));
				}
				
				if (genus.getLineSpacing() != null) {
					OWLDataProperty genusHasLineSpacing =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourEspacementDansLaLigne", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getLineSpacing()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasLineSpacing));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasLineSpacing, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasLineSpacing, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasLineSpacing, hasLineSpacing));
				}
				
				if (genus.getMinimalTemperature() != null) {
					OWLDataProperty genusHasMinimalTemperature =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourTemperatureMinimale", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getMinimalTemperature()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasMinimalTemperature));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasMinimalTemperature, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasMinimalTemperature, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasMinimalTemperature, hasMinimalTemperature));
				}
				
				if (genus.getRowSpacing() != null) {
					OWLDataProperty genusHasRowSpacing =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourEspacementDansLeRang", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getRowSpacing()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasRowSpacing));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasRowSpacing, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasRowSpacing, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasRowSpacing, hasRowSpacing));
				}
				
				if (genus.getSeedConservationDuration() != null) {
					OWLDataProperty genusHasSeedConservationDuration =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourDureeDeConservationEnTantQueGraine", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getSeedConservationDuration()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasSeedConservationDuration));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasSeedConservationDuration, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasSeedConservationDuration, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasSeedConservationDuration, hasSeedConservationDuration));
				}
				
				if (genus.getTemperatureToSprout() != null) {
					OWLDataProperty genusHasTemperatureToSprout =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourTemperatureDeLeveeDesGraines", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getTemperatureToSprout()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasTemperatureToSprout));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasTemperatureToSprout, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasTemperatureToSprout, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasTemperatureToSprout, hasTemperatureToSprout));
				}
				
				if (genus.getTimeToSprout() != null) {
					OWLDataProperty genusHasTimeToSprout =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourTempsDeLeveeDesGraines", owlPrefixManager);
					OWLDataRange range = owlFactory.getOWLDataOneOf(owlFactory.getOWLLiteral(genus.getTimeToSprout()));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDeclarationAxiom(genusHasTimeToSprout));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyDomainAxiom(genusHasTimeToSprout, owlGenusClass));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLDataPropertyRangeAxiom(genusHasTimeToSprout, range));
					owlManager.addAxiom(owlOntology, owlFactory.getOWLSubDataPropertyOfAxiom(genusHasTimeToSprout, hasTimeToSprout));
				}
				
				if (genus.getWidth() != null) {
					OWLDataProperty genusHasWidth =  owlFactory.getOWLDataProperty(":" + lowerCaseFirst(genus.getName()) +"APourLargeur", owlPrefixManager);
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
						OWLObjectProperty plantHasBeneficialNeighbor = owlFactory.getOWLObjectProperty(":" + lowerCaseFirst(genus.getName()) + "APourBonVoisin", owlPrefixManager);
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
		if (clean != null) clean = capitalize(familyName);
		if (clean != null  && clean.charAt(clean.length() -1) == 's') clean = clean.substring(0, clean.length() - 1);
		return clean;
	}
	
	public static String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}
