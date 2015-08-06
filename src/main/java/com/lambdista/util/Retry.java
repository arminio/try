package com.lambdista.util;

import java.util.function.Consumer;

/**
 * Created by armin on 06/08/15.
 */
public class Retry {

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
        System.out.println(retry(5, 0, integerFailableSupplier));
    }

    private static <T> T retry(int maxRetries, int retryCount, FailableSupplier<T> supplier) {

        Try<T> aTry = Try.apply(supplier);
        if (aTry.isFailure() && maxRetries > retryCount) {
            return retry(maxRetries, retryCount++, supplier);
        } else {
            return aTry.get();
        }
    }

}
