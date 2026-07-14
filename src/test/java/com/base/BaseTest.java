package com.base;

import org.testng.annotations.BeforeClass;

import com.utils.TestData;

import io.restassured.RestAssured;

public class BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://thinking-tester-contact-list.herokuapp.com/";
        
        // Sync the variables so the updated fields hold the actual runtime data
        TestData.updatedEmail = TestData.email;            
        TestData.updatedPassword = TestData.password;
        

        System.out.println("Base URI: " + RestAssured.baseURI);
        System.out.println("Active Test Email: " + TestData.updatedEmail);
        System.out.println("Active Test Password: " + TestData.updatedPassword);
    }
}