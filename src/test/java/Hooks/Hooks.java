package Hooks;

import LoggerUtility.LoggerUtility;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

public class Hooks {
    public String testname;

    @BeforeMethod
    public void setUp(){
        testname = this.getClass().getSimpleName();
        LoggerUtility.startTestCase(testname);

    }
    @AfterMethod
    public void tearDown(){
        LoggerUtility.endTestCase(testname);
    }
    @AfterSuite
    public void mergeLogs(){
        LoggerUtility.mergeLogsIntoOne();
    }
}