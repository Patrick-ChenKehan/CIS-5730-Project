import org.junit.Test;

import java.util.Map;
import static org.junit.Assert.*;

public class testGetContributorName {
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

    @Test
    public void testNotSuccess(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\",\"data\":\"wj\"}";

            }

        });

        String name  = dm.getContributorName("12345");
        assertNull(name);
    }

    @Test
    public void testException(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;

            }

        });

        String name  = dm.getContributorName("12346");
        assertNull(name);
    }
}
