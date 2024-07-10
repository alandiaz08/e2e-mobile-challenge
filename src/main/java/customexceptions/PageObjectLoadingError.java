package customexceptions;


/**
 * Error that should be thrown when any PO is not loaded.
 */
public class PageObjectLoadingError extends Error {

  public PageObjectLoadingError(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public PageObjectLoadingError(String errorMessage) {
    super(errorMessage);
  }
}
