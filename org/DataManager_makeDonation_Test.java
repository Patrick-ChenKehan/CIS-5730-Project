import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_makeDonation_Test {

    /*
     * This is a test class for the DataManager.makeDonation method.
     */

    @Test
    public void testSuccessfulAddedDonation() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/makeDonation")) {
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"64990e2d1202ad4a87f3a86f\"," +
                            "\"contributor\":\"649873aaf59da441a42c1de1\",\"fund\":\"64990c3a1202ad4a87f3a851\"" +
                            ",\"date\":\"2023-06-26T04:03:57.997Z\",\"amount\":40,\"__v\":0}}";
                } else if (resource.equals("/findContributorNameById")){
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                }
                return null;
            }

        });

        Donation donation = dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        assertNotNull(donation);
        assertEquals(40, donation.getAmount());
        assertEquals("wj", donation.getContributorName());
        assertEquals("64990c3a1202ad4a87f3a85", donation.getFundId());
        assertEquals("2023-06-26T04:03:57.997Z", donation.getDate());

    }

    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_ExceptionNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;

            }

        });


        Donation donation = dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        assertNull(donation);
    }

    // defensive programming tests (tests similar to RobustnessTest from Task 2.2)
    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_WebClientReturnsMalformedJSON() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "I AM NOT JSON!";
            }
        });
        dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient returns malformed JSON");
    }

    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_WebClientIsNull() {

        DataManager dm = new DataManager(null);
        dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient is null");

    }

    @Test(expected=IllegalArgumentException.class)
    public void testMakeDonation_ContributorIdIsNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.makeDonation(null, "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalArgumentxception when contributorId is null");

    }

    @Test(expected=IllegalArgumentException.class)
    public void testMakeDonation_fundIdIsNull() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.makeDonation("649873aaf59da441a42c1de1", null, 40);
        fail("DataManager.makeDonation does not throw IllegalArgumentxception when fundId is null");

    }

    // this assumes no server is running on port 3002
    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_WebClientCannotConnectToServer() {

        DataManager dm = new DataManager(new WebClient("localhost", 3002));
        dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient cannot connect to server");

    }

    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_WebClientReturnsError() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"error\":\"An unexpected database error occurred\"}";
            }
        });
        dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient returns error");

    }

    @Test(expected=IllegalStateException.class)
    public void testMakeDonation_FailureError() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":{\"_id\":\"MongoError\"}}";
            }

        });

        dm.makeDonation("649873aaf59da441a42c1de1", "64990c3a1202ad4a87f3a85", 40);
        fail("DataManager.makeDonation does not throw IllegalStateException when WebClient returns error");
    }


}
