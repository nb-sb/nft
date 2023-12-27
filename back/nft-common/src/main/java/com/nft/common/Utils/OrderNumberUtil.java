package com.nft.common.Utils;

import java.util.Date;
import java.util.Random;

public class OrderNumberUtil {
    private static final int RANDOM_NUMBER_LENGTH = 4;

    public static String generateOrderNumber(Integer userid , Integer productId) {
        synchronized (OrderNumberUtil.class) {
            long timestamp = new Date().getTime();
            String timestampString = String.valueOf(timestamp);
            String userIdString = generateSequenceString(userid);
            String productIdString = generateSequenceString(productId);
            String randomNumber = generateRandomNumber(RANDOM_NUMBER_LENGTH);
            return timestampString + userIdString + productIdString + randomNumber;
        }
    }
    private static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    private static String   generateSequenceString(Integer input) {
        StringBuilder sb = new StringBuilder();
        sb.append(input);
        if (sb.length() > 2) {
            sb.delete(0, sb.length() - 2);
        } else {
            while (sb.length() < 2) {
                sb.insert(0, "0");
            }
        }
        String result = sb.toString();
        return result;
    }
}
