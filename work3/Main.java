package work3;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int cntThreads = 3;
        final int cntTasks = 6;
        MyThreadPool pool = new MyThreadPool(cntThreads);
        System.out.println(String.format("Created pool with %d threads", cntThreads));
        System.out.println(String.format("Starting %d tasks", cntTasks));
        for (int i = 1; i <= cntTasks; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println(String.format("Task %d started in thread %s", taskId, Thread.currentThread().getName()));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(String.format("Task %d breaked", taskId));
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println(String.format("Task %d finished", taskId));
            });
        }
        Thread.sleep(500);
        pool.shutdown();
    }

}
