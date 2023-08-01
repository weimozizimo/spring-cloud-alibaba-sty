package com.wyf.util.queue;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;

/**
*@Description  封装阻塞队列
*@Author weiyifei
*@date 2023/7/29
*/

@Component("queue")
public class BlockQueue implements InitializingBean{

    private Logger log = LoggerFactory.getLogger(BlockQueue.class);

    @Value("${queue.min-size}")
    private int initialSize = 10;



    //包装一个优先级的阻塞队列
    private BlockingQueue<AbstractWorker> blockingQueue;

    //初始化阻塞队列
    public void init(){
        blockingQueue = new PriorityBlockingQueue(initialSize);
        log.info("阻塞队列已初始化");
    }


    /**
     * 添加任务
     * @param worker
     * @return
     */
    public boolean addTask(AbstractWorker worker){
        return blockingQueue.add(worker);
    }

    /**
     * 获取一个任务
     * @return
     */
    public AbstractWorker takeTask(){
        try {
            return blockingQueue.take();
        }catch (InterruptedException e){
        }
        return null;
    }

    /**
     * 按照执行顺序打印所有任务
     * @return
     */
    public void printTasks(){
        blockingQueue.forEach(new Consumer<AbstractWorker>() {
            @Override
            public void accept(AbstractWorker o) {
                log.info(JSONObject.toJSONString(o));
            }
        });
    }

    /**
     * 获取当前队列剩余任务
     */
    public int getSize(){
       return blockingQueue.size();
    }


    /**
    * @Description 获取当前队列容量
    * @param
    * @Author weiyifei
    * @return int·
    */
    public int curCapacity() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends BlockingQueue> aClass = blockingQueue.getClass();
        Field field = aClass.getDeclaredField("queue");
        field.setAccessible(true);

        Object[] objects = (Object[]) field.get(blockingQueue);
       return objects.length;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化队列
        init();
    }

    public boolean isEmpty() {
        return blockingQueue.size() == 0;
    }
}
