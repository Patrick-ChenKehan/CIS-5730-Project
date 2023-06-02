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
                return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1020\"," +
                        "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                        "{\"target\":5000," +
                        "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                        "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                        "{\"target\":5000," +
                        "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                        "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
            };

        });
        Organization org = dm.attemptLogin("Patrick", "1020");
        assertNotNull(org);
        assertEquals("Patrick", org.getName());
        assertEquals("123", org.getId());
        assertEquals("KC", org.getDescription());
        assertEquals(2, org.getFunds().size());

    }
}

