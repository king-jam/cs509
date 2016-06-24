/**
 * 
 */
package client;

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
 * This class aggregates a number of Airport. The aggregate is implemented as an ArrayList.
 * Airports can be added to the aggregate via XML strings in the format returned form the 
 * CS509 server, or as Airport objects using the ArrayList interface. Objects can 
 * be removed from the collection using the ArrayList interface.
 * 
  * @author blake
 *
 */
public class Airports extends ArrayList<Airport> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds collection of airports from airports described in XML
	 * 
	 * Parses an XML string to read each of the airports and adds each valid airport 
	 * to the collection. The method uses Java DOM (Document Object Model) to convert
	 * from XML to Java primitives.
	 * 
	 * @param xmlAirports XML string containing set of airports 
	 * @return true if the collection was modified, false otherwise
	 * @throws NullPointerException included to keep signature consistent with other addAll methods
	 * 
	 * @preconditions the xmlAirports string adheres to the format specified by the server API
	 * @postconditions the [possibly empty] set of Airports in the XML string are added to collection
	 */
	public boolean addAll (String xmlAirports) throws NullPointerException {
		
		boolean collectionUpdated = false;
		
		// Load the XML string into a DOM tree for ease of processing
		// then iterate over all nodes adding each airport to our collection
		Document docAirports = buildDomDoc (xmlAirports);
		NodeList nodesAirports = docAirports.getElementsByTagName("Airport");
		
		for (int i = 0; i < nodesAirports.getLength(); i++) {
			Element elementAirport = (Element) nodesAirports.item(i);
			Airport airport = buildAirport (elementAirport);
			
			if (airport.isValid()) {
				this.add(airport);
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
	 * Creates an Airport object form a DOM node
	 * 
	 * Processes a DOM Node that describes an Airport and creates an Airport object from the information
	 * @param nodeAirport is a DOM Node describing an Airport
	 * @return Airport object created from the DOM Node representation of the Airport
	 * 
	 * @preconditions nodeAirport is of format specified by CS509 server API
	 */
	private Airport buildAirport (Node nodeAirport) {
		/**
		 * Instantiate an empty Airport object
		 */
		Airport airport = new Airport();

		String name;
		String code;
		double latitude;
		double longitude;
		
		// The airport element has attributes of Name and 3 character airport code
		Element elementAirport = (Element) nodeAirport;
		name = elementAirport.getAttributeNode("Name").getValue();
		code = elementAirport.getAttributeNode("Code").getValue();
		
		// The latitude and longitude are child elements
		Element elementLatLng;
		elementLatLng = (Element)elementAirport.getElementsByTagName("Latitude").item(0);
		latitude = Double.parseDouble(getCharacterDataFromElement(elementLatLng));
		
		elementLatLng = (Element)elementAirport.getElementsByTagName("Longitude").item(0);
		longitude = Double.parseDouble(getCharacterDataFromElement(elementLatLng));

		/**
		 * Update the Airport object with values from XML node
		 */
		airport.name(name);
		airport.code(code);
		airport.latitude(latitude);
		airport.longitude(longitude);
		
		return airport;
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
