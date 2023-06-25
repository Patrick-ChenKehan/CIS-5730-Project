import java.util.*;

public class Fund {

	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;

	// Used for aggregated donations and order
	private HashMap<String, Integer> aggregated_donation_number;
	private HashMap<String, Long> aggregated_donation_amount;
	
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
		aggregated_donation_number = new HashMap<>();
		aggregated_donation_amount = new HashMap<>();

	}

	private void setAggregatedDonations() {
		aggregated_donation_number.clear();
		aggregated_donation_amount.clear();
		for (Donation donation:donations) {
			String contributorName = donation.getContributorName();
			aggregated_donation_number.put(contributorName,
					aggregated_donation_number.getOrDefault(contributorName, 0) + 1);
			aggregated_donation_amount.put(contributorName,
					aggregated_donation_amount.getOrDefault(contributorName, 0L) + donation.getAmount());
		}
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getTarget() {
		return target;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
		// Calculate aggregated donations and store
		this.setAggregatedDonations();
	}
	
	public List<Donation> getDonations() {
		return donations;
	}

	public HashMap<String, Integer> getAggregatedDonationNumber() {
		return aggregated_donation_number;
	}

	public HashMap<String, Long> getAggregatedDonationAmount() {
		return aggregated_donation_amount;
	}
	
}
