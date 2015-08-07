package com.lambdista.util;

import org.junit.Test;

/**
 * Created by armin.keyvanloo on 7/08/2015.
 */
public class RetryTest {

    static int someCounter = 5;
    static String x = "trying...";


    @Test
    public void should_retry_normal() {

        FailableSupplier<Integer> integerFailableSupplier = () -> {
            System.out.println(x + someCounter--);
            if (someCounter > 3) {
                throw new RuntimeException();
            }
            return someCounter;
        };
        Try retry = new Retry().retry(5, integerFailableSupplier, RuntimeException.class);
        System.out.println(retry);

    }

//    private static Try testRetryNormal() {
//        FailableSupplier<Integer> integerFailableSupplier = () -> {
//            System.out.println(x + someCounter--);
//            if (someCounter > 3) {
//                throw new RuntimeException();
//            }
//            return someCounter;
//        };
//        Try retry = new Retry().retry(5, integerFailableSupplier, RuntimeException.class);
//        System.out.println(retry);
//        return retry;
//    }

    @Test
    public void should_retry_Void() {
        VoidFunction voidFunction = () -> {
            System.out.println(x + someCounter--);
            if (someCounter > 4) {
                throw new RuntimeException();

            }
        };

        Try aTry = new Retry().retry(5, voidFunction, RuntimeException.class);
        System.out.println(aTry);

    }
}