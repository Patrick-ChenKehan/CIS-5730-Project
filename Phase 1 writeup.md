# Project 1 Writeup

Authors: Arnav Gattani, Jun Wang, Kehan Chen 

GitHub repo URL: https://github.com/Patrick-ChenKehan/CIS-5730-Project 

1. The Additional Tasks that you would like graded for this phase. Please keep in mind that we will only grade three Additional Tasks for a group of three, and four Additional Tasks for a group of four. If you list more than that, the tasks that get graded will be decided at the grader’s discretion.

   **Answer:** We choose tasks **1.7, 1.8, and 1.9** to be graded.

2. For each task you completed in this phase (Required or Additional), a brief description of the changes you made to the provided code, e.g. the names of any classes or methods that were changed, new methods that were created, etc.

   **Answer:** 

     1. **Task 1.1:** We rewrite the test cases for attemptLogin, getContributorName and createFund method for the DataManager class with 100% statement coverage.

        **Task 1.2:** We do not find any bugs from the previous task.

        **Task 1.3:** Changed displayFund() method in UserInterface class. Now displays total Amounts of donations for each fund (by aggregating the fund total every time in through for loop). Found percentage of target by, dividing totalAmount of funds that we calculated divided by the target. We truncate this percentage to 2 decimal points and display information to users. 

        **Task 1.7:** Input of option and target are handled as string instead of integer. The program will now prompt the user to input the value again. If the input is of correct format, the input will be converted to an integer (if a double was imputed, it will return the truncated integer). These error checks were made in the UserInterface class in the createFund() method. 

        **Task 1.8:** Add programs handling the connection error and output differently from login fails. Changed attemptLogin() method in the DataManager class. 

        **Task 1.9:** We would like the *Organization (Java) app* to be graded. Changed the dusplayFund() method in the UserInterface class. We take the date each donation was made and extract the month, date, and year that donation was made. We then convert each month (as a number), to a month in letter (such as 01 => January). Then, we list the donation date in the format “June 5, 2023”, instead of 2023-06-05T04:21:04.807Z.

3. A description of any bugs that you found and fixed in Task 1.2 (and also in 1.4 if you chose to do it)

   **Answer:** No bugs are found in our task 1.1. The Json can take a double as an acceptable input for the target value of a fund, but this cannot be casted to a Long. However, we do not need to handle this for this phase of the project (confirmed by Dr. Murphy). 

4. Any known bugs or other issues with the tasks you attempted in this phase.

   **Answer:** /

5. Instructions on how to start each app, if you changed anything from the original version of the code, e.g. the name of the Java main class or JavaScript entry point, arguments to the programs, etc. If you did not change anything, you may omit this.

   **Answer:** Running instructions are the same as specified in the handout. Specify runtime arguments (login password) and run UserInterface as the Java main class is located in UserInterface. 

6. A brief but specific description of each team member’s contributions, including the task numbers that they worked on. Please do not simply write “all members contributed equally to all tasks” since we know that’s not really the case. 

   **Answer:**

     Jun Wang: Tasks 1.1(getContributorName and createFund), 1.2, 1.7

     Kehan Chen: Tasks 1.1 (attemptLogin), 1.2, 1.7, 1.8

     Arnav Gattani: Tasks 1.3 and 1.9. 