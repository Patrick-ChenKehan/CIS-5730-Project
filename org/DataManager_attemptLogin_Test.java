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
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1020\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                return null;
            }

        });
        Organization org = dm.attemptLogin("Patrick", "1020");
        assertNotNull(org);
        assertEquals("Patrick", org.getName());
        assertEquals("123", org.getId());
        assertEquals("KC", org.getDescription());
        assertEquals(2, org.getFunds().size());

    }

    // similar to test in Robustness Test
    @Test(expected=IllegalStateException.class)
    public void testFailedLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":{\"name\":\"MongoError\"}}";
            }

        });
        Organization org = dm.attemptLogin("Patrick", "1020f");
    }

    // similar to test in Robustness Test
    @Test (expected = IllegalStateException.class)
    public void testFailedConnection() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }

        });
        Organization org = dm.attemptLogin("Patrick", "1020");
    }



    @Test(expected=IllegalArgumentException.class)
    public void testAttemptLogin_LoginIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1020\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                return null;
            }

        });
        Organization org = dm.attemptLogin(null, "1020");
        fail("DataManager.attemptLogin does not throw IllegalArgumentxception when contributorId is null");

    }

    @Test(expected=IllegalArgumentException.class)
    public void testAttemptLogin_PasswordIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1020\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                return null;
            }

        });
        Organization org = dm.attemptLogin("Patrick", null);
        fail("DataManager.attemptLogin does not throw IllegalArgumentxception when contributorId is null");

    }

    // this assumes no server is running on port 3002
    @Test(expected=IllegalStateException.class)
    public void testAttemptLogin_WebClientCannotConnectToServer() {

        DataManager dm = new DataManager(new WebClient("localhost", 3002));
        Organization org = dm.attemptLogin("Patrick", "1020");
        fail("DataManager.attemptLogin does not throw IllegalStateException when WebClient cannot connect to server");

    }

    @Test(expected=IllegalStateException.class)
    public void testAttemptLogin_WebClientIsNull() {

        DataManager dm = new DataManager(null);
        Organization org = dm.attemptLogin("Patrick", "1020");
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient is null");

    }
}

