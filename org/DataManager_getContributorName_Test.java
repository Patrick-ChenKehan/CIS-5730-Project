import org.junit.Test;

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
}
