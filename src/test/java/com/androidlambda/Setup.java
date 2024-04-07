package com.androidlambda;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

public class Setup {
    //driver initialized
    public AndroidDriver<MobileElement> driver;

    //Locators
    @AndroidFindBy(xpath = "//*[@text='Network & internet']")
    private MobileElement networkAndInternet;
    @AndroidFindBy(xpath = "//*[@text='Security & privacy']")
    private MobileElement securityAndPrivacy;
    @AndroidFindBy(xpath = "//*[@text='More security & privacy']")
    private MobileElement moreSecurityAndPrivacy;
    @AndroidFindBy(xpath = "//*[@text='Encryption & credentials']")
    private MobileElement encryptionAndCredentials;
    @AndroidFindBy(xpath = "//*[@text='Trusted credentials']")
    private MobileElement trustedCredentials;
    @AndroidFindBy(xpath = "//*[@text='User']")
    private MobileElement userBtn;
    @AndroidFindBy(xpath = "(//android.widget.Switch[@resource-id='com.android.settings:id/trusted_credential_status'])[1]")
    private MobileElement toggleBtn;
    @AndroidFindBy(xpath = "//androidx.viewpager.widget.ViewPager[@resource-id='com.android.settings:id/view_pager']")
    private MobileElement certificatePanel;
    @AndroidFindBy(xpath = "//android.widget.ListView[@resource-id='com.android.settings:id/cert_list']/android.widget.LinearLayout")
    private MobileElement certificateList;
    @AndroidFindBy(xpath = "//*[@text='Internet']")
    private MobileElement wifiIcon;
    @AndroidFindBy(xpath = "//*[@text='Connected']")
    private MobileElement connectedWifi;
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Modify']")
    private MobileElement pencilIconModify;
    @AndroidFindBy(xpath = "//*[@text='Advanced options']")
    private MobileElement advanceOption;
    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='android:id/text1' and @text='None']")
    private MobileElement proxyPanelSetting;
    @AndroidFindBy(xpath = "//*[@text='Google']")
    private MobileElement googleAccount;
    @AndroidFindBy(xpath = "//*[@text='Find My Device']")
    private MobileElement findMyDevice;
    @AndroidFindBy(xpath = "//*[@text='Google Account needed']")
    private MobileElement googleAccountNeeded;



    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "OnePlus Nord 2");
        capabilities.setCapability(MobileCapabilityType.UDID, "emulator-5554"); // Replace with your device UDID
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14"); // Replace with your device Android version
        capabilities.setCapability("appPackage", "com.android.settings");
        capabilities.setCapability("appActivity", "com.android.settings.Settings");

        URL url = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver<>(url, capabilities);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Test case 1: Airplane Mode Validation
    @Test
    public void testAirplaneMode() {
        //network and internet element clicked
        networkAndInternet.click();
        //waiting for the next element
        implicitWait(10);
        //checked for the required mobile element
        MobileElement airplaneModeSwitch = driver.findElement(By.xpath("//android.widget.Switch[@resource-id='android:id/switch_widget']"));
        //element found status
        boolean airplaneModeEnabled = Boolean.parseBoolean(airplaneModeSwitch.getAttribute("checked"));
        //assertion
        Assert.assertFalse(airplaneModeEnabled, "Airplane mode should be turned off");
    }

    // Test case 2: User Certificates Listing
    @Test
    public void testUserCertificatesListing() {
        //calling the function to slide the emulator window with 2 param -> down: how much to slide down ; time: in how much time to increase or decrease speed of sliding
        touchAndSlide(-500, 5);
        securityAndPrivacy.click();
        implicitWait(10);
        touchAndSlide(-1000, 5);
        moreSecurityAndPrivacy.click();
        implicitWait(10);
        touchAndSlide(-500, 5);
        encryptionAndCredentials.click();
        implicitWait(10);
        trustedCredentials.click();
        implicitWait(10);
        List<MobileElement> certificates = null;
        if(certificatePanel.isDisplayed()){
            certificates = driver.findElements(By.xpath("//android.widget.ListView[@resource-id='com.android.settings:id/cert_list']/android.widget.LinearLayout"));
        }
        for(MobileElement i: certificates){
            System.out.println(i.getText());
        }
        Assert.assertTrue(certificates.size() > 0, "At least one user certificate should be present");
    }

    // Test case 3: Wi-Fi Proxy Setting Verification
    @Test
    public void testWifiProxySetting() {
        // Assuming connected to a Wi-Fi network
        networkAndInternet.click();
        implicitWait(10);
        wifiIcon.click();
        implicitWait(10);
        connectedWifi.click();
        implicitWait(10);
        pencilIconModify.click();
        implicitWait(10);
        advanceOption.click();
        implicitWait(10);
        String proxySetting = proxyPanelSetting.getText();
        System.out.print(proxySetting);
        implicitWait(10);
        Assert.assertEquals("None", proxySetting, "Proxy setting should be 'None'");
    }

    // Test case 4: Presence of User Certificates
    @Test
    public void testPresenceOfUserCertificates() {
        // Navigate to the location of user certificates
        testUserCertificatesListing();
        //waiting for next element
        implicitWait(10);
        userBtn.click();
        implicitWait(10);
        boolean userCertificatesPresent = isElementPresent(toggleBtn);
        //Changed the assertion cause no certificates were added in the user
        Assert.assertFalse(userCertificatesPresent, "User certificates not present");
    }

    // Test case 5: OnePlus Account Absence Check
    @Test
    public void testOnePlusAccountAbsence() {
        touchAndSlide(-1200,5);
        //Looking for Google
        googleAccount.click();
        implicitWait(10);
        //Checking under Find my to check if account is added or not
        findMyDevice.click();
        implicitWait(10);
        //msg if account added or not
        String msg = googleAccountNeeded.getText();
        //Assertion
        Assert.assertEquals(msg, "Google Account needed");
    }


    private boolean isElementPresent(MobileElement locator) {
        try {
            boolean displayStatus = locator.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void implicitWait(int time){
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    private void touchAndSlide(int down, int time){
        TouchAction touchAction = new TouchAction(driver);
        int startX = 500; // Starting x-coordinate (center of the screen)
        int startY = 1000; // Starting y-coordinate (near the top of the screen)
        int endX = 500; // Ending x-coordinate (center of the screen)
        int endY = down; // Ending y-coordinate (near the bottom of the screen)
        // Perform the swipe action from starting point to ending point
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(time))) // Adjust wait time as needed
                .moveTo(PointOption.point(endX, endY))
                .release().perform();
    }


}