package dummyTestPackage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.FileReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import static org.testng.Assert.assertEquals;

public class DummyClassTest {

	private static Logger log = LogManager.getLogger(DummyClassTest.class.getName());
	public Gson gson = new Gson();

	@BeforeTest
	public void prepareTests() throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		JsonObject myEnvironment = gson.fromJson(new FileReader("EnvironmentVariables.json"), JsonObject.class);

		RestAssured.baseURI = myEnvironment.get("BaseURL").getAsString();
	}

	@Test(priority = 1)
	public void verifyTheStartIndexIsOne() {
//		https://chroniclingamerica.loc.gov/search/titles/results/?terms=texas&format=json&page=1

		Response response = RestAssured.given().param("terms", "texas").param("format", "json").param("page", "1")
				.when().get("/search/titles/results").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).extract().response();

		log.info("Searching the directory and newspaper pages using OpenSearch");

		JsonObject actualResponse = gson.fromJson(response.asString(), JsonObject.class);

		assertEquals(actualResponse.get("startIndex").getAsInt(), 1);
	}

	@Test(priority = 2)
	public void verifyThePlaceOfPublicationIsHouston() {
//		https://chroniclingamerica.loc.gov/search/titles/results/?terms=texas&format=json&page=1

		Response response = RestAssured.given().param("terms", "texas").param("format", "json").param("page", "1")
				.when().get("/search/titles/results").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).extract().response();

		log.info("Searching the directory and newspaper pages using OpenSearch");

		JsonObject actualResponse = gson.fromJson(response.asString(), JsonObject.class);
		JsonArray items = actualResponse.getAsJsonArray("items");

		assertEquals(items.get(0).getAsJsonObject().get("place_of_publication").getAsString(), "Houston, Tex.");
	}
}
