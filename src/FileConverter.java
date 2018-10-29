import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FileConverter {

	private Scanner scanner;
	private String filePath;
	private String directoryPath = "";
	private String[] fields;
	private ArrayList<Hotel> hotels;

	public FileConverter(String filePath) {
		this.filePath = filePath;
		String[] pathArray = filePath.split("/");
		for (int i = 0; i < pathArray.length - 1; i++) {
			directoryPath += pathArray[i] + "/";
		}
		hotels = new ArrayList<Hotel>();
		parseInput();
	}

	private void parseInput() {
		try {
			scanner = new Scanner(new File(filePath));
			String line = scanner.nextLine();
			fields = line.split(",");
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				Hotel hotel = createHotelObject(line);
				hotels.add(hotel);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Hotel createHotelObject(String line) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		for (int i = 0; i < fields.length && i < values.length; i++) {
			map.put(fields[i], values[i]);
		}
		return new Hotel(map);
	}

	public boolean convertToJSON() {
		File outputFile = new File(directoryPath + "output.json");
		try {
			outputFile.createNewFile();
			FileWriter fileWriter = new FileWriter(outputFile);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(hotels);
			fileWriter.write(json);
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean convertToXML() {
		File outputFile = new File(directoryPath + "output.xml");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			Element rootElement = document.createElement("hotels");
			document.appendChild(rootElement);
			Hotel hotel;
			for (int i = 0; i < hotels.size(); i++) {
				hotel = hotels.get(i);
				Element hotelElement = document.createElement("hotel");
				Attr hotelNameAttr = document.createAttribute("name");
				hotelNameAttr.setValue(hotel.getName());
				hotelElement.setAttributeNode(hotelNameAttr);

				Element hotelName = document.createElement("name");
				hotelName.appendChild(document.createTextNode(hotel.getName()));
				hotelElement.appendChild(hotelName);

				Element hotelAddress = document.createElement("address");
				hotelAddress.appendChild(document.createTextNode(hotel
						.getAddress()));
				hotelElement.appendChild(hotelAddress);

				Element hotelStars = document.createElement("stars");
				hotelStars
						.appendChild(document.createTextNode(hotel.getStars()));
				hotelElement.appendChild(hotelStars);

				Element hotelContact = document.createElement("contact");
				hotelContact.appendChild(document.createTextNode(hotel
						.getContact()));
				hotelElement.appendChild(hotelContact);

				Element hotelPhone = document.createElement("phone");
				hotelPhone
						.appendChild(document.createTextNode(hotel.getPhone()));
				hotelElement.appendChild(hotelPhone);

				Element hotelUri = document.createElement("uri");
				hotelUri.appendChild(document.createTextNode(hotel.getUri()));
				hotelElement.appendChild(hotelUri);
				rootElement.appendChild(hotelElement);
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(outputFile);
			transformer.transform(source, result);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) {
		String directory = "";
		FileConverter fc = new FileConverter(directory + "hotels.csv");
		if (fc.convertToJSON()) {
			System.out.println("JSON file created successfully!");
		} else {
			System.out.println("Error while parsing!");
		}
		if (fc.convertToXML()) {
			System.out.println("XML file created successfully!");
		} else {
			System.out.println("Error while parsing!");
		}
	}
}
