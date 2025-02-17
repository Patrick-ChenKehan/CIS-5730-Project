import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_changePassword_Test {
    @Test
    public void test_successful_change() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1999\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                else if (resource == "/updateOrg")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                return null;
            }
        });
        Organization org = dm.attemptLogin("Patrick", "1999");
        dm.changePassword(org, "new password");
    }

    @Test (expected = IllegalStateException.class)
    public void test_failed_change() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1999\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                else if (resource == "/updateOrg")
                    return "{\"status\":\"error\",\"data\":\"wj\"}";
                return null;
            }
        });
        Organization org = dm.attemptLogin("Patrick", "1999");
        dm.changePassword(org, "new password");
    }

    @Test (expected = IllegalStateException.class)
    public void test_failed_connection() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1999\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                else if (resource == "/updateOrg")
                    return null;
                return null;
            }
        });
        Organization org = dm.attemptLogin("Patrick", "1999");
        dm.changePassword(org, "new password");
    }

    @Test (expected = IllegalArgumentException.class)
    public void test_Illegal_Password() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1999\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                else if (resource == "/updateOrg")
                    return "{\"status\":\"error\",\"data\":\"wj\"}";
                return null;
            }
        });
        Organization org = dm.attemptLogin("Patrick", "1999");
        dm.changePassword(org, "");
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource == "/findOrgByLoginAndPassword")
                    return "{\"status\":\"success\",\"data\":{\"_id\":\"123\",\"login\":\"Patrick\",\"password\":\"1999\"," +
                            "\"name\":\"Patrick\",\"description\":\"KC\",\"funds\":[" +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[{\"_id\":\"647a134ed830f6d7d4353a84\",\"contributor\":\"6479e484e99150cda7f6dd78\",\"fund\":\"6479e450e99150cda7f6dd6f\",\"date\":\"2023-06-02T16:05:34.735Z\",\"amount\":100,\"__v\":0}],\"__v\":0}," +
                            "{\"target\":5000," +
                            "\"_id\":\"6479e450e99150cda7f6dd6f\",\"name\":\"Philan\",\"description\":\"Children Fund\"," +
                            "\"org\":\"6479e41ce99150cda7f6dd6c\",\"donations\":[],\"__v\":0}" + "],\"__v\":0}}";
                else if (resource == "/findContributorNameById")
                    return "{\"status\":\"success\",\"data\":\"wj\"}";
                else if (resource == "/updateOrg")
                    return "{\"status\":\"error\",\"data\":\"wj\"}";
                return null;
            }
        });
        Organization org = dm.attemptLogin("Patrick", "1999");
        dm = new DataManager(null);
        dm.changePassword(org, "pass");
        fail("DataManager.changePassword does not throw IllegalStateException when WebClient is null");

    }


    @Test(expected=IllegalStateException.class)
    public void testChangePassword_WebClientReturnsMalformedJSON() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return "{\"status\":\"success\",\"data\":{\"_id\":\"jw3\", \"name\":\"test\", \"description\":\"sse\",\"funds\":[]}}";
            }
        });

        Organization org = dm.attemptLogin("Patrick", "1999");
        dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return "I AM NOT JSON!";
            }
        });
        dm.changePassword(org, "pass");
        fail("DataManager.changePassWord does not throw IllegalStateException when WebClient is null");
    }


    @Test (expected = IllegalArgumentException.class)
    public void test_Illegal_OrgInput() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001){
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams){
                return "{\"status\":\"success\",\"data\":{\"_id\":\"jw3\", \"name\":\"test\", \"description\":\"sse\",\"funds\":[]}}";
            }
        });
        Organization org = null;
        dm.changePassword(org, "pass");
    }
}
