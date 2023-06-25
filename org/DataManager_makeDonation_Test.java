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
                return "{\"status\":\"success\"}";
            }

        });


        Donation donation = dm.makeDonation("649873aaf59da441a42c1de1", "64988c3cd99e3741cd73d527", 70);
        assertNotNull(donation);
        assertEquals("", donation.getAmount());
        assertEquals("", donation.getContributorName());
        assertEquals("", donation.getFundId());
        assertEquals(10000, donation.getDate());
    }

}
