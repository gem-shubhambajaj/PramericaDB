package com.gemini.generic.locator;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class Locator {
    public static String btn_generic = "//android.widget.Button[@text='<button>']";
    public static By LoginPageHeading = AppiumBy.xpath("//android.widget.Image/following-sibling::android.widget.TextView");
    public static By usernameInput = AppiumBy.xpath("//android.view.View[2]//android.widget.EditText");
    public static By passInput = AppiumBy.xpath("//android.view.View[3]//android.widget.EditText");
    public static By loginBtn = AppiumBy.xpath("//*[@text='Sign In']");
    public static By terminateBtn = AppiumBy.xpath("//android.widget.Button[contains(@text,'Terminate')]");
    public static By employeeCodeLogin = AppiumBy.xpath("//android.view.View/android.widget.EditText");
    public static By submitBtn = AppiumBy.className("android.widget.Button");
    public static By otpFirstBox = AppiumBy.xpath("//android.view.View/android.widget.EditText[1]");
    public static By profileDetail = AppiumBy.xpath("//android.widget.Image[@text='chevron down outline']");
    public static By leftSideMenu = AppiumBy.id("sidemenu-toggle-button-container");
    public static String agentName = "//*[@text='<agentName>']";
    public static By agentCode = AppiumBy.xpath("//android.widget.TextView[contains(@text,'Agent Code')]");
    public static By agentNum = AppiumBy.xpath("//android.widget.TextView[contains(@text,'+91')]");
    public static By errorDialog = AppiumBy.id("swal2-title");
    public static By btn_errorDialogOK = AppiumBy.xpath("//android.widget.Button[@text='OK']");



}
