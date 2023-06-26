import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import javax.xml.crypto.Data;

public class DataManager_deleteFund_Test {
    /*
     * This is a test class for the DataManager.deleteFund method.
     * Add more tests here for this method as needed.
     *
     * When writing tests for other methods, be sure to put them into separate
     * JUnit test classes.
     */
    @Test
    public void testSuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }

        });


        dm.deleteFund("647ccca1bf9db33792bea8e8");
    }

    @Test (expected = IllegalStateException.class)
    public void testFailedDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }

        });

        dm.deleteFund("647ccca1bf9db33792bea8e8");
    }

    @Test (expected = IllegalStateException.class)
    public void testFailedConnection() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }

        });

        dm.deleteFund("647ccca1bf9db33792bea8e8");
    }

    @Test(expected=IllegalStateException.class)
    public void testDeleteFund_WebClientIsNull() {

        DataManager dm = new DataManager(null);
        dm.deleteFund("647ccca1bf9db33792bea8e8");
        fail("DataManager.deleteFund does not throw IllegalStateException when WebClient is null");

    }

    @Test(expected=IllegalArgumentException.class)
    public void testDeleteFund_FundIdIsNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.deleteFund(null);
        fail("DataManager.deleteFund does not throw IllegalArgumentxception when id is null");

    }
}
