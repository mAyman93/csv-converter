import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Hotel {
	private String name;
	private String address;
	private String stars;
	private String contact;
	private String phone;
	private String uri;

	public Hotel(HashMap<String, String> map) {
		if (map.containsKey("name")) {
			this.name = (String) map.get("name");
		}
		if (map.containsKey("address")) {
			String address = (String) map.get("address");
			address = address.replaceAll("\"", "");
			this.address = address;
		}
		if (map.containsKey("stars")) {
			String stars = (String) map.get("stars");
			this.stars = isValidRating(stars) ? stars : "NA";
		}
		if (map.containsKey("contact")) {
			this.contact = (String) map.get("contact");
		}
		if (map.containsKey("phone")) {
			String phone = (String) map.get("phone");
			this.phone = isValidPhoneNumber(phone) ? phone : "NA";
		}
		if (map.containsKey("uri")) {
			String uri = (String) map.get("uri");
			this.uri = isValidUri(uri) ? uri : "NA";
		}
	}

	private boolean isValidRating(String stars) {
		int starsInt = Integer.parseInt(stars);
		if (starsInt >= 0 && starsInt <= 5) {
			return true;
		}
		return false;
	}

	public boolean isValidUri(String uri) {
		try {
			URL url = new URL(uri);
			url.toURI();
		} catch (MalformedURLException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
		return true;
	}

	// Can use regex to validate different numbers formats from different
	// countries
	public boolean isValidPhoneNumber(String number) {
		number = number.replaceAll(" ", "");
		number = number.replaceAll("-", "");
		number = number.replaceAll("\\.", "");
		number = number.replaceAll("\\(", "");
		number = number.replaceAll("\\)", "");

		if (number.matches("^(\\+)?([0-9]{8,16})(x?)([0-9]{3,6})?$")) {
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getStars() {
		return stars;
	}

	public String getContact() {
		return contact;
	}

	public String getPhone() {
		return phone;
	}

	public String getUri() {
		return uri;
	}
}
