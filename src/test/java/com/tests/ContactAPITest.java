package com.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.utils.AuthHelper;
import com.utils.ContactManager;
import com.utils.TokenManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Listeners(com.listeners.ExtentTestListener.class)

public class ContactAPITest extends BaseTest {

	@BeforeClass
	public void setupAuthentication() {
		AuthHelper.authenticate();
	}

	@Test(priority = 5)
	public void addContact() {

		System.out.println("-----------------------------------");
		System.out.println("TC5 - ADD CONTACT - POST METHOD");
		System.out.println("-----------------------------------");

		String requestBody = "{" + "\"firstName\":\"Rida\"," + "\"lastName\":\"Aafreen\","
				+ "\"birthdate\":\"2007-07-07\"," + "\"email\":\"jdoe@fake.com\"," + "\"phone\":\"8005555555\","
				+ "\"street1\":\"1 Main St.\"," + "\"street2\":\"Apartment A\"," + "\"city\":\"Anytown\","
				+ "\"stateProvince\":\"KS\"," + "\"postalCode\":\"12345\"," + "\"country\":\"USA\"" + "}";

		Response response = given().header("Authorization", "Bearer " + TokenManager.token)
				.contentType(ContentType.JSON).body(requestBody)

				.when().post("/contacts");

		response.prettyPrint();
		
		ContactManager.contactId = response.jsonPath().getString("_id");

		System.out.println("Generated Contact ID: " + ContactManager.contactId);

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 201, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("Created"), "Status Line Validation Failed");

		// Contact ID Assertion
		// Assert.assertNotNull(response.jsonPath().getString("_id"), "Contact ID should
		// not be null");

		// Response Body Assertions
		Assert.assertEquals(response.jsonPath().getString("firstName"), "Rida");
		Assert.assertEquals(response.jsonPath().getString("lastName"), "Aafreen");
		Assert.assertEquals(response.jsonPath().getString("birthdate"), "2007-07-07");
		Assert.assertEquals(response.jsonPath().getString("email"), "jdoe@fake.com");
		Assert.assertEquals(response.jsonPath().getString("phone"), "8005555555");
		Assert.assertEquals(response.jsonPath().getString("street1"), "1 Main St.");
		Assert.assertEquals(response.jsonPath().getString("street2"), "Apartment A");
		Assert.assertEquals(response.jsonPath().getString("city"), "Anytown");
		Assert.assertEquals(response.jsonPath().getString("stateProvince"), "KS");
		Assert.assertEquals(response.jsonPath().getString("postalCode"), "12345");
		Assert.assertEquals(response.jsonPath().getString("country"), "USA");

