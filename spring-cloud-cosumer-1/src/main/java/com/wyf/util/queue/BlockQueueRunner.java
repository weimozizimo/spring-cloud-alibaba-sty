package com.wyf.util.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Slf4j
@Service("blockQueueRunner")
public class BlockQueueRunner {

    /**
     * 用来判断是否运行
     */
    private volatile boolean isRunning = true;
    /**
     * 是否安全停止
     */
    private volatile boolean safeStop = true;

    @Autowired
    private BlockQueue blockQueue;

    /**
     * 开始运行
     */
    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean stop = false;

                while (!stop){
                    if(isRunning==false&&safeStop==false){
                        stop = true;
                    }else if(isRunning==false&&safeStop==true&&blockQueue.isEmpty()){
                        stop = true;
                    }else{
                        stop = false;
                    }
                    AbstractWorker worker = blockQueue.takeTask();
                    if(worker != null){
                        worker.process();
                    }
                }
                log.info("任务执行队列已停止,当前队列中还有{}个任务未执行",blockQueue.getSize());
                log.info("具体剩余任务信息为：");
                blockQueue.printTasks();
            }
        }).start();
        log.info("任务执行队列已启动");
    }


    /**
     * 添加任务
     * @param worker
     * @return 由于队列使用的PriorityBlockingQueue，所以添加任务时，会根据任务的优先级进行排序,并且PriorityBlockingQueue是支持锁的，所以不会出现并发问题，无需加锁
     */
    public boolean addTask(AbstractWorker worker){
        try {
           return blockQueue.addTask(worker);
        }catch (Exception e){
            log.info("任务执行队列添加任务失败");
            return false;
        }
    }

    /**
     * 立刻停止，不再理会队列中剩余的任务,虽然说是立刻停止，但是由于由于是同步循环，所以可能会有一些延迟，需要等当前执行的任务执行完毕才会停止
     */
    public void stopNow(){
        safeStop = false;
        isRunning = false;
    }

    /**
     * 安全的停止，会等待队列中的任务执行完毕
     */
    public void safeStop(){
        safeStop = true;
        isRunning = false;
    }

    /**
     * spring容器销毁时，需要安全的停止队列，等待任务完后，避免出现问题
     */
    @PreDestroy
    public void destroy(){
        safeStop();
    }


}
