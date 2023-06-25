import java.util.LinkedList;
import java.util.List;

public class Organization {
	
	private String id;
	private String name;
	private String description;
	private String password;
	
	private List<Fund> funds;
	
	public Organization(String id, String name, String description, String password) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.password = password;
		funds = new LinkedList<>();
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

	public String getPassword() {
		return password;
	}

	public List<Fund> getFunds() {
		return funds;
	}
	
	public void addFund(Fund fund) {
		funds.add(fund);
	}

	public void deleteFund(int fundNumber) {
		funds.remove(fundNumber - 1);
	}

	public void changePassword(String password) {
		this.password = password;
	}
	

}
