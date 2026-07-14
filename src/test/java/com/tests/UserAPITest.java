package com.tests;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.utils.TestData;
import com.utils.TokenManager;

import org.testng.annotations.Listeners;

@Listeners(com.listeners.ExtentTestListener.class)
public class UserAPITest extends BaseTest {

	
	@Test(priority = 1)
	public void addUser() {

		System.out.println("--------------------------------");
		System.out.println("TC1 - ADD NEW USER - POST METHOD");
		System.out.println("--------------------------------");

		String body = "{" + "\"firstName\":\"" + TestData.firstName + "\"," + "\"lastName\":\"" + TestData.lastName
				+ "\"," + "\"email\":\"" + TestData.email + "\"," + "\"password\":\"" + TestData.password + "\"" + "}";

		Response response = given().contentType(ContentType.JSON).body(body)

				.when().post("/users");

		response.prettyPrint();

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 201, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("Created"), "Status Line Validation Failed");

		// Generate Token
		TokenManager.token = response.jsonPath().getString("token");

		// Token Assertion
		Assert.assertNotNull(TokenManager.token, "Token is null");
		Assert.assertFalse(TokenManager.token.isEmpty(), "Token is empty");

		System.out.println("Generated Token: " + TokenManager.token);
	}

	@Test(priority = 2)
	public void getUserProfile() {

		System.out.println("-----------------------------------");
		System.out.println("TC2 - GET USER PROFILE - GET METHOD");
		System.out.println("-----------------------------------");

		Response response = given().header("Authorization", "Bearer " + TokenManager.token)

				.when().get("/users/me");

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 200, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("OK"), "Status Line Validation Failed");

		// Response Body Assertions

		Assert.assertEquals(response.jsonPath().getString("firstName"), TestData.firstName);

		Assert.assertEquals(response.jsonPath().getString("lastName"), TestData.lastName);

		Assert.assertEquals(response.jsonPath().getString("email"), TestData.email);

		// Token Validation
		Assert.assertNotNull(TokenManager.token, "Token should not be null");
		Assert.assertFalse(TokenManager.token.isEmpty(), "Token should not be empty");

	}

	@Test(priority = 3, dependsOnMethods = "addUser")
	public void updateUser() {

		System.out.println("-----------------------------------");
		System.out.println("TC3 - UPDATE USER - PATCH METHOD");
		System.out.println("-----------------------------------");

		String updatedEmail = "test2" + System.currentTimeMillis() + "@fake.com";

		String requestBody = "{" + "\"firstName\":\"Updated\"," + "\"lastName\":\"Username\"," + "\"email\":\""
				+ updatedEmail + "\"," + "\"password\":\"myNewPassword\"" + "}";

		io.restassured.response.Response response = given().header("Authorization", "Bearer " + TokenManager.token)
				.contentType(ContentType.JSON).body(requestBody)

				.when().patch("/users/me");

		// Print the complete response
		response.prettyPrint();

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 200, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("OK"), "Status Line Validation Failed");

		// Response Body Assertions

		Assert.assertEquals(response.jsonPath().getString("firstName"), "Updated", "First Name Validation Failed");

		Assert.assertEquals(response.jsonPath().getString("lastName"), "Username", "Last Name Validation Failed");

		Assert.assertEquals(response.jsonPath().getString("email"), updatedEmail, "Email Validation Failed");

		// Save the updated credentials for the login test
		TestData.updatedEmail = updatedEmail;
		TestData.updatedPassword = "myNewPassword";

	}

	@Test(priority = 4, dependsOnMethods = "updateUser")
	public void loginUser() {

		System.out.println("-----------------------------------");
		System.out.println("TC4 - LOG IN USER - POST METHOD");
		System.out.println("-----------------------------------");

		String requestBody = "{" + "\"email\":\"" + TestData.updatedEmail + "\"," + "\"password\":\""
				+ TestData.updatedPassword + "\"" + "}";

		System.out.println("Email: " + TestData.updatedEmail);
		System.out.println("Password: " + TestData.updatedPassword);
		System.out.println("Request Body: " + requestBody);

		Response response = given().log().all().contentType(ContentType.JSON).body(requestBody)

				.when().post("/users/login");

		response.prettyPrint();

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 200, "Status Code Validation Failed");

		// Status Line Assertion
		Assert.assertTrue(response.getStatusLine().contains("OK"), "Status Line Validation Failed");

		// Token Assertion
		String token = response.jsonPath().getString("token");

		Assert.assertNotNull(token, "Login token should not be null");
		Assert.assertFalse(token.isEmpty(), "Login token should not be empty");

		// Store the token
		TokenManager.token = token;

		// User ID Assertion (if returned by the API)
		// Assert.assertNotNull(response.jsonPath().getString("_id"), "User ID should
		// not be null");

		// Optional: Response Time Assertion
		Assert.assertTrue(response.getTime() < 3000, "Response time is greater than 3 seconds");

		System.out.println("Login Token: " + TokenManager.token);
	}

}
