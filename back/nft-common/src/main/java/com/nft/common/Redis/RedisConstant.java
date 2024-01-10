package com.nft.common.Redis;

import lombok.extern.log4j.Log4j2;

import java.util.Random;

public class  RedisConstant {
    public static final long MINUTE_30 =60 * 30;
    public static final long MINUTE_3 = 60 * 3;
    public static final long MINUTE_60 =60 * 30;
    public static final long DAY_ONE =60 * 60 * 24;

    public static long RandomCacheTime(long time) {
        return time+ new Random().nextInt(900) + 100;
    }
}
