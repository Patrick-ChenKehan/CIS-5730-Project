import java.util.*;

public class UserInterface {


    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);

    private boolean verbose = true;
    Map<String, String> dateConversion = new HashMap<>();

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
        dateConversion.put("01", "January");
        dateConversion.put("02", "February");
        dateConversion.put("03", "March");
        dateConversion.put("04", "April");
        dateConversion.put("05", "May");
        dateConversion.put("06", "June");
        dateConversion.put("07", "July");
        dateConversion.put("08", "August");
        dateConversion.put("09", "September");
        dateConversion.put("10", "October");
        dateConversion.put("11", "November");
        dateConversion.put("12", "December");
        while (true) {
            System.out.println("\n\n");

            if (org.getFunds().size() > 0) {

                System.out.println("There are " + org.getFunds().size() + " funds in this organization:");
                int count = 1;
                for (Fund f : org.getFunds()) {
                    System.out.println(count + ": " + f.getName());
                    count++;
                }
                System.out.println("Enter the fund number to see more information or to make a donation to that fund.");
            }
            System.out.println("Enter 0 to create a new fund");
            System.out.println("Enter -1 to log out");
            System.out.println("Enter -2 to list ALL the contributions to this organization's funds");
            System.out.println("Enter -3 to change the password");
            System.out.println("Enter -4 to update the account information");

            // handle invalid option
            String option_str = in.nextLine().trim();
            while (!((option_str.matches("-?\\d+") &&
                    Integer.parseInt(option_str) <= org.getFunds().size() &&
                    Integer.parseInt(option_str) >= -4))) {
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
            } else if (option == -3) {
                changePassword();
            } else if (option == -4) {
                updateAccount();
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

    public void makeDonation(int fundNumber){
        System.out.print("Enter the contributorID: ");
        String contributorID = in.nextLine().trim();

        while (contributorID.isEmpty()) {
            System.out.print("ContributorID cannot be null. Please re-enter: ");
            contributorID = in.nextLine().trim();
        }
        try {
            String contributorName = dataManager.getContributorName(contributorID);
            if (contributorName == null){
                System.out.println("No contributor was found with this ID. Please try making donation again:");
                makeDonation(fundNumber);
            }
        } catch (Exception e){
            System.out.println("No contributor was found with this ID. Please try making donation again:");
            makeDonation(fundNumber);
        }

        System.out.print("Please enter the donation amount: ");
        String donationAmountStr = in.nextLine().trim();

        while (!donationAmountStr.matches("\\d+(\\.\\d+)?")) {
            System.out.print("Invalid Donation Amount. Please re-enter: ");
            donationAmountStr = in.nextLine().trim();
        }
        long donationAmount = (long)Double.parseDouble(donationAmountStr);
        Donation donation = null;
        try {
            donation = dataManager.makeDonation(contributorID, org.getFunds().get(fundNumber - 1).getId(), donationAmount);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error: Please try to make donation again: ");
            makeDonation(fundNumber);
        }
        List<Donation> allDonations = org.getFunds().get(fundNumber - 1).getDonations();
        allDonations.add(donation);
        org.getFunds().get(fundNumber - 1).setDonations(allDonations);
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
            displayFund(fundNumber); // Dangerous cumulating stacks
        } else if (choice.equals("y")) {
            try {
                dataManager.deleteFund(org.getFunds().get(fundNumber - 1).getId());
                org.deleteFund(fundNumber);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("There was a problem with deletion. Press y to try deleting again " +
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
            String fundName = fund.getName();
            List<Donation> donations = fund.getDonations();
            for (Donation d : donations){
                List<String> fundInfo = new ArrayList<>();
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
            month = dateConversion.get(month);
            if (month == null){
                month = "Invalid month";
            }
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
            donations.sort(Comparator.comparing(Donation::getAmount).reversed());
            for (Donation donation : donations) {
                //donations.sort((d1, d2) -> (int) d2.getAmount() - (int) d1.getAmount()); // Sort donations in descending order
                String origDate = donation.getDate();
                String year = origDate.substring(0, 4).trim();
                String month = origDate.substring(5, 7).trim();
                String date = origDate.substring(8, 10).trim();
                // task 1.9
                month = dateConversion.get(month);
                if (month == null){
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
            System.out.println("*Enter \"Donate\" to make a donation to this fund\n" +
                    "*Enter \"Agg\" to see donations now aggregated by contributor \n" +
                    "*Enter \"Delete\" to delete the fund\n" +
                    "*Enter any other to go back to the listing of funds");
            choice = in.nextLine().trim();
            if (choice.equals("Agg")) {
                verbose = false;
                System.out.println("Donation display changed to aggregated");
            }
        } else {
            System.out.println("*Enter \"Donate\" to make a donation to this fund\n" +
                    "*Enter \"Verbose\" to see individual donations \n" +
                    "*Enter \"Delete\" to delete the fund\n" +
                    "*Enter any other to go back to the listing of funds");
            choice = in.nextLine().trim();
            if (choice.equals("Verbose")) {
                verbose = true;
                System.out.println("Donation display changed to verbose");
            }
        }

        if (choice.equals("Donate")){
            makeDonation(fundNumber);
            displayFund(fundNumber); // after donation is successfully made, again display fund information
        }
        if (choice.equals("Delete"))
            deleteFund(fundNumber);

    }

    public void changePassword() {
        System.out.println("Please enter your current password: ");
        String old_password = in.nextLine().trim();
        if (!old_password.equals(org.getPassword())) {
            System.out.println("Password incorrect. Please try again.");
            return;
        }

        String new_password_1 = null;
        String new_password_2 = null;

        while (new_password_1 == null || new_password_2 == null || !new_password_1.equals(new_password_2)) {
            System.out.println("Please enter your new password: ");
            new_password_1 = in.nextLine().trim();
            if (new_password_1.length() == 0) {
                System.out.println("Password cannot be empty. Please re-enter the password.");
                continue;
            }
            System.out.println("Please enter your new password again: ");
            new_password_2 = in.nextLine().trim();
            if (!new_password_1.equals(new_password_2)) {
                System.out.println("Passwords not match. Please try again.");
                return;
            }
        }

        try {
            dataManager.changePassword(org, new_password_1);
            org.changePassword(new_password_1);
            System.out.println("Password changed successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateAccount() {
        System.out.println("Please enter your current password: ");
        String old_password = in.nextLine().trim();
        if (!old_password.equals(org.getPassword())) {
            System.out.println("Password incorrect. Please try again.");
            return;
        }

        System.out.println("Please enter new name:");
        String new_name = in.nextLine().trim();
        System.out.println("Please enter new description: ");
        String new_description = in.nextLine().trim();

        try {
            dataManager.updateAccount(org, new_name, new_description);
            org.changeName(new_name);
            org.changeDescription(new_description);
            System.out.println("Account updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));
        String login = "";
        String password = "";
        boolean provide = false;
        try {
            login = args[0];
            password = args[1];
            provide = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Credentials invalid/unprovided");
        }


        try {
            if (provide) {
                Organization org = ds.attemptLogin(login, password);
                if (org == null) {
                    System.out.println("Login fails");
                    provide = false;
                } else {
                    UserInterface ui = new UserInterface(ds, org);
                    ui.start();
                }
            }
            if (!provide) {
                while (true) {
                    System.out.println("1. Login");
                    System.out.println("2. Create a new organization");

                    System.out.print("Enter your choice: ");
                    Scanner in = new Scanner(System.in);
                    String option_str = in.nextLine().trim();
                    if (option_str.matches("1") || option_str.matches("2")) {
                        int choice = Integer.parseInt(option_str);
                        if (choice == 1) {
                            boolean loggedIn = false;
                            System.out.print("Enter your login: ");
                            login = in.nextLine().trim();

                            System.out.print("Enter your password: ");
                            password = in.nextLine().trim();


                            try {
                                Organization org = ds.attemptLogin(login, password);
                                if (org == null) {
                                    System.out.println("Login fails");
                                } else {
                                    System.out.println("Login successful.");
                                    UserInterface ui = new UserInterface(ds, org);
                                    ui.start();
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                        }

                        if (choice == 2) {
                            String newLogin, newPassword, orgName, orgDescription;
                            while(true) {
                                System.out.print("Enter a login: ");
                                newLogin = in.nextLine().trim();
                                if (newLogin != "") {
                                    break;
                                }else{
                                    System.out.println("Could not be blank");
                                }
                            }
                            while(true) {
                                System.out.print("Enter a password: ");
                                newPassword = in.nextLine().trim();
                                if (newPassword != "") {
                                    break;
                                }else{
                                    System.out.println("Could not be blank");
                                }
                            }
                            while(true) {
                                System.out.print("Enter an organization name: ");
                                orgName = in.nextLine().trim();
                                if (orgName != "") {
                                    break;
                                } else {
                                    System.out.println("Could not be blank");
                                }
                            }
                            while(true) {
                                System.out.print("Enter an organization description: ");
                                orgDescription = in.nextLine().trim();
                                if (orgDescription != "") {
                                    break;
                                } else {
                                    System.out.println("Could not be blank");
                                }
                            }

                            try{
                                Organization org = ds.createLogin(newLogin, newPassword, orgName, orgDescription);
                                if (org == null) {
                                    System.out.println("Login fails.");
                                } else {
                                    System.out.println("Login successful.");
                                    UserInterface ui = new UserInterface(ds, org);
                                    ui.start();
                                    break;
                                }
                            } catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                    }else{
                        System.out.println("Invalid.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
