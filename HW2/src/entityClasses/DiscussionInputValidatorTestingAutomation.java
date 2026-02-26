package entityClasses;

import java.util.List;

/**
 * <p> Title: DiscussionLogicTestingAutomation </p>
 *
 * <p> Description: Console-based test automation for discussion logic (Posts/Replies, Stores,
 * subsets, deletion confirmation, and input validation). This class prints PASS/FAIL lines
 * with test IDs and detailed input/expected/actual information. </p>
 *
 * @author Amairani Caballero
 */
public class DiscussionInputValidatorTestingAutomation {

	/** Counter of the number of passed tests. */
	static int numPassed = 0;

	/** Counter of the number of failed tests. */
	static int numFailed = 0;

	public static void main(String[] args) {
		System.out.println("*** Discussion Input Validator TestingAutomation Begins ***\n");
		System.out.println("Testing: Posts, Replies, CRUD, Search Subsets, Delete Confirmation, Read/Unread, Validation\n");

		PostStore postStore = new PostStore();
		ReplyStore replyStore = new ReplyStore();

		String amy = "amybet";
		String bob = "bob42";

		// Create Post test cases
		System.out.println("---------- CREATE POSTS TEST CASES: ----------\n");

		// TC01 (Positive): valid post, default thread
		OperationResult<Post> tc01 = postStore.createPost(amy, null, "Valid Title", "Valid body.");
		expectSuccess(
				1,
				"Create Post (default thread)",
				"author=" + amy + ", thread=null, title='Valid Title', body='Valid body.'",
				tc01);

		// TC02 (Negative): title empty
		OperationResult<Post> tc02 = postStore.createPost(amy, "General", "   ", "Valid body.");
		expectFailureWithError(
				2,
				"Create Post (empty title)",
				"author=" + amy + ", thread='General', title='   ', body='Valid body.'",
				DiscussionInputValidator.ERR_TITLE_EMPTY,
				tc02);

		// TC03 (Negative): body empty
		OperationResult<Post> tc03 = postStore.createPost(amy, "General", "Valid", "   ");
		expectFailureWithError(
				3,
				"Create Post (empty body)",
				"author=" + amy + ", thread='General', title='Valid', body='   '",
				DiscussionInputValidator.ERR_BODY_EMPTY,
				tc03);

		// TC04 (Negative): title too long (151 chars)
		String longTitle = "A".repeat(DiscussionInputValidator.TITLE_MAX + 1);
		OperationResult<Post> tc04 = postStore.createPost(amy, "General", longTitle, "Body ok");
		expectFailureWithError(
				4,
				"Create Post (title too long)",
				"author=" + amy + ", thread='General', title='A' repeated 151, body='Body ok'",
				DiscussionInputValidator.ERR_TITLE_TOO_LONG,
				tc04);

		// TC05 (Negative): body too long (5001 chars)
		String longBody = "B".repeat(DiscussionInputValidator.BODY_MAX + 1);
		OperationResult<Post> tc05 = postStore.createPost(amy, "General", "Title ok", longBody);
		expectFailureWithError(
				5,
				"Create Post (body too long)",
				"author=" + amy + ", thread='General', title='Title ok', body='B' repeated 5001",
				DiscussionInputValidator.ERR_BODY_TOO_LONG,
				tc05);

		// Post update/delete test cases
		System.out.println("\n---------- POST UPDATE/DELETE TESTS CASES ----------\n");

		int postId = tc01.getValue().getPostId();

		// TC06 (Positive): update post valid
		OperationResult<Post> tc06 = postStore.updatePost(postId, "Updated Title", "Updated body");
		expectSuccess(
				6,
				"Update Post (valid)",
				"postId=" + postId + ", newTitle='Updated Title', newBody='Updated body'",
				tc06);

		// TC07 (Negative): update with invalid title
		OperationResult<Post> tc07 = postStore.updatePost(postId, "", "Body ok");
		expectFailureWithError(
				7,
				"Update Post (invalid title)",
				"postId=" + postId + ", newTitle='', newBody='Body ok'",
				DiscussionInputValidator.ERR_TITLE_EMPTY,
				tc07);

		// TC08 (Negative): delete without confirmation
		OperationResult<Boolean> tc08 = postStore.deletePost(postId, false);
		expectFailure(
				8,
				"Delete Post (confirm=false)",
				"postId=" + postId + ", confirm=false",
				tc08);

		// TC09 (Positive): delete with confirmation -> tombstone
		OperationResult<Boolean> tc09 = postStore.deletePost(postId, true);
		expectSuccess(
				9,
				"Delete Post (confirm=true)",
				"postId=" + postId + ", confirm=true",
				tc09);

		Post deletedPost = postStore.getPostById(postId);
		boolean isTombstonedCorrectly =
				deletedPost != null
				&& deletedPost.isDeleted()
				&& DiscussionInputValidator.DELETED_MESSAGE.equals(deletedPost.getTitle())
				&& DiscussionInputValidator.DELETED_MESSAGE.equals(deletedPost.getBody());

		expectBoolean(
				10,
				"Deleted post check (title & body replaced)",
				"postId=" + postId + ", expected title/body='" + DiscussionInputValidator.DELETED_MESSAGE + "'",
				true,
				isTombstonedCorrectly,
				"Actual post=" + deletedPost);

		// TC11 (Negative): cannot update a deleted post
		OperationResult<Post> tc11 = postStore.updatePost(postId, "New", "New");
		expectFailure(
				11,
				"Update deleted post (should fail)",
				"postId=" + postId + ", newTitle='New', newBody='New'",
				tc11);

		// Reply test cases
		System.out.println("\n---------- REPLY TESTS CASES ----------\n");

		// Create a fresh non-deleted post for reply tests
		OperationResult<Post> postForReplies = postStore.createPost(bob, "General", "Reply target", "Body");
		expectSuccess(
				12,
				"Create Post for reply tests",
				"author=" + bob + ", thread='General', title='Reply target', body='Body'",
				postForReplies);

		int replyPostId = postForReplies.getValue().getPostId();

		// TC13 (Positive): create valid reply
		OperationResult<Reply> tc13 = replyStore.createReply(replyPostId, amy, "Valid reply");
		expectSuccess(
				13,
				"Create Reply (valid)",
				"postId=" + replyPostId + ", author=" + amy + ", body='Valid reply'",
				tc13);

		// TC14 (Negative): create invalid reply (empty body)
		OperationResult<Reply> tc14 = replyStore.createReply(replyPostId, amy, "   ");
		expectFailureWithError(
				14,
				"Create Reply (empty body)",
				"postId=" + replyPostId + ", author=" + amy + ", body='   '",
				DiscussionInputValidator.ERR_BODY_EMPTY,
				tc14);

		// TC15 (Positive): update reply valid
		int replyId = tc13.getValue().getReplyId();
		OperationResult<Reply> tc15 = replyStore.updateReply(replyId, "Updated reply");
		expectSuccess(
				15,
				"Update Reply (valid)",
				"replyId=" + replyId + ", newBody='Updated reply'",
				tc15);

		// TC16 (Negative): delete reply without confirmation
		OperationResult<Boolean> tc16 = replyStore.deleteReply(replyId, false);
		expectFailure(
				16,
				"Delete Reply (confirm=false)",
				"replyId=" + replyId + ", confirm=false",
				tc16);

		// TC17 (Positive): delete reply with confirmation
		OperationResult<Boolean> tc17 = replyStore.deleteReply(replyId, true);
		expectSuccess(
				17,
				"Delete Reply (confirm=true)",
				"replyId=" + replyId + ", confirm=true",
				tc17);

		// Sebset search test cases
		System.out.println("\n---------- SUBSET SEARCH TESTS CASES ----------\n");

		// TC18 (Positive): search posts keyword
		postStore.refreshSubsetBySearch("Reply target", null);
		int subsetSize = postStore.getSubsetPosts().size();
		expectBoolean(
				18,
				"Search Posts subset (keyword)",
				"keyword='Reply target', thread=null",
				true,
				subsetSize >= 1,
				"subsetSize=" + subsetSize + ", subset=" + postStore.getSubsetPosts());

		// TC19 (Positive): search posts by thread only
		postStore.refreshSubsetBySearch("", "StudyGroup");
		boolean onlyStudyGroup = postStore.getSubsetPosts().stream()
				.allMatch(p -> "StudyGroup".equals(p.getThreadName()));
		expectBoolean(
				19,
				"Search Posts subset (thread filter only)",
				"keyword='', thread='StudyGroup'",
				true,
				onlyStudyGroup,
				"subset=" + postStore.getSubsetPosts());

		// Summary info
		System.out.println("\n---------- SUMMARY ----------");
		System.out.println("Passed: " + numPassed);
		System.out.println("Failed: " + numFailed);
	}

