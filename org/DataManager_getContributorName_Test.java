import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_getContributorName_Test {
    @Test
    public void test1(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"wj\"}";

            }

        });

        String name  = dm.getContributorName("12345");
        assertNotNull(name);
        assertEquals(name,"wj");
    }

    // similar to test in Robustness Test
    @Test(expected=IllegalStateException.class)
    public void testNotSuccess(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\",\"data\":\"wj\"}";

            }

        });

        String name  = dm.getContributorName("12345");
    }

    // similar to test in Robustness Test
    @Test(expected=IllegalStateException.class)
    public void testException(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;

            }

        });

        String name  = dm.getContributorName("12346");
    }

    @Test(expected=IllegalStateException.class)
    public void testWebClientIsNull() {

        DataManager dm = new DataManager(null);
        dm.getContributorName("12346");
        fail("DataManager.getContributorName does not throw IllegalStateException when WebClient is null");

    }

    @Test(expected=IllegalArgumentException.class)
    public void testIdIsNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.getContributorName(null);
        fail("DataManager.createFund does not throw IllegalArgumentxception when orgId is null");

    }

}
