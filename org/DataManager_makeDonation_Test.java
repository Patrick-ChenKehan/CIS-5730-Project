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
       // assertEquals("", donation.getFundId());
        assertEquals("2023-06-26T04:03:57.997Z", donation.getDate());
    }

}
