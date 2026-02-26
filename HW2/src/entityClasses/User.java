package entityClasses;

/**
 * <p> Title: User Class </p>
 * * <p> Description: This User class represents a user entity in the system. It contains the user's
 * details such as userName, password, and roles being played. </p>
 * * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * @author Lynn Robert Carter
 */ 
public class User {
	
    // These are the private attributes for this entity object
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private String emailAddress;
    private boolean adminRole;
    private boolean role1;
    private boolean role2;
    
    /**
     * <p> Description: This default constructor is not used in this system. </p>
     */
    public User() {
    	
    }

    /**
     * <p> Description: This constructor is used to establish user entity objects. </p>
     * @param userName specifies the account userName for this user
     @param password specifies the account password for this user
     @param fn       specifies the first name for this user
     @param mn       specifies the middle name for this user
     @param ln       specifies the last name for this user
     @param pfn      specifies the preferred first name for this user
     @param ea       specifies the email address for this user
     @param r1       specifies the Admin attribute (TRUE or FALSE) for this user
     @param r2       specifies the Role1 attribute (TRUE or FALSE) for this user
     @param r3       specifies the Role2 attribute (TRUE or FALSE) for this user
     */
    public User(String userName, String password, String fn, String mn, String ln, String pfn, 
    		String ea, boolean r1, boolean r2, boolean r3) {
        this.userName = userName;
        this.password = password;
        this.firstName = fn;
        this.middleName = mn;
        this.lastName = ln;
        this.preferredFirstName = pfn;
        this.emailAddress = ea;
        this.adminRole = r1;
        this.role1 = r2;
        this.role2 = r3;
    }

    /**
     * <p> Description: This setter defines the Admin role attribute. </p>
     * @param role is a boolean that specifies if this user is playing the Admin role.
     */
    public void setAdminRole(boolean role) {
    	this.adminRole=role;
    }

    /**
     * <p> Description: This setter defines the role1 attribute. </p>
     * @param role is a boolean that specifies if this user is playing role1.
     */
    public void setRole1User(boolean role) {
    	this.role1=role;
    }

    /**
     * <p> Description: This setter defines the role2 attribute. </p>
     * @param role is a boolean that specifies if this user is playing role2.
     */
    public void setRole2User(boolean role) {
    	this.role2=role;
    }

    /**
     * <p> Description: This getter returns the UserName. </p>
     * @return a String of the UserName
     */
    public String getUserName() { return userName; }

    /**
     * <p> Description: This getter returns the Password. </p>
     * @return a String of the password
     */
    public String getPassword() { return password; }

    /**
     * <p> Description: This getter returns the FirstName. </p>
     * @return a String of the FirstName
     */
    public String getFirstName() { return firstName; }

    /**
     * <p> Description: This getter returns the MiddleName. </p>
     * @return a String of the MiddleName
     */
    public String getMiddleName() { return middleName; }

    /**
     * <p> Description: This getter returns the LastName. </p>
     * @return a String of the LastName
     */
    public String getLastName() { return lastName; }

    /**
     * <p> Description: This getter returns the PreferredFirstName. </p>
     * @return a String of the PreferredFirstName
     */
    public String getPreferredFirstName() { return preferredFirstName; }

    /**
     * <p> Description: This getter returns the EmailAddress. </p>
     * @return a String of the EmailAddress
     */
    public String getEmailAddress() { return emailAddress; }

    /**
     * <p> Description: This setter defines the UserName attribute. </p>
     * @param s the new user name string
     */
    public void setUserName(String s) { userName = s; }

    /**
     * <p> Description: This setter defines the Password attribute. </p>
     * @param s the new password string
     */
    public void setPassword(String s) { password = s; }

    /**
     * <p> Description: This setter defines the FirstName attribute. </p>
     * @param s the new first name string
     */
    public void setFirstName(String s) { firstName = s; }

    /**
     * <p> Description: This setter defines the MiddleName attribute. </p>
     * @param s the new middle name string
     */
    public void setMiddleName(String s) { middleName = s; }

    /**
     * <p> Description: This setter defines the LastName attribute. </p>
     * @param s the new last name string
     */
    public void setLastName(String s) { lastName = s; }

    /**
     * <p> Description: This setter defines the PreferredFirstName attribute. </p>
     * @param s the new preferred first name string
     */
    public void setPreferredFirstName(String s) { preferredFirstName = s; }

    /**
     * <p> Description: This setter defines the EmailAddress attribute. </p>
     * @param s the new email address string
     */
    public void setEmailAddress(String s) { emailAddress = s; }

    /**
     * <p> Description: This getter returns the value of the Admin role attribute. </p>
     * @return a boolean of true or false based on the state of the attribute
     */
    public boolean getAdminRole() { return adminRole; }

    /**
     * <p> Description: This getter returns the value of the role1 attribute. </p>
     * @return a boolean of true or false based on the state of the attribute
     */
	public boolean getNewRole1() { return role1; }

    /**
     * <p> Description: This getter returns the value of the role2 attribute. </p>
     * @return a boolean of true or false based on the state of the attribute
     */
    public boolean getNewRole2() { return role2; }

    /**
     * <p> Description: This getter returns the number of roles this user plays (0 - 5). </p>
     * @return an integer value representing the number of roles this user plays
     */
    public int getNumRoles() {
    	int numRoles = 0;
    	if (adminRole) numRoles++;
    	if (role1) numRoles++;
    	if (role2) numRoles++;
    	return numRoles;
    }
}