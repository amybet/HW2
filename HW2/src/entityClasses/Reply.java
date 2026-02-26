package entityClasses;

import java.util.HashSet;
import java.util.Set;

/**
 * <p> Title: Reply Class </p>
 *
 * <p> Description: Represents a reply to a specific Post. </p>
 *
 * <p> Replies have their own read/unread status per viewer, similar to posts. </p>
 * @author Amairani Caballero
 */
public class Reply {

	/** Unique reply identifier assigned by ReplyStore (auto-increment). */
	private int replyId;

	/** Post id that this reply belongs to. */
	private int postId;

	/** Username of the author. */
	private String authorUserName;

	/** Reply body (1-5000 chars, non-blank). */
	private String body;

	/** Flag for reply deletion. Unlike post deletion, reply deletion removes the reply from lists. */
	private boolean deleted;

	/** Per-user read tracking. */
	private final Set<String> readers = new HashSet<>();

	/**
	 * Constructs a Reply. Intended to be called by ReplyStore.
	 *
	 * @param replyId assigned id
	 * @param postId post id
	 * @param authorUserName author
	 * @param body validated body
	 */
	public Reply(int replyId, int postId, String authorUserName, String body) {
		this.replyId = replyId;
		this.postId = postId;
		this.authorUserName = authorUserName;
		this.body = body;
		this.deleted = false;
	}

	// Getters

	public int getReplyId() {
		return replyId;
	}

	public int getPostId() {
		return postId;
	}

	public String getAuthorUserName() {
		return authorUserName;
	}

	public String getBody() {
		return body;
	}

	public boolean isDeleted() {
		return deleted;
	}

	// Read/unread

	public void markRead(String viewerUserName) {
		if (viewerUserName == null) return;
		readers.add(viewerUserName);
	}

	public boolean isUnreadBy(String viewerUserName) {
		if (viewerUserName == null) return true;
		return !readers.contains(viewerUserName);
	}

	// Mutators used by store

	/**
	 * Updates the reply body. Intended to be called by ReplyStore after validation.
	 *
	 * @param newBody validated reply body
	 */
	public void updateBody(String newBody) {
		this.body = newBody;
	}

	/**
	 * Marks this reply as deleted. ReplyStore will remove deleted replies from lists,
	 * this flag is for possible soft deleted later (not sure if will be needed yet).
	 */
	public void markDeleted() {
		this.deleted = true;
	}
	
	@Override
	public String toString() {
		return "Reply{id=" + replyId +
				", postId=" + postId +
				", author='" + authorUserName + "'" +
				", deleted=" + deleted +
				", body='" + body + "'" +
				"}";
	}
}