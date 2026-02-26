package entityClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: DiscussionInputValidator </p>
 *
 * <p> Description: Input validation logic for discussion posts and replies.
 * All validation rules and error messages are defined here so the GUI and logic
 * stay consistent across the application. </p>
 *
 * <p> Validation Rules:
 * <ul>
 *   <li>Post Title: not blank; 1-150 characters</li>
 *   <li>Post Body: not blank; 1-5000 characters</li>
 *   <li>Reply Body: not blank; 1-5000 characters</li>
 *   <li>Allowed characters: all printable characters including line breaks (so we only check blank/length)</li>
 * </ul>
 * </p>
 *
 * <p> Error Messages:
 * <ul>
 *   <li>Title empty: “The title cannot be empty.”</li>
 *   <li>Title too long: “The title is too long. It must be 150 characters or less.”</li>
 *   <li>Body empty: “The body cannot be empty.”</li>
 *   <li>Body too long: “The body is too long. It must be 5000 characters or less.”</li>
 * </ul>
 * </p>
 * @author Amairani Caballero
 */
public class DiscussionInputValidator {

	/** Maximum title length. */
	public static final int TITLE_MAX = 150;

	/** Maximum body length for post/reply. */
	public static final int BODY_MAX = 5000;

	/** Deleted placeholder message shown for both title and body after deletion. */
	public static final String DELETED_MESSAGE = "This post was deleted.";

	// Error messages
	public static final String ERR_TITLE_EMPTY = "The title cannot be empty.";
	public static final String ERR_TITLE_TOO_LONG = "The title is too long. It must be 150 characters or less.";
	public static final String ERR_BODY_EMPTY = "The body cannot be empty.";
	public static final String ERR_BODY_TOO_LONG = "The body is too long. It must be 5000 characters or less.";

	/**
	 * Validates a post title.
	 *
	 * @param title input title
	 * @return list of errors; empty if valid
	 */
	public List<String> validatePostTitle(String title) {
		List<String> errors = new ArrayList<>();

		if (title == null || title.trim().isEmpty()) {
			errors.add(ERR_TITLE_EMPTY);
			return errors; // if empty, no need to check length further
		}

		if (title.length() > TITLE_MAX) {
			errors.add(ERR_TITLE_TOO_LONG);
		}

		return errors;
	}

	/**
	 * Validates a post or reply body.
	 *
	 * @param body input body
	 * @return list of errors, empty if valid
	 */
	public List<String> validateBody(String body) {
		List<String> errors = new ArrayList<>();

		if (body == null || body.trim().isEmpty()) {
			errors.add(ERR_BODY_EMPTY);
			return errors;
		}

		if (body.length() > BODY_MAX) {
			errors.add(ERR_BODY_TOO_LONG);
		}

		return errors;
	}

	/**
	 * Validates an entire post (title and body).
	 *
	 * @param title title input
	 * @param body body input
	 * @return list of errors; empty if valid
	 */
	public List<String> validatePost(String title, String body) {
		List<String> errors = new ArrayList<>();
		errors.addAll(validatePostTitle(title));
		errors.addAll(validateBody(body));
		return errors;
	}

	/**
	 * Validates a reply (body only).
	 *
	 * @param body reply body input
	 * @return list of errors; empty if valid
	 */
	public List<String> validateReply(String body) {
		return validateBody(body);
	}
}