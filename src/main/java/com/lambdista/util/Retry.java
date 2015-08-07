package com.lambdista.util;

import java.util.Arrays;

/**
 * Created by armin on 06/08/15.
 */
public class Retry<T> {

    public Try<T> retry(int maxRetries, FailableSupplier<T> supplier, Class<? extends Exception>... onlyForTheseExceptions) {

        Try<T> aTry = Try.apply(supplier);
        if (isValidException(aTry, onlyForTheseExceptions) && maxRetries >= 0) {
            return retry(maxRetries--, supplier);
        } else {
            return aTry;
        }
    }

    public Try<Void> retry(int maxRetries, VoidFunction supplier, Class<? extends Exception>... onlyForTheseExceptions) {

        Try<Void> aTry = Try.applyVoid(supplier);
        if (isValidException((Try<T>) aTry, onlyForTheseExceptions) && maxRetries >= 0) {
            return retry(maxRetries--, supplier);
        } else {
            return aTry;
        }
    }

    private boolean isValidException(Try<T> aTry, Class<? extends Exception>... onlyForTheseExceptions) {
        boolean isFailure = aTry.isFailure();
        if (isFailure) {
            if (onlyForTheseExceptions.length > 0) {
                // only return true if the caught exception is in the list of the ones we care about
                // TODO: should we check for subclasse?
                return Arrays.asList(onlyForTheseExceptions).contains(aTry.failed().get().getClass());
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

}
