package com.intro.example;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
@RestController
public class ExampleApplication {
	public static void main(String[] args) {

		SpringApplication.run(ExampleApplication.class, args);
	}
	@GetMapping("/")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		String urlString = "https://api.wheretheiss.at/v1/satellites/25544";
		URL url = null;
		JSONObject json = null;
		StringBuilder strData = null;
		BigDecimal longitude = null;
		BigDecimal latitude = null;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			strData = new StringBuilder();
			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				strData.append(scanner.nextLine());
			}
			scanner.close();
			conn.disconnect();
			System.out.println("" + strData);

			json = new JSONObject(strData.toString());
			longitude = (BigDecimal) json.get("longitude");
			latitude = (BigDecimal) json.get("latitude");

			System.out.println(json);
			System.out.println("" + longitude);
			System.out.println("" + latitude);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String website = "<p>Where is ISS?</p><iframe width=\"425\" height=\"350\" src=\"https://www.openstreetmap.org/export/embed.html?bbox=-558.2812500000001%2C-88.70033392584459%2C582.1875000000001%2C89.87529106709619&amp;layer=mapnik&amp;marker="+latitude+"%2C"+longitude+"\" style=\"border: 1px solid black\"></iframe>";
		return website;
	}

	@GetMapping("/hello")
	public String index(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}