	// Output helpers
	private static <T> void expectSuccess(int testId, String testName, String input, OperationResult<T> result) {
		boolean pass = result != null && result.isSuccess();

		printHeader(testId, testName, input);
		System.out.println("Expected: SUCCESS");
		System.out.println("Actual:   " + (pass ? "SUCCESS" : "FAILURE"));

		if (pass) {
			System.out.println("Value:    " + result.getValue());
			markPass();
		} else {
			System.out.println("Errors:   " + safeErrors(result));
			markFail();
		}

		System.out.println();
	}

	private static <T> void expectFailure(int testId, String testName, String input, OperationResult<T> result) {
		boolean pass = result != null && !result.isSuccess();

		printHeader(testId, testName, input);
		System.out.println("Expected: FAILURE");
		System.out.println("Actual:   " + (pass ? "FAILURE" : "SUCCESS"));

		System.out.println("Errors:   " + safeErrors(result));

		if (pass) markPass();
		else markFail();

		System.out.println();
	}

	private static <T> void expectFailureWithError(
			int testId,
			String testName,
			String input,
			String expectedError,
			OperationResult<T> result) {

		boolean isFailure = result != null && !result.isSuccess();
		List<String> errs = safeErrors(result);
		boolean contains = errs.contains(expectedError);
		boolean pass = isFailure && contains;

		printHeader(testId, testName, input);
		System.out.println("Expected: FAILURE with error: " + expectedError);
		System.out.println("Actual:   " + (result != null && result.isSuccess() ? "SUCCESS" : "FAILURE"));
		System.out.println("Errors:   " + errs);

		if (pass) markPass();
		else markFail();

		System.out.println();
	}

	private static void expectBoolean(
			int testId,
			String testName,
			String input,
			boolean expected,
			boolean actual,
			String details) {

		boolean pass = (expected == actual);

		printHeader(testId, testName, input);
		System.out.println("Expected: " + expected);
		System.out.println("Actual:   " + actual);
		System.out.println("Details:  " + details);

		if (pass) markPass();
		else markFail();

		System.out.println();
	}

	private static void printHeader(int testId, String testName, String input) {
		System.out.println("TC" + pad2(testId) + " - " + testName);
		System.out.println("Input:    " + input);
	}

	private static String pad2(int n) {
		return (n < 10) ? ("0" + n) : String.valueOf(n);
	}

	private static <T> List<String> safeErrors(OperationResult<T> result) {
		if (result == null || result.getErrors() == null) return List.of("(no errors returned)");
		return result.getErrors().isEmpty() ? List.of("(none)") : result.getErrors();
	}

	private static void markPass() {
		numPassed++;
		System.out.println("Result:   PASS");
	}

	private static void markFail() {
		numFailed++;
		System.out.println("Result:   FAIL");
	}
}