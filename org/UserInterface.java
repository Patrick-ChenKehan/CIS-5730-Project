import java.util.List;
import java.util.Scanner;

public class UserInterface {


    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);

    public UserInterface(DataManager dataManager, Organization org) {
        this.dataManager = dataManager;
        this.org = org;
    }

    public void start() {

        while (true) {
            System.out.println("\n\n");
            if (org.getFunds().size() > 0) {
                System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

                int count = 1;
                for (Fund f : org.getFunds()) {

                    System.out.println(count + ": " + f.getName());

                    count++;
                }
                System.out.println("Enter the fund number to see more information.");
            }
            System.out.println("Enter 0 to create a new fund");

            // handle invalid option
            String option_str = in.nextLine().trim();
            while (!(option_str.matches("\\d+") && Integer.parseInt(option_str) <= org.getFunds().size())) {
                System.out.print("Option invalid. Please re-enter your option: ");
                option_str = in.nextLine().trim();
            }
            int option = Integer.parseInt(option_str);


            if (option == 0) {
                createFund();
            } else {
                displayFund(option);
            }
        }

    }

    public void createFund() {

        System.out.print("Enter the fund name: ");
        String name = in.nextLine().trim();

        while (name.isEmpty()) { // Task 1.7
            System.out.print("Name cannot be null. Please re-enter the fund name: ");
            name = in.nextLine().trim();
        }

        System.out.print("Enter the fund description: ");
        String description = in.nextLine().trim();

        System.out.print("Enter the fund target: ");
        String target_str = in.nextLine().trim();
        while (!target_str.matches("-?\\d+(\\.\\d+)?")) {
            System.out.print("Target should be a number. Please re-enter the target: ");
            target_str = in.nextLine().trim();
        }
        long target = Integer.parseInt(target_str);

        Fund fund = dataManager.createFund(org.getId(), name, description, target);
        org.getFunds().add(fund);


    }


    public void displayFund(int fundNumber) {

        Fund fund = org.getFunds().get(fundNumber - 1);

        System.out.println("\n\n");
        System.out.println("Here is information about this fund:");
        System.out.println("Name: " + fund.getName());
        System.out.println("Description: " + fund.getDescription());
        System.out.println("Target: $" + fund.getTarget());

        List<Donation> donations = fund.getDonations();
        System.out.println("Number of donations: " + donations.size());
        long totalAmount = 0;
        double percent = 0.0;
        for (Donation donation : donations) {
            String origDate = donation.getDate();
            String year = origDate.substring(0, 4).trim();
            String month = origDate.substring(5, 7).trim();
            String date = origDate.substring(8, 10).trim();
            // task 1.9
            if (month.equals("01")) {
                month = "January";
            } else if (month.equals("02")) {
                month = "February";
            } else if (month.equals("03")) {
                month = "March";
            } else if (month.equals("04")) {
                month = "April";
            } else if (month.equals("05")) {
                month = "May";
            } else if (month.equals("06")) {
                month = "June";
            } else if (month.equals("07")) {
                month = "July";
            } else if (month.equals("08")) {
                month = "August";
            } else if (month.equals("09")) {
                month = "September";
            } else if (month.equals("10")) {
                month = "October";
            } else if (month.equals("11")) {
                month = "November";
            } else if (month.equals("12")) {
                month = "December";
            }
            System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + month + " " + date + ", " + year);
            totalAmount += donation.getAmount();
        }
        // task 1.3
        percent = totalAmount / fund.getTarget() * 100;
        percent = ((double) ((int) (percent * 100.0))) / 100.0; // trim to two decimal places
        if (percent >= 100.00) {
            percent = 100.00;
        }
        System.out.println("Total donation amount: $" + totalAmount + " (" + percent + "% of target)");


        System.out.println("Press the Enter key to go back to the listing of funds");
        in.nextLine();
    }


    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));

        String login = args[0];
        String password = args[1];


        try {
            Organization org = ds.attemptLogin(login, password);

            if (org == null) {
                System.out.println("Login failed.");
            } else {

                UserInterface ui = new UserInterface(ds, org);

                ui.start();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
