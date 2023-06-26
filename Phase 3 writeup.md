# Phase 3 writeup

Authors: Arnav Gattani, Jun Wang, Kehan Chen 

GitHub repo URL: https://github.com/Patrick-ChenKehan/CIS-5730-Project 

1. The tasks that you would like graded for this phase. Please keep in mind that we will only grade four tasks for a group of three, and five tasks for a group of four. If you list more than that, the tasks that get graded will be decided at the grader’s discretion.

   **Answer:** 3.1, 3.2, 3.3, 3.4

2. For each task you completed in this phase, a brief description of the changes you made to the provided code, e.g. the names of any classes or methods that were changed, new methods that were created, etc.

   **Answer:**

**3.1**: I first added a createLogin method for the DataManager Class that takes in the login, password, name and description of the newly created login. The method makeRequest to /createOrg with the above variables. The /createOrg in the api.js is similar to the /createOrg in the admin.js, creating a new login. Then this method calls the attemptLogin method of the DataManager Class that automatically logs the user in once the login is successfully created. I also modify the main method of the UserInterface Class, separating the case where the login and password is created and the case where they are not both provided. If they are not both provided, then there will be two options, one is to login with existing login and password, and the other one is to create a new login. If the user chooses to create a new login, then they will be guided to enter the non-empty login, password, name and description. Then the main method will call the createLogin method of the DataManager Class. 
**3.2**: We implemented the password changing by adding password to the `Organization` class, sending requests within `DataManager` and adding corresponding prompts in `UserInterface` class. /updateOrg endpoint was added to api.js to deal with password and account information update.
**3.3**: Similar to 3.2, necessary changes were made in `Organization` class. Requests are still made in `DataManager` to the /updateOrg and corresponding prompts in `UserInterface` are implemented. If the user does not want to the change the name and/or description they can press enter or leave the prompt blank, to keep the original field(s) without any change.
**3.4**: I created a new method in the ‘DataManager’ class called makeDonation(…) and make requests to /makeDonations in api.js (similar to the one in admin.js). DataManager.makeDonation(…) which takes in the contributerID, fundID, and the donation amount as inputs, is called in the UserInterface class and a method called makeDonation() is implemented so that when a user views a fund, if they want to donate, the method takes the fundID of that fund and prompts the user to input the contributorID and the donation amount, and feeds these inputs to the madeDonation() method in the DataManager class to add the donation to the database and it will also add the donation to the respective Fund.

3. Any known bugs or other issues with the tasks you attempted in this phase.

  **Answer:** None

4. Instructions on how to start each app, if you changed anything from the original version of the code, e.g. the name of the Java main class or JavaScript entry point, arguments to the programs, etc. If you did not change anything, you may omit this.

  **Answer:** 
The user is still allowed to pass in login and password as runtime argument, but now, you may enter the program without a login and password and can then either login or create a new organization that will automatically log the user in.

 5. A brief but specific description of each team member’s contributions, including the task numbers that they worked on.

   **Answer:**

   | Team member   | Tasks       |
   | ------------- | ----------- |
   | Kehan Chen    | 3.1 and 3.3 |
   | Jun Wang      | 3.2 and 3.3 |
   | Arnav Gattani | 3.4 and 3.3 |

**ALL** team members supported each other on their own tasks by fixing old mistakes, checking test coverage, defensive programming and debugging.