		// Optional: Response Time Validation
		Assert.assertTrue(response.getTime() < 3000, "Response time exceeded 3 seconds");
	}

	@Test(priority = 6)
	public void getContactList() {

		System.out.println("-----------------------------------");
		System.out.println("TC6 - GET CONTACT LIST - GET METHOD");
		System.out.println("-----------------------------------");

		Response response = given().header("Authorization", "Bearer " + TokenManager.token)

				.when().get("/contacts");

		response.prettyPrint();

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 200, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("OK"), "Status Line Validation Failed");

		// Response Body Assertion
		Assert.assertNotNull(response.getBody().asString(), "Response Body should not be null");

		// Verify Contact List is not empty
		Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "Contact list should not be empty");

		// Optional: Response Time
		Assert.assertTrue(response.getTime() < 3000, "Response time exceeded 3 seconds");

		System.out.println("Total Contacts: " + response.jsonPath().getList("$").size());
	}

	@Test(priority = 7)
	public void getContact() {

		System.out.println("-----------------------------------");
		System.out.println("TC7 - GET CONTACT - GET METHOD");
		System.out.println("-----------------------------------");

		Response response = given().header("Authorization", "Bearer " + TokenManager.token)

				.when().get("/contacts/" + ContactManager.contactId);

		response.prettyPrint();
		
		System.out.println("Contact ID: " + ContactManager.contactId);

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 200, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("OK"), "Status Line Validation Failed");

		// Contact ID Validation
		Assert.assertEquals(response.jsonPath().getString("_id"), ContactManager.contactId,
				"Contact ID Validation Failed");

		// Contact Details Validation
		Assert.assertEquals(response.jsonPath().getString("firstName"), "Rida");

		Assert.assertEquals(response.jsonPath().getString("lastName"), "Aafreen");

		Assert.assertEquals(response.jsonPath().getString("email"), "jdoe@fake.com");

		Assert.assertEquals(response.jsonPath().getString("phone"), "8005555555");

		Assert.assertEquals(response.jsonPath().getString("city"), "Anytown");

		// Optional: Response Time
		Assert.assertTrue(response.getTime() < 3000, "Response time exceeded 3 seconds");

		System.out.println("Retrieved Contact ID: " + ContactManager.contactId);
	}
	
	@Test(priority = 8)
	public void updateContact() {

	    System.out.println("-----------------------------------");
	    System.out.println("TC8 - UPDATE CONTACT - PUT METHOD");
	    System.out.println("-----------------------------------");

	    String requestBody = "{"
	            + "\"firstName\":\"Amy\","
	            + "\"lastName\":\"Miller\","
	            + "\"birthdate\":\"1992-02-02\","
	            + "\"email\":\"amiller@fake.com\","
	            + "\"phone\":\"8005554242\","
	            + "\"street1\":\"13 School St.\","
	            + "\"street2\":\"Apt. 5\","
	            + "\"city\":\"Washington\","
	            + "\"stateProvince\":\"QC\","
	            + "\"postalCode\":\"A1A1A1\","
	            + "\"country\":\"Canada\""
	            + "}";

	    Response response = given()
	            .header("Authorization", "Bearer " + TokenManager.token)
	            .contentType(ContentType.JSON)
	            .body(requestBody)

	    .when()
	            .put("/contacts/" + ContactManager.contactId);

	    response.prettyPrint();

	    // Status Code Assertion
	    Assert.assertEquals(response.getStatusCode(), 200,
	            "Status Code Validation Failed");

	    // Status Line Assertion
	    Assert.assertTrue(response.getStatusLine().contains("OK"),
	            "Status Line Validation Failed");

	    // Contact ID Assertion
	    Assert.assertEquals(response.jsonPath().getString("_id"),
	            ContactManager.contactId,
	            "Contact ID Validation Failed");

	    // Email Assertion (Assignment Requirement)
	    Assert.assertEquals(response.jsonPath().getString("email"),
	            "amiller@fake.com",
	            "Email Validation Failed");

	    // Additional Assertions
	    Assert.assertEquals(response.jsonPath().getString("firstName"), "Amy");
	    Assert.assertEquals(response.jsonPath().getString("lastName"), "Miller");
	    Assert.assertEquals(response.jsonPath().getString("phone"), "8005554242");
	    Assert.assertEquals(response.jsonPath().getString("city"), "Washington");
	    Assert.assertEquals(response.jsonPath().getString("country"), "Canada");

	    // Optional: Response Time
	    Assert.assertTrue(response.getTime() < 3000,
	            "Response time exceeded 3 seconds");

	    System.out.println("Updated Contact ID: " + ContactManager.contactId);
	}

	@Test(priority = 9)
	public void updateContactPatch() {

	    System.out.println("-----------------------------------");
	    System.out.println("TC9 - UPDATE CONTACT - PATCH METHOD");
	    System.out.println("-----------------------------------");

	    String requestBody = "{"
	            + "\"firstName\":\"Anna\""
	            + "}";

	    Response response = given()
	            .header("Authorization", "Bearer " + TokenManager.token)
	            .contentType(ContentType.JSON)
	            .body(requestBody)

	    .when()
	            .patch("/contacts/" + ContactManager.contactId);

	    response.prettyPrint();

	    // Status Code Assertion
	    Assert.assertEquals(response.getStatusCode(), 200,
	            "Status Code Validation Failed");

	    // Status Line Assertion
	    Assert.assertTrue(response.getStatusLine().contains("OK"),
	            "Status Line Validation Failed");

	    // Contact ID Assertion
	    Assert.assertEquals(response.jsonPath().getString("_id"),
	            ContactManager.contactId,
	            "Contact ID Validation Failed");

	    // First Name Assertion
	    Assert.assertEquals(response.jsonPath().getString("firstName"),
	            "Anna",
	            "First Name Validation Failed");

	    // Optional: Response Time Assertion
	    Assert.assertTrue(response.getTime() < 3000,
	            "Response time exceeded 3 seconds");

	    System.out.println("Contact updated successfully.");
	}
	
	@Test(priority = 10)
	public void logoutUser() {

	    System.out.println("-----------------------------------");
	    System.out.println("TC10 - LOGOUT USER - POST METHOD");
	    System.out.println("-----------------------------------");

	    Response response = given()
	            .header("Authorization", "Bearer " + TokenManager.token)

	    .when()
	            .post("/users/logout");

	    response.prettyPrint();

	    // Status Code
	    Assert.assertEquals(response.getStatusCode(), 200,
	            "Status Code Validation Failed");

	    // Status Line
	    Assert.assertTrue(response.getStatusLine().contains("OK"),
	            "Status Line Validation Failed");

	    // Clear the token after successful logout
	    TokenManager.token = null;

	    System.out.println("User logged out successfully.");
	}
}
