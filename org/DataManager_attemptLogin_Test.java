import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import javax.xml.crypto.Data;

public class DataManager_attemptLogin_Test {
    /*
     * This is a test class for the DataManager.attemptLogin method.
     * Add more tests here for this method as needed.
     *
     * When writing tests for other methods, be sure to put them into separate
     * JUnit test classes.
     */

    @Test
    public void testSuccessfulLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        Organization org = dm.attemptLogin("Patrick", "1020");
    }
}

