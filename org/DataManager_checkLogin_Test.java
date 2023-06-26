import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;



public class DataManager_checkLogin_Test {

    @Test
    public void testSuccessfulNotSeenBefore() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"login failed\"}";
            }
        });
        boolean check = dm.checkLogin("arnav");
        assertEquals(false, check); // not seen before

    }

    @Test
    public void testSuccessfulSeenBefore() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"6499c51640739252c2fb3ee4\"," +
                        "\"login\":\"gatts\",\"password\":\"gatts\",\"name\":\"OKOK\",\"description\":\"YER\"," +
                        "\"funds\":[{\"target\":70,\"_id\":\"6499c7ad40739252c2fb3f90\",\"name\":\"gatts\"," +
                        "\"description\":\"gatts\",\"org\":\"6499c51640739252c2fb3ee4\"," +
                        "\"donations\":[{\"_id\":\"6499c7b340739252c2fb3f94\",\"contributor\":\"649873aaf59da441a42c1de1\"," +
                        "\"fund\":\"6499c7ad40739252c2fb3f90\",\"date\":\"2023-06-26T17:15:31.499Z\"," +
                        "\"amount\":30,\"__v\":0},{\"_id\":\"6499c91340739252c2fb3fc6\",\"contributor\":\"649873aaf59da441a42c1de1\",\"fund\":\"6499c7ad40739252c2fb3f90\"," +
                        "\"date\":\"2023-06-26T17:21:23.233Z\",\"amount\":500,\"__v\":0},{\"_id\":\"649a061740739252c2fb400a\"," +
                        "\"contributor\":\"649873aaf59da441a42c1de1\",\"fund\":\"6499c7ad40739252c2fb3f90\"," +
                        "\"date\":\"2023-06-26T21:41:43.390Z\",\"amount\":300,\"__v\":0}],\"__v\":0},{\"target\":3,\"_id\":\"649a072540739252c2fb401c\"," +
                        "\"name\":\"YER\",\"description\":\"\",\"org\":\"6499c51640739252c2fb3ee4\",\"donations\":[{\"_id\":\"649a074f40739252c2fb4021\"," +
                        "\"contributor\":\"649873aaf59da441a42c1de1\",\"fund\":\"649a072540739252c2fb401c\",\"date\":\"2023-06-26T21:46:55.926Z\",\"amount\":0,\"__v\":0}," +
                        "{\"_id\":\"649a075940739252c2fb402b\",\"contributor\":\"649873aaf59da441a42c1de1\"," +
                        "\"fund\":\"649a072540739252c2fb401c\",\"date\":\"2023-06-26T21:47:05.403Z\",\"amount\":30,\"__v\":0}]," +
                        "\"__v\":0}],\"__v\":0}}";

            }
        });

        boolean check = dm.checkLogin("gatts");
        assertEquals(true, check); // not seen before

    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidLogin() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"login failed\"}";

            }
        });

        dm.checkLogin(null);

    }

    @Test(expected=IllegalStateException.class)
    public void testWebClientIsNull() {

        DataManager dm = new DataManager(null);
        dm.checkLogin("gatts");
        fail("DataManager.checkLogin does not throw IllegalStateException when WebClient is null");

    }


    @Test(expected=IllegalStateException.class)
    public void testMalformedJSON() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Malformed JSON";
            }

        });

        dm.checkLogin("gatts");
        fail("DataManager.checkLogin does not throw IllegalStateException when WebClient returns error");
    }

    @Test(expected=IllegalStateException.class)
    public void testExceptionNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;

            }

        });


        boolean check = dm.checkLogin("gatts");
        assertNull(check);
    }

}
