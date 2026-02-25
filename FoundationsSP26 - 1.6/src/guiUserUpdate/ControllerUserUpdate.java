package guiUserUpdate;

import entityClasses.User;
import javafx.stage.Stage;

/**
 * <p> Title: ControllerUserUpdate Class</p>
 * * <p> Description: This static class supports the actions initiated by the ViewUserUpdate
 * class. It contains methods to navigate the user to their appropriate home page 
 * based on their role, or to force a logout/login sequence.</p>
 */


public class ControllerUserUpdate {
	
	/** Default constructor. */
	public ControllerUserUpdate() { }
	
	/**
	 * This method is called when the user has clicked on the button to
	 * proceed to the user's home page. It routes the user to the correct 
	 * dashboard based on their active role.
	 * * @param theStage specifies the JavaFX Stage for the next GUI page and its methods
	 * @param theUser  specifies the user so the application navigates to the correct page and displays the right information
	 */
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiRole1.ViewRole1Home.displayRole1Home(theStage, theUser);
			break;
		case 3:
			guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}

	/**
	 * This method is called when the user creates an account for the first time
	 * to force a log out and redirect them to the login screen.
	 * * @param theStage specifies the JavaFX Stage for the next GUI page and its methods
	 */
	protected static void goToLogIn(Stage theStage) {
		guiUserLogin.ViewUserLogin.displayUserLogin(theStage);
 	}
}