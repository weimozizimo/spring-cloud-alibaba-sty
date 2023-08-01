package com.wyf.util;

import com.wyf.util.queue.BlockQueueRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.stereotype.Component;

@Component
public class StartRunner implements ApplicationRunner {

    @Autowired
    private BlockQueueRunner blockQueueRunner;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //启动任务执行队列
        blockQueueRunner.start();
    }
}
