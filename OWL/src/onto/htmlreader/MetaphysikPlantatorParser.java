package onto.htmlreader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import onto.generator.Plant;
import onto.htmlreader.enums.Action;
import onto.htmlreader.enums.DivType;
import onto.htmlreader.enums.Months;

public class MetaphysikPlantatorParser {
	
	private XMLInputFactory xif;
	
	public MetaphysikPlantatorParser() {
		this.xif = XMLInputFactory.newInstance();
	}
	
	public List<Plant> read(final String filename) throws FileNotFoundException, XMLStreamException {
		XMLStreamReader xsr = xif.createXMLStreamReader(new FileReader(filename));
		Plant currentPlant = null;
		List<Plant> plants = new ArrayList<>();
		
		while (xsr.hasNext()) {
			switch(xsr.next()) {
			case XMLStreamConstants.START_ELEMENT :
				if (xsr.getLocalName().equals("tr")) currentPlant = new Plant();
				if (xsr.getLocalName().equals("div")) readFromDiv(xsr, currentPlant); 
				if (xsr.getLocalName().equals("section")) readCalendar(xsr, currentPlant);
				break;
			case XMLStreamConstants.END_ELEMENT :
				if (xsr.getLocalName().equals("tr")) {
					plants.add(currentPlant);
					currentPlant = null;
				}
				break;
			}
		}
		return plants.isEmpty() ? null : plants;
	}

	private void readFromDiv(final XMLStreamReader xsr, final Plant plant) throws XMLStreamException {
		if (xsr.getAttributeCount() > 0)
			for (int i = 0; i < xsr.getAttributeCount(); i++) {
				if (xsr.getAttributeLocalName(i).equals("title")) {
					DivType.feedFromTitle(plant, xsr.getAttributeValue(i));
					return;
				}
			}
	}

	private void readCalendar(final XMLStreamReader xsr, final Plant plant) throws XMLStreamException {
		plant.setCalendar(new EnumMap<Months, List<Action>>(Months.class));
		int i = -1;
		while (xsr.hasNext()) {
			switch(xsr.next()) {
			case XMLStreamConstants.END_ELEMENT :
				if (xsr.getLocalName().equals("section")) return;
				break;
			case XMLStreamConstants.START_ELEMENT :
				if (xsr.getLocalName().equals("img")) {
					List<Action> actions = plant.getCalendar().get(Months.values()[i]);
					if (actions == null) {
						actions = new ArrayList<>();
						plant.getCalendar().put(Months.values()[i], actions);
					}
					for (int j = 0; j < xsr.getAttributeCount(); j++)
						if (xsr.getAttributeLocalName(j).equals("alt")) {
							Action action = xsr.getAttributeValue(j).equals("semisext") ? Action.SOW : xsr.getAttributeValue(j).equals("recolte") ? Action.HARVEST : null;
							if (action != null) actions.add(action);
						}
				}
				break;
			case XMLStreamConstants.CHARACTERS :
				if(!xsr.getText().trim().equals("")) i++;
				break;
			}
			
		}
	}

}
