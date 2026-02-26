package entityClasses;

import java.util.HashSet;
import java.util.Set;

/**
 * <p> Title: Post Class </p>
 *
 * <p> Description: Represents a student created post with a title and body.
 * Posts belong to a thread (defaults to "General"). </p>
 *
 * <p> Deleting a post does NOT delete replies. The post will still exists for other  
 * students to see replies, viewers will see a message stating post was deleted. </p>
 * @author Amairani Caballero
 */
public class Post {

	/** Unique post identifier. Assigned by PostStore (auto-increment). */
	private int postId;

	/** Thread name. If not specified at creation, PostStore defaults to "General". */
	private String threadName;

	/** Username of the author (a string for now, we can link to User entity during Phase 2.) */
	private String authorUserName;

	/** Title of the post. */
	private String title;

	/** Body of the post. */
	private String body;

	/** Deleted flag. When true, the post is deleted but still present. */
	private boolean deleted;

	/**
	 * Tracks which users have read this post.
	 * If a username is not in this set, the post is considered unread for that user.
	 * Set provides O(1) lookups and prevents duplicates.
	 */
	private final Set<String> readers = new HashSet<>();

	/**
	 * Constructs a Post, called by PostStore.
	 *
	 * @param postId assigned id
	 * @param threadName thread name (already defaulted by PostStore if needed)
	 * @param authorUserName author username
	 * @param title validated title
	 * @param body validated body
	 */
	public Post(int postId, String threadName, String authorUserName, String title, String body) {
		this.postId = postId;
		this.threadName = threadName;
		this.authorUserName = authorUserName;
		this.title = title;
		this.body = body;
		this.deleted = false;
	}

	// Getters
	
	public int getPostId() {
		return postId;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getAuthorUserName() {
		return authorUserName;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public boolean isDeleted() {
		return deleted;
	}

	// Read/unread helpers
	
	/**
	 * Marks this post as read by a specific user.
	 *
	 * @param viewerUserName user who viewed the post
	 */
	public void markRead(String viewerUserName) {
		if (viewerUserName == null) return;
		readers.add(viewerUserName);
	}

	/**
	 * @param viewerUserName user viewing
	 * @return true if this post has NOT been read by the viewer
	 */
	public boolean isUnreadBy(String viewerUserName) {
		if (viewerUserName == null) return true;
		return !readers.contains(viewerUserName);
	}

	// Mutators used by stores
	/**
	 * Updates title and body, called by PostStore after validation.
	 *
	 * @param newTitle validated title
	 * @param newBody validated body
	 */
	public void update(String newTitle, String newBody) {
		// If the post is deleted, we disallow edits in the store. This method assumes allowed.
		this.title = newTitle;
		this.body = newBody;
	}

	/**
	 * Replaces both title and body with the deleted message.
	 *
	 * @param deletedMessage message shown to viewers
	 */
	public void deletedPostMessage(String deletedMessage) {
		this.deleted = true;
		this.title = deletedMessage;
		this.body = deletedMessage;
	}
	
	@Override
	public String toString() {
		return "Post{id=" + postId +
				", thread='" + threadName + "'" +
				", author='" + authorUserName + "'" +
				", deleted=" + deleted +
				", title='" + title + "'" +
				"}";
	}
}