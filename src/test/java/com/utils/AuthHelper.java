package com.utils;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthHelper {

    public static void authenticate() {

        // 1. Create user

        String body = "{"
                + "\"firstName\":\"" + TestData.firstName + "\","
                + "\"lastName\":\"" + TestData.lastName + "\","
                + "\"email\":\"" + TestData.email + "\","
                + "\"password\":\"" + TestData.password + "\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("/users");

        // 2. Login

        String loginBody = "{"
                + "\"email\":\"" + TestData.email + "\","
                + "\"password\":\"" + TestData.password + "\""
                + "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginBody)
        .when()
                .post("/users/login");

        TokenManager.token = response.jsonPath().getString("token");
        System.out.println("Login Token: " + TokenManager.token);
    }
}


//package com.utils;
//
//import static io.restassured.RestAssured.given;
//
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//
//public class AuthHelper {
//
//    public static void login() {
//    	
//    	String requestBody = "{"
//    	        + "\"email\":\"" + TestData.email + "\","
//    	        + "\"password\":\"" + TestData.password + "\""
//    	        + "}";
//
//        String requestBody = "{"
//                + "\"email\":\"" + TestData.updatedEmail + "\","
//                + "\"password\":\"" + TestData.updatedPassword + "\""
//                + "}";
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(requestBody)
//
//        .when()
//                .post("/users/login");
//
//        TokenManager.token = response.jsonPath().getString("token");
//
//        System.out.println("Login Token: " + TokenManager.token);
//    }
//}