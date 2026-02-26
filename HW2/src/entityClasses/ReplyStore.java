package entityClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> Title: ReplyStore Class </p>
 *
 * <p> Description: Stores all replies and a subset of replies (e.g., search results).
 * Supports CRUD, listing replies by post, and listing unread replies for a viewer. </p>
 * @author Amairani Caballero
 */
public class ReplyStore {

	/** All replies in the system. */
	private final List<Reply> allReplies = new ArrayList<>();

	/** Subset of replies (e.g., results of last search). */
	private final List<Reply> subsetReplies = new ArrayList<>();

	/** Auto-increment counter for reply ids. */
	private int nextReplyId = 1;

	/** Validator for body rules. */
	private final DiscussionInputValidator validator = new DiscussionInputValidator();

	/**
	 * @return unmodifiable list of all replies
	 */
	public List<Reply> getAllReplies() {
		return Collections.unmodifiableList(allReplies);
	}

	/**
	 * @return unmodifiable list of subset replies
	 */
	public List<Reply> getSubsetReplies() {
		return Collections.unmodifiableList(subsetReplies);
	}

	/**
	 * Creates a reply for a specific post id.
	 *
	 * Note: Post existence should be checked by the caller (typically using PostStore).
	 * This store focuses on reply validation and storage.
	 *
	 * @param postId post id
	 * @param authorUserName author
	 * @param body reply body
	 * @return OperationResult containing created Reply or errors
	 */
	public OperationResult<Reply> createReply(int postId, String authorUserName, String body) {
		List<String> errors = validator.validateReply(body);
		if (!errors.isEmpty()) return OperationResult.failure(errors);

		Reply created = new Reply(nextReplyId++, postId, authorUserName, body);
		allReplies.add(created);
		return OperationResult.success(created);
	}

	/**
	 * Gets a reply by id.
	 *
	 * @param replyId id
	 * @return Reply or null
	 */
	public Reply getReplyById(int replyId) {
		for (Reply r : allReplies) {
			if (r.getReplyId() == replyId) return r;
		}
		return null;
	}

	/**
	 * Lists replies for a given post id.
	 *
	 * @param postId post id
	 * @return list (may be empty)
	 */
	public List<Reply> getRepliesForPost(int postId) {
		List<Reply> result = new ArrayList<>();
		for (Reply r : allReplies) {
			if (!r.isDeleted() && r.getPostId() == postId) {
				result.add(r);
			}
		}
		return result;
	}

	/**
	 * Lists unread replies for a given post id and viewer.
	 *
	 * @param postId post id
	 * @param viewerUserName viewer
	 * @return list of unread replies (may be empty)
	 */
	public List<Reply> getUnreadRepliesForPost(int postId, String viewerUserName) {
		List<Reply> result = new ArrayList<>();
		for (Reply r : allReplies) {
			if (!r.isDeleted() && r.getPostId() == postId && r.isUnreadBy(viewerUserName)) {
				result.add(r);
			}
		}
		return result;
	}

	/**
	 * Updates a reply body with validation.
	 *
	 * @param replyId reply id
	 * @param newBody new body
	 * @return OperationResult with updated Reply or errors
	 */
	public OperationResult<Reply> updateReply(int replyId, String newBody) {
		Reply r = getReplyById(replyId);
		if (r == null || r.isDeleted()) {
			return OperationResult.failure(List.of("Reply not found."));
		}

		List<String> errors = validator.validateReply(newBody);
		if (!errors.isEmpty()) return OperationResult.failure(errors);

		r.updateBody(newBody);
		return OperationResult.success(r);
	}

	/**
	 * Deletes a reply after confirmation.
	 *
	 * @param replyId reply id
	 * @param confirm must be true to delete
	 * @return OperationResult true on success or errors
	 */
	public OperationResult<Boolean> deleteReply(int replyId, boolean confirm) {
		if (!confirm) {
			return OperationResult.failure(List.of("Deletion not confirmed."));
		}

		Reply r = getReplyById(replyId);
		if (r == null || r.isDeleted()) {
			return OperationResult.failure(List.of("Reply not found."));
		}

		r.markDeleted();
		return OperationResult.success(Boolean.TRUE);
	}

	/**
	 * Marks a reply as read by a user.
	 *
	 * @param replyId reply id
	 * @param viewerUserName viewer
	 * @return OperationResult true on success; error if not found
	 */
	public OperationResult<Boolean> markReplyRead(int replyId, String viewerUserName) {
		Reply r = getReplyById(replyId);
		if (r == null || r.isDeleted()) return OperationResult.failure(List.of("Reply not found."));

		r.markRead(viewerUserName);
		return OperationResult.success(Boolean.TRUE);
	}

	/**
	 * Updates the subset list by searching reply bodies for a keyword, optionally restricted to a post.
	 *
	 * @param keyword keyword; if blank, subset becomes all replies (optionally post-filtered)
	 * @param postIdFilter if null, search all posts; otherwise only replies for that post id
	 */
	public void refreshSubsetBySearch(String keyword, Integer postIdFilter) {
		subsetReplies.clear();

		String kw = (keyword == null) ? "" : keyword.trim().toLowerCase();
		boolean filterByKeyword = !kw.isEmpty();
		boolean filterByPost = (postIdFilter != null);

		for (Reply r : allReplies) {
			if (r.isDeleted()) continue;

			if (filterByPost && r.getPostId() != postIdFilter.intValue()) {
				continue;
			}

			if (!filterByKeyword) {
				subsetReplies.add(r);
				continue;
			}

			String body = (r.getBody() == null) ? "" : r.getBody().toLowerCase();
			if (body.contains(kw)) {
				subsetReplies.add(r);
			}
		}
	}

	/**
	 * @param postId post id
	 * @return number of non-deleted replies for that post
	 */
	public int countRepliesForPost(int postId) {
		return getRepliesForPost(postId).size();
	}

	/**
	 * @param postId post id
	 * @param viewerUserName viewer
	 * @return number of unread replies for that post
	 */
	public int countUnreadRepliesForPost(int postId, String viewerUserName) {
		return getUnreadRepliesForPost(postId, viewerUserName).size();
	}
}