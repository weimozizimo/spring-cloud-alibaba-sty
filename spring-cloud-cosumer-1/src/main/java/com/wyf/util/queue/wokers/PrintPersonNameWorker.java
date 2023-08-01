package com.wyf.util.queue.wokers;

import com.wyf.util.queue.AbstractWorker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *@Description 打印人名的worker
 *@Author weiyifei
 *@date 2023/7/29
 */
@Slf4j
@Data
public class PrintPersonNameWorker extends AbstractWorker {

    /**
     * 0-100
     */
    private Integer grade;

    /**
     * 人名
     */
    private String name;

    public PrintPersonNameWorker(Integer grade, String name) {
        this.grade = grade;
        this.name = name;
    }

    @Override
    public String getTag() {
        return String.format("PrintPersonNameWorker,人名：%s,分数：%s",name,grade);
    }

    @Override
    public int getPriority() {
        return grade;
    }

    @Override
    protected Object processBusi() {

        try {
            log.info("假装我在处理业务");
            Thread.sleep(1000);
            log.info("名称：{},得分：{}",name,grade);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
