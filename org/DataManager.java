
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataManager {

	private final WebClient client;

	public DataManager(WebClient client) {
		this.client = client;
	}

	private  Map<String, String> results = new HashMap<>();


	public Donation makeDonation(String contributorID, String fundId, long fundAmount){
		try{
			Map<String, Object> map = new HashMap<>();
			map.put("contributor", contributorID);
			map.put("fund", fundId);
			map.put("amount", fundAmount);
			String response = client.makeRequest("/makeDonation", map);

			if (response == null)
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")){
				JSONObject donation = (JSONObject)json.get("data");
				String contributorId = (String)donation.get("contributor");
				String contributorName = this.getContributorName(contributorId);
				long amount = (Long)donation.get("amount");
				String date = (String)donation.get("date");
				return new Donation(fundId, contributorName, amount, date);
			}
			else return null;

		} catch (Exception e){
			throw new IllegalStateException(e.getMessage());
		}
	}


	public Organization createLogin(String login, String password, String name, String description){
		try{
			Map<String, Object> map = new HashMap<>();
			map.put("login", login);
			map.put("password", password);
			map.put("name", name);
			map.put("description", description);
			String response = client.makeRequest("/createOrg", map);

			if (response == null)
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")){
				return attemptLogin(login, password);
			}
			else return null;


		}catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * Attempt to log the user into an Organization account using the login and password.
	 * This method uses the /findOrgByLoginAndPassword endpoint in the API
	 * @return an Organization object if successful; null if unsuccessful
	 */
	public Organization attemptLogin(String login, String password) {
		if (login == null){
			throw new IllegalArgumentException("Please reenter credentials: null login");
		}
		if (password == null){
			throw new IllegalArgumentException("Please reenter credentials: null password");
		}
			Map<String, Object> map = new HashMap<>();
			map.put("login", login);
			map.put("password", password);
			if (client == null){
				throw new IllegalStateException("Error while logging in: Webclient is null");
			}
			String response = client.makeRequest("/findOrgByLoginAndPassword", map);

			if (response == null) // Task 1.8 Handle failed connection
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json ;
			try {
				json = (JSONObject) parser.parse(response);
			} catch (Exception e){
				throw new IllegalStateException("Error while logging in");
			}
			String status = (String)json.get("status");

			if (status.equals("success")) {
				JSONObject data = (JSONObject)json.get("data");
				String fundId = (String)data.get("_id");
				String name = (String)data.get("name");
				String description = (String)data.get("description");
				String correct_password = (String)data.get("password");
				Organization org = new Organization(fundId, name, description, correct_password);

				JSONArray funds = (JSONArray)data.get("funds");
				Iterator it = funds.iterator();
				while(it.hasNext()){
					JSONObject fund = (JSONObject) it.next();
					fundId = (String)fund.get("_id");
					name = (String)fund.get("name");
					description = (String)fund.get("description");
					long target = (Long)fund.get("target");

					Fund newFund = new Fund(fundId, name, description, target);

					JSONArray donations = (JSONArray)fund.get("donations");
					List<Donation> donationList = new LinkedList<>();
					Iterator it2 = donations.iterator();
					while(it2.hasNext()){
						JSONObject donation = (JSONObject) it2.next();
						String contributorId = (String)donation.get("contributor");
						String contributorName = this.getContributorName(contributorId);
						long amount = (Long)donation.get("amount");
						String date = (String)donation.get("date");
						donationList.add(new Donation(fundId, contributorName, amount, date));
					}

					newFund.setDonations(donationList);

					org.addFund(newFund);

				}

				return org;
			}
			else throw new IllegalStateException("Login failed due to invalid login credentials.");

	}

	/**
	 * Look up the name of the contributor with the specified ID.
	 * This method uses the /findContributorNameById endpoint in the API.
	 * @return the name of the contributor on success; null if no contributor is found
	 */
	public String getContributorName(String id) {
			if (id == null){
				throw new IllegalArgumentException("Null id: Please reenter credentials");
			}
			if (results.containsKey(id)) {
				return results.get(id);
			} else {

				Map<String, Object> map = new HashMap<>();
				map.put("id", id);

				if (client == null){
					throw new IllegalStateException("Error: Webclient is null");
				}
				String response = client.makeRequest("/findContributorNameById", map);

				if (response == null) // Handle failed connection
					throw new IllegalStateException("Error in communicating with server");
				JSONParser parser = new JSONParser();
				JSONObject json;
				try {
					json = (JSONObject) parser.parse(response);
				} catch (Exception e) {
					throw new IllegalStateException();
				}
				String status = (String) json.get("status");

				if (status.equals("success")) {
					String name = (String) json.get("data");
					results.put(id, name);
					return name;
				} else throw new IllegalStateException("Failed getting contributer. WebClient returns null");


			}


	}

	/**
	 * This method creates a new fund in the database using the /createFund endpoint in the API
	 * @return a new Fund object if successful; null if unsuccessful
	 */
	public Fund createFund(String orgId, String name, String description, long target) {
			if (orgId == null){
				throw new IllegalArgumentException("Invalid input: OrgID was null");
			}
			if (name == null){
				throw new IllegalArgumentException("Invalid input: name was null");
			}
			if (description == null){
				throw new IllegalArgumentException("Invalid input: description is null");
			}

			Map<String, Object> map = new HashMap<>();
			map.put("orgId", orgId);
			map.put("name", name);
			map.put("description", description);
			map.put("target", target);
			if (client == null){
				throw new IllegalStateException("Error in creating Fund");
			}
			String response = client.makeRequest("/createFund", map);

			if (response == null) // Handle failed connection
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json;
			try {
				json = (JSONObject) parser.parse(response);
			} catch (Exception e){
				throw new IllegalStateException("Error while creating fund");
			}
			String status = (String)json.get("status");

			if (status.equals("success")) {
				JSONObject fund = (JSONObject)json.get("data");
				String fundId = (String)fund.get("_id");
				return new Fund(fundId, name, description, target);
			}
			else throw new IllegalStateException("Creating fund failed. Please try creating fund again");


	}

	public void deleteFund(String fundID) {
		if (fundID == null){
			throw new IllegalArgumentException("Input was invalid: fundID is null.");
		}
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", fundID);
			if (client == null){
				throw new IllegalStateException("Could not delete the fund.");
			}
			String response = client.makeRequest("/deleteFund", map);

			if (response == null) // Handle failed connection
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("error")) {
				throw new IllegalStateException("Deletion failed. Please try again.");
			}

		}
		catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public void changePassword(String org_id, String new_password) {
		if (org_id == null || new_password == null || new_password.isEmpty())
			throw new IllegalArgumentException("org does not exist");

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", org_id);
			map.put("password", new_password);
			String response = client.makeRequest("/updateOrg", map);

			if (response == null) // Handle failed connection
				throw new IllegalStateException("Error in communicating with server");

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("error")) {
				throw new IllegalStateException("Deletion failed. Please try again.");
			}

		}
		catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}

	}

}
