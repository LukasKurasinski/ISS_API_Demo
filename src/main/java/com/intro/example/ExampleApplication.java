package com.intro.example;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
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

		//Getting ISS data from API
		String urlString = "https://api.wheretheiss.at/v1/satellites/25544";
		URL url = null;
		StringBuilder strData = null;
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
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		JSONObject json = new JSONObject(strData.toString());
		BigDecimal longitude = (BigDecimal) json.get("longitude");
		BigDecimal latitude = (BigDecimal) json.get("latitude");
		System.out.println("" + strData);
		System.out.println(json);
		System.out.println("" + longitude);
		System.out.println("" + latitude);

		//returning map with position to the client
		String website = "<p>Where is ISS?</p><iframe width=\"425\" height=\"350\" src=\"https://www.openstreetmap.org/export/embed.html?bbox=-558.2812500000001%2C-88.70033392584459%2C582.1875000000001%2C89.87529106709619&amp;layer=mapnik&amp;marker="+latitude+"%2C"+longitude+"\" style=\"border: 1px solid black\"></iframe></br><button onclick=location.href=\"/\">Reset</button>";
		return website;
	}

	@GetMapping("/hello")
	public String index(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}