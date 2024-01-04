package com.nft.common.Redission;

import com.nft.common.Constants;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

import java.util.concurrent.TimeUnit;
@Log4j2
public class DistributedRedisLock {

    //从配置类中获取redisson对象
    private static Redisson redisson = RedissonManager.getRedisson();



    private DistributedRedisLock() {
        // 私有构造方法，避免外部实例化
    }
    //加锁
    public static boolean acquire(String lockName){
        //获取锁对象
        RLock mylock = redisson.getLock(lockName);
        try {
            // 加锁，并且设置锁过期时间3秒，防止死锁的产生  uuid+threadId
            mylock.lock(5, TimeUnit.SECONDS);
            // 加锁成功
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
            return false;
        }
    }

    //锁的释放
    public static void release(String lockName) {
        //获取所对象
        RLock mylock = redisson.getLock(lockName);
        try {
            // 释放锁（解锁）
            mylock.unlock();
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
        }
    }

    /**
     * 读锁和写锁互斥，读锁和读锁之间可以并发，但是写锁如果key是已经加了读锁的话，需要排队执行
     * 无论是读请求先执行还是写请求先执行，只要涉及到写锁，则都会阻塞，如果是先写再读，则读锁等待，如果是先读再写，则写锁等待
     * 读写锁用于更新数据和查询数据的并发情况，如果只是查询数据保证
     * 相对于普通的锁有优化
     */

    //读锁
    public static boolean acquireReadLock(String lockName) {
        //获取所对象
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockName);
        try {
            RLock rLock = readWriteLock.readLock();
            rLock.lock(5, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
            return false;
        }
    }
    //释放读锁
    public static boolean releaseReadLock(String lockName) {
        //获取所对象
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockName);
        try {
            RLock rLock = readWriteLock.readLock();
            rLock.unlock();
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
            return false;
        }
    }
    //写锁
    public static boolean acquireWriteLock(String lockName) {
        //从配置类中获取redisson对象
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockName);
        try {
            RLock rLock = readWriteLock.writeLock();
            rLock.lock(5, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
            return false;
        }
    }
    //释放写锁
    public static boolean releaseWriteLock(String lockName) {
        //获取所对象
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockName);
        try {
            RLock rLock = readWriteLock.readLock();
            rLock.unlock();
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志等
            log.error(e.getMessage());
            return false;
        }
    }

}

