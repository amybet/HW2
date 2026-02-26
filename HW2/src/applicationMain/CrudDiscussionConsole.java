package applicationMain;

import java.util.List;

import entityClasses.OperationResult;
import entityClasses.Post;
import entityClasses.PostStore;
import entityClasses.Reply;
import entityClasses.ReplyStore;

/**
 * <p> Title: CrudDiscussionConsole </p>
 *
 * <p> Description: Standalone console (no GUI) to demonstrate:
 * <ul>
 *   <li>Create/Read/Update/Delete for Posts and Replies</li>
 *   <li>Input validation rules and error messages</li>
 *   <li>Thread defaulting to "General"</li>
 *   <li>Search subsets (refresh-based)</li>
 *   <li>Read/unread and reply counts</li>
 *   <li>Post deletion (replies remain)</li>
 * </ul>
 * </p>
 * @author Amairani Caballero
 */

public class CrudDiscussionConsole {

	public static void main(String[] args) {
		System.out.println("*** Starting CRUD discussion console ***\n\n");


		PostStore postStore = new PostStore();
		ReplyStore replyStore = new ReplyStore();

		String amy = "amycaballero";
		String bob = "bob92";

		// Creates post with validation
		System.out.println("1) Create posts:\n");

		// Post without thread selection
		OperationResult<Post> p1 = postStore.createPost(amy, null,
				"Team Project Meeting",
				"Can we meet Friday at 4pm to split up the user stories?");
		printResult("Create Post p1", p1);

		// Post with thread selection
		OperationResult<Post> p2 = postStore.createPost(bob, "Homework",
				"Question about input validation",
				"Do we validate on every input field?");
		printResult("Create Post p2", p2);

		// Post with empty title, should match error message
		OperationResult<Post> badPost = postStore.createPost(amy, "General",
				"   ",
				"Body is fine but title is blank.");
		printResult("Create Post anotherBadPost", badPost);
		
		// Post with empty body, should match error message
		OperationResult<Post> anotherBadPost = postStore.createPost(amy, "General",
				"Title is fine but body is blank",
				"");
		printResult("Create Post badPost", anotherBadPost);
		

		// Read Lists and unread posts
		System.out.println("\n2) Read lists and unread posts:\n");

		System.out.println("All posts:\n");
		for (Post p : postStore.getAllPosts()) {
			System.out.println(describePost(p));
		}

		System.out.println("\nUnread posts for " + amy + ": " + postStore.countUnreadPosts(amy));
		postStore.markPostRead(p1.getValue().getPostId(), amy);
		System.out.println("Unread posts for " + amy + " after reading p1: " + postStore.countUnreadPosts(amy));

		// Update post with validation
		System.out.println("\n3) Update post:\n");

		OperationResult<Post> updateOk = postStore.updatePost(p1.getValue().getPostId(),
				"Team Project Meeting (Updated)",
				"Can we meet Thursday instead?");
		printResult("Update p1", updateOk);

		OperationResult<Post> updateBad = postStore.updatePost(p1.getValue().getPostId(),
				"",
				"Still has a body, but title is invalid.");
		printResult("Update p1 with invalid title", updateBad);
		
		OperationResult<Post> anotherUpdateBad = postStore.updatePost(p1.getValue().getPostId(),
				"Still has a title, but body is invalid.",
				"");
		printResult("Update p1 with invalid body", anotherUpdateBad);

		// Search posts (needs to be refreshed for now)
		System.out.println("\n4) Search posts:\n");

		postStore.refreshSubsetBySearch("validation", null); // search all threads
		System.out.println("Subset posts matching keyword 'validation':");
		for (Post p : postStore.getSubsetPosts()) {
			System.out.println(describePost(p));
		}

		postStore.refreshSubsetBySearch("", "Homework"); // all posts in StudyGroup
		System.out.println("\nSubset posts in thread 'StudyGroup':");
		for (Post p : postStore.getSubsetPosts()) {
			System.out.println(describePost(p));
		}

		// Create reply with validation
		System.out.println("\n5) Create replies:\n");

		int postIdForReplies = p1.getValue().getPostId();

		OperationResult<Reply> r1 = replyStore.createReply(postIdForReplies, bob,
				"Thursday 3pm works for me.");
		printResult("Create Reply r1", r1);

		OperationResult<Reply> r2 = replyStore.createReply(postIdForReplies, amy,
				"Great I will text you!");
		printResult("Create Reply r2", r2);

		OperationResult<Reply> badReply = replyStore.createReply(postIdForReplies, amy, "   ");
		printResult("Create Reply badReply", badReply);

		System.out.println("\nReplies for post " + postIdForReplies + ":");
		for (Reply r : replyStore.getRepliesForPost(postIdForReplies)) {
			System.out.println(describeReply(r));
		}

		System.out.println("\nUnread replies for " + amy + " on post " + postIdForReplies + ": "
				+ replyStore.countUnreadRepliesForPost(postIdForReplies, amy));

		// mark one reply read
		replyStore.markReplyRead(r1.getValue().getReplyId(), amy);
		System.out.println("Unread replies for " + amy + " after reading r1: "
				+ replyStore.countUnreadRepliesForPost(postIdForReplies, amy));

		// Delete post with confirmation
		System.out.println("\n6) Delete post with confirmation:\n");

		System.out.println("Attempt delete without confirmation:");
		printResult("Delete p1 confirm=false", postStore.deletePost(postIdForReplies, false));

		System.out.println("Delete with confirmation:");
		printResult("Delete p1 confirm=true", postStore.deletePost(postIdForReplies, true));

		System.out.println("\nPost after deletion (should show deleted message for title and body):");
		System.out.println(describePost(postStore.getPostById(postIdForReplies)));

		System.out.println("\nReplies still exist after post deletion:");
		for (Reply r : replyStore.getRepliesForPost(postIdForReplies)) {
			System.out.println(describeReply(r));
		}

		// Delete reply with confirmation
		System.out.println("\n7) Delete reply with confirmation:\n");

		System.out.println("Attempt delete reply without confirmation:");
		printResult("Delete r2 confirm=false", replyStore.deleteReply(r2.getValue().getReplyId(), false));

		System.out.println("Delete reply with confirmation:");
		printResult("Delete r2 confirm=true", replyStore.deleteReply(r2.getValue().getReplyId(), true));

		System.out.println("\nReplies for post " + postIdForReplies + " after deleting r2:");
		for (Reply r : replyStore.getRepliesForPost(postIdForReplies)) {
			System.out.println(describeReply(r));
		}

		System.out.println("\n *** End of CRUD Discussion Console *** ");
	}

	private static String describePost(Post p) {
		if (p == null) return "(null post)";
		return "Post{id=" + p.getPostId() +
				", thread='" + p.getThreadName() + "'" +
				", author='" + p.getAuthorUserName() + "'" +
				", deleted=" + p.isDeleted() +
				", title='" + p.getTitle() + "'" +
				"}";
	}

	private static String describeReply(Reply r) {
		if (r == null) return "(null reply)";
		return "Reply{id=" + r.getReplyId() +
				", postId=" + r.getPostId() +
				", author='" + r.getAuthorUserName() + "'" +
				", deleted=" + r.isDeleted() +
				", body='" + r.getBody() + "'" +
				"}";
	}

	private static <T> void printResult(String label, OperationResult<T> result) {
		System.out.println(label + ": " + (result.isSuccess() ? "SUCCESS" : "FAILURE"));
		if (result.isSuccess()) {
			System.out.println("  value=" + result.getValue());
		} else {
			List<String> errs = result.getErrors();
			for (String e : errs) {
				System.out.println("  error: " + e);
			}
		}
	}
}