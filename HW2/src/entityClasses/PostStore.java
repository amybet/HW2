package entityClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> Title: PostStore Class </p>
 *
 * <p> Description: Stores all posts and an arbitrary subset of posts (e.g., search results).
 * The subset is not live, it is updated when the caller requests a refresh/search. </p>
 *
 * <p> Thread creation/edit rules: Students do not have authority to create/edit threads.
 * For now, a thread is represented as a simple string label on a post. If the caller supplies
 * a threadName we accept it as a label, but operations to create/delete threads are not supported yet. </p>
 * @author Amairani Caballero
 */
public class PostStore {

	/** All posts in the system. */
	private final List<Post> allPosts = new ArrayList<>();

	/** Subset of posts */
	private final List<Post> subsetPosts = new ArrayList<>();

	/** Auto-increment counter for post ids. */
	private int nextPostId = 1;

	/** Validator used for all title/body rules. */
	private final DiscussionInputValidator validator = new DiscussionInputValidator();

	/** Default thread name when none is specified. */
	public static final String DEFAULT_THREAD = "General";

	/**
	 * @return unmodifiable view of all posts
	 */
	public List<Post> getAllPosts() {
		return Collections.unmodifiableList(allPosts);
	}

	/**
	 * @return unmodifiable view of the current subset
	 */
	public List<Post> getSubsetPosts() {
		return Collections.unmodifiableList(subsetPosts);
	}

	/**
	 * Creates a post with validation. If threadName is blank, it defaults to "General".
	 *
	 * @param authorUserName author
	 * @param threadName optional thread name; defaults if null/blank
	 * @param title post title
	 * @param body post body
	 * @return OperationResult containing created Post or validation errors
	 */
	public OperationResult<Post> createPost(String authorUserName, String threadName, String title, String body) {
		List<String> errors = validator.validatePost(title, body);
		if (!errors.isEmpty()) return OperationResult.failure(errors);

		String finalThread = normalizeThread(threadName);

		Post created = new Post(nextPostId++, finalThread, authorUserName, title, body);
		allPosts.add(created);
		return OperationResult.success(created);
	}

	/**
	 * Reads a post by id.
	 *
	 * @param postId id
	 * @return Post or null if not found
	 */
	public Post getPostById(int postId) {
		for (Post p : allPosts) {
			if (p.getPostId() == postId) return p;
		}
		return null;
	}

	/**
	 * Returns posts by a given user.
	 *
	 * @param authorUserName user
	 * @return list of posts (may be empty)
	 */
	public List<Post> getPostsByAuthor(String authorUserName) {
		List<Post> result = new ArrayList<>();
		for (Post p : allPosts) {
			if (authorUserName != null && authorUserName.equals(p.getAuthorUserName())) {
				result.add(p);
			}
		}
		return result;
	}

	/**
	 * Updates a post title/body with validation.
	 *
	 * @param postId id of post to update
	 * @param newTitle new title
	 * @param newBody new body
	 * @return OperationResult with updated Post or errors
	 */
	public OperationResult<Post> updatePost(int postId, String newTitle, String newBody) {
		Post p = getPostById(postId);
		if (p == null) {
			return OperationResult.failure(List.of("Post not found."));
		}
		if (p.isDeleted()) {
			return OperationResult.failure(List.of("Cannot edit a deleted post."));
		}

		List<String> errors = validator.validatePost(newTitle, newBody);
		if (!errors.isEmpty()) return OperationResult.failure(errors);

		p.update(newTitle, newBody);
		return OperationResult.success(p);
	}

	/**
	 * Deletes a post after confirmation
	 *
	 * @param postId id
	 * @param confirm must be true to perform the deletion ("Are you sure?")
	 * @return OperationResult with Boolean true on success or errors
	 */
	public OperationResult<Boolean> deletePost(int postId, boolean confirm) {
		if (!confirm) {
			return OperationResult.failure(List.of("Deletion not confirmed."));
		}

		Post p = getPostById(postId);
		if (p == null) {
			return OperationResult.failure(List.of("Post not found."));
		}

		// Replace both title/body with deleted message
		p.deletedPostMessage(DiscussionInputValidator.DELETED_MESSAGE);
		return OperationResult.success(Boolean.TRUE);
	}

	/**
	 * Marks a post as read by a user.
	 *
	 * @param postId post id
	 * @param viewerUserName viewer username
	 * @return OperationResult true on success; error if post not found
	 */
	public OperationResult<Boolean> markPostRead(int postId, String viewerUserName) {
		Post p = getPostById(postId);
		if (p == null) return OperationResult.failure(List.of("Post not found."));

		p.markRead(viewerUserName);
		return OperationResult.success(Boolean.TRUE);
	}

	/**
	 * Searches posts by keyword and by thread name (optional).
	 * This updates the subset list.
	 *
	 * Matching rule: case-insensitive contains() on title OR body.
	 * Note: deleted posts still match, but their title/body will be the deleted message.
	 *
	 * @param keyword keyword to search; if null/blank, subset becomes all posts (thread filtered if provided)
	 * @param threadName thread filter; if null/blank, searches all threads
	 */
	public void refreshSubsetBySearch(String keyword, String threadName) {
		subsetPosts.clear();

		String kw = (keyword == null) ? "" : keyword.trim().toLowerCase();
		String threadFilter = (threadName == null) ? "" : threadName.trim();

		boolean filterByThread = !threadFilter.isEmpty();
		boolean filterByKeyword = !kw.isEmpty();

		for (Post p : allPosts) {
			if (filterByThread && !threadFilter.equals(p.getThreadName())) {
				continue;
			}

			if (!filterByKeyword) {
				subsetPosts.add(p);
				continue;
			}

			String title = (p.getTitle() == null) ? "" : p.getTitle().toLowerCase();
			String body = (p.getBody() == null) ? "" : p.getBody().toLowerCase();

			if (title.contains(kw) || body.contains(kw)) {
				subsetPosts.add(p);
			}
		}
	}

	/**
	 * Counts how many posts are unread for a viewer.
	 *
	 * @param viewerUserName viewer
	 * @return count
	 */
	public int countUnreadPosts(String viewerUserName) {
		int count = 0;
		for (Post p : allPosts) {
			if (p.isUnreadBy(viewerUserName)) count++;
		}
		return count;
	}

	private String normalizeThread(String threadName) {
		if (threadName == null) return DEFAULT_THREAD;
		String trimmed = threadName.trim();
		return trimmed.isEmpty() ? DEFAULT_THREAD : trimmed;
	}
}