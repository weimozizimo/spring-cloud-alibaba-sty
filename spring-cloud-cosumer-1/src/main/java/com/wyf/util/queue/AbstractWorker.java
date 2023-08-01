package com.wyf.util.queue;


import com.wyf.util.DateUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import static com.wyf.util.DateUtil.DTF_TO_TIME;

/**
*@Description 抽象worker，用于执行队列的任务
*@Author weiyifei
*@date 2023/7/29
*/
@Data
public  class  AbstractWorker<T> implements Comparable<AbstractWorker<T>> {

    private Logger log = LoggerFactory.getLogger(AbstractWorker.class);


    /**
     * 工作者优先级
     */
    private int priority;

    /**
     * 执行时间
     */
    private long  executeTime;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     *任务备注
     */
    private String tag;

    /**
     * 开始
     */
    void start(){
        startTime = System.currentTimeMillis();
        log.info("{} is start",getTag());
    }


    protected T processBusi(){
        // none implementation
        return null;
    }


    /**
     * 结束
     */
    void end(){
        endTime = System.currentTimeMillis();
        executeTime = endTime-startTime;
        log.info("任务完成，开始时间为{}，结束时间为{}，消耗{}秒、{}毫秒", DateUtil.formatTimestamp(startTime,DTF_TO_TIME),DateUtil.formatTimestamp(endTime,DTF_TO_TIME),(endTime-startTime)/1000,(endTime-startTime));
    }

    /**
     * 结束
     */
    void errorEnd(){
        endTime = System.currentTimeMillis();
        executeTime = endTime-startTime;
        log.info("任务异常结束，开始时间为{}，结束时间为{}，消耗{}秒、{}毫秒", DateUtil.formatTimestamp(startTime,DTF_TO_TIME),DateUtil.formatTimestamp(endTime,DTF_TO_TIME),(endTime-startTime)/1000,(endTime-startTime));
    }

    /**
     * 执行任务的主方法
     * @return
     * @throws Exception
     */
    public T process() {
        T res = null;
        try{
            start();
            res = processBusi();
            end();
        }catch (Exception e){
            log.info("任务名称：{},执行失败，原因：{}",getTag(),e.getMessage());
            errorEnd();
            e.printStackTrace();
        }finally {
            return res;
        }
    }


    public void setPriority(int priority) {
        this.priority = priority;
    }
    /**
     * 默认实现获取priority属性
     * @return
     */
    public int getPriority(){
        return priority;
    }

    @Override
    public int compareTo(AbstractWorker<T> o) {
        //优先级值越大，则越先执行
        return (this.getPriority()>o.getPriority())?-1:(this.getPriority()==o.getPriority()?0:1);
    }
}
