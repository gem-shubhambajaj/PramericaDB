package com.gemini.generic.stepDefinition;

import com.gemini.generic.MobileAction;
import com.gemini.generic.MobileDriverManager;
import com.gemini.generic.locator.Locator;
import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bson.Document;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StepDef {
    AppiumDriver driver = MobileDriverManager.getAppiumDriver();


    public void login(String username, String pass) {
        MobileAction.waitUntilElementVisible(Locator.LoginPageHeading, 10);
        MobileAction.typeText(Locator.usernameInput, username);
        MobileAction.waitSec(2);

        MobileAction.typeText(Locator.passInput, pass);
        MobileAction.waitSec(5);
        ((AndroidDriver) driver).hideKeyboard();
        MobileAction.click(Locator.loginBtn);
        MobileAction.waitSec(5);
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @When("Login in the application with {string} and {string}")
    public void loginApp(String username, String pass) {
        Actions act = new Actions(driver);
        MobileAction.waitSec(15);
        login(username, pass);
        if (isElementDisplayed(Locator.terminateBtn)) {
            MobileAction.click(Locator.terminateBtn);
            MobileAction.waitUntilElementVisible(Locator.employeeCodeLogin, 5);
            if (isElementDisplayed(Locator.employeeCodeLogin)) {
                MobileAction.typeText(Locator.employeeCodeLogin, username);
                MobileAction.waitSec(5);
            }
            ((AndroidDriver) driver).hideKeyboard();
            MobileAction.scrollToElement("SUBMIT", false);
            MobileAction.click(Locator.submitBtn);
            MobileAction.waitSec(5);
            if (isElementDisplayed(Locator.otpFirstBox)) {
                MobileAction.click(Locator.otpFirstBox);
                act.sendKeys("8990").build().perform();
            }
            MobileAction.waitSec(3);
            ((AndroidDriver) driver).hideKeyboard();
            MobileAction.click(Locator.submitBtn);
            MobileAction.waitSec(2);
            login(username, pass);
            MobileAction.waitSec(4);
        }
    }

    @And("Click on Side Menu and go to profile")
    public void clickOnSideMenuAndGoToProfile() {
        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.tap(PointOption.point(27, 676)).perform();
//        MobileAction.click(Locator.leftSideMenu);
        MobileAction.click(Locator.profileDetail);
    }

//..............Generic.......
    public MongoCollection<Document> getCollection(String connectionString,String databaseName,String collectionName) { // return specific collection from specific database
        try {
            MongoClient client = MongoClients.create(new ConnectionString(connectionString));
            System.out.println("Connection established");
            MongoDatabase database = client.getDatabase(databaseName);
            return database.getCollection(collectionName);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public MongoIterable<String> getCollectionNames(String connectionString, String databaseName){ //gets collection name from specific database
        try {
            MongoClient client = MongoClients.create(new ConnectionString(connectionString));
            System.out.println("Connection established");
            MongoDatabase database = client.getDatabase(databaseName);
            return database.listCollectionNames();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public MongoDatabase getDatabase(String connectionString, String databaseName){
        try {
            MongoClient client = MongoClients.create(new ConnectionString(connectionString));
            System.out.println("Connection established");

            return client.getDatabase(databaseName);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public MongoIterable<String> getDatabaseNames(String connectionString){ //gets databases name
        try {
            MongoClient client = MongoClients.create(new ConnectionString(connectionString));
            System.out.println("Connection established");
            return client.listDatabaseNames();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> connectDB(MongoCollection<Document> collectionName, String fieldName,String fieldValue) {
        String json = null;

        // Define the query
        for (Document document : collectionName.find(Filters.eq(fieldName,fieldValue))) {
            json = document.toJson();
        }
        System.out.println(json);

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        System.out.println(map);
        return map;
    }

    @Then("Validate details from {string}")
    public void validateDetailsFrom(String agentCode) {
        MongoCollection<Document> collection = getCollection("mongodb+srv://SpeedBiz-QA-r:XwYNdyxbU8UiFoal@aceapp-devuat.ojbzu.mongodb.net","AceAppDev","signininfos");
        Map<String, Object> profileDetails_DB = connectDB(collection,"agentCode",agentCode);
        for (Map.Entry<String, Object> map : profileDetails_DB.entrySet()) {
            System.out.println(map.getKey() + " : " + map.getValue());
        }

        //Gathering details from UI
        String agentCode_ui = (MobileAction.getElementText(Locator.agentCode)).split(":")[1].trim();
        String agentNum_ui = (MobileAction.getElementText(Locator.agentNum)).split(" ")[1].trim();

        //comparing database details from UI
        if (profileDetails_DB.get("agentCode").equals(agentCode_ui)) {
            System.out.println("Agent Code Matched: " + agentCode_ui);
            if (isElementDisplayed(AppiumBy.xpath(Locator.agentName.replace("<agentName>",profileDetails_DB.get("agentName").toString()))))
                System.out.println("Agent Name Matched: " + profileDetails_DB.get("agentName"));
            if (profileDetails_DB.get("mobile").equals(agentNum_ui))
                System.out.println("Agent Number Matched: " + agentNum_ui);
        } else
            System.out.println("AgentCode Does not match: " + profileDetails_DB.get("agentCode") + " != " + agentCode_ui);

    }

    @Then("Get information from database with {string}")
    public void getInformationFromDatabaseWithAnd(String limit) {
        String json;
        ArrayList<String> agentCode = new ArrayList<>();
        MongoCollection<Document> collection = getCollection("mongodb+srv://SpeedBiz-QA-r:XwYNdyxbU8UiFoal@aceapp-devuat.ojbzu.mongodb.net","AceAppDev","signininfos");
        for (Document document : collection.find().limit(Integer.parseInt(limit))) {
            json = document.toJson();
            JSONObject jsonObject = new JSONObject(json);
            agentCode.add(jsonObject.get("agentCode").toString());
            System.out.println(json);
        }
        System.out.println(agentCode);
        MobileAction.waitSec(10);
        login(agentCode.get(1),"1234");
        for (String username : agentCode) {
            MobileAction.waitSec(5);
            login(username, "12345");
            MobileAction.waitSec(5);
            if (isElementDisplayed(Locator.errorDialog)) {
                MobileAction.click(Locator.btn_errorDialogOK);
            }
            MobileAction.clearText(Locator.usernameInput);
            MobileAction.clearText(Locator.passInput);
            MobileAction.waitSec(2);
        }

    }

    @Then("run generic")
    public void runGeneric() {
        MongoIterable<String> databaseName = getDatabaseNames("mongodb+srv://SpeedBiz-QA-r:XwYNdyxbU8UiFoal@aceapp-devuat.ojbzu.mongodb.net");
//        MongoIterable<String> collNames = getCollectionNames("mongodb+srv://SpeedBiz-QA-r:XwYNdyxbU8UiFoal@aceapp-devuat.ojbzu.mongodb.net","AceAppDev");
        for(String it:databaseName){
            System.out.println(it);
        }

    }
}