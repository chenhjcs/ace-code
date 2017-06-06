package com.ace.code;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Diliang.Li
 * @date 2017/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CodeApp.class)
@WebAppConfiguration()
@Transactional
@Rollback
public abstract class GenericTest {

    // 创建count个线程，每个线程都是不同的jedis连接以及不同的与时间服务器的连接
    protected List<Thread> createThreads(int count) throws IOException {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < count; i++) {
            Thread t = new Thread(getTask());
            threads.add(t);
        }
        return threads;
    }

    public abstract Runnable getTask();
}
