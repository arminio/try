package com.lambdista.util;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by armin on 06/08/15.
 */
public class Retry<T> {

    static int someCounter = 5;

    public static void main(String[] args) {
        String x = "trying...";


        FailableSupplier<Integer> integerFailableSupplier = () -> {
            System.out.println(x + someCounter--);
            if (someCounter > 3) {
                throw new RuntimeException();
            }
            return someCounter;
        };
        System.out.println(new Retry().retry(5, 0, integerFailableSupplier, RuntimeException.class));
    }

    public Try<T> retry(int maxRetries, int retryCount, FailableSupplier<T> supplier, Class<? extends Exception>... onlyForTheseExceptions) {

        Try<T> aTry = Try.apply(supplier);
        if (isValidException(aTry, onlyForTheseExceptions) && maxRetries > retryCount) {
            return retry(maxRetries, retryCount++, supplier);
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
