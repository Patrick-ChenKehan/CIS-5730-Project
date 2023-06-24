import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;



public class DataManager_createLogin_Test {
    @Test
    public void testSuccess(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return "{\"status\":\"success\",\"data\":{\"_id\":\"jw3\", \"name\":\"test\", \"description\":\"sse\",\"funds\":[]}}";
            }
        });

        Organization o = dm.createLogin("jw3", "123", "test", "sse");

        assertNotNull(o);
        assertEquals("jw3", o.getId());
        assertEquals("test", o.getName());
        assertEquals("sse", o.getDescription());
    }

    @Test(expected=IllegalStateException.class)
    public void testNull(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return null;
            }
        });

        Organization o = dm.createLogin("jw3", "123", "test", "sse");

        assertNull(o);

    }

    @Test
    public void testFailure(){
        DataManager dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return "{\"status\":\"error\",\"data\":{\"name\":\"Error\"}}";
            }
        });

        Organization o = dm.createLogin("jw3", "123", "test", "sse");

        assertNull(o);

    }
}
