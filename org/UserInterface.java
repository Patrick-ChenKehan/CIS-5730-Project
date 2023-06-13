import java.util.*;

public class UserInterface {


    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);

    private boolean verbose = true;

    public UserInterface(DataManager dataManager, Organization org) {
        this.dataManager = dataManager;
        this.org = org;
    }

    public void logout() {
        org = null; // Reset the organization
        System.out.println("Logged out successfully.");
        System.out.println("Please log in to continue.");
        login();
    }

    public void login() {

        boolean loggedIn = false;
        while(!loggedIn) {
            System.out.print("Enter your login: ");
            String login = in.nextLine().trim();

            System.out.print("Enter your password: ");
            String password = in.nextLine().trim();


            try {
                Organization newOrg = dataManager.attemptLogin(login, password);
                    org = newOrg;
                    System.out.println("Login successful.");
                    loggedIn = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Try re-inputing credentials: ");
            } catch (IllegalStateException e){
                System.out.println(e.getMessage());
            }
        }
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
            System.out.println("Enter -2 to list ALL the contributions to this organization's funds");
            System.out.println("Enter -1 to log out");

            // handle invalid option
            String option_str = in.nextLine().trim();
            while (!((option_str.matches("-?\\d+") &&
                    Integer.parseInt(option_str) <= org.getFunds().size() &&
                    Integer.parseInt(option_str) >= -2))) {
                System.out.print("Option invalid. Please re-enter your option: ");
                option_str = in.nextLine().trim();
            }
            int option = Integer.parseInt(option_str);

            if (option == 0) {
                createFund();
            } else if (option == -1) {
                logout();
            } else if (option == -2) {
                displayAllContributions();
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
        while (!target_str.matches("\\d+(\\.\\d+)?")) {
            System.out.print("Target should be a number. Please re-enter the target: ");
            target_str = in.nextLine().trim();
        }
        long target = (long)Double.parseDouble(target_str);
        Fund fund = null;
        try {
            fund = dataManager.createFund(org.getId(), name, description, target);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Please try to create fund again: ");
            createFund();
        }
        org.getFunds().add(fund);

    }

    public void deleteFund(int fundNumber) {
        System.out.println("Are you sure you want to delete fund " + fundNumber +"? (y/n)");
        String choice = in.nextLine();
        while (!choice.equals("y") && !choice.equals("n")) {
            System.out.println("Choice invalid. Are you sure you want to delete fund " + fundNumber +"? (y/n)");
            choice = in.nextLine();
        }
        if (choice.equals("n")) {
            System.out.println("Deletion canceled for fund "+ fundNumber);
        } else if (choice.equals("y")) {
            try {
                dataManager.deleteFund(org.getFunds().get(fundNumber - 1).getId());
                org.deleteFund(fundNumber);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("There was a problem with deletion. Press y to try deleting again" +
                        "or else press n to return to main menu");
            }
        }
    }

    public void displayAllContributions() {
        System.out.println("All donations are ranked by decreasing order of date and time donated. \n" +
                "The contributions for the funds in this organization are as follows: ");
        List<List<String>> allContributions = new ArrayList<>();
        List<Fund> allFunds = org.getFunds();
        for (Fund fund: allFunds) {
            List<String> fundInfo = new ArrayList<>();
            String fundName = fund.getName();
            List<Donation> donations = fund.getDonations();
            for (Donation d : donations){
                fundInfo.add(fundName); // fund name (0)
                fundInfo.add("$" + Long.toString(d.getAmount())); // donation amount (1)
                fundInfo.add(d.getDate()); // donation date (2)
                allContributions.add(fundInfo);
            }
        }

        // sort each donation by time regardless of fund
        Collections.sort(allContributions, (d1, d2) -> d1.get(2).compareTo(d2.get(2)));
        Collections.reverse(allContributions);
        for (List<String> donation : allContributions){
            String origDate = donation.get(2);
            String year = origDate.substring(0, 4).trim();
            String month = origDate.substring(5, 7).trim();
            String date = origDate.substring(8, 10).trim();
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
            } else {
                month = "Invalid month";
            }

            //System.out.println("* Fund: " + donation.get(0) + " -> " + donation.get(1) + " on " + donation.get(2));
            System.out.println("* Fund: " + donation.get(0) + " -> " + donation.get(1) + " donated on " + month + " " + date + ", " + year);
        }

        System.out.println("Press enter to go back");
        in.nextLine();

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
        double totalAmount = 0;
        double percent = 0.0;
        if (verbose) { // If in verbose mode
            for (Donation donation : donations) {
                donations.sort((d1, d2) -> (int) d2.getAmount() - (int) d1.getAmount()); // Sort donations in descending order
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
                } else {
                    month = "Invalid month";
                }
                try {
                    System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + month + " " + date + ", " + year);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    return;
                }
                totalAmount += donation.getAmount();
            }
        } else { // If in aggregated mode
            HashMap<String, Integer> aggregated_donation_number = fund.getAggregatedDonationNumber();
            HashMap<String, Long> aggregated_donation_amount = fund.getAggregatedDonationAmount();

            List<String> contributors = new ArrayList<>(aggregated_donation_amount.keySet());
            // Sort contributors in descending order
            contributors.sort((n1, n2) ->
                    aggregated_donation_amount.getOrDefault(n2, 0L).intValue() -
                            aggregated_donation_amount.getOrDefault(n1, 0L).intValue());

            for (String contributorName: contributors) {
                int num_donations = aggregated_donation_number.getOrDefault(contributorName, 0);
                long amount_donations = aggregated_donation_amount.getOrDefault(contributorName, 0L);
                totalAmount += (double)amount_donations; // update total amount
                System.out.println("* " + contributorName + ", " + num_donations + " donations, $" + amount_donations + " total");
            }
        }
        // task 1.3
        percent = totalAmount / fund.getTarget() * 100;
        percent = ((double) ((int) (percent * 100.0))) / 100.0; // trim to two decimal places
        if (percent >= 100.00) {
            percent = 100.00;
        }
        System.out.println("Total donation amount: $" + totalAmount + " (" + percent + "% of target)");
        String choice;
        if (verbose) {
            System.out.println("*Enter \"Agg\" to see contributor summary \n" +
                    "*Enter \"Delete\" to delete the fund\n" +
                    "*Enter any other to go back to the listing of funds");
            choice = in.nextLine().trim();
            if (choice.equals("Agg")) {
                verbose = false;
                System.out.println("Donation display changed to aggregated");
            }
        } else {
            System.out.println("*Enter \"Verbose\" to see contributor summary \n" +
                    "*Enter \"Delete\" to delete the fund\n" +
                    "*Enter any other to go back to the listing of funds");
            choice = in.nextLine().trim();
            if (choice.equals("Verbose")) {
                verbose = true;
                System.out.println("Donation display changed to verbose");
            }
        }

        if (choice.equals("Delete"))
            deleteFund(fundNumber);

    }



    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));
        String login ;
        String password;
        try {
            login = args[0];
            password = args[1];
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Credentials invalid/unprovided");
            return;
        }

        Organization org = null;
        try {
            org = ds.attemptLogin(login, password);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("One or more credentials are invalid. Please try again with new runtime arguments");
            return;
        } catch (IllegalStateException e){
            System.out.println(e.getMessage());
            System.out.println("Credentials not found. Please try again with new runtime arguments.");
            return;
        }

        try {
            UserInterface ui = new UserInterface(ds, org);
            ui.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
