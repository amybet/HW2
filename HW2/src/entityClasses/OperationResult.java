package entityClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> Title: OperationResult </p>
 *
 * <p> Description: This generic wrapper supports GUI friendly error handling, it returns
 * an immutable list of errors along with an optional value.
 * A non-empty error list implies failure. An empty error list implies success. </p>
 *
 * <p> Useful because we need to display multiple error messages at once without throwing exceptions.
 *  This wrapper provides a consistent pattern across CRUD operations. </p>
 * @author Amairani Caballero
 * @param <T> The type of the value returned on success (e.g., Post, Reply)
 */
public class OperationResult<T> {

	/** The value returned on success, null if the operation failed. */
	private final T value;

	/** Immutable list of errors, empty when the operation succeeds. */
	private final List<String> errors;

	/**
	 * Creates a successful result with a value.
	 *
	 * @param value the value to return, may be null if the success concept doesn't need a value
	 * @param <T> generic type
	 * @return a success OperationResult
	 */
	public static <T> OperationResult<T> success(T value) {
		return new OperationResult<>(value, Collections.emptyList());
	}

	/**
	 * Creates a failed result with one or more errors.
	 *
	 * @param errors list of errors, if null treat as empty (failures should provide errors)
	 * @param <T> generic type
	 * @return a failed OperationResult
	 */
	public static <T> OperationResult<T> failure(List<String> errors) {
		List<String> safe = (errors == null) ? new ArrayList<>() : new ArrayList<>(errors);
		return new OperationResult<>(null, Collections.unmodifiableList(safe));
	}

	/**
	 * Internal constructor.
	 *
	 * @param value value (success) or null (failure)
	 * @param errors immutable list
	 */
	private OperationResult(T value, List<String> errors) {
		this.value = value;
		this.errors = (errors == null) ? Collections.emptyList() : errors;
	}

	/**
	 * @return true if there are no errors.
	 */
	public boolean isSuccess() {
		return errors.isEmpty();
	}

	/**
	 * @return the value (non-null on success for most operations).
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return immutable list of errors (empty if successful).
	 */
	public List<String> getErrors() {
		return errors;
	}
}