# Project Phase 2 Writeup

Authors: Arnav Gattani, Jun Wang, Kehan Chen 

GitHub repo URL: https://github.com/Patrick-ChenKehan/CIS-5730-Project 

  1. The Additional Tasks that you would like graded for this phase. Please keep in mind that we will only grade three Additional Tasks for a group of three, and four Additional Tasks for a group of four. If you list more than that, the tasks that get graded will be decided at the grader’s discretion.

  **Answer:** 2.7, 2.8, 2.9

  2. For each task you completed in this phase (Required or Additional), a brief description of the changes you made to the provided code, e.g. the names of any classes or methods that were changed, new methods that were created, etc. <br>
    **Answer:**
    2.1: We create a private map in the DataManager Class, and for every time the getContributorName method encounters an id, it first checks whether the map contains the id as a key. If yes, the method returns the name stored in the map. If not, the method will make requests via the WebClient for information.
    2.2: All the Tests in the provided test suit were passed after changing the DataManager class accordingly. We modified the UserInterface class so that depending on the error, it prints out the relevant error message that we set, when the error is thrown in the DataManager class. After it is thrown it reprints the user without terminating the program. (Only exception is if the initial login password are incorrect, since then the user is not automatically prompted and must change the runtime arguments and re-run the UserInterface class to proceed)
    2.3: We created an attribute `verbose` indicating whether we are listing donations in the original verbose way or aggregated way. All displays follow a descending order in donation amounts. Aggregated donations are calculated and stored in the `Fund` class upon the `setDonation()` method being called. Added aggregate option in displayFund() method in the UserInterface class. When the user types a fund number to see more information, they have the option to type “Agg”. This will take them back to the main lobby listing all the funds. Now if the user types any number associated with a fund, they will see the donations aggregated by contributor, rather than individual donations. Then, the user can type “Verbose” to go back to the mode seeing individual donations rather than the aggregate and any fund they view will display individuals donations rather than the aggregate. 
    2.7: We add an option to delete a fund when the user looks into it with a further prompt for confirmation. The user interface sends a deletion request to RESTful API and deletes the fund locally stored. Connection error and failed deletion are handled with exception. We created the new deleteFund method in the UerInterface class. 
    2.8: We set the rule that if the user entering -1 in the menu will log out the app. Once the user logs out, the program will ask the user to enter the login and password to login back again. If the user enters an invalid login or password, the program will prompt the user to enter again until a correct login and password are received. We created the new login method in the UerInterface class. 
    2.9: To list all the contributions made by all the funds in that organization, the user must enter -2 in the menu on the page that lists all the funds. When -2 is entered, each donation is listed with the name of the fund, the donation amount, and the date of the donation ranked in the order of when the donation was made. If any key is pressed, it goes back to the original menu. We created and utilized the displayAllContributions method in the User Interface class. 
    
  3. Any known bugs or other issues with the tasks you attempted in this phase.

      **Answer:** Some of the older tests that tested the attemptLogin, createFund, and getContributeName methods in the DataManager    class now fail as we handle issues in the DataManager class differently based on teh specifications of the RobustnessTest given to us. Hence, we changed the failing tests based on our updated code and marked them so it is evident to the reader. 
    
  4. Instructions on how to start each app, if you changed anything from the original version of the code, e.g. the name of the Java main class or JavaScript entry point, arguments to the programs, etc. If you did not change anything, you may omit this.

    **Answer:** Nothing was changed. To run the program, still run UserInterface with username as the first term and the password as the second term.

  5. A brief but specific description of each team member’s contributions, including the task numbers that they worked on.

    **Answer:**
    Kehan Chen: tasks 2.3 and 2.7
    Jun Wang: tasks 2.1 and 2.8
    Arnav Gattani: tasks 2.2 and 2.9

