/**
 * 
 */
package client.airplanes;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author James
 * @version 1
 * @since 2016-07-20
 *
 */
public class Airplanes extends ArrayList<Airplane> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds collection of airplanes from airplanes described in XML
	 * 
	 * Parses an XML string to read each of the airplanes and adds each valid airplane 
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives.
	 * 
	 * @param xmlAirplanes XML string containing set of airplanes 
	 * @return true if the collection was modified, false otherwise
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @preconditions the xmlAirplanes string adheres to the format specified by the server API
	 * @postconditions the [possibly empty] set of Airplanes in the XML string are added to collection
	 */
	public boolean addAll (String xmlAirplanes) throws NullPointerException {
		
		boolean collectionUpdated = false;
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airplane to our collection
		Document docAirplanes = buildDomDoc (xmlAirplanes);
		NodeList nodesAirplanes = docAirplanes.getElementsByTagName("Airplane");
		
		for (int i = 0; i < nodesAirplanes.getLength(); i++) {
			Element elementAirplane = (Element) nodesAirplanes.item(i);
			Airplane airplane = buildAirplane (elementAirplane);
			
			if (airplane.isValid()) {
				this.add(airplane);
				collectionUpdated = true;
			}
		}
		
		return collectionUpdated;
	}
	
	/**
	 * Builds a DOM tree form an XML string
	 * 
	 * Parses the XML file and returns a DOM tree that can be processed
	 * 
	 * @param xmlString XML String containing set of objects
	 * @return DOM tree from parsed XML or null if exception is caught
	 */
	private Document buildDomDoc (String xmlString) {
		/**
		 * load the xml string into a DOM document and return the Document
		 */
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xmlString));
			
			return docBuilder.parse(inputSource);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates an Airplane object form a DOM node
	 * 
	 * Processes a DOM Node that describes an Airplane and creates an Airplane object from the information
	 * @param nodeAirplane is a DOM Node describing an Airplane
	 * @return Airplane object created from the DOM Node representation of the Airplane
	 * 
	 * @preconditions nodeAirplane is of format specified by CS509 server API
	 */
	private Airplane buildAirplane (Node nodeAirplane) {
		/**
		 * Instantiate an empty Airplane object
		 */
		Airplane airplane = new Airplane();

		String manufacturer;
		String model;
		int firstClassSeats;
		int coachSeats;
		
		// The airplane element has attributes of Manufacturer and Model
		Element elementAirplane = (Element) nodeAirplane;
		manufacturer = elementAirplane.getAttributeNode("Manufacturer").getValue();
		model = elementAirplane.getAttributeNode("Model").getValue();
		
		// The firstClassSeats and coachSeats are child elements
		Element seat;
		seat = (Element)elementAirplane.getElementsByTagName("FirstClassSeats").item(0);
		firstClassSeats = Integer.parseInt(getCharacterDataFromElement(seat));
		
		seat = (Element)elementAirplane.getElementsByTagName("CoachSeats").item(0);
		coachSeats = Integer.parseInt(getCharacterDataFromElement(seat));

		/**
		 * Update the Airplane object with values from XML node
		 */
		airplane.manufacturer(manufacturer);
		airplane.model(model);
		airplane.firstClassSeats(firstClassSeats);
		airplane.coachSeats(coachSeats);
		
		return airplane;
	}
	
	/**
	 * Retrieve character data from an element if it exists
	 * 
	 * @param e is the DOM Element to retrieve character data from
	 * @return the character data as String [possibly empty String]
	 */
	private static String getCharacterDataFromElement (Element e) {
		Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	        CharacterData cd = (CharacterData) child;
	        return cd.getData();
	      }
	      return "";
	}
}
