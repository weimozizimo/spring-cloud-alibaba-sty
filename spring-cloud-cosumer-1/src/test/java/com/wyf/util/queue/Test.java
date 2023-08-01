package com.wyf.util.queue;

public class Test {

    public static void main(String[] args) {

        TestWorker worker1 = new TestWorker();
        worker1.setPriority(1);
        TestWorker worker2 = new TestWorker();
        worker2.setPriority(2);

        BlockQueue blockQueue = new BlockQueue();
        blockQueue.init();;
        blockQueue.addTask(worker1);
        blockQueue.addTask(worker2);
        blockQueue.printTasks();

    }


    public static class TestWorker extends AbstractWorker {

    }

}
