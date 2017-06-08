package com.ace.code.lock.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.ace.code.GenericTest;

/**
 * @author Diliang.Li
 * @date 2017/6/5.
 */
public class RedisReentrantLockTest extends GenericTest{

    @Test
//    @Ignore
    public void test() throws Exception {
        // 创建8个线程不停地去重入(随机次数n, 0 <= n <=8)获取锁
        List<Thread> threads = createThreads(8);
        //开始任务
        for (Thread t : threads) {
            t.start();
        }
        // 执行60秒
        Thread.sleep(10 * 1000);
        //停止所有线程
        Task.alive = false;
        // 等待所有线程终止
        for (Thread t : threads) {
            t.join();
        }

    }

    @Override
    public Runnable getTask() {
        Jedis jedis = new Jedis("localhost", 6379);
        RedisReentrantLock lock = new RedisReentrantLock(jedis);
        return new Task(lock);
    }

    private static class Task implements Runnable {
        private RedisReentrantLock lock;
        private final int MAX_ENTRANT = 5;
        private final Random random = new Random();
        private static boolean alive = true;

        Task(RedisReentrantLock lock) {
            this.lock = lock;
        }

        public void run() {
            while (alive) {
                int times = random.nextInt(MAX_ENTRANT);
                doLock(times);
            }
        }

        private void doLock(int times) {
//            if (lock.tryLock(5, TimeUnit.SECONDS)) {
            if (lock.tryLock()) { // 非阻塞
                try {
                    if (times > 0) {
                        doLock(times-1);
                    }
                } finally {
                    if (lock != null) {
                        lock.unlock();
                    }
                }
            }
        }
    }

}