package work3;

import java.util.*;

public class MyThreadPool {

    private Deque<Runnable> tasks = new LinkedList<>();
    private List<MyThread> threads = new ArrayList<>();
    private volatile boolean shutdown = false;

    private class MyThread extends Thread {

        public void run() {
            while (true) {
                Runnable task;
                synchronized (tasks) {
                    while (!shutdown && tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (shutdown && tasks.isEmpty()) {
                        return;
                    }
                    task = tasks.removeFirst();
                }

                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MyThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            MyThread thread = new MyThread();
            threads.add(thread);
            thread.start();
        }
    }

    public void execute(Runnable task) {
        synchronized (tasks) {
            if (shutdown) {
                throw new IllegalStateException("Pool is stopped");

            }
            tasks.addLast(task);
            tasks.notifyAll();
        }
    }

    public void shutdown() {
        synchronized (tasks) {
            shutdown = true;
            tasks.notifyAll();
        }
    }

    public void shutdownNow() {
        for (Thread t : threads) {
            t.interrupt(); // пробуждает wait(), ставит флаг прерывания
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (MyThread myThread : threads) {
            myThread.join();
        }
    }

    // крутизна от ИИ
    public boolean awaitTermination(long timeout) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeout;
        for (Thread t : threads) {
            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) {
                return false;
            }
            t.join(remaining);
            if (t.isAlive()) {
                return false;
            }
        }
        return true;
    }


}
