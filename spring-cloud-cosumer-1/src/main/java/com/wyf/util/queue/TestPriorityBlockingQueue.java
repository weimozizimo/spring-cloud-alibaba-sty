package com.wyf.util.queue;

import com.alibaba.nacos.common.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class TestPriorityBlockingQueue {

    private static Logger log = LoggerFactory.getLogger(TestPriorityBlockingQueue.class);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        Class<? extends PriorityQueue> aClass = queue.getClass();
        Field field = aClass.getDeclaredField("queue");
        field.setAccessible(true);

        Object[] objects = (Object[]) field.get(queue);
        log.info("队列扩容前容量大小 {}",objects.length);
        for (int i = 0; i < 12; i++) {
            int val = RandomUtils.nextInt(1, 100);
            queue.add(val);
            log.info("add item value is {}",val);


        }
        objects = (Object[]) field.get(queue);
        log.info("队列扩容后容量大小{}",objects.length);
        log.info("队列中目前有的元素数量{}",queue.size());

        log.info("开始消费队列中的元素");
        while (!queue.isEmpty()){
            Integer num = queue.remove();
            log.info("消费元素值{}",num);
        }
        log.info("消费完毕");

    }



}
