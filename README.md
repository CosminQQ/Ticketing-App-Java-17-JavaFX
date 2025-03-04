# Ticketing-App-Java-17-JavaFX
documentation in .rtf file
postgres DDL in .txt


Ticket Master

1. Project Overview
1.1 Introduction
Ticket Master is a JavaFX-based application for buying concert tickets. The application features a user-friendly graphical interface with screens for browsing concerts, selecting tickets, buying tickets and managing a history of the user purchases as well as login and register interfaces. The project involves Java17 and JavaFX 17 for backend, FXML for the frontend and Postgres SQL for database. The project has implemented functionalities for purchasing tickets, handling user purchase history, and managing user authentication and registration. Different user types are present each of the user type having different functionalities.
The purpose of the app is to create a user-friendly and efficient platform for users to browse, select, and purchase concert tickets. The application facilitates the ticket-buying process by providing a graphical interface for users to explore available concerts, choose tickets, and manage their shopping carts seamlessly. As well as for special users(admins) to create special(admin) accounts.

1.2 Features
-logging in page with verification of user data
 
-register page with verification for user registration
 
-welcome/home page with special design
 
-store page where information about concerts is given as well as the ability to buy different tickets (if available).
 
-my tickets/purchase history page where information about past purchases presented
 
-account page where the credentials of the user can be changed, includes fields to add a credit card, and for special users implements specific functionalities
 

-log out button
-exit button
	
 
1.3 Technologies Used
Programming Language:
Java 17: The primary programming language used for the backend logic and application development.
Java Libraries and Frameworks: JavaFX with its libraries for creating and managing the GUI and handling collections of data, Java libraries for special data structures and connecting and interact with a database.
JavaFX: A Java library for building rich client applications with a modern look and feel. It's used for creating the graphical user interface (GUI) of the application.
Database: JDBC (Java Database Connectivity): A Java API for connecting and interacting with relational databases. It's used for database connectivity.
Database Management System (DBMS): PostgreSQL: The choice of the database management system for storing and retrieving data related to concerts, ticket types, and user information.
Integrated Development Environment (IDE): IntelliJ IDEA: Common Java IDEs that provide a development environment for coding, debugging, and testing Java applications. 

2. Getting Started. Prerequisites.
•	Java 17,
•	JavaFX 17, 
•	PostgreSQL database DDL is in file ‘DatabaseDDL’

3. Architecture
3.1 High-Level Architecture
The SRC file has 1 folder: 
•	-main that contains: -the java file with the models and controllers of the project
•	-resources with .FXML files (view elements)  
 
 
3.2 Design Patterns Used
I have use MVC design pattern that stands for Model – View – Controller
In the model package there are java classes for: -the database connection - the database details are hardcoded (DBUtils.java), - store user data (UserData.java), -store card details (CardDetails.java), -store concert details(ConcertData.java), -store ticket types(TicketType.java), -store tickets bought(TicketSold.java, TicketsBought.java, PurchaseBagData.java), -store information about the current user during running time(SessionManager.java);
In the view package (in resources folder actually called ‘com.example.concertticketsbuy’) there are java classes for: -the Graphical User Interface of: Log In Page Interface (LogIn.fxml), Register Page Interface (SingUp.fxml), Welcome/Home Page Interface (MainUserMenu.fxml), Store Page Interface (StoreMenu.fxml), My Tickets/Purchase History Page Interface (ShoppingCartMenu.fxml).
In the controller package: There are java classes used for communication between the Graphical User Interfaces and the Database, implementing different functionalities. The classes rundown is: -controller of the login page (Controller.java), -controller of the register page(SingUPController.java), -controller of welcome/home page (MainMenucontroller.java), -controller of store page(StoreMeniuController.java) – controller of purchasing history tab(ShoppingCartMenuController.java), -account page controller(UserAccountMenuController.java).
The model and controller packages can be found inside package ‘com.example.concertticketbuy’.
  
 
4. Code Documentation. Class and Method Documentation.
4.1 Log In Screen:
In the Log In Controller class the most important method is loginButtonAction(). This method checks if the username and password match to any other account in the users table if not it prints an error message label to the user. It calls method validateLogin() to establish a database connection and perform a query with the user inputs returning true if the username and password are correct or false elsewhere. If the validation return to be true than the user is send to the Home screen and the username is saved to the session manager class, on the contrary relevant error messages are printed. Other methods implement functionalities for the Register (change the scene to register page) and Cancel button (end the execution of the program).
  
4.2 Registe Screen:
This class implements the functionality of SingUp.fxml. When a set of full account information is received the method validateSingUp() checks after the register button is pressed whether or not all text fields are empty then is calls functions like: validateUsername to check if the username is unique in the database, validateEmailAdress to check if the inserted email address is valid, validatePhoneNumber to check if the phone number inserted is valid and validatePassword to check if the inputted password is secure enough. In the case of global validation, the scene in changed to the log in page, elsewhere an error message label is printed. 
4.2 Home Page Screen(MainMeniuController.java):
This class only implements the menu sidebar with 6 buttons: Home, Store, MyTickets, Account, Log Out and Exit.
 
4.3 Account screen(UserAccountMeniuController.java):
In this class there is the initialize() method that based on the username stored in session manager class retrieve all information available about the user from the users table, most importantly the user id. Now using the user id the methods changeUserDataOnAction, addUserCardOnAction, deleteUserCardOnAction and addAdminAccount can function properly and perform what their name says. 
It is important to mention that addAdminAccount is used only when an admin type user press the button ‘Change!’ as an admin cannot change its credentials can only create other admin accounts. It is important to mention that an admin account is immediately generated from the database DDL (admin ‘1234’). Also, addUserCardOnAction cannot be used if a card is already registered for the respective user. This function use validation methods to be sure that the user enters valid card data.
The delete account button functionality is not yet implemented!
4.4 Store Screen (StoreMenuController.java):
The initialize() method uses JavaFX functions to create and display the band information as a table.
populateTableView() populates the table with the bands and band information from database.
setupBuyColumn() add to every row a cell with a ‘Details’ Button. 
When the ‘Details’ Button is pressed the session manager stores the details about the band that was selected. And check the location of the concert to find what ticket types are available. It prints to a special label the information about the available seats. The user needs now, if he wants to buy tickets to the selected concert, to insert the ticket type and the number of tickets. It is important to mention that the ticket types are printed to a special label so that the user can see the available seats and their location on the arena. He basically inserts the location in the arena, and the number of tickets of that type he wants to buy. There is a ‘Total’ button which he can press to calculate how much he must pay and a Buy button that check if there are still tickets available to the concert in the selected arena location. If there are no more tickets it prints an error message. If there are seats available, then the order is stored in the database via session manager. 
4.5 My Tickets Screen (ShoppingCartMeniuController.java):
This class uses a new type of data described in class PurchaseDataBag.java this type of structure contains combined details about the tickets type, the concert and the user. The tickets bought by a user are taken from the database and passed to the method as a list. The table prints to screen the contents of that list. In case the user has no ticket bought there is no message displayed.
4.6 SessionManager.java
This class handles data reception and inserts or copies data about the user, user card, concert, tickets and tickets bought. It uses a new type of class ticket sold to insert the information about a purchase in the database.
 
 
5. Database Schema. Entity Relationship Diagram (ERD).
 

