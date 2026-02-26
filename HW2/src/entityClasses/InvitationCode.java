package entityClasses;

import java.sql.*;

/**
 * <p> Title: FSM-translated InputRecognizer. </p>
 * <p> Description: This represents a basic InvitationCode located in the database, with associated information. </p>
 @author Nicholas Hamilton
 @version 0.00		2018-02-04	Initial baseline 
 */
public class InvitationCode {
    
    /** The unique string representing the invitation code. */
    private String code;
    
    /** The timestamp indicating when this invitation code is no longer valid. */
    private Timestamp expiresAt;
    
    /** The email address associated with this specific invitation code. */
    private String email;

    /**
     * Constructs a new InvitationCode with the specified code, expiration timestamp, and email.
     @param code      the unique invitation code
     @param expiresAt the timestamp indicating when the code expires
     @param email     the email address the invitation is sent to or associated with
     */
    public InvitationCode(String code, Timestamp expiresAt, String email) {
        this.code = code;
        this.expiresAt = expiresAt;
        this.email = email;
    }

    /**
     * Retrieves the unique invitation code.
     @return the invitation code string
     */
    public String getCode() { 
        return code; 
    }

    /**
     * Retrieves the expiration timestamp for this invitation.
     @return the timestamp representing the expiration date and time
     */
    public Timestamp getExpiresAt() { 
        return expiresAt; 
    }

    /**
     * Retrieves the email address associated with this invitation.
     @return the associated email address string
     */
    public String getEmail() { 
        return email; 
    }
}